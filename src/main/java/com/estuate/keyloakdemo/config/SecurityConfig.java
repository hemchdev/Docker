package com.estuate.keyloakdemo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomAuthenticationProvider customAuthenticationProvider;

    @Bean
    public HttpFirewall httpFirewall() {
        StrictHttpFirewall firewall = new StrictHttpFirewall();
        firewall.setAllowSemicolon(true);
        return firewall;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // Register our custom authentication provider
                .authenticationProvider(customAuthenticationProvider)
                .authorizeHttpRequests(authorize -> authorize
                        // Allow access to home, login, and static assets
                        .requestMatchers("/", "/login", "/register", "/css/**", "/js/**", "/images/**").permitAll()
                        // All other requests must be authenticated
                        .anyRequest().authenticated()
                )
                // Configure a custom form login
                .formLogin(form -> form
                        .loginPage("/login") // The URL of our custom login page
                        .defaultSuccessUrl("/menu", true) // Where to go after a successful login
                        .failureUrl("/login?error=true") // Where to go if login fails
                        .permitAll()
                )
                // Configure logout
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout") // Redirect to login page with a logout message
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                );

        return http.build();
    }
}

