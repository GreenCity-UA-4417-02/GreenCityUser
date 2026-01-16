package greencity.dto.email;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class FindByEmailRequest {
    @NotBlank()
    @Email()
    private String email;
}
