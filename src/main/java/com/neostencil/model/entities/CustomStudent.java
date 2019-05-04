package com.neostencil.model.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.neostencil.model.entities.User;
import java.sql.Timestamp;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.neostencil.common.enums.StudentStatusType;
import com.neostencil.model.entities.TeacherStatistics;

@Entity
@Table(name = "ns_custom_students")
public class CustomStudent {

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "custom_student_gen")
	@TableGenerator(name = "custom_student_gen", table = "ns_custom_student_id_gen", pkColumnName = "GEN_NAME", valueColumnName = "GEN_VAL", pkColumnValue = "custom_studentId_Gen", initialValue = 10000, allocationSize = 1)
	@Column(name = "custom_student_id")
	int id;

	@JsonIgnoreProperties({ "userCourseBatchLinkages", "userUnitLinkages" })
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "student_id")
	User student;

	@Column(name = "enrolled_on")
	private Timestamp enrolledOn;

	@Column(name = "contribution")
	private double contribution;

	@JsonBackReference(value = "teacher-student-reference")
	@ManyToOne(fetch = FetchType.LAZY)
	private TeacherStatistics teacherStatistics;

	@Column(name = "is_visible_to_teacher")
	boolean visibleToTeacher;
	
	@Column(name="student_address")
	String address;
	
	@Column(name="student_status")
	@Enumerated(EnumType.STRING)
	StudentStatusType studentStatus;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public User getStudent() {
		return student;
	}

	public boolean isVisibleToTeacher() {
		return visibleToTeacher;
	}

	public void setVisibleToTeacher(boolean visibleToTeacher) {
		this.visibleToTeacher = visibleToTeacher;
	}

	public void setStudent(User student) {
		this.student = student;
	}

	public Timestamp getEnrolledOn() {
		return enrolledOn;
	}

	public void setEnrolledOn(Timestamp enrolledOn) {
		this.enrolledOn = enrolledOn;
	}

	public double getContribution() {
		return contribution;
	}

	public void setContribution(double contribution) {
		this.contribution = contribution;
	}

	public TeacherStatistics getTeacherStatistics() {
		return teacherStatistics;
	}

	public void setTeacherStatistics(TeacherStatistics teacherStatistics) {
		this.teacherStatistics = teacherStatistics;
	}

  /**
   * @return the address
   */
  public String getAddress() {
    return address;
  }

  /**
   * @param address the address to set
   */
  public void setAddress(String address) {
    this.address = address;
  }

  /**
   * @return the studentStatus
   */
  public StudentStatusType getStudentStatus() {
    return studentStatus;
  }

  /**
   * @param studentStatus the studentStatus to set
   */
  public void setStudentStatus(StudentStatusType studentStatus) {
    this.studentStatus = studentStatus;
  }

}
