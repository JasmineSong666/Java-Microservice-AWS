//package com.example.EurekaServer;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.Customizer;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//
//import org.springframework.security.web.SecurityFilterChain;
//
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfig {
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http.
//                csrf(customizer -> customizer.disable())
//                .authorizeHttpRequests(request -> request
////                        .requestMatchers(WHITELIST).permitAll()
//                        // The /register endpoint is made accessible without authentication
//                        .anyRequest().authenticated()
//                )
//                .httpBasic(Customizer.withDefaults());
//
//
//        return http.build();
//    }
//
//
//}
