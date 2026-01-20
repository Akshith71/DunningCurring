package com.example.DCMS.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.DCMS.DTO.CustomerDTO;
import com.example.DCMS.DTO.DunningLogDTO;
import com.example.DCMS.DTO.PaymentDTO;
import com.example.DCMS.DTO.TelecomServiceDTO;
import com.example.DCMS.Servicelayer.CustomerService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin/customers")
@RequiredArgsConstructor
public class AdminCustomerController {

	@Autowired
    private CustomerService customerService;
	@GetMapping("/{customerId}")
    public CustomerDTO getCustomer(@PathVariable Long customerId) {
        return customerService.getCustomer(customerId);
    }
    @GetMapping("/customers")
    public List<CustomerDTO> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    @GetMapping("/{customerId}/services")
    public List<TelecomServiceDTO> getServices(@PathVariable Long customerId) {
        return customerService.getCustomerServices(customerId);
    }

    @GetMapping("/{customerId}/payments")
    public List<PaymentDTO> getPayments(@PathVariable Long customerId) {
        return customerService.getPaymentHistory(customerId);
    }

    @GetMapping("/{customerId}/logs")
    public List<DunningLogDTO> getDunningLogs(@PathVariable Long customerId) {
        return customerService.getDunningHistory(customerId);
    }
   
}
