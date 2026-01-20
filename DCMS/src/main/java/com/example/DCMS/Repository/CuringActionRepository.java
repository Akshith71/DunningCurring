package com.example.DCMS.Repository;



import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.DCMS.Entity.CuringAction;

public interface CuringActionRepository extends JpaRepository<CuringAction, Long> {

    List<CuringAction> findByCustomerId(Long customerId);
}
