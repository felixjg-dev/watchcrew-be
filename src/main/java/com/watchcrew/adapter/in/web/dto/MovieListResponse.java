package com.watchcrew.adapter.in.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MovieListResponse {
  private String id;
  private String userId;
  private String name;
  private String description;
  private List<String> movieIds;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}
