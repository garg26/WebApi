package com.neostencil.requests;

public class UserExamSegmentRequest {

    String city;
    String mobileNo;
    String examSegment;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getExamSegment() {
        return examSegment;
    }

    public void setExamSegment(String examSegment) {
        this.examSegment = examSegment;
    }
}