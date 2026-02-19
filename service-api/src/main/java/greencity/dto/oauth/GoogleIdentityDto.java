package greencity.dto.oauth;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GoogleIdentityDto {
    private String sub;
    private String email;
    private boolean emailVerified;
    private String name;
    private String picture;
}
