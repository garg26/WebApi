package com.neostencil.model.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.OrderColumn;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Type;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.FieldBridge;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.hibernate.search.annotations.Store;
import org.hibernate.search.bridge.builtin.EnumBridge;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.neostencil.common.enums.CourseCategory;
import com.neostencil.common.enums.CourseSubCategory;
import com.neostencil.common.enums.CourseType;
import com.neostencil.common.enums.ExamSegmentTypes;
import com.neostencil.common.enums.PublishStatus;
import com.neostencil.utils.InstructorNameBridge;

/**
 * This is the entity class that represents a course.
 *
 * @author shilpa
 */

@Entity
@Indexed
@Table(name = "ns_courses")
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })

// @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,
// property = "id")
public class Course implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 2715908477828573996L;

	@GeneratedValue(strategy = GenerationType.TABLE, generator = "course_gen")
	@TableGenerator(name = "course_gen", table = "ns_course_id_gen", pkColumnName = "GEN_NAME", valueColumnName = "GEN_VAL", pkColumnValue = "CourseId_Gen", initialValue = 30000, allocationSize = 100)
	@Id
	@Column(name = "course_id")
	private int id;

	@JsonManagedReference(value = "batch-reference")
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "course")
	@OrderBy("start_date desc")
	private Set<CourseBatch> batches;

	@Field(index = Index.YES, analyze = Analyze.YES, store = Store.YES)
	@Column(name = "course_title", length = 10000)
	private String courseTitle;

	@Field(index = Index.YES, analyze = Analyze.YES, store = Store.YES)
	@Column(name = "course_name", length = 10000)
	private String courseName;

	@Field(index = Index.YES, analyze = Analyze.YES, store = Store.YES)
	@Column(name = "course_description", length = 30000)
	private String courseDescription;

	@Field(index = Index.YES, analyze = Analyze.YES, store = Store.YES)
	@Column(name = "course_image")
	private String courseImage;

	@Field(index = Index.YES, analyze = Analyze.YES, store = Store.YES)
	@Column(name = "course_image_url")
	private String courseImageUrl;

	@Column(name = "course_image_alt_text")
	private String imageAltText;

	// @IndexedEmbedded(includePaths = "includePaths")
	@JsonIgnoreProperties({ "courses", "institute" })
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "ns_course_instructors_linkage", joinColumns = {
			@JoinColumn(name = "course_id") }, inverseJoinColumns = { @JoinColumn(name = "course_instructor_id") })
	@Field(store = Store.YES, index = Index.YES, analyze = Analyze.NO)
	@FieldBridge(impl = InstructorNameBridge.class)
	private Set<TeacherDetails> instructors;

	@Field(index = Index.YES, analyze = Analyze.YES, store = Store.YES)
	@Column(name = "students_enrolled")
	private int studentsEnrolled;

	/**
	 * Temporary field for persisting current data information
	 */
	@Field(index = Index.YES, analyze = Analyze.YES, store = Store.YES)
	@Column(name = "instructor_name")
	private String instructorName;

	@Column(name = "course_type")
	@Enumerated(EnumType.STRING)
	private CourseType courseType;

	@Field(index = Index.YES, analyze = Analyze.YES, store = Store.YES)
	@Column(name = "course_subject", length = 10000)
	private String courseSubject;

	@Field(index = Index.YES, analyze = Analyze.YES, store = Store.YES)
	@Column(name = "course_rating")
	private float courseRating = 0;

	@Column(name = "no_of_reviewers")
	private int noOfReviewers;

	@Field(index = Index.YES, analyze = Analyze.YES, store = Store.YES)
	@Column(name = "course_about", length = 60000)
	private String courseAbout;

	@Field(index = Index.YES, analyze = Analyze.YES, store = Store.YES)
	@Column(name = "course_old_slug", unique = true)
	private String courseOldSlug;

	@Field(bridge = @FieldBridge(impl = EnumBridge.class), store = Store.YES)
	@Enumerated(EnumType.STRING)
	@Column(name = "course_status", length = 2000)
	private PublishStatus status;

	@Field(index = Index.YES, analyze = Analyze.YES, store = Store.YES)
	@Column(name = "course_price")
	private double price;

	@Column(name = "course_discount")
	private int discount;

	@Field(index = Index.YES, analyze = Analyze.YES, store = Store.YES)
	@Column(name = "demo_lecture")
	private String demoLecture;

	@Field(index = Index.YES, analyze = Analyze.YES, store = Store.YES)
	@Column(name = "course_exam_segment")
	private String courseExamSegment;

	@Field(index = Index.YES, analyze = Analyze.YES, store = Store.YES)
	@Column(name = "course_category")
	private String courseCategory;

	@Field(index = Index.YES, analyze = Analyze.YES, store = Store.YES)
	@Column(name = "course_sub_category")
	private String courseSubCategory;

	@Column(name = "suitable_for", length = 30000)
	private String suitableFor;

	@IndexedEmbedded
	@JsonIgnoreProperties({ "courses", "teachers" })
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "institute_id")
	private Institute institute;

	@Column(name = "course_medium")
	private String courseMedium;

	@Column(nullable = false, updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@CreatedDate
	private Date createdAt;

	@Column(nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@LastModifiedDate
	private Date updatedAt;

	/*
	 * The following attributes will be used for displaying important
	 * information in Course cards on frontend
	 */
	@Field(index = Index.YES, analyze = Analyze.YES, store = Store.YES)
	@Column(name = "start_date")
	private String startDate;

	@Column(name = "institute_name")
	private String instituteName;

	@Column(name = "no_of_sessions")
	private String noOfSessions;

	@Column(name = "is_popular")
	private boolean popular;

	@JsonIgnoreProperties("parentCourses")
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "ns_related_courses", joinColumns = {
			@JoinColumn(name = "course_id", referencedColumnName = "course_id") }, inverseJoinColumns = {
					@JoinColumn(name = "course_summary_id", referencedColumnName = "course_summary_id") })
	private Set<CourseSummary> relatedCourses;

	@JsonIgnoreProperties("courses")
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "ns_bought_together_courses", joinColumns = {
			@JoinColumn(name = "course_id", referencedColumnName = "course_id") }, inverseJoinColumns = {
					@JoinColumn(name = "course_summary_id", referencedColumnName = "course_summary_id") })
	private Set<CourseSummary> boughtTogetherCourses;

	@Fetch(value = FetchMode.SUBSELECT)
	@ElementCollection(fetch = FetchType.LAZY)
	@JoinTable(name = "ns_course_meta_info", joinColumns = @JoinColumn(name = "course_id"))
	@OrderColumn(name = "sequence", updatable = true)
	private List<MetaInformation> metaList;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "featured_image_id")
	private FeaturedImage featuredImage;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "breadcrumb_id")
	private BreadCrumb breadCrumb;

	@Column(name = "course_position")
	private int position;

	@Column(name = "course_schedule", columnDefinition = "TEXT")
	private String schedule;

	@Field(index = Index.YES, analyze = Analyze.YES, store = Store.YES)
	@Column(name = "title_tag")
	private String titleTag;

	@Column(name = "about_instructor", columnDefinition = "TEXT")
	private String aboutInstructor;

	/**
	 * Validity for display purpose
	 */
	@Column(name = "course_validity")
	private String courseValidity;

	@Column(name = "total_no_of_views")
	private int totalNoOfViews;

	@Column(name = "course_additional_info",columnDefinition = "TEXT")
	private String additionalInfo;

	@Column(name = "course_inclusion",columnDefinition = "TEXT")
	private String inclusion;


	@Column(name = "demo_unit")
	private int demoUnitID;

	public int getTotalNoOfViews() {
		return totalNoOfViews;
	}

	public void setTotalNoOfViews(int totalNoOfViews) {
		this.totalNoOfViews = totalNoOfViews;
	}


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
	 * @return the batches
	 */
	public Set<CourseBatch> getBatches() {
		return batches;
	}

	/**
	 * @param batches
	 *            the batches to set
	 */
	public void setBatches(Set<CourseBatch> batches) {
		this.batches = batches;
	}

	/**
	 * @return the courseTitle
	 */
	public String getCourseTitle() {
		return courseTitle;
	}

	/**
	 * @param courseTitle
	 *            the courseTitle to set
	 */
	public void setCourseTitle(String courseTitle) {
		this.courseTitle = courseTitle;
	}

	/**
	 * @return the courseName
	 */
	public String getCourseName() {
		return courseName;
	}

	/**
	 * @param courseName
	 *            the courseName to set
	 */
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	/**
	 * @return the courseDescription
	 */
	public String getCourseDescription() {
		return courseDescription;
	}

	/**
	 * @param courseDescription
	 *            the courseDescription to set
	 */
	public void setCourseDescription(String courseDescription) {
		this.courseDescription = courseDescription;
	}

	/**
	 * @return the courseImage
	 */
	public String getCourseImage() {
		return courseImage;
	}

	/**
	 * @param courseImage
	 *            the courseImage to set
	 */
	public void setCourseImage(String courseImage) {
		this.courseImage = courseImage;
	}

	/**
	 * @return the instructors
	 */
	public Set<TeacherDetails> getInstructors() {
		return instructors;
	}

	/**
	 * @param instructors
	 *            the instructors to set
	 */
	public void setInstructors(Set<TeacherDetails> instructors) {
		this.instructors = instructors;
	}

	/**
	 * @return the studentsEnrolled
	 */
	public int getStudentsEnrolled() {
		return studentsEnrolled;
	}

	/**
	 * @param studentsEnrolled
	 *            the studentsEnrolled to set
	 */
	public void setStudentsEnrolled(int studentsEnrolled) {
		this.studentsEnrolled = studentsEnrolled;
	}

	/**
	 * @return the instructorName
	 */
	public String getInstructorName() {
		return instructorName;
	}

	/**
	 * @param instructorName
	 *            the instructorName to set
	 */
	public void setInstructorName(String instructorName) {
		this.instructorName = instructorName;
	}

	/**
	 * @return the courseType
	 */
	public CourseType getCourseType() {
		return courseType;
	}

	/**
	 * @param courseType
	 *            the courseType to set
	 */
	public void setCourseType(CourseType courseType) {
		this.courseType = courseType;
	}

	/**
	 * @return the courseSubject
	 */
	public String getCourseSubject() {
		return courseSubject;
	}

	/**
	 * @param courseSubject
	 *            the courseSubject to set
	 */
	public void setCourseSubject(String courseSubject) {
		this.courseSubject = courseSubject;
	}

	/**
	 * @return the courseRating
	 */
	public float getCourseRating() {
		return courseRating;
	}

	/**
	 * @param courseRating
	 *            the courseRating to set
	 */
	public void setCourseRating(float courseRating) {
		this.courseRating = courseRating;
	}

	/**
	 * @return the noOfReviewers
	 */
	public int getNoOfReviewers() {
		return noOfReviewers;
	}

	/**
	 * @param noOfReviewers
	 *            the noOfReviewers to set
	 */
	public void setNoOfReviewers(int noOfReviewers) {
		this.noOfReviewers = noOfReviewers;
	}

	/**
	 * @return the courseAbout
	 */
	public String getCourseAbout() {
		return courseAbout;
	}

	/**
	 * @param courseAbout
	 *            the courseAbout to set
	 */
	public void setCourseAbout(String courseAbout) {
		this.courseAbout = courseAbout;
	}

	/**
	 * @return the courseOldSlug
	 */
	public String getCourseOldSlug() {
		return courseOldSlug;
	}

	/**
	 * @param courseOldSlug
	 *            the courseOldSlug to set
	 */
	public void setCourseOldSlug(String courseOldSlug) {
		this.courseOldSlug = courseOldSlug;
	}

	/**
	 * @return the status
	 */
	public PublishStatus getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(PublishStatus status) {
		this.status = status;
	}


	/**
	 * @return the price
	 */
	public double getPrice() {
		return price;
	}

	/**
	 * @param price
	 *            the price to set
	 */
	public void setPrice(double price) {
		this.price = price;
	}

	/**
	 * @return the discount
	 */
	public int getDiscount() {
		return discount;
	}

	/**
	 * @param discount
	 *            the discount to set
	 */
	public void setDiscount(int discount) {
		this.discount = discount;
	}

	/**
	 * @return the demoLecture
	 */
	public String getDemoLecture() {
		return demoLecture;
	}

	/**
	 * @param demoLecture
	 *            the demoLecture to set
	 */
	public void setDemoLecture(String demoLecture) {
		this.demoLecture = demoLecture;
	}

	/**
	 * @return the courseExamSegment
	 */
	public String getCourseExamSegment() {
		return courseExamSegment;
	}

	/**
	 * @param courseExamSegment
	 *            the courseExamSegment to set
	 */
	public void setCourseExamSegment(String courseExamSegment) {
		this.courseExamSegment = courseExamSegment;
	}

	/**
	 * @return the courseCategory
	 */
	public String getCourseCategory() {
		return courseCategory;
	}

	/**
	 * @param courseCategory
	 *            the courseCategory to set
	 */
	public void setCourseCategory(String courseCategory) {
		this.courseCategory = courseCategory;
	}

	/**
	 * @return the courseSubCategory
	 */
	public String getCourseSubCategory() {
		return courseSubCategory;
	}

	/**
	 * @param courseSubCategory
	 *            the courseSubCategory to set
	 */
	public void setCourseSubCategory(String courseSubCategory) {
		this.courseSubCategory = courseSubCategory;
	}

	/**
	 * @return the suitableFor
	 */
	public String getSuitableFor() {
		return suitableFor;
	}

	/**
	 * @param suitableFor
	 *            the suitableFor to set
	 */
	public void setSuitableFor(String suitableFor) {
		this.suitableFor = suitableFor;
	}

	/**
	 * @return the institute
	 */
	public Institute getInstitute() {
		return institute;
	}

	/**
	 * @param institute
	 *            the institute to set
	 */
	public void setInstitute(Institute institute) {
		this.institute = institute;
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
		Course other = (Course) obj;
		if (id != other.id) {
			return false;
		}
		return true;
	}

	/**
	 * @return the createdAt
	 */
	public Date getCreatedAt() {
		return createdAt;
	}

	/**
	 * @param createdAt
	 *            the createdAt to set
	 */
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	/**
	 * @return the updatedAt
	 */
	public Date getUpdatedAt() {
		return updatedAt;
	}

	/**
	 * @param updatedAt
	 *            the updatedAt to set
	 */
	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	/**
	 * @return the courseMedium
	 */
	public String getCourseMedium() {
		return courseMedium;
	}

	/**
	 * @param courseMedium
	 *            the courseMedium to set
	 */
	public void setCourseMedium(String courseMedium) {
		this.courseMedium = courseMedium;
	}

	/**
	 * @return the startDate
	 */
	public String getStartDate() {
		return startDate;
	}

	/**
	 * @param startDate
	 *            the startDate to set
	 */
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	/**
	 * @return the instituteName
	 */
	public String getInstituteName() {
		return instituteName;
	}

	/**
	 * @param instituteName
	 *            the instituteName to set
	 */
	public void setInstituteName(String instituteName) {
		this.instituteName = instituteName;
	}

	/**
	 * @return the noOfSessions
	 */
	public String getNoOfSessions() {
		return noOfSessions;
	}

	/**
	 * @param noOfSessions
	 *            the noOfSessions to set
	 */
	public void setNoOfSessions(String noOfSessions) {
		this.noOfSessions = noOfSessions;
	}

	/**
	 * @return the popular
	 */
	public boolean isPopular() {
		return popular;
	}

	/**
	 * @param popular
	 *            the popular to set
	 */
	public void setPopular(boolean popular) {
		this.popular = popular;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	/**
	 * @return the courseImageUrl
	 */
	public String getCourseImageUrl() {
		return courseImageUrl;
	}

	/**
	 *
	 * @param courseImageUrl
	 */
	public void setCourseImageUrl(String courseImageUrl) {
		this.courseImageUrl = courseImageUrl;
	}

	/**
	 * @return the metaList
	 */
	public List<MetaInformation> getMetaList() {
		return metaList;
	}

	/**
	 * @param metaList
	 *            the metaList to set
	 */
	public void setMetaList(List<MetaInformation> metaList) {
		this.metaList = metaList;
	}

	/**
	 * @return the relatedCourses
	 */
	public Set<CourseSummary> getRelatedCourses() {
		return relatedCourses;
	}

	/**
	 * @param relatedCourses
	 *            the relatedCourses to set
	 */
	public void setRelatedCourses(Set<CourseSummary> relatedCourses) {
		this.relatedCourses = relatedCourses;
	}

	/**
	 * @return the boughtTogetherCourses
	 */
	public Set<CourseSummary> getBoughtTogetherCourses() {
		return boughtTogetherCourses;
	}

	/**
	 * @param boughtTogetherCourses
	 *            the boughtTogetherCourses to set
	 */
	public void setBoughtTogetherCourses(Set<CourseSummary> boughtTogetherCourses) {
		this.boughtTogetherCourses = boughtTogetherCourses;
	}

	/**
	 * @return the featuredImage
	 */
	public FeaturedImage getFeaturedImage() {
		return featuredImage;
	}

	/**
	 * @param featuredImage
	 *            the featuredImage to set
	 */
	public void setFeaturedImage(FeaturedImage featuredImage) {
		this.featuredImage = featuredImage;
	}

	/**
	 * @return the breadCrumb
	 */
	public BreadCrumb getBreadCrumb() {
		return breadCrumb;
	}

	/**
	 * @param breadCrumb
	 *            the breadCrumb to set
	 */
	public void setBreadCrumb(BreadCrumb breadCrumb) {
		this.breadCrumb = breadCrumb;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public String getSchedule() {
		return schedule;
	}

	public void setSchedule(String schedule) {
		this.schedule = schedule;
	}

	/**
	 * @return the titleTag
	 */
	public String getTitleTag() {
		return titleTag;
	}

	/**
	 * @param titleTag
	 *            the titleTag to set
	 */
	public void setTitleTag(String titleTag) {
		this.titleTag = titleTag;
	}

	public String getImageAltText() {
		return imageAltText;
	}

	public void setImageAltText(String imageAltText) {
		this.imageAltText = imageAltText;
	}

	public String getAboutInstructor() {
		return aboutInstructor;
	}

	public void setAboutInstructor(String aboutInstructor) {
		this.aboutInstructor = aboutInstructor;
	}

	public String getCourseValidity() {
		return courseValidity;
	}

	public void setCourseValidity(String courseValidity) {
		this.courseValidity = courseValidity;
	}


	public String getAdditionalInfo() {
		return additionalInfo;
	}

	public void setAdditionalInfo(String additionalInfo) {
		this.additionalInfo = additionalInfo;
	}

	public String getInclusion() {
		return inclusion;
	}

	public void setInclusion(String inclusion) {
		this.inclusion = inclusion;
	}

	public int getDemoUnitID() {
		return demoUnitID;
	}

	public void setDemoUnitID(int demoUnitID) {
		this.demoUnitID = demoUnitID;
	}
}
