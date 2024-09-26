package com.example.UserService.controller;

import com.example.UserService.exception.UserAlreadyExistsException;
import com.example.UserService.model.CreateUserRequestModel;
import com.example.UserService.model.CreateUserResponseModel;
import com.example.UserService.model.User;
import com.example.UserService.model.UserPrincipal;
import com.example.UserService.service.JwtService;
import com.example.UserService.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import io.jsonwebtoken.Claims;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "http://localhost:3000")
@Tag(name = "User Management", description = "APIs related to User operations")
public class UserController {

    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public UserController (UserService userService, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/register")
    @Operation(summary = "Register a new user", description = "Registers a new user and returns the registered user")
    public ResponseEntity<CreateUserResponseModel> register(@Valid @RequestBody CreateUserRequestModel userDetails) {
        try {
            ModelMapper modelMapper = new ModelMapper();
            modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
            User user = modelMapper.map(userDetails, User.class);

            User registeredUser = userService.saveUser(user);

            CreateUserResponseModel returnValue = modelMapper.map(registeredUser, CreateUserResponseModel.class);
            return new ResponseEntity<>(returnValue, HttpStatus.CREATED);
        } catch (UserAlreadyExistsException e) {
            return new ResponseEntity<>(null, HttpStatus.CONFLICT);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/login")
    @Operation(summary = "Login a user", description = "Authenticates a user and returns a JWT token if successful")
    public ResponseEntity<String> login(@RequestBody User user) {
        try {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
            // The AuthenticationManager delegates the authentication request to an AuthenticationProvider. This provider is responsible for validating the credentials.
            // The AuthenticationProvider can be customized, but most commonly, you use the DaoAuthenticationProvider to implement
            if(authentication.isAuthenticated()) {

                String userId = String.valueOf(((UserPrincipal) authentication.getPrincipal()).getUserId());
                // Generate the token
                String token = jwtService.generateToken(user.getUsername(), userId);

//                return new ResponseEntity<>(token, HttpStatus.OK);
                // Create headers and add your custom headers
                HttpHeaders headers = new HttpHeaders();
                headers.add("token", token);
                headers.add("userId", userId);

                // Return an empty body with the headers
                return new ResponseEntity<>(headers, HttpStatus.OK);

            } else {
                return new ResponseEntity<>("Login failed due to incorrect credentials.", HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred during login", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{userId}")
    @Operation(summary = "Get a user info", description = "Return the info of a user")
    public ResponseEntity<CreateUserResponseModel> getUserById(@PathVariable int userId) {
        return userService.getUserById(userId);

    }

    @GetMapping("/getUserIdFromToken")
    public ResponseEntity<String> getUserIdFromToken(@RequestHeader("Authorization") String token) {

        String userId = jwtService.extractUserId(token);

        return new ResponseEntity<>(userId, HttpStatus.OK);
    }


}
