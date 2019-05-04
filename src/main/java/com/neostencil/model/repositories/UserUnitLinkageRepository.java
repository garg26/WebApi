package com.neostencil.model.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.neostencil.model.entities.Unit;
import com.neostencil.model.entities.UserUnitLinkage;
import com.neostencil.projections.UserUnitLinkageProjection;

public interface UserUnitLinkageRepository extends JpaRepository<UserUnitLinkage, Integer> {

  UserUnitLinkage findByLinkageId(int linkageId);

  @Query("select uul from UserUnitLinkage uul where unit.unitId=:unitId")
  List<UserUnitLinkage> findByUnit(@Param("unitId") int unitId);

  @Query("select uul from UserUnitLinkage uul where unit.unitId=:unitId and user.emailId=:email")
  UserUnitLinkage findByUserAndUnit(@Param("email") String email, @Param("unitId") int unitId);

  @Query("select uul from UserUnitLinkage uul where unit.unitId=:unitId and user.userId=:userId")
  UserUnitLinkage findByUserAndUnit(@Param("userId") long userId, @Param("unitId") int unitId);
  
  @Query("select uul from UserUnitLinkage uul where user.userId=:userId order by unit.position")
  List<UserUnitLinkage> findByUser(@Param("userId")long userId);

  List<UserUnitLinkageProjection> findAllProjectedByUnit(Unit unit);
}
