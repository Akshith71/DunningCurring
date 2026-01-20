package com.example.DCMS.Security;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.DCMS.Entity.User;
import com.example.DCMS.Repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // 1. Fetch the User Entity from the DB
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // 2. Extract the Customer ID (if available)
        Long customerId = null;
        if (user.getCustomer() != null) {
            customerId = user.getCustomer().getId();
        }

        // 3. Return YOUR CustomUserPrincipal, not the standard User
        return new CustomUserPrincipal(
                user.getUsername(),
                user.getRole(),       // Pass the role string
                customerId,           // Pass the ID we just found
                List.of(new SimpleGrantedAuthority(user.getRole())) // Authorities
        );
    }
}