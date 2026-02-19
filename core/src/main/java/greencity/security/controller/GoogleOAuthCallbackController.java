package greencity.security.controller;

import greencity.config.GoogleOAuthProperties;
import greencity.security.oauth.GoogleOAuthStateService;
import java.net.URI;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/auth/google")
public class GoogleOAuthCallbackController {
    private static final String GOOGLE_AUTH_URL = "https://accounts.google.com/o/oauth2/v2/auth";

    private final GoogleOAuthProperties props;
    private final GoogleOAuthStateService stateService;

    public GoogleOAuthCallbackController(GoogleOAuthProperties props, GoogleOAuthStateService stateService) {
        this.props = props;
        this.stateService = stateService;
    }

    @GetMapping
    public ResponseEntity<Void> redirectToGoogle() {
        String state = stateService.generateAndStore();

        URI redirect = UriComponentsBuilder
            .fromHttpUrl(GOOGLE_AUTH_URL)
            .queryParam("client_id", props.getClientId())
            .queryParam("redirect_uri", props.getRedirectUri())
            .queryParam("scope", "email profile")
            .queryParam("response_type", "code")
            .queryParam("state", state)
            .build()
            .encode()
            .toUri();

        return ResponseEntity.status(302)
            .header(HttpHeaders.LOCATION, redirect.toString())
            .build();
    }

    @GetMapping("/callback")
    public ResponseEntity<String> callback(@RequestParam(required = false) String code,
        @RequestParam(required = false) String state,
        @RequestParam(required = false) String error) {
        if (error != null) {
            return ResponseEntity.badRequest().body("Google OAuth error: " + error);
        }

        if (state == null || !stateService.verifyAndConsume(state)) {
            return ResponseEntity.badRequest().body("Invalid or expired state");
        }

        return ResponseEntity.ok("Google OAuth callback received.");
    }
}
