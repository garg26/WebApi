package com.neostencil.model.repositories;

import com.neostencil.model.entities.JWMacro;
import com.neostencil.projections.JWMacroProjection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JWMacroRepository extends JpaRepository<JWMacro, String> {

  JWMacro findByName(String name);

  JWMacroProjection findAllProjectedByName(String name);

  List<JWMacroProjection> findAllProjectedBy();
}
