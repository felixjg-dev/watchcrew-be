package com.watchcrew.adapter.in.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecommendationResponse {
  private String id;
  private String fromUserId;
  private String fromUsername;
  private String toUserId;
  private String movieId;
  private String movieTitle;
  private String message;
  private boolean viewed;
  private LocalDateTime createdAt;
}
