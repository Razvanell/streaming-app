package finalproject.playlistapp.security.login;

import finalproject.playlistapp.model.user.User;
import finalproject.playlistapp.security.JwtTokenUtil;
import finalproject.playlistapp.security.util.ServerResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/login")
@AllArgsConstructor
public class LoginController {

    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationManager authenticationManager;

    @PostMapping
    public ServerResponse loginWithToken(@RequestBody LoginRequest request) {
        System.out.println(request.getEmail());
        System.out.println(request.getPassword());
        try {
            Authentication authenticate = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

            User user = (User) authenticate.getPrincipal();

            // Preparing object for client
            LoginResponse loginResponseModel = new LoginResponse(jwtTokenUtil.generateAccessToken(user), user);
            return new ServerResponse(HttpStatus.OK.value(), "Welcome" + user.getFirstName(), loginResponseModel);

        } catch (BadCredentialsException ex) {
            return new ServerResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
        }
    }

}

