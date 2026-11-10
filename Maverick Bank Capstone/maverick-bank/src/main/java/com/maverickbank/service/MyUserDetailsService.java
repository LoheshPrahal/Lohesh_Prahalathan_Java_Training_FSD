package com.maverickbank.service;

import com.maverickbank.model.User;
import com.maverickbank.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MyUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Loading user {}", username);

        User user = userRepository.loadUserByUsername(username,true);

        if(user == null){
            throw new UsernameNotFoundException("Invalid or inactive user");
        }

        return user;

    }
}
