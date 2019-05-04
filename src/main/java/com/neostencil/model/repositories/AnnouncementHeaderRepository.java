package com.neostencil.model.repositories;

import com.neostencil.model.entities.AnnouncementHeader;
import java.util.List;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AnnouncementHeaderRepository extends JpaRepository<AnnouncementHeader, Integer> {

  AnnouncementHeader findByHeaderId(int headerId);


//  @Query(value="select * from AnnouncementHeader a where %:slug% like %a.onPageToDisplay%", nativeQuery = true)
//  List<AnnouncementHeader> findByRelatedKeywordContainingIgnoreCase(@Param("slug") String slug);



}
