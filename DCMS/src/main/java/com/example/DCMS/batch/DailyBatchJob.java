package com.example.DCMS.batch;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.DCMS.Entity.Customer;
import com.example.DCMS.Repository.CustomerRepository;

@Component
public class DailyBatchJob {

    @Autowired
    private CustomerRepository customerRepository;

    // Runs automatically at midnight
    @Scheduled(cron = "0 0 0 * * ?") 
    @Transactional
    public void incrementOverdueDays() {
        try {
            System.out.println("⏰ Starting Daily Overdue Batch Job: " + LocalDateTime.now());

            // 1. Use the new safer query
            List<Customer> overdueCustomers = customerRepository.findCustomersWithDues();

            if (overdueCustomers.isEmpty()) {
                System.out.println("✅ No overdue customers found today.");
                return;
            }

            // 2. Increment days safely
            for (Customer customer : overdueCustomers) {
                // Handle NULL days safely by defaulting to 0
                int currentDays = (customer.getOverdueDays() != 0) ? customer.getOverdueDays() : 0;
                customer.setOverdueDays(currentDays + 1);
            }

            // 3. Save changes
            customerRepository.saveAll(overdueCustomers);
            System.out.println("✅ Success! Updated overdue days for " + overdueCustomers.size() + " customers.");

        } catch (Exception e) {
            // This prints the REAL error to your console
            System.err.println("❌ CRITICAL BATCH JOB ERROR:");
            e.printStackTrace(); 
            throw new RuntimeException("Batch Job Failed: " + e.getMessage());
        }
    }
}