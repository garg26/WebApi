package com.neostencil.responses;

import com.neostencil.common.enums.PublishStatus;

public class PostCommentsResponse {

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

}
