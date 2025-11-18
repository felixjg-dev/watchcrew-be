package com.watchcrew.application.service;

import com.watchcrew.application.port.in.CommunicationUseCase;
import com.watchcrew.application.port.out.MessageRepository;
import com.watchcrew.domain.model.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommunicationService implements CommunicationUseCase {
  
  private final MessageRepository messageRepository;
  
  @Override
  public Message sendMessage(String fromUserId, String fromUsername, String toUserId, String content) {
    Message message = Message.builder()
        .fromUserId(fromUserId)
        .fromUsername(fromUsername)
        .toUserId(toUserId)
        .content(content)
        .read(false)
        .build();
    
    return messageRepository.save(message);
  }
  
  @Override
  public List<Message> getConversation(String userId1, String userId2) {
    return messageRepository.findConversation(userId1, userId2).stream()
        .sorted(Comparator.comparing(Message::getSentAt))
        .toList();
  }
  
  @Override
  public List<Message> getUnreadMessages(String userId) {
    return messageRepository.findByToUserIdAndRead(userId, false);
  }
  
  @Override
  public Message markMessageAsRead(String messageId) {
    Message message = messageRepository.findById(messageId)
        .orElseThrow(() -> new IllegalArgumentException("Message not found"));
    
    message.setRead(true);
    return messageRepository.save(message);
  }
  
  @Override
  public void deleteMessage(String messageId, String userId) {
    Message message = messageRepository.findById(messageId)
        .orElseThrow(() -> new IllegalArgumentException("Message not found"));
    
    if (!message.getFromUserId().equals(userId) && !message.getToUserId().equals(userId)) {
      throw new IllegalArgumentException("Unauthorized to delete this message");
    }
    
    messageRepository.deleteById(messageId);
  }
}
