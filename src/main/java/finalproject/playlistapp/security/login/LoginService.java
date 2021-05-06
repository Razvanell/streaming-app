package finalproject.playlistapp.security.login;

import finalproject.playlistapp.model.user.User;
import finalproject.playlistapp.model.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class LoginService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public ResponseEntity<LoginRequest> login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new IllegalStateException("User does not exist"));

        if(bCryptPasswordEncoder.matches(request.getPassword(), user.getPassword())) {
            return new ResponseEntity<>(request, HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>(request, HttpStatus.CONFLICT);
    }

}

