package personal.project.loginpage.service;

import java.util.List;
import java.util.Optional;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import personal.project.loginpage.dto.EmailDto;
import personal.project.loginpage.dto.LoginDto;
import personal.project.loginpage.dto.TokenDto;
import personal.project.loginpage.dto.UserDto;
import personal.project.loginpage.dto.UserWithoutPasswordDto;
import personal.project.loginpage.dto.UsernameDto;
import personal.project.loginpage.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import personal.project.loginpage.exception.DuplicateEntryException;
import personal.project.loginpage.exception.InvalidEmailFormatException;
import personal.project.loginpage.exception.InvalidLoginException;
import personal.project.loginpage.exception.NotFoundException;
import personal.project.loginpage.repository.UserRepository;
import personal.project.loginpage.util.EmailValidator;

@Service
public class UserService implements UserDetailsService {

  private final UserRepository userRepository;
  private final TokenService tokenService;

  @Autowired
  public UserService(UserRepository userRepository, TokenService tokenService) {
    this.userRepository = userRepository;
    this.tokenService = tokenService;
  }

  public UserDto create(UserDto userDto) {
    User userToSave = UserDto.userDtoToUser(userDto);
    boolean isEmail = EmailValidator.isValidEmail(userToSave.getEmail());

    if (!isEmail) {
      throw new InvalidEmailFormatException("Formato de email não é valido");
    }

    Optional<User> verifyUsernameOptional = userRepository.findByUsername(userToSave.getUsername());
    if (verifyUsernameOptional.isPresent()) {
      throw new DuplicateEntryException("User already exist");
    }

    Optional<User> verifyUserEmailOptional = userRepository.findByEmail(userToSave.getEmail());
    if (verifyUserEmailOptional.isPresent()) {
      throw new DuplicateEntryException("Email already registered");
    }

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

  public TokenDto login(UserDetails user) {
    Optional<User> userOptional = userRepository.findByUsername(user.getUsername());
    if (userOptional.isEmpty()) {
      throw new InvalidLoginException("Username or password not found");
    }
//    Verificação de senha agora é feita pelo authenticationManager
//    BCryptPasswordEncoder bcp = new BCryptPasswordEncoder();
//    System.out.println(bcp.encode(loginDto.password()));
//    System.out.println(userOptional.get().getPassword());
//    if (!bcp.matches(user.getPassword(), userOptional.get().getPassword())) {
//      throw new InvalidLoginException("Username or password not found");
//    }
//    System.out.println(user.getUsername() + user.getPassword());
    String token = tokenService.generateToken(user.getUsername());
//    System.out.println(token);
    return new TokenDto(token);
//    return "ok";
  }

  public String deleteUser(Long id) {
    Optional<User> userOptional = userRepository.findById(id);
    if (userOptional.isEmpty()) {
      throw new NotFoundException("ID does not match any user");
    }
    userRepository.deleteById(id);
    return "Done";
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Optional<User> userByUsernameOptional = userRepository.findByUsername(username);
    if (userByUsernameOptional.isEmpty()) {
      throw new InvalidLoginException("Username or password not found");
    }
    UserDetails user = userByUsernameOptional.get();
    return user;
  }
}
