package yelshod.com.paymentprocessingsystem.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import yelshod.com.paymentprocessingsystem.dto.PaymentRequest;
import yelshod.com.paymentprocessingsystem.entity.Payment;
import yelshod.com.paymentprocessingsystem.entity.User;
import yelshod.com.paymentprocessingsystem.repository.PaymentRepository;
import yelshod.com.paymentprocessingsystem.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;

    public PaymentService(PaymentRepository paymentRepository, UserRepository userRepository) {
        this.paymentRepository = paymentRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Payment createPayment(PaymentRequest paymentRequest) {
        User user = userRepository.findById(paymentRequest.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getBalance().compareTo(paymentRequest.getAmount()) < 0) {
            throw new RuntimeException("Insufficient balance");
        }

        user.setBalance(user.getBalance().subtract(paymentRequest.getAmount()));
        userRepository.save(user);

        Payment payment = new Payment();
        payment.setUser(user);
        payment.setAmount(paymentRequest.getAmount());
        payment.setCurrency(paymentRequest.getCurrency());
        payment.setTimestamp(LocalDateTime.now());

        return paymentRepository.save(payment);
    }

    public List<Payment> getPaymentByUserId(Long userId) {
        return paymentRepository.findByUserId(userId);
    }
    @Transactional
    public Payment getPaymentById(Long paymentId) {
        return paymentRepository.findById(paymentId)
                .orElseThrow( () -> new RuntimeException("Payment not found"));
    }
}
