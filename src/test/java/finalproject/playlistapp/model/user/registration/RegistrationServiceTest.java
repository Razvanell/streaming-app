package finalproject.playlistapp.model.user.registration;

import finalproject.playlistapp.model.user.User;
import finalproject.playlistapp.model.user.UserRole;
import finalproject.playlistapp.model.user.UserService;
import finalproject.playlistapp.model.user.registration.email.EmailValidator;
import finalproject.playlistapp.model.user.registration.token.ConfirmationToken;
import finalproject.playlistapp.model.user.registration.token.ConfirmationTokenService;
import finalproject.playlistapp.security.util.ServerResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ContextConfiguration(classes = {ConfirmationTokenService.class, EmailValidator.class, BCryptPasswordEncoder.class,
        RegistrationService.class, UserService.class})
@ExtendWith(SpringExtension.class)
public class RegistrationServiceTest {
    @MockBean
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @MockBean
    private ConfirmationTokenService confirmationTokenService;

    @MockBean
    private EmailValidator emailValidator;

    @Autowired
    private RegistrationService registrationService;

    @MockBean
    private UserService userService;

    @Test
    public void testRegister() {
        doNothing().when(this.userService).postUser(any());
        when(this.emailValidator.test(anyString())).thenReturn(true);
        when(this.bCryptPasswordEncoder.encode(any())).thenReturn("foo");
        RegistrationRequest registrationRequest = new RegistrationRequest("Jane", "Doe", "jane.doe@example.org", "iloveyou",
                "iloveyou", "https://example.org/example");
        ServerResponse actualRegisterResult = this.registrationService.register(registrationRequest);
        assertEquals(" ", actualRegisterResult.getError());
        assertEquals(200, actualRegisterResult.getStatus());
        assertSame(registrationRequest, actualRegisterResult.getResult());
        assertEquals("User created", actualRegisterResult.getMessage());
        verify(this.bCryptPasswordEncoder).encode(any());
        verify(this.emailValidator).test(anyString());
        verify(this.userService).postUser(any());
    }

    @Test
    public void testRegister2() {
        doNothing().when(this.userService).postUser(any());
        when(this.emailValidator.test(anyString())).thenReturn(false);
        when(this.bCryptPasswordEncoder.encode(any())).thenReturn("foo");
        ServerResponse actualRegisterResult = this.registrationService.register(new RegistrationRequest("Jane", "Doe",
                "jane.doe@example.org", "iloveyou", "iloveyou", "https://example.org/example"));
        assertEquals("Invalid Email", actualRegisterResult.getError());
        assertEquals(400, actualRegisterResult.getStatus());
        assertNull(actualRegisterResult.getResult());
        assertEquals("", actualRegisterResult.getMessage());
        verify(this.emailValidator).test(anyString());
    }

    @Test
    public void testRegister3() {
        doNothing().when(this.userService).postUser(any());
        when(this.emailValidator.test(anyString())).thenReturn(true);
        when(this.bCryptPasswordEncoder.encode(any())).thenReturn("foo");
        ServerResponse actualRegisterResult = this.registrationService.register(new RegistrationRequest("Jane", "Doe",
                "jane.doe@example.org", "User created", "iloveyou", "https://example.org/example"));
        assertEquals("Passwords do not match", actualRegisterResult.getError());
        assertEquals(400, actualRegisterResult.getStatus());
        assertNull(actualRegisterResult.getResult());
        assertEquals("", actualRegisterResult.getMessage());
        verify(this.emailValidator).test(anyString());
    }

