package personal.project.loginpage;

public record UserDto(
  Long id,
  String username,
  String email,
  String password,
  String role
) {}
