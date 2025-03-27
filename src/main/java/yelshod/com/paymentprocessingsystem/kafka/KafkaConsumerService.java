package yelshod.com.paymentprocessingsystem.kafka;

import org.springframework.kafka.annotation.KafkaListener;

public class KafkaConsumerService {
    @KafkaListener(topics = "payment-transactions", groupId = "payment-group")
    public void listenTransactionStatus(String message) {
        System.out.println("Received message from Kafka: " + message);
    }
}
