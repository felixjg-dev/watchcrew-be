package com.watchcrew.adapter.in.web.controller;

import com.watchcrew.adapter.in.web.dto.RecommendationRequest;
import com.watchcrew.adapter.in.web.dto.RecommendationResponse;
import com.watchcrew.application.port.in.RecommendationUseCase;
import com.watchcrew.domain.model.Recommendation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recommendations")
@RequiredArgsConstructor
@Tag(name = "Recommendations", description = "Movie recommendation sharing endpoints")
public class RecommendationController {
  
  private final RecommendationUseCase recommendationUseCase;
  
  @PostMapping
  public ResponseEntity<RecommendationResponse> recommendMovieToFriend(
      @RequestParam String fromUserId,
      @RequestParam String fromUsername,
      @RequestBody RecommendationRequest request) {
    Recommendation recommendation = recommendationUseCase.recommendMovieToFriend(
        fromUserId, 
        fromUsername, 
        request.getFriendId(), 
        request.getMovieId(),
        "", // movieTitle will be fetched by frontend or passed separately
        request.getMessage()
    );
    return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(recommendation));
  }
  
  @GetMapping("/pending/{userId}")
  public ResponseEntity<List<RecommendationResponse>> getPendingRecommendations(
      @PathVariable String userId) {
    List<RecommendationResponse> recommendations = 
        recommendationUseCase.getPendingRecommendations(userId).stream()
            .map(this::toResponse)
            .toList();
    return ResponseEntity.ok(recommendations);
  }
  
  @PutMapping("/{recommendationId}/view")
  public ResponseEntity<RecommendationResponse> markRecommendationAsViewed(
      @PathVariable String recommendationId) {
    Recommendation recommendation = 
        recommendationUseCase.markRecommendationAsViewed(recommendationId);
    return ResponseEntity.ok(toResponse(recommendation));
  }
  
  @DeleteMapping("/{recommendationId}")
  public ResponseEntity<Void> deleteRecommendation(
      @PathVariable String recommendationId,
      @RequestParam String userId) {
    recommendationUseCase.deleteRecommendation(recommendationId, userId);
    return ResponseEntity.noContent().build();
  }
  
  private RecommendationResponse toResponse(Recommendation recommendation) {
    return RecommendationResponse.builder()
        .id(recommendation.getId())
        .fromUserId(recommendation.getFromUserId())
        .fromUsername(recommendation.getFromUsername())
        .toUserId(recommendation.getToUserId())
        .movieId(recommendation.getMovieId())
        .movieTitle(recommendation.getMovieTitle())
        .message(recommendation.getMessage())
        .viewed(recommendation.isViewed())
        .createdAt(recommendation.getCreatedAt())
        .build();
  }
}
