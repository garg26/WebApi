package com.neostencil.model.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.neostencil.model.entities.CustomPost;

public interface CustomPostRepository extends JpaRepository<CustomPost, String>{
  
  CustomPost findBySlug(String slug);
 
  CustomPost findByCustomPostId(int id);
}

