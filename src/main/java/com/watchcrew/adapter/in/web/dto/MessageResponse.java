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
public class MessageResponse {
  private String id;
  private String fromUserId;
  private String fromUsername;
  private String toUserId;
  private String content;
  private boolean read;
  private LocalDateTime sentAt;
}
