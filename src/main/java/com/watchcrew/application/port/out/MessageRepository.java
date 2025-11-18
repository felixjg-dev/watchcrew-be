package com.watchcrew.application.port.out;

import com.watchcrew.domain.model.Message;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface MessageRepository extends MongoRepository<Message, String> {
  @Query("{ $or: [ { 'fromUserId': ?0, 'toUserId': ?1 }, { 'fromUserId': ?1, 'toUserId': ?0 } ] }")
  List<Message> findConversation(String userId1, String userId2);
  
  List<Message> findByToUserIdAndRead(String toUserId, boolean read);
  List<Message> findByFromUserId(String fromUserId);
  List<Message> findByToUserId(String toUserId);
}
