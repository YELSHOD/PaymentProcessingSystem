package yelshod.com.paymentprocessingsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import yelshod.com.paymentprocessingsystem.entity.Transaction;
import yelshod.com.paymentprocessingsystem.entity.User;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByPaymentId(Long paymentId);
}
