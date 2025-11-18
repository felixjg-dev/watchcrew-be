package com.watchcrew.adapter.in.web.controller;

import com.watchcrew.adapter.in.web.dto.MessageRequest;
import com.watchcrew.adapter.in.web.dto.MessageResponse;
import com.watchcrew.application.port.in.CommunicationUseCase;
import com.watchcrew.domain.model.Message;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
@Tag(name = "Messages", description = "User messaging and communication endpoints")
public class MessageController {
  
  private final CommunicationUseCase communicationUseCase;
  
  @PostMapping
  public ResponseEntity<MessageResponse> sendMessage(
      @RequestParam String fromUserId,
      @RequestParam String fromUsername,
      @RequestBody MessageRequest request) {
    Message message = communicationUseCase.sendMessage(
        fromUserId, 
        fromUsername, 
        request.getToUserId(), 
        request.getContent()
    );
    return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(message));
  }
  
  @GetMapping("/conversation")
  public ResponseEntity<List<MessageResponse>> getConversation(
      @RequestParam String userId1,
      @RequestParam String userId2) {
    List<MessageResponse> messages = communicationUseCase.getConversation(userId1, userId2)
        .stream()
        .map(this::toResponse)
        .toList();
    return ResponseEntity.ok(messages);
  }
  
  @GetMapping("/unread/{userId}")
  public ResponseEntity<List<MessageResponse>> getUnreadMessages(@PathVariable String userId) {
    List<MessageResponse> messages = communicationUseCase.getUnreadMessages(userId)
        .stream()
        .map(this::toResponse)
        .toList();
    return ResponseEntity.ok(messages);
  }
  
  @PutMapping("/{messageId}/read")
  public ResponseEntity<MessageResponse> markMessageAsRead(@PathVariable String messageId) {
    Message message = communicationUseCase.markMessageAsRead(messageId);
    return ResponseEntity.ok(toResponse(message));
  }
  
  @DeleteMapping("/{messageId}")
  public ResponseEntity<Void> deleteMessage(
      @PathVariable String messageId,
      @RequestParam String userId) {
    communicationUseCase.deleteMessage(messageId, userId);
    return ResponseEntity.noContent().build();
  }
  
  private MessageResponse toResponse(Message message) {
    return MessageResponse.builder()
        .id(message.getId())
        .fromUserId(message.getFromUserId())
        .fromUsername(message.getFromUsername())
        .toUserId(message.getToUserId())
        .content(message.getContent())
        .read(message.isRead())
        .sentAt(message.getSentAt())
        .build();
  }
}
