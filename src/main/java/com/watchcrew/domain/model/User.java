package com.watchcrew.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "users")
public class User {
  @Id
  private String id;
  private String username;
  private String email;
  private String password;
  
  @Builder.Default
  private List<String> friendIds = new ArrayList<>();
  
  @Builder.Default
  private List<Genre> preferredGenres = new ArrayList<>();
  
  @Builder.Default
  private List<String> myMovieIds = new ArrayList<>();
  
  @Builder.Default
  private List<String> customListIds = new ArrayList<>();
  
  @Builder.Default
  private List<String> pendingRecommendationIds = new ArrayList<>();
}