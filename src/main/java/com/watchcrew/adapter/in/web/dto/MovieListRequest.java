package com.watchcrew.adapter.in.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieListRequest {
  private String name;
  private String description;
  private List<String> movieIds;
}
