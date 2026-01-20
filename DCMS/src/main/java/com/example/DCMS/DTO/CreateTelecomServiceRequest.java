package com.example.DCMS.DTO;

public class CreateTelecomServiceRequest {

    private Long customerId;
    private String serviceType;   // MOBILE / BROADBAND
    private String identifier;    // phone / account number

    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }

    public String getServiceType() { return serviceType; }
    public void setServiceType(String serviceType) { this.serviceType = serviceType; }

    public String getIdentifier() { return identifier; }
    public void setIdentifier(String identifier) { this.identifier = identifier; }
}

