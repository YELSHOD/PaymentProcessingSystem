package yelshod.com.paymentprocessingsystem.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import yelshod.com.paymentprocessingsystem.entity.User;
import yelshod.com.paymentprocessingsystem.repository.UserRepository;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public User registerUser(String email, String fullName, BigDecimal balance) {
        User user = new User();
        user.setEmail(email);
        user.setFullName(fullName);
        user.setBalance(balance);
        return userRepository.save(user);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Transactional
    public void addBalance(Long userId, BigDecimal amount) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setBalance(user.getBalance().add(amount));
        userRepository.save(user);
    }
}
