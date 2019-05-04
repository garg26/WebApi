package com.neostencil.model.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

@Entity
@Table(name = "ns_teacher_statistics")
public class TeacherStatistics {

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "teacher_stats_gen")
	@TableGenerator(name = "teacher_stats_gen", table = "ns_teacher_stats_id_gen", pkColumnName = "GEN_NAME", valueColumnName = "GEN_VAL", pkColumnValue = "teacher_statsId_Gen", initialValue = 10000, allocationSize = 1)
	@Column(name = "stats_id")
	private int id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "teacher_id")
	private TeacherDetails teacher;

	@JsonManagedReference(value = "teacher-student-reference")
	@OneToMany(mappedBy = "teacherStatistics", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<CustomStudent> students;

	/**
	 * Saved in the form of batchId#CourseName|BatchName
	 */
	@Column(name = "course_batch")
	private String courseBatch;

	@Column(name = "commission")
	private double commission;

	@Column(name = "month_year")
	private String monthYear;

	@Column(name = "teacher_commission")
	private double teacherCommission;

	@Column(name = "is_visible_to_teacher")
	boolean visibleToTeacher;

	public double getTeacherCommission() {
		return teacherCommission;
	}

	public void setTeacherCommission(double teacherCommission) {
		this.teacherCommission = teacherCommission;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public TeacherDetails getTeacher() {
		return teacher;
	}

	public void setTeacher(TeacherDetails teacher) {
		this.teacher = teacher;
	}

	public List<CustomStudent> getStudents() {
		return students;
	}

	public void setStudents(List<CustomStudent> students) {
		this.students = students;
	}

	public String getCourseBatch() {
		return courseBatch;
	}

	public void setCourseBatch(String courseBatch) {
		this.courseBatch = courseBatch;
	}

	public double getCommission() {
		return commission;
	}

	public void setCommission(double commission) {
		this.commission = commission;
	}

	public String getMonthYear() {
		return monthYear;
	}

	public void setMonthYear(String monthYear) {
		this.monthYear = monthYear;
	}

	public boolean isVisibleToTeacher() {
		return visibleToTeacher;
	}

	public void setVisibleToTeacher(boolean visibleToTeacher) {
		this.visibleToTeacher = visibleToTeacher;
	}
}
