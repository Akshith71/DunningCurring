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
@Table(name = "curing_actions")
public class CuringAction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime curedAt;

    // AUTO / MANUAL
    private String curedBy;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

	public CuringAction(Long id, LocalDateTime curedAt, String curedBy, Customer customer) {
		super();
		this.id = id;
		this.curedAt = curedAt;
		this.curedBy = curedBy;
		this.customer = customer;
	}

	public CuringAction(LocalDateTime curedAt, String curedBy, Customer customer) {
		super();
		this.curedAt = curedAt;
		this.curedBy = curedBy;
		this.customer = customer;
	}

	public CuringAction() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDateTime getCuredAt() {
		return curedAt;
	}

	public void setCuredAt(LocalDateTime curedAt) {
		this.curedAt = curedAt;
	}

	public String getCuredBy() {
		return curedBy;
	}

	public void setCuredBy(String curedBy) {
		this.curedBy = curedBy;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
    
    
}

