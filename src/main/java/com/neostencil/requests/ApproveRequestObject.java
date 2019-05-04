package com.neostencil.requests;

/**
 * 
 * @author shilpa
 *
 */

public class ApproveRequestObject {

  long requestId;

  String approverComment;

  /**
   * @return the requestId
   */
  public long getRequestId() {
    return requestId;
  }

  /**
   * @param requestId the requestId to set
   */
  public void setRequestId(long requestId) {
    this.requestId = requestId;
  }

  /**
   * @return the approverComment
   */
  public String getApproverComment() {
    return approverComment;
  }

  /**
   * @param approverComment the approverComment to set
   */
  public void setApproverComment(String approverComment) {
    this.approverComment = approverComment;
  }


}
