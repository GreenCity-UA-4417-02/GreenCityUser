package greencity.exception.exceptions;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OAuthErrorResponse {
  private String code;
  private String message;
}
