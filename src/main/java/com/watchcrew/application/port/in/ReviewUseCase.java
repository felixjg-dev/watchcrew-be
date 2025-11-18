package com.watchcrew.application.port.in;

import com.watchcrew.domain.model.Review;

import java.util.List;
import java.util.Optional;

public interface ReviewUseCase {
  Review createReview(String userId, String username, String movieId, String content);
  Review updateReview(String reviewId, String userId, String content);
  void deleteReview(String reviewId, String userId);
  Optional<Review> getReviewById(String reviewId);
  List<Review> getReviewsByMovieId(String movieId);
  List<Review> getReviewsByUserId(String userId);
}
