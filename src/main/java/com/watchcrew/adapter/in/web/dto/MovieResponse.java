package com.watchcrew.adapter.in.web.dto;

import com.watchcrew.domain.model.Genre;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MovieResponse {
  private String id;
  private String externalId;
  private String title;
  private String originalTitle;
  private List<Genre> genres;
  private Double voteAverage;
  private Integer voteCount;
  private LocalDate releaseDate;
  private String overview;
  private String posterPath;
  private String backdropPath;
  private Integer runtime;
  private String originalLanguage;
  private Double averageUserRating;
}
