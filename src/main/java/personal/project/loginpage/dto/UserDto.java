package personal.project.loginpage.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import personal.project.loginpage.entity.User;
public record UserDto(
  Long id,
  @NotBlank(message = "Field username can not be null or empty") String username,
  @NotBlank(message = "Field email can not be null or empty") String email,
  @NotBlank(message = "Field password can not be null or empty")
  @Size(min = 6, max = 12, message = "Password must be between 6 and 12 characters") String password,
  @NotBlank(message = "Field role can not be null or empty") String role
) {
  public static UserDto userToUserDto(User user) {
    return new UserDto(
        user.getId(),
        user.getUsername(),
        user.getEmail(),
        user.getPassword(),
        user.getRole()
    );
  }

  public static User userDtoToUser(UserDto userDto) {
    User user = new User();
    user.setId(userDto.id());
    user.setUsername(userDto.username());
    user.setEmail(userDto.email());
    user.setPassword(userDto.password());
    user.setRole(userDto.role());
    return user;
  }
}
