package com.watchcrew.application.port.in;

import com.watchcrew.domain.model.Recommendation;

import java.util.List;

public interface RecommendationUseCase {
  Recommendation recommendMovieToFriend(String fromUserId, String fromUsername, 
                                        String toUserId, String movieId, 
                                        String movieTitle, String message);
  List<Recommendation> getPendingRecommendations(String userId);
  Recommendation markRecommendationAsViewed(String recommendationId);
  void deleteRecommendation(String recommendationId, String userId);
}
