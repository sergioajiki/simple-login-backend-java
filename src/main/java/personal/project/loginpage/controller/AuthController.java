package personal.project.loginpage.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import personal.project.loginpage.dto.LoginDto;
import personal.project.loginpage.dto.TokenDto;
import personal.project.loginpage.dto.UserWithoutPasswordDto;
import personal.project.loginpage.service.UserService;

@RestController
@RequestMapping(value = "/auth")
public class AuthController {
  private final UserService userService;

  public AuthController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping("/login")
  public ResponseEntity<TokenDto> login(@RequestBody @Valid LoginDto loginDto) {
    TokenDto resultOfLogin = userService.login(loginDto);
    return ResponseEntity.status(HttpStatus.OK).body(resultOfLogin);
  }

}
