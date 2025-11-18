package com.watchcrew.application.port.in;

import com.watchcrew.domain.model.Message;

import java.util.List;

public interface CommunicationUseCase {
  Message sendMessage(String fromUserId, String fromUsername, String toUserId, String content);
  List<Message> getConversation(String userId1, String userId2);
  List<Message> getUnreadMessages(String userId);
  Message markMessageAsRead(String messageId);
  void deleteMessage(String messageId, String userId);
}
