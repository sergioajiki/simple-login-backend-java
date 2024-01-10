package personal.project.loginpage.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;
import personal.project.loginpage.service.TokenService;
import personal.project.loginpage.service.UserService;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {

  private final UserService userService;
  private final TokenService tokenService;
  private final ObjectMapper objectMapper;
  private final HandlerExceptionResolver handlerExceptionResolver;
  private final CustomBasicAuthenticationEntryPoint customBasicAuthenticationEntryPoint;

  public SecurityConfig(
      UserService userService,
      TokenService tokenService,
      ObjectMapper objectMapper,
      HandlerExceptionResolver handlerExceptionResolver,
      CustomBasicAuthenticationEntryPoint customBasicAuthenticationEntryPoint) {
    this.userService = userService;
    this.tokenService = tokenService;
    this.objectMapper = objectMapper;
    this.handlerExceptionResolver = handlerExceptionResolver;
    this.customBasicAuthenticationEntryPoint = customBasicAuthenticationEntryPoint;
  }

  //  configuração básica
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
    return httpSecurity
        //    .csrf(csrf -> csrf.disable()); Desabilita o cors
        .csrf(AbstractHttpConfigurer::disable)
        .sessionManagement(
            //    não guarda o state
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        )
        .authorizeHttpRequests(
            authorize -> authorize
                .requestMatchers(HttpMethod.POST, "/users").permitAll()
                .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                .requestMatchers("/health").permitAll()
                .requestMatchers("/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/**")
                .permitAll()
                .anyRequest().authenticated()
        )
        .httpBasic((httpBasic -> httpBasic.authenticationEntryPoint(customBasicAuthenticationEntryPoint)))
        .addFilterBefore(
            new CustomFilter(userService, tokenService, handlerExceptionResolver, objectMapper),
            UsernamePasswordAuthenticationFilter.class
        )
        .build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationManager authenticationManager(
      AuthenticationConfiguration authenticationConfiguration
  ) throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }
}

