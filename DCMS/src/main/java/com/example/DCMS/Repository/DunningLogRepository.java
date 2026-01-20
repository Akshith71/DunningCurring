package com.example.DCMS.Repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.DCMS.Entity.Customer;
import com.example.DCMS.Entity.DunningLog;

public interface DunningLogRepository extends JpaRepository<DunningLog, Long> {

    List<DunningLog> findByCustomerId(Long customerId);

    List<DunningLog> findByAction(String action);
    List<DunningLog> findByCustomerIdOrderByActionDateDesc(Customer customer);
    List<DunningLog> findByCustomerIdOrderByActionDateDesc(Long customerId);
    List<DunningLog> findByCustomerOrderByActionDateDesc(Customer customer);
}
