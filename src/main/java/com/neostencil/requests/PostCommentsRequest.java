package com.neostencil.requests;

import com.neostencil.common.enums.PublishStatus;

public class PostCommentsRequest {

  public String getSlug() {
    return slug;
  }

  public void setSlug(String slug) {
    this.slug = slug;
  }

  String slug;

  public PublishStatus getStatus() {
    return status;
  }

  public void setStatus(PublishStatus status) {
    this.status = status;
  }

  PublishStatus status;

  public int getCommentId() {
    return commentId;
  }

  public void setCommentId(int commentId) {
    this.commentId = commentId;
  }

  int commentId;
}
