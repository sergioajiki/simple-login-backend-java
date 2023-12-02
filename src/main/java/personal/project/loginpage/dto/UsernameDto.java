package personal.project.loginpage.dto;
import jakarta.validation.constraints.NotBlank;

public record UsernameDto(
    @NotBlank(message = "Field username can not be null or empty") String username
) {}
