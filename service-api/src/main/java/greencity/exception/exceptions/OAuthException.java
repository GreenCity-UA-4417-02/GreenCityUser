package greencity.exception.exceptions;

import lombok.Getter;

@Getter
public class OAuthException extends RuntimeException {
    private final OAuthErrorCode code;

    public OAuthException(OAuthErrorCode code, String message) {
        super(message);
        this.code = code;
    }
}
