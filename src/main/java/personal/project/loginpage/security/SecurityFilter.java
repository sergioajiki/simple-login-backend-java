package personal.project.loginpage.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;
import personal.project.loginpage.advice.Problem;
import personal.project.loginpage.service.TokenService;
import personal.project.loginpage.service.UserService;

public class SecurityFilter extends OncePerRequestFilter {

  private final TokenService tokenService;
  private final UserService userService;
  private final ObjectMapper objectMapper;

  @Autowired
  public SecurityFilter(TokenService tokenService, UserService userService,
      ObjectMapper objectMapper) {
    this.tokenService = tokenService;
    this.userService = userService;
    this.objectMapper = objectMapper;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    Optional<String> token = extractToken(request);
    String subject = tokenService.validateToken(token.get());
    UserDetails userDetails = userService.loadUserByUsername(subject);
    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
        userDetails, null, userDetails.getAuthorities());
    SecurityContextHolder.getContext().setAuthentication(authentication);
    filterChain.doFilter(request, response);
    return;
  }

//  @Override
//  public boolean shouldNotFilter(HttpServletRequest request) {
//    String method = request.getMethod();
//    String contextPath = request.getRequestURI();
//
//    System.out.println(method);
//    System.out.println(contextPath);
//
//
//    return method.equals("POST") && contextPath.equals("/users");
//  }

  // Metodo para extrair o token do Header
  private Optional<String> extractToken(HttpServletRequest request) {
    String authHeader = request.getHeader("Authorization");
    if (authHeader != null) {
      authHeader = authHeader.replace("Bearer ", "");
    }
    return Optional.ofNullable(authHeader);
  }

  private void catchJWTError(HttpServletResponse response, String title, String detail)
      throws IOException {
    Problem problem = new Problem(
        HttpStatus.FORBIDDEN.value(),
        "Token expired or invalid",
        detail,
        null
    );
    String json = objectMapper.writeValueAsString(problem);
    response.setContentType("application/json; charset=UTF-8");
    response.getWriter().write(json);
    response.getWriter().flush();
    response.getWriter().close();
  }
}

