package com.watchcrew.adapter.in.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Registration request payload")
public class RegisterRequest {
  
  @NotBlank(message = "Username is required")
  @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
  @Schema(description = "Username", example = "johndoe")
  private String username;
  
  @NotBlank(message = "Email is required")
  @Email(message = "Email should be valid")
  @Schema(description = "Email address", example = "john@example.com")
  private String email;
  
  @NotBlank(message = "Password is required")
  @Size(min = 6, message = "Password must be at least 6 characters")
  @Schema(description = "Password", example = "password123")
  private String password;
  
  @Schema(description = "Preferred movie genres")
  private Set<String> preferredGenres;
}
