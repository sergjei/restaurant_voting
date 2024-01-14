package com.github.sergjei.restaurant_voting.config;

import com.github.sergjei.restaurant_voting.model.AuthUser;
import com.github.sergjei.restaurant_voting.model.User;
import com.github.sergjei.restaurant_voting.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Optional;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private static final Logger log = LoggerFactory.getLogger(WebSecurityConfig.class);
    private final UserRepository userRepository;
    public final static PasswordEncoder ENCODER = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    public WebSecurityConfig(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return email -> {
            log.info("get authorized user");
            Optional<User> optionalUser = userRepository.findByEmailIgnoreCase(email);
            return new AuthUser(optionalUser.orElseThrow(
                    () -> new UsernameNotFoundException("User '" + email + "' was not found")));
        };
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers(HttpMethod.POST, "/rest/profile/register")
                        .anonymous()
                        .requestMatchers("/rest/profile/**")
                        .hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/rest/restaurants/**")
                        .hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/rest/admin/**")
                        .hasAnyRole("ADMIN")
                        .requestMatchers("/v3/**", "/swagger-ui/**").permitAll()//https://stackoverflow.com/questions/72316850/swagger-ui-getting-403-error-even-after-adding-resources-in-the-ignored-list-o
                        .anyRequest().authenticated()
                )
                .httpBasic()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().csrf().disable();
        return http.build();
    }
}
