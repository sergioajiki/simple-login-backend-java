package personal.project.loginpage.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import personal.project.loginpage.dto.EmailDto;
import personal.project.loginpage.dto.UserDto;
import personal.project.loginpage.dto.UserWithoutPasswordDto;
import personal.project.loginpage.dto.UsernameDto;
import personal.project.loginpage.entity.User;
import personal.project.loginpage.service.UserService;

@RestController
@RequestMapping(value = "/users")
//@Api(tags = "Users Managemant", description = "Endpoints for users management")
public class UserController {

  private final UserService userService;

  @Autowired
  public UserController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping
  @Operation(description = "Register User")
  public ResponseEntity<UserDto> createUser(@RequestBody @Valid UserDto userDto) {
    UserDto savedUser = userService.create(userDto);
    return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
  }

  @GetMapping
  @Secured("admin")
  @Operation(description = "Get list of all users")
  public ResponseEntity<List<UserWithoutPasswordDto>> getAllUsers() {
    List<UserWithoutPasswordDto> allUsers = userService.findAll();
    return ResponseEntity.status(HttpStatus.OK).body(allUsers);
  }

  @GetMapping("/{id}")
//  @ApiOperation("Select user by Id")
  public ResponseEntity<UserWithoutPasswordDto> getUserById(@PathVariable Long id) {
    UserWithoutPasswordDto userFoundDto = userService.findUserById(id);
    return ResponseEntity.status(HttpStatus.OK).body(userFoundDto);
  }

  @PostMapping("/username")
//  @ApiOperation("Select user by username")
  public ResponseEntity<UserWithoutPasswordDto> getUserByUsername(@RequestBody @Valid UsernameDto username) {

    UserWithoutPasswordDto userFoundDto = userService.findUserByUsername(username);
    return ResponseEntity.status(HttpStatus.OK).body(userFoundDto);
  }

  @PostMapping("/email")
//  @ApiOperation("Select user by email")
  public ResponseEntity<UserWithoutPasswordDto> getUserByEmail(@RequestBody @Valid EmailDto email) {
    UserWithoutPasswordDto userFoundDto = userService.findUserByEmail(email);
    return ResponseEntity.status(HttpStatus.OK).body(userFoundDto);
  }

  @DeleteMapping("/{id}")
//  @ApiOperation("Delete user by Id")
  public ResponseEntity<String> deleteUserById(@PathVariable Long id) {
    String result = userService.deleteUser(id);
    return ResponseEntity.status(HttpStatus.OK).body(result);
  }
}
