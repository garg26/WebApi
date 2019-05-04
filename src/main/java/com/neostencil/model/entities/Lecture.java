package com.neostencil.model.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "ns_lectures")
public class Lecture extends DomainObject {

	@Id
	@Column(name = "lecture_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ns_lecture_seq")
	@SequenceGenerator(name = "ns_lecture_seq", sequenceName = "ns_lecture_seq", allocationSize = 1)
	private int id;

	@Column(name = "lecture_name", length = 1000)
	private String name;

	@Column(name = "url", length = 1000)
	private String url;

	@Column(name = "release_date")
	private String releaseDate;

	@Column(name = "duration")
	private String duration;

	@Column(name = "heading")
	private String heading;

	@JsonBackReference(value = "unit-reference")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "course_batch_id")
	private CourseBatch courseBatch;

	@Column(name = "teacher_name")
	private String teacherName;

	@Column(name = "footer_notes", columnDefinition = "TEXT", length = 200000)
	private String footerNotes;

	@JsonBackReference(value = "lecture-jw")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "jw_macro_id")
	private JWMacro jwMacro;

	@Column(name = "no_of_views")
	int noOfViews;

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url
	 *            the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the releaseDate
	 */
	public String getReleaseDate() {
		return releaseDate;
	}

	/**
	 * @param releaseDate
	 *            the releaseDate to set
	 */
	public void setReleaseDate(String releaseDate) {
		this.releaseDate = releaseDate;
	}

	/**
	 * @return the duration
	 */
	public String getDuration() {
		return duration;
	}

	/**
	 * @param duration
	 *            the duration to set
	 */
	public void setDuration(String duration) {
		this.duration = duration;
	}

	/**
	 * @return the heading
	 */
	public String getHeading() {
		return heading;
	}

	/**
	 * @param heading
	 *            the heading to set
	 */
	public void setHeading(String heading) {
		this.heading = heading;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
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
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Lecture other = (Lecture) obj;
		if (id != other.id) {
			return false;
		}
		return true;
	}

	/**
	 * @return the teacherName
	 */
	public String getTeacherName() {
		return teacherName;
	}

	/**
	 * @param teacherName
	 *            the teacherName to set
	 */
	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}

	/**
	 * @return the jwMacro
	 */
	public JWMacro getJwMacro() {
		return jwMacro;
	}

	/**
	 * @param jwMacro
	 *            the jwMacro to set
	 */
	public void setJwMacro(JWMacro jwMacro) {
		this.jwMacro = jwMacro;
	}

	/**
	 * @return the courseBatch
	 */
	public CourseBatch getCourseBatch() {
		return courseBatch;
	}

	/**
	 * @param courseBatch
	 *            the courseBatch to set
	 */
	public void setCourseBatch(CourseBatch courseBatch) {
		this.courseBatch = courseBatch;
	}

	public String getFooterNotes() {
		return footerNotes;
	}

	public void setFooterNotes(String footerNotes) {
		this.footerNotes = footerNotes;
	}

	/**
	 * @return the noOfViews
	 */
	public int getNoOfViews() {
		return noOfViews;
	}

	/**
	 * @param noOfViews
	 *            the noOfViews to set
	 */
	public void setNoOfViews(int noOfViews) {
		this.noOfViews = noOfViews;
	}
}
