package com.watchcrew.application.port.out;

import com.watchcrew.domain.model.Recommendation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface RecommendationRepository extends MongoRepository<Recommendation, String> {
  List<Recommendation> findByToUserId(String toUserId);
  List<Recommendation> findByToUserIdAndViewed(String toUserId, boolean viewed);
  List<Recommendation> findByFromUserId(String fromUserId);
}
