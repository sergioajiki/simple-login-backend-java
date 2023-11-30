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
      throw new NotFoundException("User not found");
    }
    UserWithoutPasswordDto userFoundDto = UserWithoutPasswordDto.UserWithoutPasswordToDto(userOptional.get());
    return userFoundDto;
  }

  public User findUserByUsername(String usernameDto) {
    Optional<User> userOptional = userRepository.findByUsername(usernameDto);
    if (userOptional.isEmpty()) {
      throw new NotFoundException("User not found");
    }
    User userFound = userOptional.get();
    return userFound;
  }

  public User findUserByEmail(String emailDto) {
    Optional<User> userOptional = userRepository.findByEmail(emailDto);
    if (userOptional.isEmpty()) {
      throw new NotFoundException("User not found");
    }
    User userFound = userOptional.get();
    return userFound;
  }
}
