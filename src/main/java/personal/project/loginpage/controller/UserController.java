package personal.project.loginpage.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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

  @PostMapping()
    public ResponseEntity<User> createUser(@RequestBody User user) {
    User savedUser = userService.create(user);
    return ResponseEntity.status(201).body(savedUser);
  }
}
