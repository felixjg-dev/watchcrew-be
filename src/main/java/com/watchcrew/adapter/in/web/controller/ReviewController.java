package com.watchcrew.adapter.in.web.controller;

import com.watchcrew.adapter.in.web.dto.ReviewRequest;
import com.watchcrew.adapter.in.web.dto.ReviewResponse;
import com.watchcrew.application.port.in.ReviewUseCase;
import com.watchcrew.domain.model.Review;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
@Tag(name = "Reviews", description = "Movie review management endpoints")
public class ReviewController {
  
  private final ReviewUseCase reviewUseCase;
  
  @PostMapping
  public ResponseEntity<ReviewResponse> createReview(
      @RequestParam String userId,
      @RequestParam String username,
      @RequestBody ReviewRequest request) {
    Review review = reviewUseCase.createReview(
        userId, username, request.getMovieId(), request.getContent());
    return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(review));
  }
  
  @PutMapping("/{reviewId}")
  public ResponseEntity<ReviewResponse> updateReview(
      @PathVariable String reviewId,
      @RequestParam String userId,
      @RequestBody ReviewRequest request) {
    Review review = reviewUseCase.updateReview(reviewId, userId, request.getContent());
    return ResponseEntity.ok(toResponse(review));
  }
  
  @DeleteMapping("/{reviewId}")
  public ResponseEntity<Void> deleteReview(
      @PathVariable String reviewId,
      @RequestParam String userId) {
    reviewUseCase.deleteReview(reviewId, userId);
    return ResponseEntity.noContent().build();
  }
  
  @GetMapping("/{reviewId}")
  public ResponseEntity<ReviewResponse> getReviewById(@PathVariable String reviewId) {
    return reviewUseCase.getReviewById(reviewId)
        .map(review -> ResponseEntity.ok(toResponse(review)))
        .orElse(ResponseEntity.notFound().build());
  }
  
  @GetMapping("/movie/{movieId}")
  public ResponseEntity<List<ReviewResponse>> getReviewsByMovieId(@PathVariable String movieId) {
    List<ReviewResponse> reviews = reviewUseCase.getReviewsByMovieId(movieId).stream()
        .map(this::toResponse)
        .toList();
    return ResponseEntity.ok(reviews);
  }
  
  @GetMapping("/user/{userId}")
  public ResponseEntity<List<ReviewResponse>> getReviewsByUserId(@PathVariable String userId) {
    List<ReviewResponse> reviews = reviewUseCase.getReviewsByUserId(userId).stream()
        .map(this::toResponse)
        .toList();
    return ResponseEntity.ok(reviews);
  }
  
  private ReviewResponse toResponse(Review review) {
    return ReviewResponse.builder()
        .id(review.getId())
        .userId(review.getUserId())
        .username(review.getUsername())
        .movieId(review.getMovieId())
        .content(review.getContent())
        .createdAt(review.getCreatedAt())
        .updatedAt(review.getUpdatedAt())
        .build();
  }
}
