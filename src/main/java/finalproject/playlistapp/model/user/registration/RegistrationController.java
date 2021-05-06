package finalproject.playlistapp.model.user.registration;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/registration")
@AllArgsConstructor
public class RegistrationController {

    private final RegistrationService registrationService;

    @PostMapping
    public ResponseEntity<RegistrationRequest> register(@RequestBody RegistrationRequest request) {
        registrationService.register(request);
        return new ResponseEntity<>(request, HttpStatus.CREATED);
    }

    @GetMapping(path = "confirm")
    public void confirm(@RequestParam("token") String token) {
        registrationService.confirmToken(token);
    }
}
