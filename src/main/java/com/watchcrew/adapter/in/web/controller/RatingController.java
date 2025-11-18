package com.watchcrew.adapter.in.web.controller;

import com.watchcrew.adapter.in.web.dto.RatingRequest;
import com.watchcrew.adapter.in.web.dto.RatingResponse;
import com.watchcrew.application.port.in.RatingUseCase;
import com.watchcrew.domain.model.Rating;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ratings")
@RequiredArgsConstructor
@Tag(name = "Ratings", description = "Movie rating management endpoints")
public class RatingController {
  
  private final RatingUseCase ratingUseCase;
  
  @PostMapping
  public ResponseEntity<RatingResponse> rateMovie(
      @RequestParam String userId,
      @RequestBody RatingRequest request) {
    Rating rating = ratingUseCase.rateMovie(userId, request.getMovieId(), request.getScore());
    return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(rating));
  }
  
  @PutMapping("/{ratingId}")
  public ResponseEntity<RatingResponse> updateRating(
      @PathVariable String ratingId,
      @RequestParam String userId,
      @RequestBody RatingRequest request) {
    Rating rating = ratingUseCase.updateRating(ratingId, userId, request.getScore());
    return ResponseEntity.ok(toResponse(rating));
  }
  
  @DeleteMapping("/{ratingId}")
  public ResponseEntity<Void> deleteRating(
      @PathVariable String ratingId,
      @RequestParam String userId) {
    ratingUseCase.deleteRating(ratingId, userId);
    return ResponseEntity.noContent().build();
  }
  
  @GetMapping("/{ratingId}")
  public ResponseEntity<RatingResponse> getRatingById(@PathVariable String ratingId) {
    return ratingUseCase.getRatingById(ratingId)
        .map(rating -> ResponseEntity.ok(toResponse(rating)))
        .orElse(ResponseEntity.notFound().build());
  }
  
  @GetMapping("/movie/{movieId}")
  public ResponseEntity<List<RatingResponse>> getRatingsByMovieId(@PathVariable String movieId) {
    List<RatingResponse> ratings = ratingUseCase.getRatingsByMovieId(movieId).stream()
        .map(this::toResponse)
        .toList();
    return ResponseEntity.ok(ratings);
  }
  
  @GetMapping("/user/{userId}")
  public ResponseEntity<List<RatingResponse>> getRatingsByUserId(@PathVariable String userId) {
    List<RatingResponse> ratings = ratingUseCase.getRatingsByUserId(userId).stream()
        .map(this::toResponse)
        .toList();
    return ResponseEntity.ok(ratings);
  }
  
  @GetMapping("/user/{userId}/movie/{movieId}")
  public ResponseEntity<RatingResponse> getUserRatingForMovie(
      @PathVariable String userId,
      @PathVariable String movieId) {
    return ratingUseCase.getUserRatingForMovie(userId, movieId)
        .map(rating -> ResponseEntity.ok(toResponse(rating)))
        .orElse(ResponseEntity.notFound().build());
  }
  
  @GetMapping("/movie/{movieId}/average")
  public ResponseEntity<Double> getAverageRatingForMovie(@PathVariable String movieId) {
    Double averageRating = ratingUseCase.getAverageRatingForMovie(movieId);
    return ResponseEntity.ok(averageRating);
  }
  
  private RatingResponse toResponse(Rating rating) {
    return RatingResponse.builder()
        .id(rating.getId())
        .userId(rating.getUserId())
        .movieId(rating.getMovieId())
        .score(rating.getScore())
        .createdAt(rating.getCreatedAt())
        .updatedAt(rating.getUpdatedAt())
        .build();
  }
}
