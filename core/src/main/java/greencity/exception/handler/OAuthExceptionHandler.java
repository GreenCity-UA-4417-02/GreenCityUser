package greencity.exception.handler;

import greencity.exception.exceptions.OAuthErrorResponse;
import greencity.exception.exceptions.OAuthException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static greencity.exception.exceptions.OAuthErrorCode.STATE_MISMATCH;

@RestControllerAdvice
public class OAuthExceptionHandler {

    @ExceptionHandler(OAuthException.class)
    public ResponseEntity<OAuthErrorResponse> handle(OAuthException ex) {
        int status = switch (ex.getCode()) {
            case STATE_MISMATCH -> 400;
            case INVALID_CODE -> 400;
            case INVALID_ID_TOKEN -> 400;
            case UNVERIFIED_EMAIL -> 403;
        };

        return ResponseEntity.status(status).body(
            OAuthErrorResponse.builder()
                .code(ex.getCode().name())
                .message(ex.getMessage())
                .build());
    }
}
