package personal.project.loginpage.service;

import java.util.List;
import java.util.Optional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import personal.project.loginpage.dto.UserDto;
import personal.project.loginpage.dto.UserWithoutPasswordDto;
import personal.project.loginpage.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import personal.project.loginpage.exception.NotFoundException;
import personal.project.loginpage.repository.UserRepository;

@Service
public class UserService {

  private final UserRepository userRepository;

  @Autowired
  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public UserDto create(UserDto userDto) {
    User userToSave = UserDto.userDtoToUser(userDto);
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
    UserWithoutPasswordDto userFoundDto = UserWithoutPasswordDto.UserWithoutPasswordToDto(userOptional.get());
    return userFoundDto;
  }

  public UserWithoutPasswordDto findUserByUsername(UserDto username) {
    Optional<User> userOptional = userRepository.findByUsername(username.username());
    if (userOptional.isEmpty()) {
      throw new NotFoundException("Username does not match any user");
    }
    User userFound = userOptional.get();
    UserWithoutPasswordDto userFoundDto = UserWithoutPasswordDto.UserWithoutPasswordToDto(userFound);
    return userFoundDto;
  }

  public UserWithoutPasswordDto findUserByEmail(UserDto email) {
    Optional<User> userOptional = userRepository.findByEmail(email.email());
    if (userOptional.isEmpty()) {
      throw new NotFoundException("Email does not match any user");
    }
    User userFound = userOptional.get();
    UserWithoutPasswordDto userFoundDto = UserWithoutPasswordDto.UserWithoutPasswordToDto(userFound);
    return userFoundDto;
  }
}
