package com.example.DCMS.Servicelayer;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.DCMS.Entity.Customer;
import com.example.DCMS.Entity.DunningLog;
import com.example.DCMS.Repository.CustomerRepository;
import com.example.DCMS.Repository.DunningLogRepository;

@Service
public class CuringActionService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private DunningLogRepository dunningLogRepository;

    public void cureCustomer(Customer customer) {
        // 1. Reset Customer Status
        customer.setStatus("ACTIVE");
        customer.setOverdueDays(0);
        customer.setOverdueAmount(0.0);
        
        customerRepository.save(customer);

        // 2. Log the Curing Action
        DunningLog log = new DunningLog();
        log.setCustomer(customer);
        log.setAction("CURED");
        log.setMessage("Customer fully cured via Payment.");
        log.setActionDate(LocalDateTime.now());
        
        dunningLogRepository.save(log);
        
        System.out.println("✅ Customer " + customer.getId() + " has been Auto-Cured.");
    }
}