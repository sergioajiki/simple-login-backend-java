package personal.project.loginpage.dto;

import personal.project.loginpage.entity.User;

public record UserWithoutPasswordDto(
    Long id,
    String username,
    String email,
    String role
) {
  public static UserWithoutPasswordDto UserWithoutPasswordToDto(User user) {
    return new UserWithoutPasswordDto(
        user.getId(),
        user.getUsername(),
        user.getEmail(),
        user.getRole()
    );
  }

  public static User UserWithoutPasswordDtoToUser(UserDto userDto) {
    User user = new User();
    user.setId(userDto.id());
    user.setUsername(userDto.username());
    user.setEmail(userDto.email());
    user.setRole(userDto.role());
    return user;
  }
}
