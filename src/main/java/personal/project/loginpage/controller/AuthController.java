package personal.project.loginpage.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import personal.project.loginpage.dto.LoginDto;
import personal.project.loginpage.dto.TokenDto;
import personal.project.loginpage.service.UserService;

@RestController
@RequestMapping(value = "/auth")
public class AuthController {
  private final UserService userService;
  private final AuthenticationManager authenticationManager;
  @Autowired

  public AuthController(UserService userService, AuthenticationManager authenticationManager) {
    this.userService = userService;
    this.authenticationManager = authenticationManager;
  }

  @PostMapping("/login")
  public ResponseEntity<TokenDto> login(@RequestBody @Valid LoginDto loginDto) {
    UsernamePasswordAuthenticationToken usernamePassword =
        new UsernamePasswordAuthenticationToken(loginDto.username(), loginDto.password());
    Authentication auth = authenticationManager.authenticate(usernamePassword);
//    System.out.println(auth);
    UserDetails user = (UserDetails) auth.getPrincipal(); //Força tipar UserDetails
//    System.out.println(userDetails);
    TokenDto resultOfLogin = userService.login(user);
    return ResponseEntity.status(HttpStatus.OK).body(resultOfLogin);
//    return "Success - logged with " + auth.getName();
  }
}
