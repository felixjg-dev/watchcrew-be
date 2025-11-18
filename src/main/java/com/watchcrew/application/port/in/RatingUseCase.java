package com.watchcrew.application.port.in;

import com.watchcrew.domain.model.Rating;

import java.util.List;
import java.util.Optional;

public interface RatingUseCase {
  Rating rateMovie(String userId, String movieId, Double score);
  Rating updateRating(String ratingId, String userId, Double score);
  void deleteRating(String ratingId, String userId);
  Optional<Rating> getRatingById(String ratingId);
  Optional<Rating> getUserRatingForMovie(String userId, String movieId);
  List<Rating> getRatingsByMovieId(String movieId);
  List<Rating> getRatingsByUserId(String userId);
  Double getAverageRatingForMovie(String movieId);
}
