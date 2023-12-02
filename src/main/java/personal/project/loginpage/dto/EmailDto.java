package personal.project.loginpage.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record EmailDto(
    @Email @NotBlank(message = "Field email can not be null or empty") String email
) {

}
