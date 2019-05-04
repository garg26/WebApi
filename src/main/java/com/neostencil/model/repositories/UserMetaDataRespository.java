package com.neostencil.model.repositories;

import com.neostencil.model.entities.UserLectureStats;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserMetaDataRespository extends JpaRepository<UserLectureStats, Long> {

/*
  UserLectureStats findByUserUserIdAndUnitUnitId(long userId,int unitId);
*/

 UserLectureStats findByIdUserUserIdAndIdUnitUnitId(long userId,int unitId);

}
