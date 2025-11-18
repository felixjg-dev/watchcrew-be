package com.watchcrew.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "messages")
public class Message {
  @Id
  private String id;
  private String fromUserId;
  private String fromUsername;
  private String toUserId;
  private String content;
  private boolean read;
  
  @Builder.Default
  private LocalDateTime sentAt = LocalDateTime.now();
}
