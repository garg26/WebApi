package com.neostencil.responses;

public class UpdateProfileResponse {

    private boolean updateSuccess;

    public UpdateProfileResponse() {
        super();
    }

    public boolean isUpdateSuccess() {
        return updateSuccess;
    }
    public void setUpdateSuccess(boolean updateSuccess) {
        this.updateSuccess = updateSuccess;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    String fullName;
    String city;
    String state;
    String pincode;
    String password;
    String address;
    String mobile;

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    String emailId;
}
