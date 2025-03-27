package yelshod.com.paymentprocessingsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import yelshod.com.paymentprocessingsystem.entity.User;

import java.util.Optional;

public interface UserRepository  extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
