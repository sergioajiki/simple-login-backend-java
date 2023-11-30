package personal.project.loginpage.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import personal.project.loginpage.dto.UserDto;
import personal.project.loginpage.entity.User;
import personal.project.loginpage.service.UserService;

@RestController
@RequestMapping(value = "/users")
public class UserController {

  private final UserService userService;

  @Autowired
  public UserController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping
  public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto) {
    UserDto savedUser = userService.create(userDto);
    return ResponseEntity.status(201).body(savedUser);
  }

  @GetMapping
  public List<User> getAllUsers() {
    List<User> allUsers = userService.findAll();
    return allUsers;
  }

  @GetMapping("/{id}")
  public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
    UserDto userFoundDto = userService.findUserById(id);
    return ResponseEntity.status(HttpStatus.OK).body(userFoundDto);
  }
}
