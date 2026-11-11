package com.maverickbank.service;

import com.maverickbank.enums.UserRole;
import com.maverickbank.exception.InvalidCallException;
import com.maverickbank.exception.ResourceNotFoundException;
import com.maverickbank.model.User;
import com.maverickbank.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @InjectMocks
    private UserService userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    public void getUserObjectTest() {
        when(userRepository.countByUsername("john")).thenReturn(0L);
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");

        User user = userService.getUserObject("john", "password123", UserRole.CUSTOMER);

        assertEquals("john", user.getUsername());
        assertEquals("encodedPassword", user.getPassword());
        assertEquals(UserRole.CUSTOMER, user.getRole());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void getUserObjectTest_Invalid() {
        when(userRepository.countByUsername("john")).thenReturn(1L);

        assertThrows(InvalidCallException.class,
                () -> userService.getUserObject("john", "password123", UserRole.CUSTOMER));
        verify(userRepository, times(0)).save(any(User.class));
    }

    @Test
    public void getUserByUsernameTest() {
        User user = new User();
        user.setUsername("john");
        when(userRepository.loadUserByUsername("john", true)).thenReturn(user);

        assertEquals("john", userService.getUserByUsername("john").getUsername());
        verify(userRepository, times(1)).loadUserByUsername("john", true);
    }

    @Test
    public void getUserByUsernameTest_Invalid() {
        when(userRepository.loadUserByUsername("missing", true)).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> userService.getUserByUsername("missing"));
        verify(userRepository, times(1)).loadUserByUsername("missing", true);
    }

    @Test
    public void getUserByIdTest() {
        User user = new User();
        user.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        assertEquals(1L, userService.getUserById(1L).getId());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    public void getUserByIdTest_Invalid() {
        when(userRepository.findById(5L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.getUserById(5L));
        verify(userRepository, times(1)).findById(5L);
    }

    @Test
    public void deactivateUserTest() {
        User user = new User();
        user.setId(1L);
        user.setActive(true);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        userService.deactivateUser(1L);

        assertFalse(user.isActive());
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(user);
    }
}
