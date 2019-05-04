package com.neostencil.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class GeneralException extends RuntimeException {

  private String errorMessage;
  private String resourceName;

  public GeneralException(String errorMessage, String resourceName) {
    super();
    this.errorMessage = errorMessage;
    this.resourceName = resourceName;
  }

  /**
   * @return the errorMessage
   */
  public String getErrorMessage() {
    return errorMessage;
  }

  /**
   * @param errorMessage the errorMessage to set
   */
  public void setErrorMessage(String errorMessage) {
    this.errorMessage = errorMessage;
  }

  /**
   * @return the resourceName
   */
  public String getResourceName() {
    return resourceName;
  }

  /**
   * @param resourceName the resourceName to set
   */
  public void setResourceName(String resourceName) {
    this.resourceName = resourceName;
  }
}
