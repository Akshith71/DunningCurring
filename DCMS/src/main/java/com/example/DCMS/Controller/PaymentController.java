package com.example.DCMS.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.DCMS.Entity.Payment;

import com.example.DCMS.Servicelayer.PaymentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/payment")
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
public class PaymentController {

    @Autowired
    private  PaymentService paymentService;

    // 1. INITIATE PAYMENT (Sends Verification Email)
    @PostMapping("/initiate")
    public ResponseEntity<String> initiatePayment(
            @RequestParam Long customerId,
            @RequestParam Double amount,
            @RequestParam String paymentMethod
    ) {
        // Calls the service to create PENDING payment & send email
        return paymentService.initiatePayment(customerId, amount, paymentMethod);
    }

    // 2. VERIFY PAYMENT (Clicked from Email Link)
    @GetMapping("/verify")
    public String verifyPayment(@RequestParam Long paymentId) {
        // Calls the service to mark SUCCESS & unblock customer
        return paymentService.verifyPayment(paymentId);
    }

    // 3. GET HISTORY (For Dashboard Display)
    @GetMapping("/history")
    public ResponseEntity<List<Payment>> getPaymentHistory() {
        return ResponseEntity.ok(paymentService.getPaymentHistory());
    }

    // 4. ADMIN SIMULATION (Direct Success - No Email)
    @PostMapping("/simulate")
    public Payment simulatePayment(
            @RequestParam Long customerId,
            @RequestParam double amount
    ) {
        // Admin tool to instantly resolve payment
        return paymentService.simulatePayment(customerId, amount);
    }
    
}