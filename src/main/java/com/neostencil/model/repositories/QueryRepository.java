package com.neostencil.model.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import com.neostencil.model.entities.Query;
import com.neostencil.model.entities.Unit;
import com.neostencil.model.entities.User;

public interface QueryRepository
    extends JpaRepository<Query, Integer>, JpaSpecificationExecutor<Query> {

  Query findById(int id);

  List<Query> findByQueriedByOrderByCreatedAtDesc(User queriedBy);

  List<Query> findByQueriedToOrderByCreatedAtDesc(User queriedTo);

  List<Query> findByUnitOrderByCreatedAtDesc(Unit unit);
}
