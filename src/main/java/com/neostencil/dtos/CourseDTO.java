package com.neostencil.dtos;

import java.util.Date;
import java.util.List;
import java.util.Set;
import com.neostencil.common.enums.CourseCategory;
import com.neostencil.common.enums.CourseSubCategory;
import com.neostencil.common.enums.CourseType;
import com.neostencil.common.enums.ExamSegmentTypes;
import com.neostencil.common.enums.PublishStatus;
import com.neostencil.model.entities.BreadCrumb;
import com.neostencil.model.entities.CourseSummary;
import com.neostencil.model.entities.FeaturedImage;
import com.neostencil.model.entities.Highlights;
import com.neostencil.model.entities.MetaInformation;

/**
 * 
 * @author shilpa
 *
 */
public class CourseDTO {

	private int id;
	private String courseTitle;
	private String courseName;
	private String courseDescription;
	private String courseImage;
	private String courseImageUrl;
	private String imageAltText;
	private Set<TeacherSummaryDTO> instructors;
	private int studentsEnrolled;
	private String instructorName;
	private CourseType courseType;
	private String courseSubject;
	private float courseRating;
	private int noOfReviewers;
	private String courseAbout;
	private String courseOldSlug;
	private PublishStatus status;
	private Set<String> courseRequirements;
	private double price;
	private int discount;
	private String demoLecture;
	private String courseExamSegment;
	private String courseCategory;
	private String courseSubCategory;
	private String suitableFor;
	private InstituteDTO institute;
	private String courseMedium;
	private Date createdAt;
	private Date updatedAt;
	private String startDate;
	private String noOfSessions;
	private boolean popular;
	private Set<CourseSummaryDTO> relatedCourses;
	private Set<CourseSummaryDTO> boughtTogetherCourses;
	private List<MetaInformation> metaList;
	private FeaturedImage featuredImage;
	private BreadCrumb breadCrumb;
	private int position;
	private String schedule;
	private String titleTag;
	private String aboutInstructor;
	private String courseValidity;
	private Set<CourseBatchDetailedDTO> batches;
	private int totalNoOfViews;
	private List<Highlights> courseAdditional;
	private List<Highlights> courseInclusions;
  private int demoUnitID;
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
	 * @return the courseImageUrl
	 */
	public String getCourseImageUrl() {
		return courseImageUrl;
	}

	/**
	 * @param courseImageUrl
	 *            the courseImageUrl to set
	 */
	public void setCourseImageUrl(String courseImageUrl) {
		this.courseImageUrl = courseImageUrl;
	}

	/**
	 * @return the imageAltText
	 */
	public String getImageAltText() {
		return imageAltText;
	}

	/**
	 * @param imageAltText
	 *            the imageAltText to set
	 */
	public void setImageAltText(String imageAltText) {
		this.imageAltText = imageAltText;
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
	 * @return the courseRequirements
	 */
	public Set<String> getCourseRequirements() {
		return courseRequirements;
	}

	/**
	 * @param courseRequirements
	 *            the courseRequirements to set
	 */
	public void setCourseRequirements(Set<String> courseRequirements) {
		this.courseRequirements = courseRequirements;
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
	public InstituteDTO getInstitute() {
		return institute;
	}

	/**
	 * @param institute
	 *            the institute to set
	 */
	public void setInstitute(InstituteDTO institute) {
		this.institute = institute;
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

	/**
	 * @return the position
	 */
	public int getPosition() {
		return position;
	}

	/**
	 * @param position
	 *            the position to set
	 */
	public void setPosition(int position) {
		this.position = position;
	}

	/**
	 * @return the schedule
	 */
	public String getSchedule() {
		return schedule;
	}

	/**
	 * @param schedule
	 *            the schedule to set
	 */
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

	/**
	 * @return the aboutInstructor
	 */
	public String getAboutInstructor() {
		return aboutInstructor;
	}

	/**
	 * @param aboutInstructor
	 *            the aboutInstructor to set
	 */
	public void setAboutInstructor(String aboutInstructor) {
		this.aboutInstructor = aboutInstructor;
	}

	/**
	 * @return the courseValidity
	 */
	public String getCourseValidity() {
		return courseValidity;
	}

	/**
	 * @param courseValidity
	 *            the courseValidity to set
	 */
	public void setCourseValidity(String courseValidity) {
		this.courseValidity = courseValidity;
	}

	/**
	 * @return the relatedCourses
	 */
	public Set<CourseSummaryDTO> getRelatedCourses() {
		return relatedCourses;
	}

	/**
	 * @param relatedCourses
	 *            the relatedCourses to set
	 */
	public void setRelatedCourses(Set<CourseSummaryDTO> relatedCourses) {
		this.relatedCourses = relatedCourses;
	}

	/**
	 * @return the boughtTogetherCourses
	 */
	public Set<CourseSummaryDTO> getBoughtTogetherCourses() {
		return boughtTogetherCourses;
	}

	/**
	 * @param boughtTogetherCourses
	 *            the boughtTogetherCourses to set
	 */
	public void setBoughtTogetherCourses(Set<CourseSummaryDTO> boughtTogetherCourses) {
		this.boughtTogetherCourses = boughtTogetherCourses;
	}

	/**
	 * @return the instructors
	 */
	public Set<TeacherSummaryDTO> getInstructors() {
		return instructors;
	}

	/**
	 * @param instructors
	 *            the instructors to set
	 */
	public void setInstructors(Set<TeacherSummaryDTO> instructors) {
		this.instructors = instructors;
	}

	/**
	 * @return the batches
	 */
	public Set<CourseBatchDetailedDTO> getBatches() {
		return batches;
	}

	/**
	 * @param batches
	 *            the batches to set
	 */
	public void setBatches(Set<CourseBatchDetailedDTO> batches) {
		this.batches = batches;
	}

	public List<Highlights> getCourseAdditional() {
		return courseAdditional;
	}

	public void setCourseAdditional(
			List<Highlights> courseAdditional) {
		this.courseAdditional = courseAdditional;
	}

	public List<Highlights> getCourseInclusions() {
		return courseInclusions;
	}

	public void setCourseInclusions(
			List<Highlights> courseInclusions) {
		this.courseInclusions = courseInclusions;
	}

	public String getCourseImage() {
		return courseImage;
	}

	public void setCourseImage(String courseImage) {
		this.courseImage = courseImage;
	}

	public int getTotalNoOfViews() {
		return totalNoOfViews;
	}

	public void setTotalNoOfViews(int totalNoOfViews) {
		this.totalNoOfViews = totalNoOfViews;
	}

	public int getDemoUnitID() {
		return demoUnitID;
	}

	public void setDemoUnitID(int demoUnitID) {
		this.demoUnitID = demoUnitID;
	}
}
