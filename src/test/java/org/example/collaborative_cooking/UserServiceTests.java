package org.example.collaborative_cooking;

import jakarta.persistence.EntityNotFoundException;
import org.example.collaborative_cooking.user.UserService;
import org.example.collaborative_cooking.user.dto.RequestUser;
import org.example.collaborative_cooking.user.dto.ResponseUser;
import org.example.collaborative_cooking.user.entity.User;
import org.example.collaborative_cooking.user.entity.UserRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    public void testCreateUser() {
        // Arrange
        RequestUser requestUser = new RequestUser("testuser", "testpassword", "test@example.com", "USER");
        User savedUser = new User(1L, "testuser", "testpassword", "test@example.com", "USER");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // Act
        ResponseUser response = userService.create(requestUser);

        // Assert
        assertNotNull(response);
        assertEquals("testuser", response.getUsername());
        assertEquals("test@example.com", response.getEmail());
        assertEquals("USER", response.getRole());
    }

    @Test
    public void testFindAllUsers() {
        // Arrange
        User user1 = new User(1L, "user1", "password1", "user1@example.com", "USER");
        User user2 = new User(2L, "user2", "password2", "user2@example.com", "ADMIN");
        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));

        // Act
        List<ResponseUser> users = userService.findAll();

        // Assert
        assertNotNull(users);
        assertEquals(2, users.size());
        assertEquals("user1", users.get(0).getUsername());
        assertEquals("user2", users.get(1).getUsername());
    }

    @Test
    public void testFindOneUser() {
        // Arrange
        Long userId = 1L;
        User user = new User(userId, "testuser", "testpassword", "test@example.com", "USER");
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // Act
        ResponseUser response = userService.findOne(userId);

        // Assert
        assertNotNull(response);
        assertEquals("testuser", response.getUsername());
        assertEquals("test@example.com", response.getEmail());
        assertEquals("USER", response.getRole());
    }

    @Test
    public void testFindOneUser_NotFound() {
        // Arrange
        Long userId = 999L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(EntityNotFoundException.class, () -> userService.findOne(userId));
    }

    @Test
    public void testUpdateUser() {
        // Arrange
        Long userId = 1L;
        RequestUser updatedUser = new RequestUser("updateduser", "updatedpassword", "updated@example.com", "ADMIN");
        User existingUser = new User(userId, "testuser", "testpassword", "test@example.com", "USER");
        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        ResponseUser updatedResponse = userService.update(userId, updatedUser);

        // Assert
        assertNotNull(updatedResponse);
        assertEquals("updateduser", updatedResponse.getUsername());
        assertEquals("USER", updatedResponse.getRole());
    }

    @Test
    public void testUpdateUser_NotFound() {
        // Arrange
        Long userId = 999L;
        RequestUser updatedUser = new RequestUser("updateduser", "updatedpassword", "updated@example.com", "ADMIN");
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(EntityNotFoundException.class, () -> userService.update(userId, updatedUser));
    }

    @Test
    public void testDeleteUser() {
        // Arrange
        Long userId = 1L;
        doNothing().when(userRepository).deleteById(userId);

        // Act
        Long deletedUserId = userService.deleteById(userId);

        // Assert
        assertEquals(userId, deletedUserId);
        verify(userRepository, times(1)).deleteById(userId);
    }
}
