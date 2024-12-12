package kea.guessr;

import kea.guessr.Controller.AuthController;
import kea.guessr.Model.User;
import kea.guessr.Repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.http.ResponseEntity;

import jakarta.servlet.http.HttpSession;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthControllerTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private HttpSession session;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCompleteRegistration_Success() {
        // Arrange
        String tempEmail = "test@example.com";
        String tempPassword = "password123";
        String username = "testuser";
        String name = "Test User";

        when(session.getAttribute("tempEmail")).thenReturn(tempEmail);
        when(session.getAttribute("tempPassword")).thenReturn(tempPassword);
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<String> response = authController.completeRegistration(username, name, session);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Registration completed successfully", response.getBody());

        verify(userRepository, times(1)).save(any(User.class));
        verify(session, times(1)).removeAttribute("tempEmail");
        verify(session, times(1)).removeAttribute("tempPassword");
    }

    @Test
    void testCompleteRegistration_UsernameExists() {
        // Arrange
        String tempEmail = "test@example.com";
        String tempPassword = "password123";
        String username = "existinguser";

        when(session.getAttribute("tempEmail")).thenReturn(tempEmail);
        when(session.getAttribute("tempPassword")).thenReturn(tempPassword);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(new User()));

        // Act
        ResponseEntity<String> response = authController.completeRegistration(username, "Name", session);

        // Assert
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Username already exists", response.getBody());

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testCompleteRegistration_NoPreRegistrationData() {
        // Arrange
        when(session.getAttribute("tempEmail")).thenReturn(null);
        when(session.getAttribute("tempPassword")).thenReturn(null);

        // Act
        ResponseEntity<String> response = authController.completeRegistration("testuser", "Name", session);

        // Assert
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("No pre-registration data found", response.getBody());
    }

    @Test
    void testDeleteAccount_Success() {
        // Arrange
        String username = "testuser";
        User mockUser = new User();
        mockUser.setUsername(username);

        when(session.getAttribute("username")).thenReturn(username);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(mockUser));

        // Act
        ResponseEntity<String> response = authController.deleteAccount(username, session);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Account deleted successfully", response.getBody());

        verify(userRepository, times(1)).delete(mockUser);
        verify(session, times(1)).invalidate();
    }

    @Test
    void testDeleteAccount_Unauthorized() {
        // Arrange
        String username = "testuser";

        when(session.getAttribute("username")).thenReturn("anotheruser");

        // Act
        ResponseEntity<String> response = authController.deleteAccount(username, session);

        // Assert
        assertEquals(401, response.getStatusCodeValue());
        assertEquals("Unauthorized action", response.getBody());

        verify(userRepository, never()).delete(any(User.class));
    }

}
