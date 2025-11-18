package com.watchcrew.adapter.in.web.controller;

import com.watchcrew.adapter.in.web.dto.MovieResponse;
import com.watchcrew.application.port.in.MovieQuery;
import com.watchcrew.application.port.in.UserUseCase;
import com.watchcrew.domain.model.Genre;
import com.watchcrew.domain.model.Movie;
import com.watchcrew.domain.model.User;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movies")
@RequiredArgsConstructor
@Tag(name = "Movies", description = "Movie information and search endpoints")
public class MovieController {
  
  private final MovieQuery movieQuery;
  private final UserUseCase userUseCase;
  
  @GetMapping("/{movieId}")
  public ResponseEntity<MovieResponse> getMovieById(@PathVariable String movieId) {
    return movieQuery.getMovieById(movieId)
        .map(movie -> ResponseEntity.ok(toResponse(movie)))
        .orElse(ResponseEntity.notFound().build());
  }
  
  @GetMapping("/external/{externalId}")
  public ResponseEntity<MovieResponse> getMovieByExternalId(@PathVariable String externalId) {
    return movieQuery.getMovieByExternalId(externalId)
        .map(movie -> ResponseEntity.ok(toResponse(movie)))
        .orElse(ResponseEntity.notFound().build());
  }
  
  @GetMapping("/latest")
  public ResponseEntity<List<MovieResponse>> getLatestMovies(
      @RequestParam(required = false) List<Genre> genres,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "20") int size) {
    List<Movie> movies = movieQuery.getLatestMoviesByPreferences(genres, page, size);
    List<MovieResponse> responses = movies.stream()
        .map(this::toResponse)
        .toList();
    return ResponseEntity.ok(responses);
  }
  
  @GetMapping("/user/{userId}/recommendations")
  public ResponseEntity<List<MovieResponse>> getRecommendedMoviesForUser(
      @PathVariable String userId,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "20") int size) {
    User user = userUseCase.getUserById(userId)
        .orElseThrow(() -> new IllegalArgumentException("User not found"));
    
    List<Movie> movies = movieQuery.getRecommendedMoviesBasedOnGenres(
        user.getPreferredGenres(), page, size);
    List<MovieResponse> responses = movies.stream()
        .map(this::toResponse)
        .toList();
    return ResponseEntity.ok(responses);
  }
  
  @GetMapping("/{movieId}/similar")
  public ResponseEntity<List<MovieResponse>> getSimilarMovies(
      @PathVariable String movieId,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "20") int size) {
    List<Movie> movies = movieQuery.getSimilarMovies(movieId, page, size);
    List<MovieResponse> responses = movies.stream()
        .map(this::toResponse)
        .toList();
    return ResponseEntity.ok(responses);
  }
  
  @GetMapping("/search")
  public ResponseEntity<List<MovieResponse>> searchMovies(
      @RequestParam String query,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "20") int size) {
    List<Movie> movies = movieQuery.searchMovies(query, page, size);
    List<MovieResponse> responses = movies.stream()
        .map(this::toResponse)
        .toList();
    return ResponseEntity.ok(responses);
  }
  
  private MovieResponse toResponse(Movie movie) {
    return MovieResponse.builder()
        .id(movie.getId())
        .externalId(movie.getExternalId())
        .title(movie.getTitle())
        .originalTitle(movie.getOriginalTitle())
        .genres(movie.getGenres())
        .voteAverage(movie.getVoteAverage())
        .voteCount(movie.getVoteCount())
        .releaseDate(movie.getReleaseDate())
        .overview(movie.getOverview())
        .posterPath(movie.getPosterPath())
        .backdropPath(movie.getBackdropPath())
        .runtime(movie.getRuntime())
        .originalLanguage(movie.getOriginalLanguage())
        .averageUserRating(movie.getAverageUserRating())
        .build();
  }
}
