package com.neostencil.requests;

import java.io.Serializable;
import java.util.List;
import com.neostencil.model.entities.UserCourseBatchLinkage;

public class UserBatchLinkagesRequest implements Serializable {

  List<String> emails;
  int batchId;
  int newValidity;

  /**
   * @return the emails
   */
  public List<String> getEmails() {
    return emails;
  }

  /**
   * @param emails the emails to set
   */
  public void setEmails(List<String> emails) {
    this.emails = emails;
  }

  /**
   * @return the batchId
   */
  public int getBatchId() {
    return batchId;
  }

  /**
   * @param batchId the batchId to set
   */
  public void setBatchId(int batchId) {
    this.batchId = batchId;
  }

  /**
   * @return the newValidity
   */
  public int getNewValidity() {
    return newValidity;
  }

  /**
   * @param newValidity the newValidity to set
   */
  public void setNewValidity(int newValidity) {
    this.newValidity = newValidity;
  }


}
