package com.example.lms.security;

import com.example.lms.user.UserRepository;
import com.example.lms.user.User;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    System.out.println("🔍 Looking for user with username: " + username);

    User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

    String password = user.getPassword();
    if (password == null || password.isEmpty()) {
        password = "N/A";
    }

    return new org.springframework.security.core.userdetails.User(
            user.getUsername(),
            password,
            Collections.singleton(new SimpleGrantedAuthority("ROLE_" + user.getRole())));
}

}
