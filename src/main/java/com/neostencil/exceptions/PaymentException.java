package com.neostencil.exceptions;

public class PaymentException extends RuntimeException {

  public PaymentException(String message, Throwable cause) {
    super(message, cause);
  }
}

