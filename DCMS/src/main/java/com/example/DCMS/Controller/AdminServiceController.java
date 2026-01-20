package com.example.DCMS.Controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.DCMS.DTO.CustomerDTO;
import com.example.DCMS.DTO.TicketDTO;
import com.example.DCMS.Entity.Customer;
import com.example.DCMS.Entity.DunningLog;
import com.example.DCMS.Entity.Ticket;
import com.example.DCMS.Entity.TelecomService; // ✅ Import Added
import com.example.DCMS.Repository.CustomerRepository;
import com.example.DCMS.Repository.DunningLogRepository;
import com.example.DCMS.Repository.TicketRepository;
import com.example.DCMS.Repository.TelecomServiceRepository; // ✅ Import Added
import com.example.DCMS.Servicelayer.CustomerService;

@RestController
@RequestMapping("/api/admin/services")
@CrossOrigin(origins = "http://localhost:5173")
public class AdminServiceController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private DunningLogRepository dunningLogRepository;
    
    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private TelecomServiceRepository telecomServiceRepository; // ✅ INJECTED REPOSITORY

    // Existing Service Status Endpoint
    @PutMapping("/{serviceId}/status")
    public ResponseEntity<String> updateServiceStatus(
            @PathVariable Long serviceId, 
            @RequestParam String status
    ) {
        customerService.updateServiceStatus(serviceId, status);
        return ResponseEntity.ok("Service status updated to " + status);
    }

    // Existing Dunning Logs Endpoint
    @GetMapping("/dunning/logs")
    public ResponseEntity<List<DunningLog>> getAllDunningLogs() {
        return ResponseEntity.ok(dunningLogRepository.findAll());
    }

    // Simulation Endpoint
    @PostMapping("/simulate/notification")
    public ResponseEntity<String> simulateNotification(
            @RequestParam Long customerId,
            @RequestParam String action,
            @RequestParam String message
    ) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        DunningLog log = new DunningLog();
        log.setCustomer(customer);
        log.setAction(action);
        log.setMessage(message);
        // log.setChannel("ADMIN_SIMULATION");
        log.setActionDate(LocalDateTime.now());
        
        dunningLogRepository.save(log);

        return ResponseEntity.ok("Notification simulated successfully!");
    }

    // Get All Tickets
    @GetMapping("/tickets")
    public List<TicketDTO> getAllTickets() {
        return ticketRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // ✅ FIXED: Resolve Ticket & Unblock ALL Services
    @PostMapping("/tickets/{ticketId}/resolve")
    public ResponseEntity<?> resolveTicket(@PathVariable Long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));
        
        Customer customer = ticket.getCustomer();

        // 1. Safety Check: Are dues paid?
        if (customer.getOverdueAmount() > 0) {
            return ResponseEntity.badRequest().body("Cannot resolve: Customer still has pending dues of $" + customer.getOverdueAmount());
        }

        // 2. Unblock Customer Global Status
        customer.setStatus("ACTIVE"); 
        customerRepository.save(customer);
        
        // ✅ 3. Unblock ALL Individual Services
        // We fetch services by Customer ID and update them one by one
        List<TelecomService> services = telecomServiceRepository.findByCustomerId(customer.getId());
        for (TelecomService service : services) {
            service.setStatus("ACTIVE");
            telecomServiceRepository.save(service);
        }

        // 4. Close Ticket
        ticket.setStatus("RESOLVED");
        ticketRepository.save(ticket);

        // 5. Log to Dunning Logs
        DunningLog log = new DunningLog();
        log.setCustomer(customer);
        log.setAction("UNBLOCK"); 
        log.setMessage("Ticket #" + ticketId + " resolved. All Services unblocked.");
        // log.setChannel("ADMIN_TICKET");
        log.setActionDate(LocalDateTime.now());
        
        dunningLogRepository.save(log);

        // Return the DTO
        return ResponseEntity.ok(convertToDTO(ticket));
    }

    // Helper Method
    private TicketDTO convertToDTO(Ticket ticket) {
        TicketDTO dto = new TicketDTO();
        dto.setId(ticket.getId());
        dto.setSubject(ticket.getSubject());
        dto.setDescription(ticket.getDescription());
        dto.setStatus(ticket.getStatus());
        dto.setCreatedAt(ticket.getCreatedAt());

        if (ticket.getCustomer() != null) {
            CustomerDTO custDTO = new CustomerDTO();
            custDTO.setId(ticket.getCustomer().getId());
            custDTO.setName(ticket.getCustomer().getName());
            custDTO.setEmail(ticket.getCustomer().getEmail());
            custDTO.setMobile(ticket.getCustomer().getMobile());
            custDTO.setOverdueAmount(ticket.getCustomer().getOverdueAmount());
            custDTO.setOverdueDays(ticket.getCustomer().getOverdueDays());
            
            dto.setCustomer(custDTO);
        }
        return dto;
    }   
}