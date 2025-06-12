package techno_express.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.http.HttpMethod;
import techno_express.backend.filters.JwtFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
          .csrf(csrf -> csrf.disable())
          .headers(headers -> headers.frameOptions().disable()) // necesario para h2-console
          .authorizeHttpRequests(auth -> auth
              // Auth público para login/register
              .requestMatchers("/api/auth/**").permitAll()

              // Permitir GET y POST en productos y ventas
              .requestMatchers(HttpMethod.GET,  "/api/products/**", "/api/sales/**")
                .permitAll()
              .requestMatchers(HttpMethod.POST, "/api/products/**", "/api/sales/**")
                .permitAll()

              // El resto de rutas exige token
              .anyRequest().authenticated()
          )
          .sessionManagement(sess ->
              sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
          )
          .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
