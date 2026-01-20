package com.example.DCMS.Controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.DCMS.DTO.DunningLogDTO;
import com.example.DCMS.DTO.PaymentDTO;
import com.example.DCMS.DTO.TelecomServiceDTO;
import com.example.DCMS.DTO.TicketRequest;
import com.example.DCMS.Entity.Customer;
import com.example.DCMS.Entity.TelecomService;
import com.example.DCMS.Entity.Ticket;
import com.example.DCMS.Repository.CustomerRepository;
import com.example.DCMS.Repository.DunningLogRepository;
import com.example.DCMS.Repository.TelecomServiceRepository;
import com.example.DCMS.Repository.TicketRepository;
import com.example.DCMS.Security.CustomUserPrincipal;
import com.example.DCMS.Servicelayer.CustomerService;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/customer")
public class CustomerController {

	@Autowired
    private  CustomerService customerService;

  
    @GetMapping("/profile")
    public Object getMyProfile(Authentication authentication) {
        CustomUserPrincipal principal =
                (CustomUserPrincipal) authentication.getPrincipal();

        return customerService.getCustomer(principal.getCustomerId());
    }

   
    @GetMapping("/services")
    public List<TelecomServiceDTO> getMyServices(Authentication authentication) {

        CustomUserPrincipal principal =
                (CustomUserPrincipal) authentication.getPrincipal();

        return customerService.getCustomerServices(principal.getCustomerId());
    }

   
    @GetMapping("/payments")
    public List<PaymentDTO> getMyPayments(Authentication authentication) {

        CustomUserPrincipal principal =
                (CustomUserPrincipal) authentication.getPrincipal();

        return customerService.getPaymentHistory(principal.getCustomerId());
    }

  
    @Autowired
    private DunningLogRepository dunningLogRepository;

    // Endpoint to fetch notifications for a logged-in customer
    @GetMapping("/{customerId}/notifications")
    public List<DunningLogDTO> getMyNotifications(@PathVariable Long customerId) {
        
        // Step 1: Find Customer Object first (Prevents JPA Naming Crash)
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        // Step 2: Pass Customer Object to Repository
        return dunningLogRepository.findByCustomerOrderByActionDateDesc(customer)
                .stream()
                .map(log -> {
                    DunningLogDTO dto = new DunningLogDTO();
                    dto.setId(log.getId());
                    dto.setAction(log.getAction());
                    dto.setMessage(log.getMessage());
                    dto.setActionDate(log.getActionDate()); 
                    return dto;
                })
                .collect(Collectors.toList());
    }
    @Autowired
    private TicketRepository ticketRepository;
    
    @Autowired
    private CustomerRepository customerRepository;
    
    // ✅ Endpoint: Customer Raises a Ticket
    @PostMapping("/{customerId}/tickets")
    public ResponseEntity<String> raiseTicket(
            @PathVariable Long customerId,
            @RequestBody TicketRequest ticketDTO // ✅ Changed from 'Ticket' to 'TicketRequest'
    ) {
        // 1. Fetch Customer
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        // 2. Create New Ticket Entity manually
        Ticket ticket = new Ticket();
        ticket.setCustomer(customer);
        ticket.setSubject(ticketDTO.getSubject());       // ✅ Map from DTO
        ticket.setDescription(ticketDTO.getDescription()); // ✅ Map from DTO
        ticket.setStatus("OPEN");
        ticket.setCreatedAt(LocalDateTime.now());

        // 3. Save
        ticketRepository.save(ticket);

        return ResponseEntity.ok("Ticket raised successfully! ID: " + ticket.getId());
    }
    @Autowired
    private TelecomServiceRepository telecomServiceRepository; // ✅ Ensure this is injected

    // ✅ NEW ENDPOINT: Add a New Service
    @PostMapping("/{customerId}/services")
    public ResponseEntity<String> addService(
            @PathVariable Long customerId,
            @RequestParam String serviceType
    ) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        TelecomService service = new TelecomService();
        service.setCustomer(customer);
        service.setServiceType(serviceType.toUpperCase());
        service.setStatus("ACTIVE"); // Default to active for simulation
        
        telecomServiceRepository.save(service);
        
        return ResponseEntity.ok("New service (" + serviceType + ") added successfully!");
    }
    
    
}
