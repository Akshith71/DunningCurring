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
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double amount;
    private LocalDateTime paymentDate;

    // SUCCESS / FAILED / PENDING
    private String status;

    private String Paymentmathod; 
    public String getPaymentmathod() {
		return Paymentmathod;
	}

	public void setPaymentmathod(String paymentmathod) {
		Paymentmathod = paymentmathod;
	}

	@ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public LocalDateTime getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(LocalDateTime paymentDate) {
		this.paymentDate = paymentDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Payment(Long id, double amount, LocalDateTime paymentDate, String status, Customer customer) {
		super();
		this.id = id;
		this.amount = amount;
		this.paymentDate = paymentDate;
		this.status = status;
		this.customer = customer;
	}

	public Payment(double amount, LocalDateTime paymentDate, String status, Customer customer) {
		super();
		this.amount = amount;
		this.paymentDate = paymentDate;
		this.status = status;
		this.customer = customer;
	}

	public Payment() {
		super();
	}
    
    
}
