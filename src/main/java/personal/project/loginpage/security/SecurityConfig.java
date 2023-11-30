package personal.project.loginpage.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

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
        .build();
  }
}

