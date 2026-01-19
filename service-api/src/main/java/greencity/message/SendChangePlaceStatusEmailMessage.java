package greencity.message;

import java.io.Serializable;

import greencity.constant.ValidationConstants;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public final class SendChangePlaceStatusEmailMessage implements Serializable {
    @NotBlank()
    private String authorFirstName;
    @NotBlank()
    private String placeName;
    @NotBlank
    private String placeStatus;
    @NotBlank
    @Email()
    private String authorEmail;
}
