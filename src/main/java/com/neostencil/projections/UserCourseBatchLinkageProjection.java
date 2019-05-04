package com.neostencil.projections;

import java.sql.Timestamp;
import org.springframework.data.rest.core.config.Projection;
import com.neostencil.common.enums.UserCourseLinkageStatus;
import com.neostencil.model.entities.UserCourseBatchLinkage;

@Projection(name = "userCourseBatchLinkageProjection", types = UserCourseBatchLinkage.class)
public interface UserCourseBatchLinkageProjection {

  int getLinkageId();

  UserProjection getUser();

  CourseBatchProjection getCourseBatch();

  Timestamp getEnrolledOn();

  Timestamp getExpiryDate();

  UserCourseLinkageStatus getStatus();

  void setStatus();

  boolean isRestrictedAccess();
}
