package com.example.DCMS.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.DCMS.DTO.RegisterRequest;
import com.example.DCMS.Entity.Customer;
import com.example.DCMS.Entity.User;
import com.example.DCMS.Repository.UserRepository;
import com.example.DCMS.Security.JwtUtil;
import com.example.DCMS.Servicelayer.CustomerService;
import com.example.DCMS.Servicelayer.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

	@Autowired
    private  UserRepository userRepository;
	@Autowired
    private  PasswordEncoder passwordEncoder;
	@Autowired
    private  JwtUtil jwtUtil;
	
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private  UserService userService;

	@PostMapping("/login")
	public ResponseEntity<String> login(
	        @RequestParam String username,
	        @RequestParam String password
	) {
	    User user = userRepository.findByUsername(username)
	            .orElseThrow(() -> new RuntimeException("Invalid credentials"));

	    if (!passwordEncoder.matches(password, user.getPassword())) {
	        throw new RuntimeException("Invalid credentials");
	    }

	    Long customerId = null;
	    if ("CUSTOMER".equals(user.getRole()) && user.getCustomer() != null) {
	        customerId = user.getCustomer().getId();
	    }

	    String token = jwtUtil.generateToken(
	            user.getUsername(),
	            user.getRole(),
	            customerId
	    );

	    return ResponseEntity.ok(token);
	}
	
	@PostMapping("/register")
	public ResponseEntity<String> registerCustomer(
	        @RequestBody RegisterRequest request
	) {
	    // 1. Create Customer (ENTITY)
	    Customer customer = customerService.createCustomer(
	            request.getName(),
	            request.getEmail(),
	            request.getMobile(),
	            request.getCustomerType()
	    );

	    // 2. Create User linked to Customer
	    userService.createCustomerUser(
	            request.getUsername(),
	            request.getPassword(),
	            customer
	    );

	    return ResponseEntity.ok("Customer registered successfully");
	}

}
