package com.watchcrew.adapter.in.web.controller;

import com.watchcrew.adapter.in.web.dto.MovieListRequest;
import com.watchcrew.adapter.in.web.dto.MovieListResponse;
import com.watchcrew.application.port.in.MovieListUseCase;
import com.watchcrew.domain.model.MovieList;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movie-lists")
@RequiredArgsConstructor
@Tag(name = "Movie Lists", description = "Custom movie list management endpoints")
public class MovieListController {
  
  private final MovieListUseCase movieListUseCase;
  
  @PostMapping
  public ResponseEntity<MovieListResponse> createMovieList(
      @RequestParam String userId,
      @RequestBody MovieListRequest request) {
    MovieList movieList = movieListUseCase.createMovieList(
        userId, 
        request.getName(), 
        request.getDescription()
    );
    return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(movieList));
  }
  
  @PutMapping("/{listId}")
  public ResponseEntity<MovieListResponse> updateMovieList(
      @PathVariable String listId,
      @RequestParam String userId,
      @RequestBody MovieListRequest request) {
    MovieList movieList = movieListUseCase.updateMovieList(
        listId, 
        userId, 
        request.getName(), 
        request.getDescription()
    );
    return ResponseEntity.ok(toResponse(movieList));
  }
  
  @PostMapping("/{listId}/movies/{movieId}")
  public ResponseEntity<MovieListResponse> addMovieToList(
      @PathVariable String listId,
      @PathVariable String movieId,
      @RequestParam String userId) {
    MovieList movieList = movieListUseCase.addMovieToList(listId, userId, movieId);
    return ResponseEntity.ok(toResponse(movieList));
  }
  
  @DeleteMapping("/{listId}/movies/{movieId}")
  public ResponseEntity<MovieListResponse> removeMovieFromList(
      @PathVariable String listId,
      @PathVariable String movieId,
      @RequestParam String userId) {
    MovieList movieList = movieListUseCase.removeMovieFromList(listId, userId, movieId);
    return ResponseEntity.ok(toResponse(movieList));
  }
  
  @DeleteMapping("/{listId}")
  public ResponseEntity<Void> deleteMovieList(
      @PathVariable String listId,
      @RequestParam String userId) {
    movieListUseCase.deleteMovieList(listId, userId);
    return ResponseEntity.noContent().build();
  }
  
  @GetMapping("/{listId}")
  public ResponseEntity<MovieListResponse> getMovieListById(@PathVariable String listId) {
    return movieListUseCase.getMovieListById(listId)
        .map(movieList -> ResponseEntity.ok(toResponse(movieList)))
        .orElse(ResponseEntity.notFound().build());
  }
  
  @GetMapping("/user/{userId}")
  public ResponseEntity<List<MovieListResponse>> getMovieListsByUserId(@PathVariable String userId) {
    List<MovieListResponse> movieLists = movieListUseCase.getMovieListsByUserId(userId)
        .stream()
        .map(this::toResponse)
        .toList();
    return ResponseEntity.ok(movieLists);
  }
  
  private MovieListResponse toResponse(MovieList movieList) {
    return MovieListResponse.builder()
        .id(movieList.getId())
        .userId(movieList.getUserId())
        .name(movieList.getName())
        .description(movieList.getDescription())
        .movieIds(movieList.getMovieIds())
        .createdAt(movieList.getCreatedAt())
        .updatedAt(movieList.getUpdatedAt())
        .build();
  }
}
