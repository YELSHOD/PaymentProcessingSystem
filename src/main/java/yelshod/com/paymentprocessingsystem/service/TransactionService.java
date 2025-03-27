package yelshod.com.paymentprocessingsystem.service;

import jakarta.transaction.Transactional;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import yelshod.com.paymentprocessingsystem.entity.Payment;
import yelshod.com.paymentprocessingsystem.entity.Transaction;
import yelshod.com.paymentprocessingsystem.kafka.KafkaProducerService;
import yelshod.com.paymentprocessingsystem.repository.PaymentRepository;
import yelshod.com.paymentprocessingsystem.repository.TransactionRepository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final PaymentRepository paymentRepository;
    private final KafkaProducerService kafkaProducerService;
    private final RedisTemplate<String, Transaction> redisTemplate;

    public TransactionService(TransactionRepository transactionRepository,PaymentRepository paymentRepository,
                              KafkaProducerService kafkaProducerService, RedisTemplate<String, Transaction> redisTemplate) {
        this.transactionRepository = transactionRepository;
        this.paymentRepository = paymentRepository;
        this.kafkaProducerService = kafkaProducerService;
        this.redisTemplate = redisTemplate;
    }

    @Transactional
    public Transaction processTransaction(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        Transaction transaction = new Transaction();
        transaction.setPayment(payment);
        transaction.setTimestamp(LocalDateTime.now());

        // Используем compareTo() вместо "<=" и subtract() вместо "-"
        if (payment.getUser().getBalance().compareTo(payment.getAmount()) >= 0) {
            // Подтверждаем платеж
            payment.getUser().setBalance(payment.getUser().getBalance().subtract(payment.getAmount()));
            transaction.setStatus("APPROVED");
        } else {
            // Отклоняем платеж
            transaction.setStatus("FAILED");
        }

        // Сохраняем транзакцию
        Transaction savedTransaction = transactionRepository.save(transaction);

        // Кэшируем транзакцию в Redis
        redisTemplate.opsForValue().set(
                "transaction:" + savedTransaction.getId(),
                savedTransaction,
                Duration.ofMinutes(10)
        );

        // Отправляем статус транзакции в Kafka
        kafkaProducerService.sendTransactionStatus(savedTransaction.getId(), savedTransaction.getStatus());

        return savedTransaction;
    }



    @Cacheable(value = "transactions", key = "#transactionId")
    public Transaction getTransactionsByPaymentId(Long transactionId) {
        // Проверяем, есть ли транзакция в Redis
        Transaction cachedTransaction = redisTemplate.opsForValue().get("transaction: " + transactionId);
        if (cachedTransaction != null) {
            return cachedTransaction;
        }

        // Если нет в кэше, загружаем из БД
        return transactionRepository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));
    }
}
