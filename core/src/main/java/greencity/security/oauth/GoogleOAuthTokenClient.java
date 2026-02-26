package greencity.security.oauth;

import greencity.config.GoogleOAuthProperties;
import greencity.dto.oauth.GoogleTokenResponse;
import greencity.exception.exceptions.OAuthErrorCode;
import greencity.exception.exceptions.OAuthException;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

@Component
public class GoogleOAuthTokenClient {
    private final RestTemplate restTemplate;
    private final GoogleOAuthProperties props;

    public GoogleOAuthTokenClient(RestTemplate restTemplate, GoogleOAuthProperties props) {
        this.restTemplate = restTemplate;
        this.props = props;
    }

    public GoogleTokenResponse exchangeCode(String code) {
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("code", code);
        form.add("client_id", props.getClientId());
        form.add("client_secret", props.getClientSecret());
        form.add("redirect_uri", props.getRedirectUri());
        form.add("grant_type", "authorization_code");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(form, headers);

        try {
            ResponseEntity<GoogleTokenResponse> resp =
                restTemplate.postForEntity(props.getTokenUri(), request, GoogleTokenResponse.class);

            GoogleTokenResponse body = resp.getBody();
            if (!resp.getStatusCode().is2xxSuccessful() || body == null || body.getIdToken() == null) {
                throw new OAuthException(OAuthErrorCode.INVALID_CODE, "Invalid or expired authorization code");
            }
            return body;

        } catch (HttpStatusCodeException e) {
            throw new OAuthException(OAuthErrorCode.INVALID_CODE, "Invalid or expired authorization code");
        }
    }
}
