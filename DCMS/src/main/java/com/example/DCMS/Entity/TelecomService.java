package com.example.DCMS.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "services")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TelecomService {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// Mobile / Broadband
	private String serviceType;

	// Phone number / Account ID
	private String identifier;

	// ACTIVE / THROTTLED / BLOCKED
	private String status;

	@ManyToOne
	@JoinColumn(name = "customer_id")
	@JsonBackReference
	private Customer customer;

	public TelecomService(Long id, String serviceType, String identifier, String status, Customer customer) {
		super();
		this.id = id;
		this.serviceType = serviceType;
		this.identifier = identifier;
		this.status = status;
		this.customer = customer;
	}

	public TelecomService(String serviceType, String identifier, String status, Customer customer) {
		super();
		this.serviceType = serviceType;
		this.identifier = identifier;
		this.status = status;
		this.customer = customer;
	}

	public TelecomService() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
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
	
	
}
