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
public class ReviewResponse {
  private String id;
  private String userId;
  private String username;
  private String movieId;
  private String content;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}
