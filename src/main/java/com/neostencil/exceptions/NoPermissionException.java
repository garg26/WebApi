package com.neostencil.exceptions;

public class NoPermissionException extends RuntimeException {

  public NoPermissionException(String message, Throwable cause) {
    super(message, cause);
  }
}

