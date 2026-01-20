package com.example.DCMS.Repository;



import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.DCMS.Entity.Customer;
import com.example.DCMS.Entity.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    List<Payment> findByCustomerId(Long customerId);

    List<Payment> findByStatus(String status);
    List<Payment> findByCustomerIdOrderByPaymentDateDesc(Long customerId);
    List<Payment> findByCustomerOrderByPaymentDateDesc(Customer customer);
}