    @Test
    public void testRegister4() {
        doNothing().when(this.userService).postUser(any());
        when(this.emailValidator.test(anyString())).thenReturn(true);
        when(this.bCryptPasswordEncoder.encode(any())).thenReturn("foo");
        RegistrationRequest registrationRequest = mock(RegistrationRequest.class);
        when(registrationRequest.getImageUrl()).thenReturn("https://example.org/example");
        when(registrationRequest.getLastName()).thenReturn("foo");
        when(registrationRequest.getFirstName()).thenReturn("foo");
        when(registrationRequest.getConfirmPassword()).thenReturn("foo");
        when(registrationRequest.getPassword()).thenReturn("foo");
        when(registrationRequest.getEmail()).thenReturn("foo");
        ServerResponse actualRegisterResult = this.registrationService.register(registrationRequest);
        assertEquals(" ", actualRegisterResult.getError());
        assertEquals(200, actualRegisterResult.getStatus());
        assertEquals("User created", actualRegisterResult.getMessage());
        verify(this.bCryptPasswordEncoder).encode(any());
        verify(this.emailValidator).test(anyString());
        verify(registrationRequest).getFirstName();
        verify(registrationRequest).getLastName();
        verify(registrationRequest, times(2)).getEmail();
        verify(registrationRequest).getImageUrl();
        verify(registrationRequest, times(2)).getPassword();
        verify(registrationRequest).getConfirmPassword();
        verify(this.userService).postUser(any());
    }

    @Test
    public void testConfirmToken() {
        User user = new User();
        user.setLastName("Doe");
        user.setEmail("jane.doe@example.org");
        user.setPassword("iloveyou");
        user.setTokens(new ArrayList<>());
        user.setPlaylists(new ArrayList<>());
        user.setLocked(true);
        user.setImageUrl("https://example.org/example");
        user.setId(123L);
        user.setUserRole(UserRole.USER);
        user.setEnabled(true);
        user.setFirstName("Jane");

        ConfirmationToken confirmationToken = new ConfirmationToken();
        confirmationToken.setConfirmedAt(LocalDateTime.of(1, 1, 1, 1, 1));
        confirmationToken.setCreatedAt(LocalDateTime.of(1, 1, 1, 1, 1));
        confirmationToken.setId(123L);
        confirmationToken.setExpiresAt(LocalDateTime.of(1, 1, 1, 1, 1));
        confirmationToken.setToken("ABC123");
        confirmationToken.setUser(user);
        Optional<ConfirmationToken> ofResult = Optional.of(confirmationToken);
        when(this.confirmationTokenService.getToken(anyString())).thenReturn(ofResult);
        ServerResponse actualConfirmTokenResult = this.registrationService.confirmToken("ABC123");
        assertEquals("Email already confirmed", actualConfirmTokenResult.getError());
        assertEquals(400, actualConfirmTokenResult.getStatus());
        assertNull(actualConfirmTokenResult.getResult());
        assertEquals("", actualConfirmTokenResult.getMessage());
        verify(this.confirmationTokenService).getToken(anyString());
    }

    @Test
    public void testConfirmToken2() {
        User user = new User();
        user.setLastName("Doe");
        user.setEmail("jane.doe@example.org");
        user.setPassword("iloveyou");
        user.setTokens(new ArrayList<>());
        user.setPlaylists(new ArrayList<>());
        user.setLocked(true);
        user.setImageUrl("https://example.org/example");
        user.setId(123L);
        user.setUserRole(UserRole.USER);
        user.setEnabled(true);
        user.setFirstName("Jane");

        ConfirmationToken confirmationToken = new ConfirmationToken();
        confirmationToken.setConfirmedAt(null);
        confirmationToken.setCreatedAt(LocalDateTime.of(1, 1, 1, 1, 1));
        confirmationToken.setId(123L);
        confirmationToken.setExpiresAt(LocalDateTime.of(1, 1, 1, 1, 1));
        confirmationToken.setToken("ABC123");
        confirmationToken.setUser(user);
        Optional<ConfirmationToken> ofResult = Optional.of(confirmationToken);
        when(this.confirmationTokenService.getToken(anyString())).thenReturn(ofResult);
        ServerResponse actualConfirmTokenResult = this.registrationService.confirmToken("ABC123");
        assertEquals("Token expired", actualConfirmTokenResult.getError());
        assertEquals(400, actualConfirmTokenResult.getStatus());
        assertNull(actualConfirmTokenResult.getResult());
        assertEquals("", actualConfirmTokenResult.getMessage());
        verify(this.confirmationTokenService).getToken(anyString());
    }

    @Test
    public void testConfirmToken3() {
        when(this.confirmationTokenService.getToken(anyString())).thenReturn(Optional.empty());
        this.registrationService.confirmToken("ABC123");
        verify(this.confirmationTokenService).getToken(anyString());
    }
}

