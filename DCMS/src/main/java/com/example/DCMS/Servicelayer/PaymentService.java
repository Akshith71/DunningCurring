package com.example.DCMS.Servicelayer;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.example.DCMS.Entity.Customer;
import com.example.DCMS.Entity.Payment;
import com.example.DCMS.Entity.TelecomService;
import com.example.DCMS.Repository.CustomerRepository;
import com.example.DCMS.Repository.PaymentRepository;
import com.example.DCMS.Repository.TelecomServiceRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentService {

    @Autowired private PaymentRepository paymentRepository;
    @Autowired private CustomerRepository customerRepository;
    @Autowired private TelecomServiceRepository telecomServiceRepository;
    @Autowired private EmailService emailService;
    @Autowired private CuringActionService curingActionService; 

    // --- 1. INITIATE LOGIC ---
    public ResponseEntity<String> initiatePayment(Long customerId, Double amount,String  paymentMethod) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        Payment payment = new Payment();
        payment.setCustomer(customer);
        payment.setAmount(amount);
        payment.setPaymentDate(LocalDateTime.now());
        payment.setStatus("PENDING"); 
        payment.setPaymentDate(LocalDateTime.now());
        
        payment = paymentRepository.save(payment);

        if (customer.getEmail() != null) {
            emailService.sendPaymentVerificationEmail(customer.getEmail(), payment.getId(), amount);
            return ResponseEntity.ok("Payment Initiated! Verification email sent to " + customer.getEmail());
        }
        
        return ResponseEntity.ok("Payment saved (Pending), but no email found.");
    }

    // --- 2. VERIFY LOGIC ---
    public String verifyPayment(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        if ("SUCCESS".equals(payment.getStatus())) {
            return "<html><body><h1>⚠️ Payment already processed!</h1></body></html>";
        }

        payment.setStatus("SUCCESS");
        paymentRepository.save(payment);

        Customer customer = payment.getCustomer();
        double currentDue = customer.getOverdueAmount() != 0 ? customer.getOverdueAmount() : 0.0;
        double newDue = Math.max(0, currentDue - payment.getAmount());
        customer.setOverdueAmount(newDue);

        //AUTO-CURE LOGIC
        if (customer.getOverdueAmount() <= 0) {
            
            // Unblock services
            List<TelecomService> services = telecomServiceRepository.findByCustomerId(customer.getId());
            for (TelecomService s : services) {
                s.setStatus("ACTIVE");
                telecomServiceRepository.save(s);
            }
           
            // Call Curing Service
            curingActionService.cureCustomer(customer);
        } else {
            customerRepository.save(customer);
        }

        return "<html><body><h1 style='color:green'>✅ Payment Verified!</h1></body></html>";
    }

    // --- 3. HISTORY LOGIC ---
    public List<Payment> getPaymentHistory() {
        String username = getLoggedInUsername();
        Customer customer = customerRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        //  This requires the 'payment_method' column in DB to work
        return paymentRepository.findByCustomerIdOrderByPaymentDateDesc(customer.getId());
    }

    public Payment simulatePayment(Long customerId, double amount) {
        Customer customer = customerRepository.findById(customerId).orElseThrow();
        Payment payment = new Payment();
        payment.setCustomer(customer);
        payment.setAmount(amount);
        payment.setStatus("SUCCESS");
        payment.setPaymentmathod("ADMIN_SIMULATION");
        payment.setPaymentDate(LocalDateTime.now());
        paymentRepository.save(payment);
        
        double newDue = Math.max(0, (customer.getOverdueAmount() != 0 ? customer.getOverdueAmount() : 0.0) - amount);
        customer.setOverdueAmount(newDue);
        
        if (newDue == 0) {
            curingActionService.cureCustomer(customer);
        } else {
            customerRepository.save(customer);
        }
        return payment;
    }
    

    private String getLoggedInUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) return ((UserDetails) principal).getUsername();
        return principal.toString();
    }
}