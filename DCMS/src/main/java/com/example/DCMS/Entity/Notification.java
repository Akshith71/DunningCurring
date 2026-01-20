package com.example.DCMS.Entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "notifications")

public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // SMS / EMAIL / APP
    private String channel;

    private String message;
    private LocalDateTime sentAt;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

	public Notification(Long id, String channel, String message, LocalDateTime sentAt, Customer customer) {
		super();
		this.id = id;
		this.channel = channel;
		this.message = message;
		this.sentAt = sentAt;
		this.customer = customer;
	}

	public Notification(String channel, String message, LocalDateTime sentAt, Customer customer) {
		super();
		this.channel = channel;
		this.message = message;
		this.sentAt = sentAt;
		this.customer = customer;
	}

	public Notification() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public LocalDateTime getSentAt() {
		return sentAt;
	}

	public void setSentAt(LocalDateTime sentAt) {
		this.sentAt = sentAt;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
    
    
}
