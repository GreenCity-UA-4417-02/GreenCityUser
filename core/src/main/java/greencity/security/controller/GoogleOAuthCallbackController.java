package greencity.security.controller;

import greencity.config.GoogleOAuthProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth/google")
public class GoogleOAuthCallbackController {
    private final GoogleOAuthProperties props;

    public GoogleOAuthCallbackController(GoogleOAuthProperties props) {
        this.props = props;
    }

    @GetMapping("/callback")
    public ResponseEntity<String> callback(@RequestParam(required = false) String code,
        @RequestParam(required = false) String state,
        @RequestParam(required = false) String error) {
        if (error != null) {
            return ResponseEntity.badRequest().body("Google OAuth error: " + error);
        }
        return ResponseEntity.ok("Google OAuth callback received. code=" + code);
    }
}
