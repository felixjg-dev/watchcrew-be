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
@Document(collection = "reviews")
public class Review {
  @Id
  private String id;
  private String userId;
  private String username;
  private String movieId;
  private String content;
  
  @Builder.Default
  private LocalDateTime createdAt = LocalDateTime.now();
  
  private LocalDateTime updatedAt;
}
