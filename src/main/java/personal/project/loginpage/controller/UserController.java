package personal.project.loginpage.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import personal.project.loginpage.service.UserService;

@RestController
@RequestMapping(value = "/users")
public class UserController {
  private final UserService userService;
  @Autowired
  public UserController(UserService userService) {
    this.userService = userService;
  }
}
