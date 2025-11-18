package com.watchcrew.adapter.in.web.controller;

import com.watchcrew.adapter.in.web.dto.UpdatePreferencesRequest;
import com.watchcrew.adapter.in.web.dto.UserRegistrationRequest;
import com.watchcrew.adapter.in.web.dto.UserResponse;
import com.watchcrew.application.port.in.UserUseCase;
import com.watchcrew.domain.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "User management endpoints")
public class UserController {
  
  private final UserUseCase userUseCase;
  
  @PostMapping("/register")
  @Operation(summary = "Register a new user", description = "Creates a new user account with preferences")
  public ResponseEntity<UserResponse> registerUser(@RequestBody UserRegistrationRequest request) {
    User user = userUseCase.registerUser(
        request.getUsername(), 
        request.getPassword(), 
        request.getPreferences()
    );
    return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(user));
  }
  
  @GetMapping("/{userId}")
  @Operation(summary = "Get user by ID", description = "Retrieves user information by user ID")
  public ResponseEntity<UserResponse> getUserById(
      @Parameter(description = "User ID") @PathVariable String userId) {
    return userUseCase.getUserById(userId)
        .map(user -> ResponseEntity.ok(toResponse(user)))
        .orElse(ResponseEntity.notFound().build());
  }
  
  @GetMapping("/username/{username}")
  public ResponseEntity<UserResponse> getUserByUsername(@PathVariable String username) {
    return userUseCase.getUserByUsername(username)
        .map(user -> ResponseEntity.ok(toResponse(user)))
        .orElse(ResponseEntity.notFound().build());
  }
  
  @PutMapping("/{userId}/preferences")
  @Operation(summary = "Update user preferences", description = "Updates the user's genre preferences")
  public ResponseEntity<UserResponse> updatePreferences(
      @Parameter(description = "User ID") @PathVariable String userId, 
      @RequestBody UpdatePreferencesRequest request) {
    User user = userUseCase.updatePreferences(userId, request.getPreferences());
    return ResponseEntity.ok(toResponse(user));
  }
  
  @PostMapping("/{userId}/friends/{friendId}")
  public ResponseEntity<UserResponse> addFriend(
      @PathVariable String userId, 
      @PathVariable String friendId) {
    User user = userUseCase.addFriend(userId, friendId);
    return ResponseEntity.ok(toResponse(user));
  }
  
  @DeleteMapping("/{userId}/friends/{friendId}")
  public ResponseEntity<UserResponse> removeFriend(
      @PathVariable String userId, 
      @PathVariable String friendId) {
    User user = userUseCase.removeFriend(userId, friendId);
    return ResponseEntity.ok(toResponse(user));
  }
  
  @GetMapping("/{userId}/friends")
  public ResponseEntity<List<UserResponse>> getFriends(@PathVariable String userId) {
    List<UserResponse> friends = userUseCase.getFriends(userId).stream()
        .map(this::toResponse)
        .toList();
    return ResponseEntity.ok(friends);
  }
  
  @PostMapping("/{userId}/movies/{movieId}")
  public ResponseEntity<UserResponse> addMovieToMyList(
      @PathVariable String userId, 
      @PathVariable String movieId) {
    User user = userUseCase.addMovieToMyList(userId, movieId);
    return ResponseEntity.ok(toResponse(user));
  }
  
  @DeleteMapping("/{userId}/movies/{movieId}")
  public ResponseEntity<UserResponse> removeMovieFromMyList(
      @PathVariable String userId, 
      @PathVariable String movieId) {
    User user = userUseCase.removeMovieFromMyList(userId, movieId);
    return ResponseEntity.ok(toResponse(user));
  }
  
  @GetMapping("/{userId}/movies")
  public ResponseEntity<List<String>> getMyMovies(@PathVariable String userId) {
    List<String> movieIds = userUseCase.getMyMovies(userId);
    return ResponseEntity.ok(movieIds);
  }
  
  private UserResponse toResponse(User user) {
    return UserResponse.builder()
        .id(user.getId())
        .username(user.getUsername())
        .preferences(user.getPreferredGenres())
        .friendIds(user.getFriendIds())
        .myMovieIds(user.getMyMovieIds())
        .customListIds(user.getCustomListIds())
        .build();
  }
}
