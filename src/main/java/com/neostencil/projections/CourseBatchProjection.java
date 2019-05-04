package com.neostencil.projections;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;
import com.neostencil.model.entities.CourseBatch;

@Projection(name = "courseBatchProjection", types = CourseBatch.class)
public interface CourseBatchProjection {

  int getId();

  String getBatchName();

  CourseProjection getCourse();

}
