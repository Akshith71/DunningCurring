package com.example.DCMS.DTO;

import java.time.LocalDateTime;

public class PaymentDTO {

    private double amount;
    private String status;
    private LocalDateTime paymentDate;
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public LocalDateTime getPaymentDate() {
		return paymentDate;
	}
	public void setPaymentDate(LocalDateTime paymentDate) {
		this.paymentDate = paymentDate;
	}

    
    // getters & setters
}
