package com.watchcrew.application.service;

import com.watchcrew.application.port.in.UserUseCase;
import com.watchcrew.application.port.out.UserRepository;
import com.watchcrew.domain.model.Genre;
import com.watchcrew.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService implements UserUseCase {
  
  private static final String USER_NOT_FOUND = "User not found";
  
  private final UserRepository userRepository;
  
  @Override
  public User registerUser(String username, String password, List<Genre> preferences) {
    if (userRepository.existsByUsername(username)) {
      throw new IllegalArgumentException("Username already exists");
    }
    
    User user = User.builder()
        .username(username)
        .password(password)
        .preferredGenres(preferences != null ? preferences : new ArrayList<>())
        .build();
    
    return userRepository.save(user);
  }
  
  @Override
  public User createUser(User user) {
    return userRepository.save(user);
  }
  
  @Override
  public Optional<User> getUserById(String userId) {
    return userRepository.findById(userId);
  }
  
  @Override
  public Optional<User> getUserByUsername(String username) {
    return userRepository.findByUsername(username);
  }
  
  @Override
  public Optional<User> findByUsername(String username) {
    return userRepository.findByUsername(username);
  }
  
  @Override
  public Optional<User> findByEmail(String email) {
    return userRepository.findByEmail(email);
  }
  
  @Override
  public User updatePreferences(String userId, List<Genre> preferences) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new IllegalArgumentException(USER_NOT_FOUND));
    
    user.setPreferredGenres(preferences);
    return userRepository.save(user);
  }
  
  @Override
  public User updatePreferredGenres(String userId, Set<Genre> genres) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new IllegalArgumentException(USER_NOT_FOUND));
    
    user.setPreferredGenres(new ArrayList<>(genres));
    return userRepository.save(user);
  }
  
  @Override
  public User addFriend(String userId, String friendId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new IllegalArgumentException(USER_NOT_FOUND));
    
    if (!userRepository.existsById(friendId)) {
      throw new IllegalArgumentException("Friend not found");
    }
    
    if (!user.getFriendIds().contains(friendId)) {
      user.getFriendIds().add(friendId);
      user = userRepository.save(user);
    }
    
    return user;
  }
  
  @Override
  public User removeFriend(String userId, String friendId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new IllegalArgumentException(USER_NOT_FOUND));
    
    user.getFriendIds().remove(friendId);
    return userRepository.save(user);
  }
  
  @Override
  public List<User> getFriends(String userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new IllegalArgumentException(USER_NOT_FOUND));
    
    return userRepository.findAllById(user.getFriendIds());
  }
  
  @Override
  public User addMovieToMyList(String userId, String movieId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new IllegalArgumentException(USER_NOT_FOUND));
    
    if (!user.getMyMovieIds().contains(movieId)) {
      user.getMyMovieIds().add(movieId);
      user = userRepository.save(user);
    }
    
    return user;
  }
  
  @Override
  public User removeMovieFromMyList(String userId, String movieId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new IllegalArgumentException(USER_NOT_FOUND));
    
    user.getMyMovieIds().remove(movieId);
    return userRepository.save(user);
  }
  
  @Override
  public List<String> getMyMovies(String userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new IllegalArgumentException(USER_NOT_FOUND));
    
    return user.getMyMovieIds();
  }
}
