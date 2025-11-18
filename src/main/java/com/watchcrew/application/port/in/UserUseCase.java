package com.watchcrew.application.port.in;

import com.watchcrew.domain.model.Genre;
import com.watchcrew.domain.model.User;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserUseCase {
  User registerUser(String username, String password, List<Genre> preferences);
  User createUser(User user);
  Optional<User> getUserById(String userId);
  Optional<User> getUserByUsername(String username);
  Optional<User> findByUsername(String username);
  Optional<User> findByEmail(String email);
  User updatePreferences(String userId, List<Genre> preferences);
  User updatePreferredGenres(String userId, Set<Genre> genres);
  User addFriend(String userId, String friendId);
  User removeFriend(String userId, String friendId);
  List<User> getFriends(String userId);
  User addMovieToMyList(String userId, String movieId);
  User removeMovieFromMyList(String userId, String movieId);
  List<String> getMyMovies(String userId);
}
