package com.watchcrew.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "ratings")
public class Rating {
  @Id
  private String id;
  private String userId;
  private String movieId;
  private Double score; // 1-10 scale
  
  @Builder.Default
  private LocalDateTime createdAt = LocalDateTime.now();
  
  private LocalDateTime updatedAt;
}
