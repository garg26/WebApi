package com.neostencil.requests;

import java.io.Serializable;

public class PayLaterRequest implements Serializable {
    String firstName;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCollegeName() {
        return collegeName;
    }

    public void setCollegeName(String collegeName) {
        this.collegeName = collegeName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPassyear() {
        return passyear;
    }

    public void setPassyear(String passyear) {
        this.passyear = passyear;
    }

    String lastName;
    String email;
    String mobile;
    String collegeName;
    String city;
    String passyear;
    String courseName;

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCoursePrice() {
        return coursePrice;
    }

    public void setCoursePrice(String coursePrice) {
        this.coursePrice = coursePrice;
    }

    public String getCoursePage() {
        return coursePage;
    }

    public void setCoursePage(String coursePage) {
        this.coursePage = coursePage;
    }

    String coursePrice;
    String coursePage;

}
