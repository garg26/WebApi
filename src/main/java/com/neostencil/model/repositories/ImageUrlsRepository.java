package com.neostencil.model.repositories;

import com.neostencil.model.entities.ImageUrls;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageUrlsRepository extends JpaRepository<ImageUrls, String> {

  ImageUrls findByImagePath(String path);

  ImageUrls findByImageServingUrl(String servingUrl);


}
