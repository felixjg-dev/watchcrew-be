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
public class RatingResponse {
  private String id;
  private String userId;
  private String movieId;
  private Double score;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}
