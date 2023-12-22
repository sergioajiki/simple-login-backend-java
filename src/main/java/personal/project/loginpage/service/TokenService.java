package personal.project.loginpage.service;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import personal.project.loginpage.dto.LoginDto;

@Service
public class TokenService {

  @Value("${api.security.token.secret}")
  private String codeSecret;

  public String generateToken(String username) {
    Algorithm algorithm = Algorithm.HMAC256(codeSecret);
    return JWT.create()
        .withSubject(username)
        .withExpiresAt(generateExpirationDate())
        .sign(algorithm);
  }

  private Instant generateExpirationDate() {
    return LocalDateTime.now()
        .plusHours(2)
        .toInstant(ZoneOffset.of("-03:00"));
  }

  public String validateToken(String token) {
    Algorithm algorithm = Algorithm.HMAC256(codeSecret);
    return JWT.require(algorithm)
        .build()
        .verify(token)
        .getSubject();
  }
}
