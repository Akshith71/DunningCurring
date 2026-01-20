package com.example.DCMS.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.DCMS.Entity.Customer;
import com.example.DCMS.Entity.TelecomService;
import com.example.DCMS.Entity.Ticket;
import com.example.DCMS.Repository.CustomerRepository;
import com.example.DCMS.Repository.TelecomServiceRepository; // ✅ Import
import com.example.DCMS.Repository.TicketRepository;         // ✅ Import
import com.example.DCMS.Servicelayer.GeminiService;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins = "http://localhost:5173")
public class ChatBotController {

    @Autowired private GeminiService geminiService;
    @Autowired private CustomerRepository customerRepository;
    
    // ✅ Inject new Repositories
    @Autowired private TelecomServiceRepository telecomServiceRepository; 
    @Autowired private TicketRepository ticketRepository; 

    @PostMapping("/ask")
    public ResponseEntity<Map<String, String>> chatWithAI(@RequestBody Map<String, Object> payload) {
        String userMessage = (String) payload.get("message");
        
        Object idObj = payload.get("customerId");
        Long customerId = (idObj != null) ? Long.valueOf(idObj.toString()) : null;

        Map<String, String> response = new HashMap<>();

        if (customerId == null) {
            response.put("response", "Please log in.");
            return ResponseEntity.ok(response);
        }

        Customer customer = customerRepository.findById(customerId).orElse(null);
        if (customer == null) {
            response.put("response", "Customer not found.");
            return ResponseEntity.ok(response);
        }

        // Get Services & Tickets for this customer
        List<TelecomService> services = telecomServiceRepository.findByCustomer(customer);
        List<Ticket> tickets = ticketRepository.findByCustomer(customer); // Assumes you have this method

    
        String aiResponse = geminiService.callGemini(userMessage, customer, services, tickets);
        
        response.put("response", aiResponse);
        return ResponseEntity.ok(response);
    }
}