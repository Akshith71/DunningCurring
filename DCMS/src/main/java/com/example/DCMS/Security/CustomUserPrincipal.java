package com.example.DCMS.Security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;

import org.springframework.security.core.userdetails.UserDetails;

@SuppressWarnings("serial")
public class CustomUserPrincipal implements UserDetails {

    private String username;
    private String role;
    private Long customerId;
    private Collection<? extends GrantedAuthority> authorities;

    public CustomUserPrincipal(
            String username,
            String role,
            Long customerId,
            Collection<? extends GrantedAuthority> authorities) {

        this.username = username;
        this.role = role;
        this.customerId = customerId;
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getUsername() {
        return username;
    }


    @Override public String getPassword() { return null; }
    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }

    public String getRole() {
        return role;
    }

    public Long getCustomerId() {
        return customerId;
    }
}
