package com.watchcrew.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "movie_lists")
public class MovieList {
  @Id
  private String id;
  private String userId;
  private String name;
  private String description;
  
  @Builder.Default
  private List<String> movieIds = new ArrayList<>();
  
  @Builder.Default
  private LocalDateTime createdAt = LocalDateTime.now();
  
  private LocalDateTime updatedAt;
}
