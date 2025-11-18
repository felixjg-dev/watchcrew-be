package com.watchcrew.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "movies")
public class Movie {
  @Id
  private String id;
  private String externalId; // TMDB or IMDB ID
  private String title;
  private String originalTitle;
  
  @Builder.Default
  private List<Genre> genres = new ArrayList<>();
  
  private Double voteAverage;
  private Integer voteCount;
  private LocalDate releaseDate;
  private String overview;
  private String posterPath;
  private String backdropPath;
  private Integer runtime;
  private String originalLanguage;
  
  @Builder.Default
  private List<String> reviewIds = new ArrayList<>();
  
  @Builder.Default
  private List<String> ratingIds = new ArrayList<>();
  
  private Double averageUserRating;
}
