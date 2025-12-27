package com.tansen.app.core.service;

import com.tansen.entity.Role;
import com.tansen.entity.User;
import com.tansen.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class UserDetailService implements UserDetailsService {
    private final UserRepository userRepository;
    public UserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username);
        if(user == null) { throw new UsernameNotFoundException("User not found"); }
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                getAuthorities(user.getRole()));
    }

    private Set<GrantedAuthority> getAuthorities(Role role) {
        return Set.of(new SimpleGrantedAuthority("ROLE_" + role.getName()));
    }
}