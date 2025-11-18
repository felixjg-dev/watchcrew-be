package com.watchcrew.adapter.in.websocket;

import com.watchcrew.adapter.in.web.dto.MessageRequest;
import com.watchcrew.adapter.in.web.dto.MessageResponse;
import com.watchcrew.application.port.in.CommunicationUseCase;
import com.watchcrew.domain.model.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class WebSocketMessageController {
  
  private final CommunicationUseCase communicationUseCase;
  private final SimpMessagingTemplate messagingTemplate;
  
  @MessageMapping("/chat.send")
  public void sendMessage(@Payload MessageRequest request, Principal principal) {
    // In production, extract userId and username from authenticated principal
    // For now, assuming they're passed in the request
    String fromUserId = principal.getName(); // This would come from security context
    
    Message message = communicationUseCase.sendMessage(
        fromUserId, 
        fromUserId, // username would come from user service
        request.getToUserId(), 
        request.getContent()
    );
    
    MessageResponse response = toResponse(message);
    
    // Send to specific user
    messagingTemplate.convertAndSendToUser(
        request.getToUserId(),
        "/queue/messages",
        response
    );
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
