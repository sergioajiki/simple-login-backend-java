package personal.project.loginpage.service;

import java.util.List;
import java.util.Optional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import personal.project.loginpage.UserDto;
import personal.project.loginpage.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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

  public User findUserById(Long id) {
    Optional<User> user = userRepository.findById(id);
    return user.get();
  }
}
