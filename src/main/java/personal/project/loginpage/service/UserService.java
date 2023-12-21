package personal.project.loginpage.service;

import java.util.List;
import java.util.Optional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import personal.project.loginpage.dto.EmailDto;
import personal.project.loginpage.dto.LoginDto;
import personal.project.loginpage.dto.UserDto;
import personal.project.loginpage.dto.UserWithoutPasswordDto;
import personal.project.loginpage.dto.UsernameDto;
import personal.project.loginpage.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import personal.project.loginpage.exception.InvalidEmailFormatException;
import personal.project.loginpage.exception.InvalidLoginException;
import personal.project.loginpage.exception.NotFoundException;
import personal.project.loginpage.repository.UserRepository;
import personal.project.loginpage.util.EmailValidator;

@Service
public class UserService {

  private final UserRepository userRepository;

  @Autowired
  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public UserDto create(UserDto userDto) {
    User userToSave = UserDto.userDtoToUser(userDto);
    boolean isEmail = EmailValidator.isValidEmail(userToSave.getEmail());
    if (!isEmail) {
      throw new InvalidEmailFormatException("Formato de email não é valido");
    }
//    Optional<User> verifyUserOptional = userRepository.findByUsername(userToSave.getUsername());
//    if (verifyUserOptional.isPresent()) {
//      throw new
//    }
    String hashedPassword = new BCryptPasswordEncoder()
        .encode(userToSave.getPassword());
    userToSave.setPassword(hashedPassword);
    userRepository.save(userToSave);
    UserDto savedUser = UserDto.userToUserDto(userToSave);
    return savedUser;
  }

  public List<User> findAll() {
    return userRepository.findAll();
  }

  public UserWithoutPasswordDto findUserById(Long id) {
    Optional<User> userOptional = userRepository.findById(id);
    if (userOptional.isEmpty()) {
      throw new NotFoundException("ID does not match any user");
    }
    UserWithoutPasswordDto userFoundDto = UserWithoutPasswordDto.UserWithoutPasswordToDto(
        userOptional.get());
    return userFoundDto;
  }

  public UserWithoutPasswordDto findUserByUsername(UsernameDto username) {
    Optional<User> userOptional = userRepository.findByUsername(username.username());
    if (userOptional.isEmpty()) {
      throw new NotFoundException("Username does not match any user");
    }
    User userFound = userOptional.get();
    UserWithoutPasswordDto userFoundDto = UserWithoutPasswordDto.UserWithoutPasswordToDto(
        userFound);
    return userFoundDto;
  }

  public UserWithoutPasswordDto findUserByEmail(EmailDto email) {
    boolean isEmail = EmailValidator.isValidEmail(email.email());
    if (!isEmail) {
      throw new InvalidEmailFormatException("Formato de email não é valido");
    }
    Optional<User> userOptional = userRepository.findByEmail(email.email());
    if (userOptional.isEmpty()) {
      throw new NotFoundException("Email does not match any user");
    }
    User userFound = userOptional.get();
    UserWithoutPasswordDto userFoundDto = UserWithoutPasswordDto.UserWithoutPasswordToDto(
        userFound);
    return userFoundDto;
  }

  public String login(LoginDto login) {
    Optional<User> userOptional = userRepository.findByUsername(login.username());
    if (userOptional.isEmpty()) {
      throw new InvalidLoginException("Username or password not found");
    }
    BCryptPasswordEncoder bcp = new BCryptPasswordEncoder();
    System.out.println(bcp.encode(login.password()));
    System.out.println(userOptional.get().getPassword());

    if (bcp.matches(login.password(), userOptional.get().getPassword())) {
      return "Sucess login";
    } else {
      throw new InvalidLoginException("Username or password not found");
    }
  }
}
