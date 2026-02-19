package greencity.security.controller;

import greencity.config.GoogleOAuthProperties;
import greencity.exception.exceptions.OAuthErrorCode;
import greencity.exception.exceptions.OAuthException;
import greencity.security.oauth.GoogleOAuthService;
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

    private final GoogleOAuthService googleOAuthService;
    private final GoogleOAuthProperties props;
    private final GoogleOAuthStateService stateService;

    public GoogleOAuthCallbackController(GoogleOAuthProperties props,
        GoogleOAuthStateService stateService,
        GoogleOAuthService googleOAuthService) {
        this.props = props;
        this.stateService = stateService;
        this.googleOAuthService = googleOAuthService;
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
    public ResponseEntity<?> callback(@RequestParam(required = false) String code,
        @RequestParam(required = false) String state,
        @RequestParam(required = false) String error) {
        if (error != null) {
            return ResponseEntity.badRequest().body("Google OAuth error: " + error);
        }
        if (code == null) {
            return ResponseEntity.badRequest().body("Missing code");
        }
        if (state == null || !stateService.verifyAndConsume(state)) {
            throw new OAuthException(OAuthErrorCode.STATE_MISMATCH, "Invalid or expired state");
        }

        return ResponseEntity.ok(googleOAuthService.exchangeAndValidate(code));
    }

}
