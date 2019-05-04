package com.neostencil.responses;

import com.neostencil.model.entities.Cart;

public class ValidateCouponResponse extends OrderResponse{

  String messages;
  boolean valid;

  public ValidateCouponResponse(){
    super();
  }

  public ValidateCouponResponse(Cart c,double discount,
       boolean valid,String messages) {
    super(c,discount);
    this.messages = messages;
    this.valid = valid;
  }

  public String getMessages() {
    return messages;
  }

  public void setMessages(String messages) {
    this.messages = messages;
  }

  public boolean isValid() {
    return valid;
  }

  public void setValid(boolean valid) {
    this.valid = valid;
  }
}
