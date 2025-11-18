package com.watchcrew.adapter.in.web.controller;

import com.watchcrew.adapter.in.web.dto.AuthResponse;
import com.watchcrew.adapter.in.web.dto.LoginRequest;
import com.watchcrew.adapter.in.web.dto.RegisterRequest;
import com.watchcrew.application.port.in.UserUseCase;
import com.watchcrew.domain.model.Genre;
import com.watchcrew.domain.model.User;
import com.watchcrew.security.CustomUserDetailsService;
import com.watchcrew.security.JwtTokenUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "User authentication and registration endpoints")
public class AuthController {
  
  private final AuthenticationManager authenticationManager;
  private final UserUseCase userUseCase;
  private final CustomUserDetailsService userDetailsService;
  private final JwtTokenUtil jwtTokenUtil;
  private final PasswordEncoder passwordEncoder;
  
  @PostMapping("/login")
  @Operation(summary = "Login user", description = "Authenticates a user and returns a JWT token")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successfully authenticated",
          content = @Content(schema = @Schema(implementation = AuthResponse.class))),
      @ApiResponse(responseCode = "401", description = "Invalid credentials",
          content = @Content)
  })
  public ResponseEntity<Object> login(@Valid @RequestBody LoginRequest loginRequest) {
    try {
      authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
      );
    } catch (BadCredentialsException e) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
    }
    
    final UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getUsername());
    final String token = jwtTokenUtil.generateToken(userDetails);
    final User user = userDetailsService.loadUserEntityByUsername(loginRequest.getUsername());
    
    return ResponseEntity.ok(new AuthResponse(token, user.getId(), user.getUsername(), user.getEmail()));
  }
  
  @PostMapping("/register")
  @Operation(summary = "Register new user", description = "Creates a new user account and returns a JWT token")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Successfully registered",
          content = @Content(schema = @Schema(implementation = AuthResponse.class))),
      @ApiResponse(responseCode = "400", description = "Invalid input or username/email already exists",
          content = @Content)
  })
  public ResponseEntity<Object> register(@Valid @RequestBody RegisterRequest registerRequest) {
    // Check if username or email already exists
    if (userUseCase.findByUsername(registerRequest.getUsername()).isPresent()) {
      return ResponseEntity.badRequest().body("Username is already taken");
    }
    if (userUseCase.findByEmail(registerRequest.getEmail()).isPresent()) {
      return ResponseEntity.badRequest().body("Email is already registered");
    }
    
    // Create new user
    User newUser = new User();
    newUser.setUsername(registerRequest.getUsername());
    newUser.setEmail(registerRequest.getEmail());
    newUser.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
    
    if (registerRequest.getPreferredGenres() != null) {
      List<Genre> genres = registerRequest.getPreferredGenres().stream()
          .map(Genre::valueOf)
          .toList();
      newUser.setPreferredGenres(genres);
    }
    
    User savedUser = userUseCase.createUser(newUser);
    
    // Generate token
    final UserDetails userDetails = userDetailsService.loadUserByUsername(savedUser.getUsername());
    final String token = jwtTokenUtil.generateToken(userDetails);
    
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(new AuthResponse(token, savedUser.getId(), savedUser.getUsername(), savedUser.getEmail()));
  }
}
