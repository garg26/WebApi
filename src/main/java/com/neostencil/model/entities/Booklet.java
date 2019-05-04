package com.neostencil.model.entities;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Booklet {

  @Column(name = "booklet_title")
  String bookletTitle;

  @Column(name = "content_url")
  String contentUrl;

  /**
   * @return the bookletTitle
   */
  public String getBookletTitle() {
    return bookletTitle;
  }

  /**
   * @param bookletTitle the bookletTitle to set
   */
  public void setBookletTitle(String bookletTitle) {
    this.bookletTitle = bookletTitle;
  }

  /**
   * @return the contentUrl
   */
  public String getContentUrl() {
    return contentUrl;
  }

  /**
   * @param contentUrl the contentUrl to set
   */
  public void setContentUrl(String contentUrl) {
    this.contentUrl = contentUrl;
  }

}
