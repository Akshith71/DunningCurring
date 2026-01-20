package com.example.DCMS.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.DCMS.Entity.Customer;
import com.example.DCMS.Entity.DunningLog;
import com.example.DCMS.Repository.CustomerRepository;
import com.example.DCMS.Repository.DunningLogRepository;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/admin/notifications")
public class NotificationController {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private DunningLogRepository dunningLogRepository;

    @PostMapping("/simulate-payment")
    public ResponseEntity<String> simulatePaymentNotification(
            @RequestParam Long customerId, 
            @RequestParam Double amount
    ) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        // 1. Construct the message
        String message = String.format("SIMULATED SMS to %s: Dear %s, we have received your payment of $%.2f. Thank you!", 
                customer.getMobile(), customer.getName(), amount);

        // 2. Log it to the database so it appears in the dashboard
        DunningLog log = new DunningLog();
        log.setCustomer(customer);
        log.setAction("PAYMENT_NOTIF");
        log.setMessage(message);
        log.setActionDate(LocalDateTime.now());
        dunningLogRepository.save(log);

        return ResponseEntity.ok("Notification Sent: " + message);
    }
}