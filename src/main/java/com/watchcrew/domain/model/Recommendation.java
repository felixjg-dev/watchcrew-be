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
@Document(collection = "recommendations")
public class Recommendation {
  @Id
  private String id;
  private String fromUserId;
  private String fromUsername;
  private String toUserId;
  private String movieId;
  private String movieTitle;
  private String message;
  private boolean viewed;
  
  @Builder.Default
  private LocalDateTime createdAt = LocalDateTime.now();
}
