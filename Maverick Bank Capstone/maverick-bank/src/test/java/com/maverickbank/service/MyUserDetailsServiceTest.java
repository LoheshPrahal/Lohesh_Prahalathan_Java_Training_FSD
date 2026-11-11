package com.maverickbank.service;

import com.maverickbank.model.User;
import com.maverickbank.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MyUserDetailsServiceTest {
    @InjectMocks
    private MyUserDetailsService myUserDetailsService;
    @Mock
    private UserRepository userRepository;

    @Test
    public void loadUserByUsernameTest() {
        User user = new User();
        user.setUsername("john");
        when(userRepository.loadUserByUsername("john", true)).thenReturn(user);

        assertEquals("john", myUserDetailsService.loadUserByUsername("john").getUsername());
        verify(userRepository, times(1)).loadUserByUsername("john", true);
    }

    @Test
    public void loadUserByUsernameTest_Invalid() {
        when(userRepository.loadUserByUsername("missing", true)).thenReturn(null);

        assertThrows(UsernameNotFoundException.class,
                () -> myUserDetailsService.loadUserByUsername("missing"));
        verify(userRepository, times(1)).loadUserByUsername("missing", true);
    }
}
