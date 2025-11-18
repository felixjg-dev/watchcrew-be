package com.watchcrew.adapter.in.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Authentication response with JWT token")
public class AuthResponse {
  
  @Schema(description = "JWT access token", example = "eyJhbGciOiJIUzUxMiJ9...")
  private String token;
  
  @Schema(description = "Token type", example = "Bearer")
  private String type = "Bearer";
  
  @Schema(description = "User ID", example = "507f1f77bcf86cd799439011")
  private String userId;
  
  @Schema(description = "Username", example = "johndoe")
  private String username;
  
  @Schema(description = "Email address", example = "john@example.com")
  private String email;
  
  public AuthResponse(String token, String userId, String username, String email) {
    this.token = token;
    this.userId = userId;
    this.username = username;
    this.email = email;
  }
}
