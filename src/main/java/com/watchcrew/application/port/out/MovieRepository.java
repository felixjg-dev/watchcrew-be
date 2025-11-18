package com.watchcrew.application.port.out;

import com.watchcrew.domain.model.Movie;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface MovieRepository extends MongoRepository<Movie, String> {
  Optional<Movie> findByExternalId(String externalId);
}
