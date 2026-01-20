package com.example.DCMS.Servicelayer;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // ✅ Keep this for Lazy Loading

import com.example.DCMS.DTO.CustomerDTO;
import com.example.DCMS.DTO.DunningLogDTO;
import com.example.DCMS.DTO.PaymentDTO;
import com.example.DCMS.DTO.TelecomServiceDTO;
import com.example.DCMS.Entity.Customer;
import com.example.DCMS.Entity.DunningLog;
import com.example.DCMS.Entity.TelecomService;
import com.example.DCMS.Repository.CustomerRepository;
import com.example.DCMS.Repository.DunningLogRepository;
import com.example.DCMS.Repository.PaymentRepository;
import com.example.DCMS.Repository.TelecomServiceRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;
    
    @Autowired
    private TelecomServiceRepository telecomServiceRepository; 
    
    @Autowired
    private PaymentRepository paymentRepository;
    
    @Autowired
    private DunningLogRepository dunningLogRepository;

    // ================= CUSTOMER LIST =================
    @Transactional(readOnly = true)
    public List<CustomerDTO> getAllCustomers() {
        return customerRepository.findAll()
                .stream()
                .map(this::convertToDTO) // ✅ CRITICAL FIX: Use the helper method!
                .collect(Collectors.toList());
    }

    // ================= SINGLE CUSTOMER =================
    @Transactional(readOnly = true)
    public CustomerDTO getCustomer(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        return convertToDTO(customer); // ✅ CRITICAL FIX: Use the helper method!
    }

    // ================= SERVICES =================
    @Transactional(readOnly = true)
    public List<TelecomServiceDTO> getCustomerServices(Long customerId) {
        return telecomServiceRepository.findByCustomerId(customerId)
                .stream()
                .map(service -> {
                    TelecomServiceDTO dto = new TelecomServiceDTO();
                    dto.setId(service.getId());
                    dto.setServiceType(service.getServiceType());
                    dto.setStatus(service.getStatus());
                    return dto;
                })
                .toList();
    }

    // ================= PAYMENTS =================
    @Transactional(readOnly = true)
    public List<PaymentDTO> getPaymentHistory(Long customerId) {
        return paymentRepository.findByCustomerId(customerId)
                .stream()
                .map(payment -> {
                    PaymentDTO dto = new PaymentDTO();
                    dto.setAmount(payment.getAmount());
                    dto.setStatus(payment.getStatus());
                    dto.setPaymentDate(payment.getPaymentDate());
                    return dto;
                })
                .toList();
    }

    // ================= DUNNING LOGS =================
    @Transactional(readOnly = true)
    public List<DunningLogDTO> getDunningHistory(Long customerId) {
        return dunningLogRepository.findByCustomerId(customerId)
                .stream()
                .map(log -> {
                    DunningLogDTO dto = new DunningLogDTO();
                    dto.setAction(log.getAction());
                    dto.setActionDate(log.getActionDate());
                    return dto;
                })
                .toList();
    }

    // ================= CREATE CUSTOMER =================
    @Transactional
    public Customer createCustomer(String name, String email, String mobile, String customerType) {
        Customer customer = new Customer();
        customer.setName(name);
        customer.setEmail(email);
        customer.setMobile(mobile);
        customer.setCustomerType(customerType);
        customer.setOverdueAmount(0);
        customer.setOverdueDays(0);
        return customerRepository.save(customer);
    }
    
    public List<DunningLogDTO> getCustomerNotifications(Long customerId) {
        List<DunningLog> logs = dunningLogRepository.findByCustomerIdOrderByActionDateDesc(customerId);
        
        // Convert Entity -> DTO so React understands it
        return logs.stream().map(log -> {
            DunningLogDTO dto = new DunningLogDTO();
            dto.setId(log.getId());
            dto.setAction(log.getAction());
            dto.setMessage(log.getMessage()); // Ensure your Entity has getMessage()
            dto.setActionDate(log.getActionDate());
            return dto;
        }).collect(Collectors.toList());
    }
   
    @Transactional
    public void updateServiceStatus(Long serviceId, String status) {
        TelecomService service = telecomServiceRepository.findById(serviceId)
                .orElseThrow(() -> new RuntimeException("Service not found with ID: " + serviceId));

        service.setStatus(status); 
        telecomServiceRepository.save(service);
    }

    // 🔥 HELPER METHOD (The Logic)
    private CustomerDTO convertToDTO(Customer customer) {
        CustomerDTO dto = new CustomerDTO();
        dto.setId(customer.getId());
        dto.setName(customer.getName());
        dto.setEmail(customer.getEmail());
        dto.setMobile(customer.getMobile());
        dto.setCustomerType(customer.getCustomerType());
        dto.setOverdueAmount(customer.getOverdueAmount());
        dto.setOverdueDays(customer.getOverdueDays());

        // --- CHECK BLOCKED STATUS ---
        boolean isBlocked = false;
        
        if (customer.getServices() != null && !customer.getServices().isEmpty()) {
            isBlocked = customer.getServices().stream()
                    .anyMatch(s -> "BLOCKED".equalsIgnoreCase(s.getStatus()));
        }

        if (isBlocked) {
            dto.setStatus("BLOCKED"); // ✅ Sets status to BLOCKED for frontend
            System.out.println("Customer " + customer.getId() + " status set to BLOCKED"); // Debug log
        } else if (customer.getOverdueDays() > 0) {
            dto.setStatus("OVERDUE");
        } else {
            dto.setStatus("ACTIVE");
        }

        return dto;
    }
}