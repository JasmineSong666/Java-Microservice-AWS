package com.example.JobService.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
//@SecurityScheme(
//        name =  "JWTAuth",
//        type = SecuritySchemeType.HTTP,
//        scheme = "bearer",
//        bearerFormat = "JWT") // add JWT authorisation to the path in API Doc
public class SecurityConfig {

//    private final Environment environment;

//    public SecurityConfig(Environment environment) {
//        this.environment = environment;
//    }

    private static final String[] WHITELIST = {
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/swagger-resources/**",
            "/swagger-resources",
            "/jobPosts/**",
            "/jobPost/**",
            "/load",
            "/actuator/**"

    };




    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            http.csrf(customizer -> customizer.disable())
                .authorizeHttpRequests(request -> request
                        // The endpoint is made accessible without authentication
                        .requestMatchers(WHITELIST).permitAll()
                        // Allow requests only from the API Gateway IP
//                        .requestMatchers("/user/**").access(
//                                new WebExpressionAuthorizationManager("hasIpAddress('"+environment.getProperty("gateway.ip")+"')"))
//                        .requestMatchers(req -> new IpAddressMatcher(environment.getProperty("gateway.ip")).matches(req)).permitAll()
//                        .anyRequest().authenticated()
                )
//                .httpBasic(Customizer.withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));


        return http.build();
    }

}
