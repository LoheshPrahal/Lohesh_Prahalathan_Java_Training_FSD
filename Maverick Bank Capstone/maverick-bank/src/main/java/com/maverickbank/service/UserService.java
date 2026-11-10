package com.maverickbank.service;

import com.maverickbank.enums.UserRole;
import com.maverickbank.exception.InvalidCallException;
import com.maverickbank.exception.ResourceNotFoundException;
import com.maverickbank.model.User;
import com.maverickbank.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
// Logging - the "log" object of this class comes from @Slf4j
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User getUserObject(String username, String password, UserRole role){
        if(userRepository.countByUsername(username) > 0){
            throw new InvalidCallException("Username is already taken!");
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(role);
        userRepository.save(user);

        // Log: a login user is created (the password is never logged)
        log.info("User {} is created with the role {}", username, role);

        return user;
    }

    public User getUserByUsername(String username){
        // Log: user is fetched with the username
        log.info("Fetching user {}", username);

        User user = userRepository.loadUserByUsername(username, true);
        if(user == null) throw new ResourceNotFoundException("User not found!");
        return user;
    }

    public User getUserById(Long id){
        return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found!"));
    }

    public void deactivateUser(Long id){
        User user = getUserById(id);
        user.setActive(false);
        userRepository.save(user);

        // Log: the user cannot login anymore
        log.info("User id {} is deactivated", id);
    }
}
