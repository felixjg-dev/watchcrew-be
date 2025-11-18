package com.watchcrew.application.port.out;

import com.watchcrew.domain.model.MovieList;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MovieListRepository extends MongoRepository<MovieList, String> {
  List<MovieList> findByUserId(String userId);
}
