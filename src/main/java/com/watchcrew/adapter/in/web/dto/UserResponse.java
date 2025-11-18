package com.watchcrew.adapter.in.web.dto;

import com.watchcrew.domain.model.Genre;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
  private String id;
  private String username;
  private List<Genre> preferences;
  private List<String> friendIds;
  private List<String> myMovieIds;
  private List<String> customListIds;
}
