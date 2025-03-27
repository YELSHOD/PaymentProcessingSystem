package yelshod.com.paymentprocessingsystem.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import yelshod.com.paymentprocessingsystem.dto.PaymentRequest;
import yelshod.com.paymentprocessingsystem.entity.Payment;
import yelshod.com.paymentprocessingsystem.service.PaymentService;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final PaymentService paymentService;

    public TransactionController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public ResponseEntity<Payment> createPayment(@RequestBody PaymentRequest request) {
        Payment payment = paymentService.createPayment(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(payment);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Payment> getPayment(@PathVariable Long paymentYESId) {
        Payment payment = paymentService.getPaymentById(paymentYESId);
        return ResponseEntity.ok(payment);
    }
}
