package com.neostencil.model.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.neostencil.model.entities.Lock;
import com.neostencil.model.entities.LockId;

/**
 * 
 * @author shilpa
 *
 */
@Repository
public interface LockRepository extends JpaRepository<Lock, LockId>{

}
