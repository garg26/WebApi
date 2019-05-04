package com.neostencil.model.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.neostencil.model.entities.VideoChunks;

public interface VideoChunkRepository extends JpaRepository<VideoChunks, Long>{
  
}
