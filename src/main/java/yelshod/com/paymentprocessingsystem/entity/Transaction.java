package yelshod.com.paymentprocessingsystem.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
    @Table
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    public class Transaction {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @ManyToOne
        @JoinColumn(name = "payment_id", nullable = false)
        private Payment payment;

        @Column(nullable = false)
        private String status; //"PENDING, SUCCESS, FAILED"

        @Column(nullable = false)
        private LocalDateTime timestamp;
    }

