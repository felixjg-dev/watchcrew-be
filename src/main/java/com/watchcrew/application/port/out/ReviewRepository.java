package com.watchcrew.application.port.out;

import com.watchcrew.domain.model.Review;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ReviewRepository extends MongoRepository<Review, String> {
  List<Review> findByMovieId(String movieId);
  List<Review> findByUserId(String userId);
}
