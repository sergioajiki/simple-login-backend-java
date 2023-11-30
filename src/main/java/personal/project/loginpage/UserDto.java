package personal.project.loginpage;

import personal.project.loginpage.entity.User;
public record UserDto(
  Long id,
  String username,
  String email,
  String password,
  String role
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
