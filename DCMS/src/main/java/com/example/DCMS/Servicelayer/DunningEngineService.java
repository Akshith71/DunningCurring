package com.example.DCMS.Servicelayer;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.DCMS.Entity.Customer;
import com.example.DCMS.Entity.DunningLog;
import com.example.DCMS.Entity.DunningRule;
import com.example.DCMS.Entity.TelecomService;
import com.example.DCMS.Repository.CustomerRepository;
import com.example.DCMS.Repository.DunningLogRepository;
import com.example.DCMS.Repository.DunningRuleRepository;
import com.example.DCMS.Repository.TelecomServiceRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DunningEngineService {

    @Autowired private CustomerRepository customerRepository;
    @Autowired private DunningRuleRepository dunningRuleRepository;
    @Autowired private TelecomServiceRepository telecomServiceRepository;
    @Autowired private DunningLogRepository dunningLogRepository;
    @Autowired private NotificationService notificationService;

    public void applyDunning(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        int overdueDays = customer.getOverdueDays();

        List<DunningRule> rules = dunningRuleRepository
                .findByActiveTrueAndStartDayLessThanEqualAndEndDayGreaterThanEqual(overdueDays, overdueDays);

        for (DunningRule rule : rules) {
            applyRule(customer, rule);
        }
    }

    private void applyRule(Customer customer, DunningRule rule) {
        // 1. Apply Actions (Block/Throttle)
        List<TelecomService> services = telecomServiceRepository.findByCustomerId(customer.getId());
        
        if ("THROTTLE".equals(rule.getAction())) {
            services.forEach(s -> s.setStatus("THROTTLED"));
            customer.setStatus("THROTTLED");
        } else if ("BLOCK".equals(rule.getAction()) || "BLOCK_SERVICE".equals(rule.getAction())) {
            services.forEach(s -> s.setStatus("BLOCKED"));
            customer.setStatus("BLOCKED");
        }
        
        telecomServiceRepository.saveAll(services);
        customerRepository.save(customer);

        // 2. Send Notification
        try {
            notificationService.sendNotification(customer, rule);
        } catch (Exception ex) {
            System.err.println("Notification failed: " + ex.getMessage());
        }

        // ✅ FIX STARTS HERE ---------------------------
        
        // Step A: Generate the message string
        String messageText = generateMessage(customer.getName(), rule.getAction(), customer.getOverdueDays());
        
        // Step B: Call the helper method (This was missing in your code!)
        saveDunningLog(customer, rule.getAction(), messageText);
        
        // ❌ DELETE THE OLD BLOCK BELOW (This is why your message was empty)
        /*
        DunningLog log = new DunningLog();
        log.setCustomer(customer);
        log.setAction(rule.getAction());
        log.setActionDate(LocalDateTime.now());
        dunningLogRepository.save(log);
        */
        
        // ✅ FIX ENDS HERE -----------------------------
    }

    // Helper to generate message text
    private String generateMessage(String name, String action, int days) {
        if ("BLOCK".equals(action) || "BLOCK_SERVICE".equals(action)) {
            return "Dear " + name + ", account overdue by " + days + " days. Action: BLOCKED. Please pay now.";
        } else if ("THROTTLE".equals(action)) {
            return "Dear " + name + ", account overdue by " + days + " days. Action: THROTTLED (Speed Reduced).";
        } else {
            return "Dear " + name + ", reminder: Your bill is overdue by " + days + " days.";
        }
    }

    // Helper to save log
    private void saveDunningLog(Customer customer, String action, String message) {
        DunningLog log = new DunningLog();
        log.setCustomer(customer);
        log.setAction(action);
        log.setMessage(message); // ✅ Stores the text
        log.setActionDate(LocalDateTime.now());
        
        dunningLogRepository.save(log);
        System.out.println("📝 Log Saved: " + message);
    }
}