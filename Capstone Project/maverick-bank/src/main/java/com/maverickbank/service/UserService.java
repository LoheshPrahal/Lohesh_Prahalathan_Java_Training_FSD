package com.maverickbank.service;

import com.maverickbank.enums.UserRole;
import com.maverickbank.exception.ResourceNotFoundException;
import com.maverickbank.model.User;
import com.maverickbank.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User getUserObject(String username, String password, UserRole role){
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setRole(role);
        userRepository.save(user);
        return user;
    }

    public User getUserById(long id){
        return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found!"));
    }

    public void deactivateUser(long id){
        User user = getUserById(id);
        user.setActive(false);
        userRepository.save(user);
    }
}
