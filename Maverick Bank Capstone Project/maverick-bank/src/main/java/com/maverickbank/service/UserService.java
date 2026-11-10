package com.maverickbank.service;

import com.maverickbank.enums.UserRole;
import com.maverickbank.exception.InvalidCallException;
import com.maverickbank.exception.ResourceNotFoundException;
import com.maverickbank.model.User;
import com.maverickbank.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
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
        return user;
    }

    public User getUserByUsername(String username){
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
    }
}
