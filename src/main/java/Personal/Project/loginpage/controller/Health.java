package Personal.Project.loginpage.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/health")
public class Health {
  @GetMapping("/check")
  public String sayHello() {
    return "Hello World";
  }

}
