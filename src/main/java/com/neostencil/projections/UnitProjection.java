package com.neostencil.projections;

import com.neostencil.common.enums.PublishStatus;
import com.neostencil.common.enums.UnitType;
import org.springframework.beans.factory.annotation.Value;

public interface UnitProjection {

  int getUnitId();

  int getPosition();

  String getTitle();

  UnitType getType();

  String getTopic();


  @Value("#{target?.batch?.batchName}")
  String getBatchName();

  @Value("#{target?.batch?.id}")
  Integer getBatchId();

  PublishStatus getStatus();

  @Value("#{target?.batch?.course?.courseName}")
  String getCourseName();
}
