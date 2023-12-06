package personal.project.loginpage.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import personal.project.loginpage.entity.User;

public record LoginDto(
    @NotBlank String username,
    @NotBlank(message = "Field password can not be null or empty")
    @Size(min = 6, max = 12, message = "Password must be between 6 and 12 characters") String password
) {
  public static User loginDtoToUser(LoginDto loginDto) {
    User user = new User();
    user.setUsername(loginDto.username());
    user.setPassword(loginDto.password());
    return user;
  }
}
