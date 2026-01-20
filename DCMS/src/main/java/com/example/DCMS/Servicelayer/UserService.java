package com.example.DCMS.Servicelayer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.DCMS.Entity.Customer;
import com.example.DCMS.Entity.User;
import com.example.DCMS.Repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	@Autowired
    private  UserRepository userRepository;

	@Autowired
	 private  PasswordEncoder passwordEncoder;
    public User getByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
    public User createCustomerUser(
            String username,
            String rawPassword,
            Customer customer   // ✅ ENTITY, NOT DTO
    ) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setRole("CUSTOMER");
        user.setCustomer(customer);

        return userRepository.save(user);
    }


    public User save(User user) {
        return userRepository.save(user);
    }
}
