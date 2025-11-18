package com.watchcrew.application.port.out;

import com.watchcrew.domain.model.Rating;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface RatingRepository extends MongoRepository<Rating, String> {
  Optional<Rating> findByUserIdAndMovieId(String userId, String movieId);
  List<Rating> findByMovieId(String movieId);
  List<Rating> findByUserId(String userId);
}
