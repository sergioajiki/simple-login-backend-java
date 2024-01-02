package personal.project.loginpage.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Health")
@RequestMapping("/health")
public class HealthController {

  @GetMapping
  public static String sayHello() {
    return "Success!!";
  }
}
