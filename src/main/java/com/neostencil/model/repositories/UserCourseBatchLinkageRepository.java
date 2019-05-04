package com.neostencil.model.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.neostencil.common.enums.UserCourseLinkageStatus;
import com.neostencil.model.entities.CourseBatch;
import com.neostencil.model.entities.User;
import com.neostencil.model.entities.UserCourseBatchLinkage;
import com.neostencil.projections.UserCourseBatchLinkageProjection;

public interface UserCourseBatchLinkageRepository
    extends JpaRepository<UserCourseBatchLinkage, Integer> {

  UserCourseBatchLinkage findByLinkageId(int id);

  @Query("select ucbl from UserCourseBatchLinkage ucbl where user.userId = :userId and courseBatch.id= :batchId")
  List<UserCourseBatchLinkage> findByUserAndBatch(@Param("userId") long userId,
      @Param("batchId") int batchId);

  @Query("select ucbl from UserCourseBatchLinkage ucbl where user.emailId = :email and courseBatch.id= :batchId")
  UserCourseBatchLinkage findByUserEmailIdAndBatch(@Param("email") String email,
      @Param("batchId") int batchId);

  @Query("select ul from UserCourseBatchLinkage ul where user.emailId=:email and courseBatch.id=:batchId")
  List<UserCourseBatchLinkage> findByUserAndBatch(@Param("email") String email,
      @Param("batchId") int batchId);

  @Query("select ucbl from UserCourseBatchLinkage ucbl where ucbl.courseBatch.id=:batchId order by ucbl.enrolledOn")
  List<UserCourseBatchLinkage> findByCourseBatch(@Param("batchId") int batchId);

  @Query("select ucbl from UserCourseBatchLinkage ucbl where user.userId = :userId and courseBatch.id= :batchId and ucbl.status = :status")
  UserCourseBatchLinkage findByUserAndBatchAndStatus(@Param("userId") long userId,
      @Param("batchId") int batchId, @Param("status") UserCourseLinkageStatus status);

  @Query("select ul from UserCourseBatchLinkage ul where user.emailId=:email and courseBatch.id=:batchId and ul.status = :status")
  UserCourseBatchLinkage findByUserAndBatchAndStatus(@Param("email") String email,
      @Param("batchId") int batchId, @Param("status") UserCourseLinkageStatus status);

  List<UserCourseBatchLinkageProjection> findAllProjectedByCourseBatch(CourseBatch cb);

  List<UserCourseBatchLinkage> findAllByUserAndStatus(User user, UserCourseLinkageStatus status);


}
