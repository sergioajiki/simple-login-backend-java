package personal.project.loginpage.security;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.GenericFilter;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.servlet.HandlerExceptionResolver;
import personal.project.loginpage.advice.Problem;
import personal.project.loginpage.service.TokenService;
import personal.project.loginpage.service.UserService;

public class CustomFilter extends GenericFilter {

  private final UserService userService;
  private final TokenService tokenService;
  private final HandlerExceptionResolver handlerExceptionResolver;
  private final ObjectMapper objectMapper;

  public CustomFilter(UserService userService, TokenService tokenService,
      HandlerExceptionResolver handlerExceptionResolver, ObjectMapper objectMapper) {
    this.userService = userService;
    this.tokenService = tokenService;
    this.handlerExceptionResolver = handlerExceptionResolver;
    this.objectMapper = objectMapper;
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response,
      FilterChain filterChain) throws IOException, ServletException {

    Optional<String> token = extractToken(request);
    if (token.isPresent()) {
      try {
        String subject = tokenService.validateToken(token.get());
        UserDetails userDetails = userService.loadUserByUsername(subject);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
            userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
      } catch (
          TokenExpiredException
          | JWTDecodeException exception) {
        System.out.println(exception.getMessage());
        handlerExceptionResolver.resolveException((HttpServletRequest) request,
            (HttpServletResponse) response, null, exception);
        return;
      } catch (AccessDeniedException exception) {
        System.out.println(exception.getMessage());
        return;
      }
    }
    filterChain.doFilter(request, response);
  }

  // Metodo para extrair o token do Header
  private Optional<String> extractToken(ServletRequest servletRequest) {
    HttpServletRequest req = (HttpServletRequest) servletRequest;
    String authHeader = req.getHeader("Authorization");
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

  @Override
  public void destroy() {
    super.destroy();
  }
}
