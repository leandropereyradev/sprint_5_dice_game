package cat.itacademy.barcelonactiva.pereyra.gastonleandro.s05.t02.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Bean
    SecurityFilterChain web(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((authorize) -> authorize
                        .anyRequest().authenticated()
                );

        http
                .formLogin(Customizer.withDefaults());

        http
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }
}
