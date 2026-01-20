package com.example.DCMS.Entity;



import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "customers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    @Email(message = "Invalid Email Format")
    private String email;
    @Pattern(regexp = "^\\d{10}$", message = "Mobile must be 10 digits")
    private String mobile;

    // PREPAID / POSTPAID
    private String customerType;

    private int overdueDays;
    private double overdueAmount;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<TelecomService> services;
    private String status;
    
    
	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	public Customer(Long id, String name, String email, String mobile, String customerType, int overdueDays,
			double overdueAmount, List<TelecomService> services) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.mobile = mobile;
		this.customerType = customerType;
		this.overdueDays = overdueDays;
		this.overdueAmount = overdueAmount;
		this.services = services;
	}
	

	public Customer() {
		super();
	}
	
	public Customer(String name, String email, String mobile, String customerType, int overdueDays,
			double overdueAmount, List<TelecomService> services) {
		super();
		this.name = name;
		this.email = email;
		this.mobile = mobile;
		this.customerType = customerType;
		this.overdueDays = overdueDays;
		this.overdueAmount = overdueAmount;
		this.services = services;
	}


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getCustomerType() {
		return customerType;
	}

	public void setCustomerType(String customerType) {
		this.customerType = customerType;
	}

	public int getOverdueDays() {
		return overdueDays;
	}

	public void setOverdueDays(int overdueDays) {
		this.overdueDays = overdueDays;
	}

	public double getOverdueAmount() {
		return overdueAmount;
	}

	public void setOverdueAmount(double overdueAmount) {
		this.overdueAmount = overdueAmount;
	}

	public List<TelecomService> getServices() {
		return services;
	}

	public void setServices(List<TelecomService> services) {
		this.services = services;
	}
    
    
}
