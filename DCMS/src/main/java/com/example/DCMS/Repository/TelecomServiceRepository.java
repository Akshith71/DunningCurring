package com.example.DCMS.Repository;



import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.DCMS.Entity.Customer;
import com.example.DCMS.Entity.TelecomService;

public interface TelecomServiceRepository extends JpaRepository<TelecomService, Long> {

    List<TelecomService> findByCustomerId(Long customerId);

    List<TelecomService> findByStatus(String status);
    List<TelecomService> findByCustomer(Customer customer);
   
}

