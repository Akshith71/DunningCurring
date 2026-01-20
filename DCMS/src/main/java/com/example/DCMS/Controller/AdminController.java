package com.example.DCMS.Controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.DCMS.Entity.Customer;
import com.example.DCMS.Entity.DunningLog;
import com.example.DCMS.Repository.CustomerRepository;
import com.example.DCMS.Repository.DunningLogRepository;
import com.example.DCMS.batch.DailyBatchJob;

public class AdminController {
	

    @Autowired
    private CustomerRepository customerRepository;
    
    @Autowired
    private DunningLogRepository dunningLogRepository;

    // ✅ NEW ENDPOINT: Manually create a notification/log (Simulation)
    @PostMapping("/simulate/notification")
    public ResponseEntity<String> simulateNotification(
            @RequestParam Long customerId,
            @RequestParam String action, // e.g., "PAYMENT_REMINDER", "BLOCK", "SUCCESS"
            @RequestParam String message
    ) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        DunningLog log = new DunningLog();
        log.setCustomer(customer);
        log.setAction(action);
        log.setMessage(message);
       // log.setChannel("ADMIN_SIMULATION"); // Mark as manual
        log.setActionDate(LocalDateTime.now());
        
        dunningLogRepository.save(log); // ✅ Saving ensures it appears on Customer Dashboard

        return ResponseEntity.ok("Notification simulated successfully!");
    }
    @Autowired
    private DailyBatchJob dailyBatchJob; // ✅ Inject Batch Job

    // ... existing endpoints ...

    // ✅ NEW ENDPOINT: Manual Trigger for Batch Job
    @PostMapping("/batch/run")
    public ResponseEntity<String> runBatchJobManually() {
        dailyBatchJob.incrementOverdueDays(); // Calls the logic immediately
        return ResponseEntity.ok("✅ Daily Batch Executed! Overdue days updated.");
    }
}
