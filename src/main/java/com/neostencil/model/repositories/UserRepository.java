package com.neostencil.model.repositories;

import com.neostencil.model.entities.Authority;
import com.neostencil.model.entities.User;
import com.neostencil.projections.UserProjection;
import java.util.List;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {

  User findByUserName(String username);

  User findByEmailId(String email);

  User findByUserId(Long id);

  User findByUserIdOrderByUserCourseBatchLinkagesDesc(Long id);

  @Query("SELECT distinct u from User u JOIN FETCH u.userCourseBatchLinkages where u.userId = id")
  User fetchUserByUserIdAndBatches(long id);

  UserProjection findAllProjectedByUserId(long userId);

  List<UserProjection> findAllProjectedBy();
  
  List<User> findByAuthorities(Set<Authority> authorities);

}
