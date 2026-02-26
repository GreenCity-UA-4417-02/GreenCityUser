package greencity.security.oauth;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import java.io.IOException;
import java.security.GeneralSecurityException;
import greencity.dto.oauth.GoogleIdentityDto;
import greencity.dto.oauth.GoogleTokenResponse;
import greencity.exception.exceptions.OAuthErrorCode;
import greencity.exception.exceptions.OAuthException;
import org.springframework.stereotype.Service;

@Service
public class GoogleOAuthService {
    private final GoogleOAuthTokenClient tokenClient;
    private final GoogleIdTokenVerifier idTokenVerifier;

    public GoogleOAuthService(GoogleOAuthTokenClient tokenClient, GoogleIdTokenVerifier idTokenVerifier) {
        this.tokenClient = tokenClient;
        this.idTokenVerifier = idTokenVerifier;
    }

    public GoogleIdentityDto exchangeAndValidate(String code) {
        GoogleTokenResponse tokens = tokenClient.exchangeCode(code);

        GoogleIdToken idToken;
        try {
            idToken = idTokenVerifier.verify(tokens.getIdToken());
        } catch (GeneralSecurityException | IOException e) {
            throw new OAuthException(OAuthErrorCode.INVALID_ID_TOKEN, "Failed to validate id_token");
        }

        if (idToken == null) {
            throw new OAuthException(OAuthErrorCode.INVALID_ID_TOKEN, "Invalid id_token");
        }

        GoogleIdToken.Payload payload = idToken.getPayload();

        Boolean emailVerified = (Boolean) payload.getEmailVerified();
        if (emailVerified == null || !emailVerified) {
            throw new OAuthException(OAuthErrorCode.UNVERIFIED_EMAIL, "Email is not verified");
        }

        return GoogleIdentityDto.builder()
            .sub(payload.getSubject())
            .email(payload.getEmail())
            .emailVerified(true)
            .name((String) payload.get("name"))
            .picture((String) payload.get("picture"))
            .build();
    }
}
