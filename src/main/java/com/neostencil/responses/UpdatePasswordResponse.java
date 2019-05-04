package com.neostencil.responses;

public class UpdatePasswordResponse {

  String userName;
  private boolean updateSuccess;

  public UpdatePasswordResponse() {
    super();
  }

  public boolean isUpdateSuccess() {
    return updateSuccess;
  }

  public void setUpdateSuccess(boolean updateSuccess) {
    this.updateSuccess = updateSuccess;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }
}
