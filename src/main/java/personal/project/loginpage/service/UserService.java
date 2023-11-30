package personal.project.loginpage.service;

import java.util.List;
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

  public User create(User user) {
    return userRepository.save(user);
  }

  public List<User> findAll() {
    return userRepository.findAll();
  }
}
