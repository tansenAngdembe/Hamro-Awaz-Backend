package com.tansen.government.core.service;

import com.tansen.entity.AuthorityUser;
import com.tansen.repository.AuthorityUserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthorityUserDetailService implements UserDetailsService {
    private final AuthorityUserRepository authorityUserRepository;

    public AuthorityUserDetailService(AuthorityUserRepository authorityUserRepository) {
        this.authorityUserRepository = authorityUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<AuthorityUser> byEmail = authorityUserRepository.findByEmail(email);
        if(byEmail.isEmpty()) { throw new UsernameNotFoundException("User not found"); }

        return new org.springframework.security.core.userdetails.User(
                byEmail.get().getEmail(),
                byEmail.get().getPassword(),
                byEmail.get().getAuthorities());
    }
}
