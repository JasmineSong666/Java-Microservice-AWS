package com.example.UserService.service;


import com.example.UserService.exception.UserAlreadyExistsException;
import com.example.UserService.model.CreateUserResponseModel;
import com.example.UserService.model.User;
import com.example.UserService.repo.UserRepo;
import jakarta.servlet.http.HttpServletRequest;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.UUID;


@Service
public class UserService {

    private UserRepo userRepo;
    private PasswordEncoder passwordEncoder; // Inject the PasswordEncoder bean

    public UserService (UserRepo userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder =  passwordEncoder;
    }

    public User saveUser(User user) {

        // Check if the user already exists (optional)
        if (userRepo.existsByUsername(user.getUsername())) {
            throw new UserAlreadyExistsException("User with username " + user.getUsername() + " already exists.");
        }

        // Encode the password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Generate unique ID
        user.setCompanyId(UUID.randomUUID().toString());

        // Save the user to the repository
        return userRepo.save(user);
    }


    public ResponseEntity<CreateUserResponseModel> getUserById(int userId) {
        try {
            Optional<User> user = userRepo.findById(userId); // it returns Optional  repo.findById(postId).orElse(new JobPost())
            if (user.isPresent()) {

                ModelMapper modelMapper = new ModelMapper();
                modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
                CreateUserResponseModel userResponse = modelMapper.map(user.get(), CreateUserResponseModel.class);

                return new ResponseEntity<>(userResponse, HttpStatus.OK);
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
            }
        } catch (ResponseStatusException e) {
            throw e; // Re-throw the exception to be caught by the GlobalExceptionHandler
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error retrieving the user", e);
        }
    }

}
