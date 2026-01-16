package vinicius.dev.CronoTask.domain.usecases;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import vinicius.dev.CronoTask.domain.entities.User;
import vinicius.dev.CronoTask.domain.repositories.UserRepository;
import vinicius.dev.CronoTask.dto.UserInputDTO;
import vinicius.dev.CronoTask.dto.UserOutputDTO;
import vinicius.dev.CronoTask.dto.UserPatchDTO;
import vinicius.dev.CronoTask.infra.exceptions.EmailAlreadyExistsException;
import vinicius.dev.CronoTask.infra.exceptions.ResourceNotFoundException;
import vinicius.dev.CronoTask.infra.mappers.UserMapper;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserUseCase Tests")
class UserUseCaseImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserUseCaseImpl userUseCase;

    private UUID userId;
    private User user;
    private UserInputDTO inputDTO;
    private UserOutputDTO outputDTO;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();

        user = new User(
                userId,
                "Test User",
                "test@example.com",
                "password123"
        );

        inputDTO = new UserInputDTO();
        inputDTO.setName("Test User");
        inputDTO.setEmail("test@example.com");
        inputDTO.setPassword("password123");

        outputDTO = new UserOutputDTO(
                userId,
                "Test User",
                "test@example.com"
        );
    }

    @Test
    @DisplayName("Should create user successfully")
    void shouldCreateUserSuccessfully() {
        // Given
        when(userRepository.existsByEmail(inputDTO.getEmail())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.toDTO(any(User.class))).thenReturn(outputDTO);

        // When
        UserOutputDTO result = userUseCase.create(inputDTO);

        // Then
        assertNotNull(result);
        assertEquals(userId, result.getId());
        assertEquals("Test User", result.getName());
        assertEquals("test@example.com", result.getEmail());
        verify(userRepository, times(1)).existsByEmail(inputDTO.getEmail());
        verify(userRepository, times(1)).save(any(User.class));
        verify(userMapper, times(1)).toDTO(any(User.class));
    }

    @Test
    @DisplayName("Should throw EmailAlreadyExistsException when email already exists")
    void shouldThrowExceptionWhenEmailAlreadyExists() {
        // Given
        when(userRepository.existsByEmail(inputDTO.getEmail())).thenReturn(true);

        // When & Then
        EmailAlreadyExistsException exception = assertThrows(
                EmailAlreadyExistsException.class,
                () -> userUseCase.create(inputDTO)
        );

        assertTrue(exception.getMessage().contains("Email already exists"));
        verify(userRepository, times(1)).existsByEmail(inputDTO.getEmail());
        verify(userRepository, never()).save(any());
        verify(userMapper, never()).toDTO(any());
    }

    @Test
    @DisplayName("Should find user by id successfully")
    void shouldFindUserByIdSuccessfully() {
        // Given
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userMapper.toDTO(user)).thenReturn(outputDTO);

        // When
        UserOutputDTO result = userUseCase.findById(userId);

        // Then
        assertNotNull(result);
        assertEquals(userId, result.getId());
        assertEquals("Test User", result.getName());
        verify(userRepository, times(1)).findById(userId);
        verify(userMapper, times(1)).toDTO(user);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when user not found by id")
    void shouldThrowExceptionWhenUserNotFoundById() {
        // Given
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // When & Then
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> userUseCase.findById(userId)
        );

        assertTrue(exception.getMessage().contains("User not found"));
        verify(userRepository, times(1)).findById(userId);
        verify(userMapper, never()).toDTO(any());
    }

    @Test
    @DisplayName("Should find user by email successfully")
    void shouldFindUserByEmailSuccessfully() {
        // Given
        String email = "test@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(userMapper.toDTO(user)).thenReturn(outputDTO);

        // When
        UserOutputDTO result = userUseCase.findByEmail(email);

        // Then
        assertNotNull(result);
        assertEquals(email, result.getEmail());
        verify(userRepository, times(1)).findByEmail(email);
        verify(userMapper, times(1)).toDTO(user);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when user not found by email")
    void shouldThrowExceptionWhenUserNotFoundByEmail() {
        // Given
        String email = "notfound@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // When & Then
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> userUseCase.findByEmail(email)
        );

        assertTrue(exception.getMessage().contains("User not found"));
        verify(userRepository, times(1)).findByEmail(email);
        verify(userMapper, never()).toDTO(any());
    }

    @Test
    @DisplayName("Should update user successfully")
    void shouldUpdateUserSuccessfully() {
        // Given
        inputDTO.setName("Updated User");
        inputDTO.setEmail("test@example.com"); // Same email

        User updatedUser = new User(userId, "Updated User", "test@example.com", "password123");
        UserOutputDTO updatedOutputDTO = new UserOutputDTO(userId, "Updated User", "test@example.com");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);
        when(userMapper.toDTO(updatedUser)).thenReturn(updatedOutputDTO);

        // When
        UserOutputDTO result = userUseCase.update(userId, inputDTO);

        // Then
        assertNotNull(result);
        assertEquals("Updated User", result.getName());
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Should throw exception when updating with existing email")
    void shouldThrowExceptionWhenUpdatingWithExistingEmail() {
        // Given
        inputDTO.setEmail("another@example.com");
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.existsByEmail("another@example.com")).thenReturn(true);

        // When & Then
        EmailAlreadyExistsException exception = assertThrows(
                EmailAlreadyExistsException.class,
                () -> userUseCase.update(userId, inputDTO)
        );

        assertTrue(exception.getMessage().contains("Email already exists"));
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw exception when updating non-existent user")
    void shouldThrowExceptionWhenUpdatingNonExistentUser() {
        // Given
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> userUseCase.update(userId, inputDTO));
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should patch user name successfully")
    void shouldPatchUserNameSuccessfully() {
        // Given
        UserPatchDTO patchDTO = new UserPatchDTO();
        patchDTO.setName("Patched Name");

        User patchedUser = new User(userId, "Patched Name", "test@example.com", "password123");
        UserOutputDTO patchedOutputDTO = new UserOutputDTO(userId, "Patched Name", "test@example.com");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(patchedUser);
        when(userMapper.toDTO(patchedUser)).thenReturn(patchedOutputDTO);

        // When
        UserOutputDTO result = userUseCase.patch(userId, patchDTO);

        // Then
        assertNotNull(result);
        assertEquals("Patched Name", result.getName());
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Should patch user email successfully")
    void shouldPatchUserEmailSuccessfully() {
        // Given
        UserPatchDTO patchDTO = new UserPatchDTO();
        patchDTO.setEmail("newemail@example.com");

        User patchedUser = new User(userId, "Test User", "newemail@example.com", "password123");
        UserOutputDTO patchedOutputDTO = new UserOutputDTO(userId, "Test User", "newemail@example.com");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.existsByEmail("newemail@example.com")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(patchedUser);
        when(userMapper.toDTO(patchedUser)).thenReturn(patchedOutputDTO);

        // When
        UserOutputDTO result = userUseCase.patch(userId, patchDTO);

        // Then
        assertNotNull(result);
        assertEquals("newemail@example.com", result.getEmail());
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).existsByEmail("newemail@example.com");
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Should throw exception when patching with existing email")
    void shouldThrowExceptionWhenPatchingWithExistingEmail() {
        // Given
        UserPatchDTO patchDTO = new UserPatchDTO();
        patchDTO.setEmail("existing@example.com");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.existsByEmail("existing@example.com")).thenReturn(true);

        // When & Then
        EmailAlreadyExistsException exception = assertThrows(
                EmailAlreadyExistsException.class,
                () -> userUseCase.patch(userId, patchDTO)
        );

        assertTrue(exception.getMessage().contains("Email already exists"));
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should delete user successfully")
    void shouldDeleteUserSuccessfully() {
        // Given
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        doNothing().when(userRepository).deleteById(userId);

        // When
        userUseCase.delete(userId);

        // Then
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    @DisplayName("Should throw exception when deleting non-existent user")
    void shouldThrowExceptionWhenDeletingNonExistentUser() {
        // Given
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> userUseCase.delete(userId));
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, never()).deleteById(any());
    }
}
