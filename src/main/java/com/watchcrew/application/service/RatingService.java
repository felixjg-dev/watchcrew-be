package com.watchcrew.application.service;

import com.watchcrew.application.port.in.RatingUseCase;
import com.watchcrew.application.port.out.MovieRepository;
import com.watchcrew.application.port.out.RatingRepository;
import com.watchcrew.domain.model.Movie;
import com.watchcrew.domain.model.Rating;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RatingService implements RatingUseCase {
  
  private final RatingRepository ratingRepository;
  private final MovieRepository movieRepository;
  
  @Override
  public Rating rateMovie(String userId, String movieId, Double score) {
    if (score < 1.0 || score > 10.0) {
      throw new IllegalArgumentException("Rating score must be between 1 and 10");
    }
    
    // Check if user already rated this movie
    Optional<Rating> existingRating = ratingRepository.findByUserIdAndMovieId(userId, movieId);
    if (existingRating.isPresent()) {
      return updateRating(existingRating.get().getId(), userId, score);
    }
    
    Rating rating = Rating.builder()
        .userId(userId)
        .movieId(movieId)
        .score(score)
        .build();
    
    rating = ratingRepository.save(rating);
    updateMovieAverageRating(movieId);
    
    return rating;
  }
  
  @Override
  public Rating updateRating(String ratingId, String userId, Double score) {
    if (score < 1.0 || score > 10.0) {
      throw new IllegalArgumentException("Rating score must be between 1 and 10");
    }
    
    Rating rating = ratingRepository.findById(ratingId)
        .orElseThrow(() -> new IllegalArgumentException("Rating not found"));
    
    if (!rating.getUserId().equals(userId)) {
      throw new IllegalArgumentException("Unauthorized to update this rating");
    }
    
    rating.setScore(score);
    rating.setUpdatedAt(LocalDateTime.now());
    rating = ratingRepository.save(rating);
    
    updateMovieAverageRating(rating.getMovieId());
    
    return rating;
  }
  
  @Override
  public void deleteRating(String ratingId, String userId) {
    Rating rating = ratingRepository.findById(ratingId)
        .orElseThrow(() -> new IllegalArgumentException("Rating not found"));
    
    if (!rating.getUserId().equals(userId)) {
      throw new IllegalArgumentException("Unauthorized to delete this rating");
    }
    
    String movieId = rating.getMovieId();
    ratingRepository.deleteById(ratingId);
    updateMovieAverageRating(movieId);
  }
  
  @Override
  public Optional<Rating> getRatingById(String ratingId) {
    return ratingRepository.findById(ratingId);
  }
  
  @Override
  public Optional<Rating> getUserRatingForMovie(String userId, String movieId) {
    return ratingRepository.findByUserIdAndMovieId(userId, movieId);
  }
  
  @Override
  public List<Rating> getRatingsByMovieId(String movieId) {
    return ratingRepository.findByMovieId(movieId);
  }
  
  @Override
  public List<Rating> getRatingsByUserId(String userId) {
    return ratingRepository.findByUserId(userId);
  }
  
  @Override
  public Double getAverageRatingForMovie(String movieId) {
    List<Rating> ratings = ratingRepository.findByMovieId(movieId);
    
    if (ratings.isEmpty()) {
      return 0.0;
    }
    
    return ratings.stream()
        .mapToDouble(Rating::getScore)
        .average()
        .orElse(0.0);
  }
  
  private void updateMovieAverageRating(String movieId) {
    Optional<Movie> movieOpt = movieRepository.findById(movieId);
    if (movieOpt.isPresent()) {
      Movie movie = movieOpt.get();
      Double averageRating = getAverageRatingForMovie(movieId);
      movie.setAverageUserRating(averageRating);
      movieRepository.save(movie);
    }
  }
}
