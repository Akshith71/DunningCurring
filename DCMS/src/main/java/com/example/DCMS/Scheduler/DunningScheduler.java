package com.example.DCMS.Scheduler;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.DCMS.Entity.Customer;
import com.example.DCMS.Repository.CustomerRepository;
import com.example.DCMS.Servicelayer.DunningEngineService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DunningScheduler {

	@Autowired
    private  CustomerRepository customerRepository;
	@Autowired
    private  DunningEngineService dunningEngineService;

    /**
     * 🔁 Runs every day at 2 AM
     * Cron: second minute hour day month weekday
     */
    @Scheduled(cron = "0 0 2 * * ?")
    public void runDailyDunning() {

        System.out.println("Dunning job started at " + LocalDateTime.now());

        List<Customer> overdueCustomers =
                customerRepository.findByOverdueDaysGreaterThan(0);

        for (Customer customer : overdueCustomers) {
            try {
                dunningEngineService.applyDunning(customer.getId());
            } catch (Exception ex) {
                // Important: don't stop entire job
                System.err.println(
                        "❌ Dunning failed for customer " + customer.getId()
                                + " : " + ex.getMessage()
                );
            }
        }

        System.out.println("✅ Dunning job finished");
    }
}
