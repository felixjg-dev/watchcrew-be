package com.watchcrew.application.port.in;

import com.watchcrew.domain.model.MovieList;

import java.util.List;
import java.util.Optional;

public interface MovieListUseCase {
  MovieList createMovieList(String userId, String name, String description);
  MovieList updateMovieList(String listId, String userId, String name, String description);
  MovieList addMovieToList(String listId, String userId, String movieId);
  MovieList removeMovieFromList(String listId, String userId, String movieId);
  void deleteMovieList(String listId, String userId);
  Optional<MovieList> getMovieListById(String listId);
  List<MovieList> getMovieListsByUserId(String userId);
}
