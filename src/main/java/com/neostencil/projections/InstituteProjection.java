package com.neostencil.projections;

import org.springframework.data.rest.core.config.Projection;
import com.neostencil.common.enums.PublishStatus;
import com.neostencil.model.entities.Institute;

@Projection(name = "instituteProjection", types = Institute.class)
public interface InstituteProjection {

  String getName();

  int getId();

  String getInstituteSlug();

  PublishStatus getStatus();

  String getImageAltText();

}
