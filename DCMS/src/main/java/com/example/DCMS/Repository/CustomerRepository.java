package com.example.DCMS.Repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.DCMS.Entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Optional<Customer> findByEmail(String email);

    Optional<Customer> findByMobile(String mobile);
    
    List<Customer> findByOverdueDaysGreaterThan(int days);
   // Optional<Customer> findByEmail(String email);

    // ✅ NEW: Find all customers who owe money (overdueAmount > 0)
    List<Customer> findByOverdueAmountGreaterThan(double amount);
    @Query("SELECT c FROM Customer c WHERE c.overdueAmount > 0")
    List<Customer> findCustomersWithDues();
}
