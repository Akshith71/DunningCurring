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
@Table(name = "dunning_logs")

public class DunningLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // NOTIFY / THROTTLE / BLOCK
    private String action;
    private String message;

    private LocalDateTime actionDate;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

	public DunningLog(Long id, String action, LocalDateTime actionDate, Customer customer) {
		super();
		this.id = id;
		this.action = action;
		this.actionDate = actionDate;
		this.customer = customer;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public DunningLog(String action, LocalDateTime actionDate, Customer customer) {
		super();
		this.action = action;
		this.actionDate = actionDate;
		this.customer = customer;
	}

	public DunningLog() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public LocalDateTime getActionDate() {
		return actionDate;
	}

	public void setActionDate(LocalDateTime actionDate) {
		this.actionDate = actionDate;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	
    
    
}
