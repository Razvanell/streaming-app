package finalproject.playlistapp.model.user.registration;

import finalproject.playlistapp.model.user.User;
import finalproject.playlistapp.model.user.UserRole;
import finalproject.playlistapp.model.user.UserService;
import finalproject.playlistapp.model.user.registration.token.ConfirmationToken;
import finalproject.playlistapp.model.user.registration.token.ConfirmationTokenService;
import finalproject.playlistapp.security.util.ServerResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class RegistrationService {

    private final UserService userService;
    private final EmailValidator emailValidator;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ConfirmationTokenService confirmationTokenService;

    public ServerResponse register(RegistrationRequest request) {
        if (!emailValidator.test(request.getEmail())) {
            return new ServerResponse(HttpStatus.BAD_REQUEST.value(), "Invalid Email");
        }

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            System.out.println("passwords misstach");
            return new ServerResponse(HttpStatus.BAD_REQUEST.value(), "Passwords do not match");
        }
        userService.postUser(User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(bCryptPasswordEncoder.encode(request.getPassword()))
                .imageUrl(request.getImageUrl())
                .userRole(UserRole.USER)
                .build());
        return new ServerResponse(HttpStatus.OK.value(), "User created", request);
    }

    @Transactional
    public void confirmToken(String token) {
        ConfirmationToken confirmationToken = confirmationTokenService.getToken(token).orElseThrow(() -> new IllegalStateException("token not found"));

        if (confirmationToken.getConfirmedAt() != null) {
            throw new IllegalStateException("Email already confirmed");
        }

        if (confirmationToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Token expired");
        }

        confirmationTokenService.setConfirmedAt(token);
        userService.enableUser(confirmationToken.getUser().getEmail());
    }



}
