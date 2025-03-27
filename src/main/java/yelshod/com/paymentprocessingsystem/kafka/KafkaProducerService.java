package yelshod.com.paymentprocessingsystem.kafka;

import org.apache.kafka.common.protocol.types.Field;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {
        private static final String TOPIC = "payment-transactions";
        private final KafkaTemplate<String, String> kafkaTemplate;

    public KafkaProducerService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendTransactionStatus(Long transactionId, String status) {
        String message = "Transaction ID: " + transactionId + ", Status: " + status;
        kafkaTemplate.send(TOPIC, message);
        System.out.println("Sent a message: " + message);
    }
}
