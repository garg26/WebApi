package com.neostencil.model.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.neostencil.model.entities.ParentStudentLinkage;

public interface ParentStudentLinkageRepository extends JpaRepository<ParentStudentLinkage, Long>{

  List<ParentStudentLinkage> findAllByParentId(long parentId);
  
  ParentStudentLinkage findByStudentId(long studentId);
  
}
