package com.neostencil.model.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.neostencil.model.entities.VideoRequest;

public interface VideoRequestRepository extends JpaRepository<VideoRequest, Long> {

  VideoRequest findById(long id);
}
