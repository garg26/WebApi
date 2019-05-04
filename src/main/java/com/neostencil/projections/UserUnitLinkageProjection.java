package com.neostencil.projections;

import org.springframework.data.rest.core.config.Projection;
import com.neostencil.model.entities.AssignmentSubmission;
import com.neostencil.model.entities.UserUnitLinkage;

@Projection(name = "userUnitLinkageProjection", types = UserUnitLinkage.class)
public interface UserUnitLinkageProjection {

  int getLinkageId();

  boolean isActive();

  UserProjection getUser();

  UnitProjection getUnit();

  AssignmentSubmission getAssignmentSubmission();

}
