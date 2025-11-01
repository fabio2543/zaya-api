package com.zaya.api.config;

import com.zaya.api.domain.auth.service.JwtService;
import com.zaya.api.domain.user.model.User;
import com.zaya.api.domain.user.model.UserStatus;
import com.zaya.api.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException; 
import java.util.List;

@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity  
public class SecurityConfig {

    private final JwtService jwtService;
    private final UserRepository repo;

    @Bean
    public UserDetailsService userDetailsService(UserRepository repo) {
        return (String username) -> {
            User u = repo.findByEmail(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
            boolean enabled = u.getStatus() == UserStatus.ATIVO;
            List<SimpleGrantedAuthority> auths =
                    List.of(new SimpleGrantedAuthority("ROLE_" + u.getRole().name()));
            return org.springframework.security.core.userdetails.User
                    .withUsername(u.getEmail())
                    .password(u.getPassword())
                    .authorities(auths)
                    .accountExpired(false).accountLocked(false).credentialsExpired(false).disabled(!enabled)
                    .build();
        };
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        var jwtFilter = new JwtAuthenticationFilter(jwtService, userDetailsService(repo));

        http.csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/health", "/actuator/health", "/auth/login", "/auth/register","/usuarios").permitAll()
                .anyRequest().authenticated()
            )
            .httpBasic(Customizer.withDefaults());

        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration cfg) throws Exception {
        return cfg.getAuthenticationManager();
    }
}
