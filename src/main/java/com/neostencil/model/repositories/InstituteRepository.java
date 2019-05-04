package com.neostencil.model.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.neostencil.model.entities.Institute;
import com.neostencil.projections.InstituteProjection;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface InstituteRepository extends JpaRepository<Institute, Integer> {


  Institute findById(int id);

  @Query("select ins from Institute ins where ins.name like %:name%")
  Institute findByName(@Param("name") String name);

  Institute findByInstituteSlug(String slug);

  List<InstituteProjection> findAllProjectedBy();
}
