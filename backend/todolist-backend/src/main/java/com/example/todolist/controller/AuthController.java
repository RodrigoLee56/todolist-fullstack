package com.example.todolist.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.todolist.model.User;
import com.example.todolist.payload.request.LoginRequest;
import com.example.todolist.payload.request.SignupRequest;
import com.example.todolist.payload.response.JwtResponse;
import com.example.todolist.payload.response.MessageResponse;
import com.example.todolist.security.jwt.JwtUtils;
import com.example.todolist.security.services.UserDetailsImpl;
import com.example.todolist.service.UserService;

import jakarta.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    public AuthController(UserService userService, 
                         AuthenticationManager authenticationManager,
                         JwtUtils jwtUtils) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();        

        return ResponseEntity.ok(new JwtResponse(jwt, 
                userDetails.getId(), 
                userDetails.getUsername()));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        try {
            User user = new User();
            user.setUsername(signUpRequest.getUsername());
            user.setPassword(signUpRequest.getPassword());
            
            userService.registerUser(user);
            
            return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: " + e.getMessage()));
        }
    }
}