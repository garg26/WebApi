package com.neostencil.responses;

import java.util.List;
import com.neostencil.dtos.UserCourseBatchLinkageDTO;

public class StudentsPerBatchResponse {

  String courseBatchName;
  List<UserCourseBatchLinkageDTO> linkages;

  /**
   * @return the courseBatchName
   */
  public String getCourseBatchName() {
    return courseBatchName;
  }

  /**
   * @param courseBatchName the courseBatchName to set
   */
  public void setCourseBatchName(String courseBatchName) {
    this.courseBatchName = courseBatchName;
  }

  /**
   * @return the linkages
   */
  public List<UserCourseBatchLinkageDTO> getLinkages() {
    return linkages;
  }

  /**
   * @param linkages the linkages to set
   */
  public void setLinkages(List<UserCourseBatchLinkageDTO> linkages) {
    this.linkages = linkages;
  }



}
