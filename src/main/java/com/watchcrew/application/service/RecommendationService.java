package com.watchcrew.application.service;

import com.watchcrew.application.port.in.RecommendationUseCase;
import com.watchcrew.application.port.out.RecommendationRepository;
import com.watchcrew.application.port.out.UserRepository;
import com.watchcrew.domain.model.Recommendation;
import com.watchcrew.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecommendationService implements RecommendationUseCase {
  
  private final RecommendationRepository recommendationRepository;
  private final UserRepository userRepository;
  
  @Override
  public Recommendation recommendMovieToFriend(String fromUserId, String fromUsername, 
                                               String toUserId, String movieId, 
                                               String movieTitle, String message) {
    // Verify both users exist
    if (!userRepository.existsById(fromUserId)) {
      throw new IllegalArgumentException("From user not found");
    }
    if (!userRepository.existsById(toUserId)) {
      throw new IllegalArgumentException("To user not found");
    }
    
    Recommendation recommendation = Recommendation.builder()
        .fromUserId(fromUserId)
        .fromUsername(fromUsername)
        .toUserId(toUserId)
        .movieId(movieId)
        .movieTitle(movieTitle)
        .message(message)
        .viewed(false)
        .build();
    
    recommendation = recommendationRepository.save(recommendation);
    
    // Add to user's pending recommendations
    User toUser = userRepository.findById(toUserId)
        .orElseThrow(() -> new IllegalArgumentException("To user not found"));
    toUser.getPendingRecommendationIds().add(recommendation.getId());
    userRepository.save(toUser);
    
    return recommendation;
  }
  
  @Override
  public List<Recommendation> getPendingRecommendations(String userId) {
    return recommendationRepository.findByToUserIdAndViewed(userId, false);
  }
  
  @Override
  public Recommendation markRecommendationAsViewed(String recommendationId) {
    Recommendation recommendation = recommendationRepository.findById(recommendationId)
        .orElseThrow(() -> new IllegalArgumentException("Recommendation not found"));
    
    recommendation.setViewed(true);
    return recommendationRepository.save(recommendation);
  }
  
  @Override
  public void deleteRecommendation(String recommendationId, String userId) {
    Recommendation recommendation = recommendationRepository.findById(recommendationId)
        .orElseThrow(() -> new IllegalArgumentException("Recommendation not found"));
    
    if (!recommendation.getFromUserId().equals(userId) && !recommendation.getToUserId().equals(userId)) {
      throw new IllegalArgumentException("Unauthorized to delete this recommendation");
    }
    
    recommendationRepository.deleteById(recommendationId);
  }
}
