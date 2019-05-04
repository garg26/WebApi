package com.neostencil.model.entities;

import com.neostencil.common.enums.AssignmentStatus;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;


@Embeddable
public class AssignmentSubmission {

  @Column(name = "attachment_url")
  private String attachmentUrl;

  @Column(name = "attachment_name")
  private String attachmentName;

  @Column(name = "attachment_type")
  private String type;

  @Column(name = "upload_date")
  Date submittedDate;

  @Column(name = "upload_status")
  @Enumerated(EnumType.STRING)
  AssignmentStatus status;


  public String getAttachmentName() {
    return attachmentName;
  }

  public void setAttachmentName(String attachmentName) {
    this.attachmentName = attachmentName;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public Date getSubmittedDate() {
    return submittedDate;
  }

  public void setSubmittedDate(Date submittedDate) {
    this.submittedDate = submittedDate;
  }

  public AssignmentStatus getStatus() {
    return status;
  }

  public void setStatus(AssignmentStatus status) {
    this.status = status;
  }

  public String getAttachmentUrl() {
    return attachmentUrl;
  }

  public void setAttachmentUrl(String attachmentUrl) {
    this.attachmentUrl = attachmentUrl;
  }
}
