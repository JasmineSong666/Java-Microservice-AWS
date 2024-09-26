package com.example.UserService.config;


import com.example.UserService.service.JwtService;
import com.example.UserService.service.MyUserDetailService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@Component
public class JwtFilter extends OncePerRequestFilter { // ensures the filter is executed once per request

    @Autowired
    JwtService jwtService;

    @Autowired
    ApplicationContext context;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // extract the JWT token
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String userName = null;

        if(authHeader != null && authHeader.startsWith("Bearer ")){
            token = authHeader.substring(7);
            userName = jwtService.extractUserName(token);
        }

        if(userName != null && SecurityContextHolder.getContext().getAuthentication() == null){ // Check if userName is not null and if there is no existing authentication set in the security context.
            // Load user details
            UserDetails userDetails = context.getBean(MyUserDetailService.class).loadUserByUsername(userName);

            // Validate Token
            if(jwtService.validateToken(token, userDetails)){
                // create an authentication token and set it in the Spring Security context, so that Throughout the user's session, the token is used to check if the user has the necessary permissions to access different parts of the application.
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()); // It contains the user's principal (usually their UserDetails), their credentials (password, In the context of JWT authentication, the credentials (password) are not needed after the token is verified.), and their authorities (roles and permissions).

                // Set additional details from the request
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request)); // includes details about the current request, such as the remote address and session ID

                // Set the authentication in the security context
                SecurityContextHolder.getContext().setAuthentication(authToken);
                // The SecurityContext is a central place where Spring Security stores details about the currently authenticated user.
            }
        }
        // Pass the request and response to the next filter in the chain.
        filterChain.doFilter(request, response);
    }
}
