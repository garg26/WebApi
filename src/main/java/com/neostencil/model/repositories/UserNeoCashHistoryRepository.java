package com.neostencil.model.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.neostencil.model.entities.UserNeoCashHistory;

public interface UserNeoCashHistoryRepository extends JpaRepository<UserNeoCashHistory, Long>{
  

    List<UserNeoCashHistory> findAllByUserIdOrderByCreatedAt(long userId);
    
    UserNeoCashHistory findTopByUserIdOrderByCreatedAt(long userId);
}
