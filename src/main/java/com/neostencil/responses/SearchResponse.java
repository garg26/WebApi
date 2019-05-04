package com.neostencil.responses;

import com.neostencil.model.entities.Course;
import com.neostencil.model.entities.Institute;
import com.neostencil.model.entities.Post;
import com.neostencil.model.entities.TeacherDetails;
import java.util.ArrayList;
import java.util.List;

public class SearchResponse {

  List<Object> teachers;

  List<Object> courses;

  /*List<Object> institutes;*/

  List<Object> posts;

  public SearchResponse() {
    teachers = new ArrayList<>();
    courses = new ArrayList<>();
   /* institutes = new ArrayList<>();*/
    posts = new ArrayList<>();
  }

  /**
   * @return the teachers
   */
  public List<Object> getTeachers() {
    return teachers;
  }

  /**
   * @param teachers the teachers to set
   */
  public void setTeachers(List<Object> teachers) {
    this.teachers = teachers;
  }

  /**
   * @return the courses
   */
  public List<Object> getCourses() {
    return courses;
  }

  /**
   * @param courses the courses to set
   */
  public void setCourses(List<Object> courses) {
    this.courses = courses;
  }

 /* *//**
   * @return the institutes
   *//*
  public List<Object> getInstitutes() {
    return institutes;
  }

  *//**
   * @param institutes the institutes to set
   *//*
  public void setInstitutes(List<Object> institutes) {
    this.institutes = institutes;
  }*/

  /**
   * @return the posts
   */
  public List<Object> getPosts() {
    return posts;
  }

  /**
   * @param posts the posts to set
   */
  public void setPosts(List<Object> posts) {
    this.posts = posts;
  }
}
