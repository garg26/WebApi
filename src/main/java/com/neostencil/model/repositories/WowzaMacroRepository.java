package com.neostencil.model.repositories;

import com.neostencil.model.entities.WowzaMacro;
import com.neostencil.projections.WowzaMacroProjection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WowzaMacroRepository extends JpaRepository<WowzaMacro, String> {

  WowzaMacro findByName(String name);

  WowzaMacroProjection findAllProjectedByName(String name);

  List<WowzaMacroProjection> findAllProjectedBy();
}
