package com.neostencil.model.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.neostencil.model.entities.Discussion;
import com.neostencil.model.entities.User;

public interface DiscussionRepository extends JpaRepository<Discussion, Long>{
  

  List<Discussion> findAllByOrderByUpdatedAtDesc();
  
  List<Discussion> findAllBySlug(String slug);
  
  List<Discussion> findAllByUserOrderByUpdatedAtDesc(User user);
  
}
