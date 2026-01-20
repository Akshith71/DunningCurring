package com.example.DCMS.Servicelayer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.DCMS.Constants.ServiceStatus;
import com.example.DCMS.Entity.Customer;
import com.example.DCMS.Entity.TelecomService;
import com.example.DCMS.Repository.CustomerRepository;
import com.example.DCMS.Repository.TelecomServiceRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TelecomServiceService {

	@Autowired
    private  TelecomServiceRepository telecomServiceRepository;
	@Autowired
    private  CustomerRepository customerRepository;
	  public TelecomService updateServiceStatus(Long serviceId, String status) {

	        TelecomService service = telecomServiceRepository.findById(serviceId)
	                .orElseThrow(() -> new RuntimeException("Service not found"));

	        service.setStatus(status);
	        return telecomServiceRepository.save(service);
	    }
	  public TelecomService activate(Long serviceId) {
	        return updateServiceStatus(serviceId, ServiceStatus.ACTIVE);
	    }

	    public TelecomService throttle(Long serviceId) {
	        return updateServiceStatus(serviceId, ServiceStatus.THROTTLED);
	    }

	    public TelecomService block(Long serviceId) {
	        return updateServiceStatus(serviceId, ServiceStatus.BLOCKED);
	    }

    // ✅ ADMIN ONLY
    public TelecomService createService(
            Long customerId,
            String serviceType,
            String identifier
    ) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        TelecomService service = new TelecomService();
        service.setCustomer(customer);
        service.setServiceType(serviceType);
        service.setIdentifier(identifier);
        service.setStatus("ACTIVE"); // default

        return telecomServiceRepository.save(service);
    }
    
}

