package com.neostencil.model.repositories;

import com.neostencil.model.entities.Unit;
import com.neostencil.projections.CourseProjection;
import com.neostencil.projections.UnitProjection;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UnitRepository extends JpaRepository<Unit, Integer> {

  Unit findByUnitId(int id);

  List<Unit> findByTypeId(int typeId);

  // List<Unit> findByProduct(boolean isProduct);
  List<UnitProjection> findByProduct(boolean isProduct);

  Collection<UnitProjection> findAllProjectedBy();

  Collection<UnitProjection> findAllProjectedByUpdatedAtGreaterThan(Date d);
}
