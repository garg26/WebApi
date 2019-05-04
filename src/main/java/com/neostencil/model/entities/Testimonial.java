package com.neostencil.model.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

@Entity
@Table(name = "ns_testimonials")
public class Testimonial extends DomainObject {

  @Id
  @GeneratedValue(strategy = GenerationType.TABLE, generator = "testimonial_gen")
  @TableGenerator(name = "testimonial_gen", table = "ns_testimonial_id_gen",
      pkColumnName = "GEN_NAME", valueColumnName = "GEN_VAL", pkColumnValue = "TestimonialId_Gen",
      initialValue = 1000, allocationSize = 1)
  @Column(name = "testimonial_id")
  private int id;

  @Column(name = "text", length = 10000)
  private String text;

  @Column(name = "student_name")
  private String studentName;

  @Column(name = "student_image")
  private String studentImage;

  @Column(name = "student_rank")
  private Integer studentRank;

  @Column(name = "exam")
  private String exam;

  @Column(name = "year")
  private String year;

  @Column(name = "location")
  private String location;

  @Column(name = "course_url")
  private String courseUrl;

  @Column(name = "testimonial_title")
  private String testimonialTitle;

  @Column(name = "course_title")
  private  String courseTitle;

  @Column(name = "testimonial_position")
  private Integer position;

  public boolean isNeostencilUser() {
    return isNeostencilUser;
  }

  public void setNeostencilUser(boolean neostencilUser) {
    isNeostencilUser = neostencilUser;
  }

  @Column (name = "is_neostencil_user")
  public boolean isNeostencilUser;

  public Testimonial() {
    super();
  }

  public Testimonial(int id, String text, String studentName, String studentImage,
      Integer studentRank, String exam, String year, String location,String testimonialTitle,String courseTitle, String courseUrl,Integer position,boolean isNeostencilUser) {
    super();
    this.id = id;
    this.text = text;
    this.studentName = studentName;
    this.studentImage = studentImage;
    this.studentRank = studentRank;
    this.year = year;
    this.exam = exam;
    this.location = location;
    this.testimonialTitle = testimonialTitle;
    this.courseTitle = courseTitle;
    this.courseUrl = courseUrl;
    this.position=position;
    this.isNeostencilUser=isNeostencilUser;
  }

  public String getCourseUrl() {
    return courseUrl;
  }

  public void setCourseUrl(String courseUrl) { this.courseUrl = courseUrl; }
  public String getTestimonialTitle() { return testimonialTitle; }

  public void setTestimonialTitle(String testimonialTitle) { this.testimonialTitle = testimonialTitle; }
  public String getCourseTitle() { return courseTitle; }

  public void setCourse_title(String courseTitle) { this.courseTitle = courseTitle; }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public String getStudentName() {
    return studentName;
  }

  public void setStudentName(String studentName) {
    this.studentName = studentName;
  }

  public String getStudentImage() {
    return studentImage;
  }

  public void setStudentImage(String studentImage) {
    this.studentImage = studentImage;
  }

  public Integer getStudentRank() {
    return studentRank;
  }

  public void setStudentRank(Integer studentRank) {
    this.studentRank = studentRank;
  }

  public String getExam() {
    return exam;
  }

  public void setExam(String exam) {
    this.exam = exam;
  }

  public String getYear() {
    return year;
  }

  public void setYear(String year) {
    this.year = year;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public Integer getPosition() {
    return position;
  }

  public void setPosition(Integer position) {
    this.position = position;
  }

  /*
   * (non-Javadoc)
   *
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + id;
    return result;
  }

  /*
   * (non-Javadoc)
   *
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!super.equals(obj)) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    Testimonial other = (Testimonial) obj;
    if (id != other.id) {
      return false;
    }
    return true;
  }


}
