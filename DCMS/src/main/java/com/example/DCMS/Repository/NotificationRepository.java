package com.example.DCMS.Repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.DCMS.Entity.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByCustomerId(Long customerId);
}
