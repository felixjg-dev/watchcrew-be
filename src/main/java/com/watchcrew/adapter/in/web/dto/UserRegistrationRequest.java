package com.watchcrew.adapter.in.web.dto;

import com.watchcrew.domain.model.Genre;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegistrationRequest {
  private String username;
  private String password;
  private List<Genre> preferences;
}
