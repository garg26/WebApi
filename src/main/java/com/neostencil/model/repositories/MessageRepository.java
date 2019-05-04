package com.neostencil.model.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.neostencil.model.entities.Message;

public interface MessageRepository extends JpaRepository<Message, Integer> {

  Message findById(int id);

  List<Message> findByGroupIdOrderBySendTimeDesc(int groupId);

  List<Message> findByMessageFromOrderBySendTimeDesc(String messageFrom);

  List<Message> findByMessageToOrderBySendTimeDesc(String to);

  @Query("select m from Message m where m.messageFrom like :currentUser or m.messageTo like :currentUser order by m.sendTime")
  List<Message> findAllMessageThreads(@Param("currentUser") String currentUser);
}
