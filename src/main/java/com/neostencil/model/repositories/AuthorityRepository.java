package com.neostencil.model.repositories;

import com.neostencil.model.entities.Authority;
import com.neostencil.model.entities.AuthorityName;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {

  Authority findByAuthId(long authId);

  Authority findByName(AuthorityName name);
}
