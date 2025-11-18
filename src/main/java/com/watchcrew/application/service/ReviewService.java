package com.watchcrew.application.service;

import com.watchcrew.application.port.in.ReviewUseCase;
import com.watchcrew.application.port.out.ReviewRepository;
import com.watchcrew.domain.model.Review;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewService implements ReviewUseCase {
  
  private final ReviewRepository reviewRepository;
  
  @Override
  public Review createReview(String userId, String username, String movieId, String content) {
    Review review = Review.builder()
        .userId(userId)
        .username(username)
        .movieId(movieId)
        .content(content)
        .build();
    
    return reviewRepository.save(review);
  }
  
  @Override
  public Review updateReview(String reviewId, String userId, String content) {
    Review review = reviewRepository.findById(reviewId)
        .orElseThrow(() -> new IllegalArgumentException("Review not found"));
    
    if (!review.getUserId().equals(userId)) {
      throw new IllegalArgumentException("Unauthorized to update this review");
    }
    
    review.setContent(content);
    review.setUpdatedAt(LocalDateTime.now());
    
    return reviewRepository.save(review);
  }
  
  @Override
  public void deleteReview(String reviewId, String userId) {
    Review review = reviewRepository.findById(reviewId)
        .orElseThrow(() -> new IllegalArgumentException("Review not found"));
    
    if (!review.getUserId().equals(userId)) {
      throw new IllegalArgumentException("Unauthorized to delete this review");
    }
    
    reviewRepository.deleteById(reviewId);
  }
  
  @Override
  public Optional<Review> getReviewById(String reviewId) {
    return reviewRepository.findById(reviewId);
  }
  
  @Override
  public List<Review> getReviewsByMovieId(String movieId) {
    return reviewRepository.findByMovieId(movieId);
  }
  
  @Override
  public List<Review> getReviewsByUserId(String userId) {
    return reviewRepository.findByUserId(userId);
  }
}
