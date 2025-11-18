package com.watchcrew.application.service;

import com.watchcrew.application.port.in.MovieListUseCase;
import com.watchcrew.application.port.out.MovieListRepository;
import com.watchcrew.domain.model.MovieList;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MovieListService implements MovieListUseCase {
  
  private static final String MOVIE_LIST_NOT_FOUND = "Movie list not found";
  private static final String UNAUTHORIZED_TO_MODIFY = "Unauthorized to modify this list";
  
  private final MovieListRepository movieListRepository;
  
  @Override
  public MovieList createMovieList(String userId, String name, String description) {
    MovieList movieList = MovieList.builder()
        .userId(userId)
        .name(name)
        .description(description)
        .build();
    
    return movieListRepository.save(movieList);
  }
  
  @Override
  public MovieList updateMovieList(String listId, String userId, String name, String description) {
    MovieList movieList = movieListRepository.findById(listId)
        .orElseThrow(() -> new IllegalArgumentException(MOVIE_LIST_NOT_FOUND));
    
    if (!movieList.getUserId().equals(userId)) {
      throw new IllegalArgumentException(UNAUTHORIZED_TO_MODIFY);
    }
    
    movieList.setName(name);
    movieList.setDescription(description);
    movieList.setUpdatedAt(LocalDateTime.now());
    
    return movieListRepository.save(movieList);
  }
  
  @Override
  public MovieList addMovieToList(String listId, String userId, String movieId) {
    MovieList movieList = movieListRepository.findById(listId)
        .orElseThrow(() -> new IllegalArgumentException(MOVIE_LIST_NOT_FOUND));
    
    if (!movieList.getUserId().equals(userId)) {
      throw new IllegalArgumentException(UNAUTHORIZED_TO_MODIFY);
    }
    
    if (!movieList.getMovieIds().contains(movieId)) {
      movieList.getMovieIds().add(movieId);
      movieList.setUpdatedAt(LocalDateTime.now());
      movieList = movieListRepository.save(movieList);
    }
    
    return movieList;
  }
  
  @Override
  public MovieList removeMovieFromList(String listId, String userId, String movieId) {
    MovieList movieList = movieListRepository.findById(listId)
        .orElseThrow(() -> new IllegalArgumentException(MOVIE_LIST_NOT_FOUND));
    
    if (!movieList.getUserId().equals(userId)) {
      throw new IllegalArgumentException(UNAUTHORIZED_TO_MODIFY);
    }
    
    movieList.getMovieIds().remove(movieId);
    movieList.setUpdatedAt(LocalDateTime.now());
    
    return movieListRepository.save(movieList);
  }
  
  @Override
  public void deleteMovieList(String listId, String userId) {
    MovieList movieList = movieListRepository.findById(listId)
        .orElseThrow(() -> new IllegalArgumentException(MOVIE_LIST_NOT_FOUND));
    
    if (!movieList.getUserId().equals(userId)) {
      throw new IllegalArgumentException("Unauthorized to delete this list");
    }
    
    movieListRepository.deleteById(listId);
  }
  
  @Override
  public Optional<MovieList> getMovieListById(String listId) {
    return movieListRepository.findById(listId);
  }
  
  @Override
  public List<MovieList> getMovieListsByUserId(String userId) {
    return movieListRepository.findByUserId(userId);
  }
}
