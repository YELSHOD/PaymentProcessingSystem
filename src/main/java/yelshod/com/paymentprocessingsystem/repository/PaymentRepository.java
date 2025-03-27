package yelshod.com.paymentprocessingsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import yelshod.com.paymentprocessingsystem.entity.Payment;
import yelshod.com.paymentprocessingsystem.entity.User;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByUserId(Long userId);

}
