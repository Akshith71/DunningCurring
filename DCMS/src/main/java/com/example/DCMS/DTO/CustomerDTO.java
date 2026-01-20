package com.example.DCMS.DTO;

public class CustomerDTO {

    private Long id;
    private String name;
    private String email;
    private String mobile;
    private String customerType;
    
    public String getCustomerType() {
		return customerType;
	}
	public void setCustomerType(String customerType) {
		this.customerType = customerType;
	}
	private double overdueAmount;
    private int overdueDays;
    private String status;
    

	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
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
	public double getOverdueAmount() {
		return overdueAmount;
	}
	public void setOverdueAmount(double overdueAmount) {
		this.overdueAmount = overdueAmount;
	}
	public int getOverdueDays() {
		return overdueDays;
	}
	public void setOverdueDays(int overdueDays) {
		this.overdueDays = overdueDays;
	}
	public CustomerDTO(Long id, String name, String email, String mobile, double overdueAmount, int overdueDays) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.mobile = mobile;
		this.overdueAmount = overdueAmount;
		this.overdueDays = overdueDays;
	}
	public CustomerDTO(String name, String email, String mobile, double overdueAmount, int overdueDays) {
		super();
		this.name = name;
		this.email = email;
		this.mobile = mobile;
		this.overdueAmount = overdueAmount;
		this.overdueDays = overdueDays;
	}
	public CustomerDTO() {
		super();
	}

	
    // getters & setters
    
}
