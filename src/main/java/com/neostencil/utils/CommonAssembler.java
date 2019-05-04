package com.neostencil.utils;

import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import org.apache.http.util.TextUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.neostencil.common.enums.PublishStatus;
import com.neostencil.dtos.CommentDTO;
import com.neostencil.dtos.CourseBatchDTO;
import com.neostencil.dtos.CourseBatchDetailedDTO;
import com.neostencil.dtos.CourseDTO;
import com.neostencil.dtos.CourseSummaryDTO;
import com.neostencil.dtos.InstituteDTO;
import com.neostencil.dtos.InstituteDetailedDTO;
import com.neostencil.dtos.TeacherDetailsDTO;
import com.neostencil.dtos.TeacherSummaryDTO;
import com.neostencil.dtos.UserCourseBatchLinkageDTO;
import com.neostencil.dtos.UserDTO;
import com.neostencil.model.entities.Achievements;
import com.neostencil.model.entities.Comment;
import com.neostencil.model.entities.Course;
import com.neostencil.model.entities.CourseBatch;
import com.neostencil.model.entities.CourseSummary;
import com.neostencil.model.entities.Discussion;
import com.neostencil.model.entities.Highlights;
import com.neostencil.model.entities.Institute;
import com.neostencil.model.entities.MetaInformation;
import com.neostencil.model.entities.TeacherDetails;
import com.neostencil.model.entities.Unit;
import com.neostencil.model.entities.User;
import com.neostencil.model.entities.UserCourseBatchLinkage;
import com.neostencil.model.entities.UserLectureStats;
import com.neostencil.model.entities.UserNeoCashHistory;
import com.neostencil.model.entities.VideoChunks;
import com.neostencil.model.entities.VideoRequest;
import com.neostencil.model.repositories.UserMetaDataRespository;
import com.neostencil.responses.DiscussionDTO;
import com.neostencil.responses.UnitDTO;
import com.neostencil.responses.UserNeoCashHistoryDTO;
import com.neostencil.responses.VideoChunksDTO;
import com.neostencil.responses.VideoRequestDTO;

public class CommonAssembler {
  
  private static final ModelMapper modelMapper = new ModelMapper();

  public static UserCourseBatchLinkageDTO toUserCourseBatchLinkageDTO(
      UserCourseBatchLinkage linkage) {
    UserCourseBatchLinkageDTO dto = new UserCourseBatchLinkageDTO();

    if (linkage != null) {
      dto.setLinkageId(linkage.getLinkageId());
      dto.setEnrolledOn(linkage.getEnrolledOn());
      dto.setExpiryDate(linkage.getExpiryDate());
      dto.setStatus(linkage.getStatus());
      dto.setUser(toUserDto(linkage.getUser()));
      dto.setCourseBatch(toCourseBatchDTo(linkage.getCourseBatch()));
      dto.setRestrictedAccess(linkage.isRestrictedAccess());
    }

    return dto;
  }

  public static CourseBatchDTO toCourseBatchDTo(CourseBatch courseBatch) {
    CourseBatchDTO courseBatchDTO = null;
    if (courseBatch != null) {

      courseBatchDTO = new CourseBatchDTO();
      courseBatchDTO.setId(courseBatch.getId());
      courseBatchDTO.setBatchName(courseBatch.getBatchName());
    }
    return courseBatchDTO;
  }

  public static UserDTO toUserDto(User user) {
    UserDTO userDTO = null;
    if (user != null) {
      userDTO = new UserDTO();

      userDTO.setAvatar(user.getAvatar());
      userDTO.setEmailId(user.getEmailId());
      userDTO.setFullName(user.getFullName());
      userDTO.setUserId(user.getUserId());
    }

    return userDTO;
  }

  public static TeacherDetailsDTO toTeacherDetailsDTO(TeacherDetails td) {

    TeacherDetailsDTO dto = null;
    if (td != null) {

      dto = new TeacherDetailsDTO();
      dto.setAddress(td.getAddress());
      dto.setBreadCrumb(td.getBreadCrumb());
      dto.setCertificates(td.getCertificates());
      dto.setContactNo(td.getContactNo());
      dto.setContactNoToDisplay(td.getContactNoToDisplay());
      dto.setEmailToDisplay(td.getEmailToDisplay());
      dto.setDisplayPictureUrl(td.getDisplayPictureUrl());

      dto.setFacebookUrl(td.getFacebookUrl());
      dto.setFeaturedImage(td.getFeaturedImage());
      dto.setGooglePlusUrl(td.getGooglePlusUrl());
      dto.setId(td.getId());
      dto.setImageAltText(td.getImageAltText());
      dto.setLinkedinUrl(td.getLinkedinUrl());
      dto.setNoOfReviewers(td.getNoOfReviewers());
      dto.setNoOfStudents(td.getNoOfStudents());
      dto.setPosition(td.getPosition());
      dto.setSlug(td.getSlug());
      dto.setStatus(td.getStatus());
      dto.setSubjects(td.getSubjects());
      dto.setTeacherBio(td.getTeacherBio());
      dto.setTeacherCategory(td.getTeacherCategory());
      dto.setTeacherDescription(td.getTeacherDescription());
      dto.setTeacherExamSegment(td.getTeacherExamSegment());
      dto.setTeacherGallery(td.getTeacherGallery());
      dto.setTeacherLocation(td.getTeacherLocation());
      dto.setTeacherName(td.getTeacherName());
      dto.setTeacherRating(td.getTeacherRating());
      dto.setTitleTag(td.getTitleTag());
      dto.setMetaList(td.getMetaList());
      dto.setTotalExperience(td.getTotalExperience());
      dto.setTwitterUrl(td.getTwitterUrl());
      dto.setWebsite(td.getWebsite());
      dto.setCurrentStatus(td.getCurrentStatus());
      if (td.getInstitute() != null) {
        InstituteDTO insDTO = toInstituteDTO(td.getInstitute());
        dto.setInstitute(insDTO);
      }

      // Setting courses if (td.getCourses() != null &&
      // td.getCourses().size() > 0) {
      Set<CourseSummaryDTO> courseList = new LinkedHashSet<CourseSummaryDTO>();

      for (Course c : td.getCourses()) {
        if (c != null && c.getStatus() != null && c.getStatus().equals(PublishStatus.publish)) {
          CourseSummaryDTO courseSummaryDTO = toCourseSummaryDTO(c);
          courseList.add(courseSummaryDTO);

        }
      }
      dto.setCourses(courseList);
      dto = setTeacherMetaInformation(dto);

      String achievements = td.getAchievements();
      String educations = td.getEducations();
      String experiences = td.getExperiences();

      Type highlightType = new TypeToken<List<Highlights>>() {}.getType();
      Type achievementType = new TypeToken<List<Achievements>>() {}.getType();
      Gson converter = new Gson();

      List<Achievements> achievementsList = converter.fromJson(achievements, achievementType);
      List<Highlights> eductionList = converter.fromJson(educations, highlightType);
      List<Highlights> experienceList = converter.fromJson(experiences, highlightType);

      dto.setTeacherAchievements(achievementsList);
      dto.setEducation(eductionList);
      dto.setExperience(experienceList);
    }

    // TeacherDetailsDTO dto = modelMapper.map(td, TeacherDetailsDTO.class);

    return dto;
  }

  public static CourseSummaryDTO toCourseSummaryDTO(Course course) {
    CourseSummaryDTO dto = null;
    if (course != null) {
      List<String> teachers = new LinkedList<String>();

      dto = new CourseSummaryDTO();
      dto.setCourseImageUrl(course.getCourseImageUrl());
      dto.setCourseRating(course.getCourseRating());
      dto.setCourseOldSlug(course.getCourseOldSlug());
      dto.setCourseTitle(course.getCourseTitle());

      if (course.getInstitute() != null) {
        dto.setInstituteName(course.getInstitute().getName());
        dto.setInstituteSlug(course.getInstitute().getInstituteSlug());

      }

      dto.setInstructorName(course.getInstructorName());
      Map<String, String> teachersMap = new TreeMap<String, String>();
      int i = 0;
      if (course.getInstructors() != null && course.getInstructors().size() > 0) {
        Set<TeacherDetails> temp = new LinkedHashSet<TeacherDetails>();
        temp.addAll(course.getInstructors());
        for (TeacherDetails td : course.getInstructors()) {
          if (td.getTeacherName().equals(dto.getInstructorName())) {
            teachersMap.put(td.getSlug(), td.getTeacherName());
            temp.remove(td);
          }

        }
        for (TeacherDetails t : temp) {
          teachersMap.put(t.getSlug(), t.getTeacherName());
        }

      }

      dto.setTeachersMap(teachersMap);
      dto.setNoOfReviewers(course.getNoOfReviewers());
      dto.setPrice(course.getPrice());
      dto.setStartDate(course.getStartDate());
      dto.setStatus(course.getStatus());
      dto.setStudentsEnrolled(course.getStudentsEnrolled());
    }

    return dto;

  }

  /**
   * For setting meta information of the teacher
   */
  public static TeacherDetailsDTO setTeacherMetaInformation(TeacherDetailsDTO teacher) {

    if (teacher != null) {
      List<MetaInformation> metaList = new LinkedList<MetaInformation>();
      if (teacher.getMetaList() == null || teacher.getMetaList().size() == 0) {

        MetaInformation metaInfo = new MetaInformation();
        metaInfo.setAttributeType("name");
        metaInfo.setType("description");
        metaInfo.setContent(teacher.getTeacherDescription());
        metaList.add(metaInfo);

        teacher.setMetaList(metaList);
        /*
         * metaInfo = new MetaInformation(); metaInfo.setAttributeType("name");
         * metaInfo.setType("title"); metaInfo.setContent(course.getCourseTitle());
         * metaList.add(metaInfo);
         */
      } else {

        boolean foundDesc = false;
        for (MetaInformation meta : teacher.getMetaList()) {

          if (meta != null) {
            switch (meta.getType()) {
              case "description":
                foundDesc = true;
                break;

            }
          }
        }
        if (!foundDesc) {
          MetaInformation metaInfo = new MetaInformation();
          metaInfo.setAttributeType("name");
          metaInfo.setType("description");
          metaInfo.setContent(teacher.getTeacherDescription());
          metaList.add(metaInfo);
        }

        teacher.getMetaList().addAll(metaList);
      }

      if (teacher.getTitleTag() == null || teacher.getTitleTag().isEmpty()) {
        teacher.setTitleTag(teacher.getTeacherName());
      }
    }
    return teacher;
  }

  public static CourseDTO setCourseMetaInformation(CourseDTO course) {

    if (course != null) {
      List<MetaInformation> metaList = new LinkedList<MetaInformation>();
      if (course.getMetaList() == null || course.getMetaList().size() == 0) {

        MetaInformation metaInfo = new MetaInformation();
        metaInfo.setAttributeType("name");
        metaInfo.setType("description");
        metaInfo.setContent(course.getCourseDescription());
        metaList.add(metaInfo);

        course.setMetaList(metaList);
        /*
         * metaInfo = new MetaInformation(); metaInfo.setAttributeType("name");
         * metaInfo.setType("title"); metaInfo.setContent(course.getCourseTitle());
         * metaList.add(metaInfo);
         */
      } else {

        boolean foundDesc = false;
        for (MetaInformation meta : course.getMetaList()) {

          if (meta != null) {
            switch (meta.getType()) {
              case "description":
                foundDesc = true;
                break;

            }
          }
        }
        if (!foundDesc) {
          MetaInformation metaInfo = new MetaInformation();
          metaInfo.setAttributeType("name");
          metaInfo.setType("description");
          metaInfo.setContent(course.getCourseDescription());
          metaList.add(metaInfo);
        }

        course.getMetaList().addAll(metaList);
      }

      if (course.getTitleTag() == null || course.getTitleTag().isEmpty()) {
        course.setTitleTag(course.getCourseName());
      }
    }
    return course;
  }

  public static InstituteDetailedDTO toInstituteDetailedDTO(Institute ins) {
    InstituteDetailedDTO dto = null;
    if (ins != null) {
      dto = new InstituteDetailedDTO();
      dto.setAddress(ins.getAddress());
      dto.setBreadCrumb(ins.getBreadCrumb());
      dto.setContactNo(ins.getContactNo());
      // Setting courses
      if (ins.getCourses() != null && ins.getCourses().size() > 0) {
        Set<CourseSummaryDTO> courseList = new LinkedHashSet<CourseSummaryDTO>();
        for (Course c : ins.getCourses()) {
          if (c != null && c.getStatus() != null && c.getStatus().equals(PublishStatus.publish)) {
            CourseSummaryDTO courseDTO = toCourseSummaryDTO(c);
            courseList.add(courseDTO);
          }
        }
        dto.setCourses(courseList);
      }
      dto.setDescription(ins.getDescription());
      dto.setEmailId(ins.getEmailId());
      dto.setFacebookUrl(ins.getFacebookUrl());
      dto.setGooglePlusUrl(ins.getGooglePlusUrl());
      dto.setFeaturedImage(ins.getFeaturedImage());
      dto.setId(ins.getId());
      dto.setImage(ins.getImage());
      dto.setImageAltText(ins.getImageAltText());
      dto.setImageUrl(ins.getImageUrl());
      dto.setInstituteSlug(ins.getInstituteSlug());
      dto.setLinkedinUrl(ins.getLinkedinUrl());
      dto.setMetaList(ins.getMetaList());
      dto.setName(ins.getName());
      dto.setOtherInformation(ins.getOtherInformation());
      dto.setStatus(ins.getStatus());
      dto.setOwnerName(ins.getOwnerName());
      // Teachers
      if (ins.getTeachers() != null && ins.getTeachers().size() > 0) {
        Set<TeacherSummaryDTO> teacherList = new LinkedHashSet<TeacherSummaryDTO>();

        for (TeacherDetails td : ins.getTeachers()) {
          if (td != null && td.getStatus() != null
              && td.getStatus().equals(PublishStatus.publish)) {
            TeacherSummaryDTO tsDTO = toTeacherSummaryDTO(td);
            teacherList.add(tsDTO);
          }
        }
        dto.setTeachers(teacherList);
      }
      dto.setTitleTag(ins.getTitleTag());
      dto.setTwitterUrl(ins.getTwitterUrl());
      dto.setUrl(ins.getUrl());
      dto.setBannerImage(ins.getBannerImage());
      dto = setInstituteMetaInformation(dto);

      String achievements = ins.getAchievements();
      String relatedInfo = ins.getRelatedInfo();
      String highlightInfo = ins.getHighlights();

      Type highlightType = new TypeToken<List<Highlights>>() {}.getType();
      Type achievementType = new TypeToken<List<Achievements>>() {}.getType();
      Gson converter = new Gson();

      List<Achievements> achievementsList = converter.fromJson(achievements, achievementType);
      List<Highlights> relatedList = converter.fromJson(relatedInfo, highlightType);
      List<Highlights> highlightsList = converter.fromJson(highlightInfo, highlightType);

      dto.setInstituteAchievements(achievementsList);
      dto.setInstituteRelateds(relatedList);
      dto.setInstituteHighlights(highlightsList);

    }
    return dto;
  }

  public static TeacherSummaryDTO toTeacherSummaryDTO(TeacherDetails td) {
    TeacherSummaryDTO dto = null;
    if (td != null) {

      dto = new TeacherSummaryDTO();
      dto.setDisplayPictureUrl(td.getDisplayPictureUrl());
      dto.setId(td.getId());
      dto.setImageAltText(td.getImageAltText());
      dto.setNoOfReviewers(td.getNoOfReviewers());
      dto.setNoOfStudents(td.getNoOfStudents());
      dto.setSlug(td.getSlug());
      dto.setSubjects(td.getSubjects());
      dto.setTeacherName(td.getTeacherName());
      dto.setEmailId(td.getEmailId());
      if (td.getInstitute() != null) {
        InstituteDTO insDTO = toInstituteDTO(td.getInstitute());
        dto.setInstitute(insDTO);
      }

      if (td.getCourses() != null && td.getCourses().size() > 0) {
        Set<CourseSummaryDTO> courses = new HashSet<>();
        for (Course c : td.getCourses()) {
          if (c != null && c.getStatus() != null && c.getStatus().equals(PublishStatus.publish)) {
            CourseSummaryDTO tsDTO = toCourseSummaryDTO(c);
            courses.add(tsDTO);
          }
        }

        dto.setCourses(courses);
      }

    }

    return dto;

  }

  public static InstituteDetailedDTO setInstituteMetaInformation(InstituteDetailedDTO institute) {

    if (institute != null) {
      List<MetaInformation> metaList = new LinkedList<MetaInformation>();
      if (institute.getMetaList() == null || institute.getMetaList().size() == 0) {

        MetaInformation metaInfo = new MetaInformation();
        metaInfo.setAttributeType("name");
        metaInfo.setType("description");
        metaInfo.setContent(institute.getDescription());
        metaList.add(metaInfo);

        institute.setMetaList(metaList);
        /*
         * metaInfo = new MetaInformation(); metaInfo.setAttributeType("name");
         * metaInfo.setType("title"); metaInfo.setContent(course.getCourseTitle());
         * metaList.add(metaInfo);
         */
      } else {

        boolean foundDesc = false;
        for (MetaInformation meta : institute.getMetaList()) {

          if (meta != null) {
            switch (meta.getType()) {
              case "description":
                foundDesc = true;
                break;

            }
          }
        }
        if (!foundDesc) {
          MetaInformation metaInfo = new MetaInformation();
          metaInfo.setAttributeType("name");
          metaInfo.setType("description");
          metaInfo.setContent(institute.getDescription());
          metaList.add(metaInfo);
        }

        institute.getMetaList().addAll(metaList);
      }

      if (institute.getTitleTag() == null || institute.getTitleTag().isEmpty()) {
        institute.setTitleTag(institute.getName());
      }
    }
    return institute;
  }

  public static InstituteDTO toInstituteDTO(Institute institute) {
    InstituteDTO dto = null;
    if (institute != null) {
      dto = new InstituteDTO();
      dto.setId(institute.getId());
      dto.setName(institute.getName());
      dto.setUrl(institute.getUrl());
      dto.setInstituteSlug(institute.getInstituteSlug());
      dto.setBannerImage(institute.getBannerImage());
      dto.setImageUrl(institute.getImageUrl());
    }
    return dto;
  }

  public static CourseDTO toCourseDTO(Course course) {
    CourseDTO dto = null;
    if (course != null) {
      dto = new CourseDTO();
      dto.setAboutInstructor(course.getAboutInstructor());
      dto.setBreadCrumb(course.getBreadCrumb());
      dto.setCourseAbout(course.getCourseAbout());

      if (course.getBatches() != null && course.getBatches().size() > 0) {
        Set<CourseBatchDetailedDTO> batches = new LinkedHashSet<CourseBatchDetailedDTO>();
        for (CourseBatch cb : course.getBatches()) {
          if (cb != null && PublishStatus.publish.equals(cb.getStatus())) {
            CourseBatchDetailedDTO cbdDTO = toCourseBatchDetailedDTO(cb);
            batches.add(cbdDTO);
          }
        }
        dto.setBatches(batches);
      }
      if (course.getBoughtTogetherCourses() != null
          && course.getBoughtTogetherCourses().size() > 0) {

        Set<CourseSummaryDTO> boughtTogetherList = new LinkedHashSet<CourseSummaryDTO>();
        for (CourseSummary cs : course.getBoughtTogetherCourses()) {
          if (cs != null && PublishStatus.publish.equals(cs.getStatus())) {
            boughtTogetherList.add(toCourseSummaryDTO(cs));
          }
        }
        dto.setBoughtTogetherCourses(boughtTogetherList);
      }

      dto.setCourseCategory(course.getCourseCategory());
      dto.setCourseDescription(course.getCourseDescription());
      dto.setCourseExamSegment(course.getCourseExamSegment());
      dto.setCourseImageUrl(course.getCourseImageUrl());
      dto.setCourseImage(course.getCourseImage());
      dto.setCourseMedium(course.getCourseMedium());
      dto.setCourseName(course.getCourseName());
      dto.setCourseOldSlug(course.getCourseOldSlug());
      dto.setCourseRating(course.getCourseRating());
      dto.setCourseSubCategory(course.getCourseSubCategory());
      dto.setCourseSubject(course.getCourseSubject());
      dto.setCourseTitle(course.getCourseTitle());
      dto.setCourseType(course.getCourseType());
      dto.setCourseValidity(course.getCourseValidity());
      dto.setCreatedAt(course.getCreatedAt());
      dto.setDemoLecture(course.getDemoLecture());
      dto.setDiscount(course.getDiscount());
      dto.setFeaturedImage(course.getFeaturedImage());
      dto.setId(course.getId());
      dto.setImageAltText(course.getImageAltText());
      dto.setTotalNoOfViews(course.getTotalNoOfViews());
      if (course.getInstitute() != null) {
        InstituteDTO insDTO = toInstituteDTO(course.getInstitute());
        dto.setInstitute(insDTO);
      }
      if (course.getInstructors() != null && course.getInstructors().size() > 0) {
        Set<TeacherSummaryDTO> tsDTOList = new LinkedHashSet<TeacherSummaryDTO>();

        if (course.getInstructors().size() > 1) {
          String instructorName = course.getInstructorName();
          dto.setInstructorName(instructorName);
          if (!TextUtils.isEmpty(instructorName)) {

            for (TeacherDetails td : course.getInstructors()) {
              if (td != null && td.getTeacherName().equals(instructorName)) {
                TeacherSummaryDTO tsDTO = toTeacherSummaryDTO(td);
                tsDTOList.add(tsDTO);
                break;
              }

            }

            for (TeacherDetails td : course.getInstructors()) {
              if (td != null && !td.getTeacherName().equals(instructorName)) {
                TeacherSummaryDTO tsDTO = toTeacherSummaryDTO(td);
                tsDTOList.add(tsDTO);
              }
            }

          } else {
            for (TeacherDetails td : course.getInstructors()) {
              if (td != null) {
                TeacherSummaryDTO tsDTO = toTeacherSummaryDTO(td);
                tsDTOList.add(tsDTO);
              }
            }
          }
        } else {
          for (TeacherDetails td : course.getInstructors()) {
            if (td != null) {
              TeacherSummaryDTO tsDTO = toTeacherSummaryDTO(td);
              tsDTOList.add(tsDTO);
            }
          }
        }
        dto.setInstructors(tsDTOList);

      }

      dto.setMetaList(course.getMetaList());
      dto.setNoOfReviewers(course.getNoOfReviewers());
      dto.setNoOfSessions(course.getNoOfSessions());
      dto.setPopular(course.isPopular());
      dto.setPosition(course.getPosition());
      dto.setPrice(course.getPrice());
      if (course.getRelatedCourses() != null && course.getRelatedCourses().size() > 0) {
        Set<CourseSummaryDTO> relatedList = new LinkedHashSet<CourseSummaryDTO>();
        for (CourseSummary cs : course.getRelatedCourses()) {
          if (cs != null && PublishStatus.publish.equals(cs.getStatus())) {
            CourseSummaryDTO csDTO = toCourseSummaryDTO(cs);
            relatedList.add(csDTO);
          }
        }
        dto.setRelatedCourses(relatedList);

      }
      dto.setSchedule(course.getSchedule());
      dto.setStartDate(course.getStartDate());
      dto.setStatus(course.getStatus());
      dto.setStudentsEnrolled(course.getStudentsEnrolled());
      dto.setSuitableFor(course.getSuitableFor());
      dto.setTitleTag(course.getTitleTag());
      dto.setUpdatedAt(course.getUpdatedAt());

      String additional = course.getAdditionalInfo();
      String inclusions = course.getInclusion();

      Type type = new TypeToken<List<Highlights>>() {}.getType();
      Gson converter = new Gson();

      List<Highlights> courseAdditionalList = converter.fromJson(additional, type);
      List<Highlights> courseInclusionList = converter.fromJson(inclusions, type);

      dto.setCourseAdditional(courseAdditionalList);
      dto.setCourseInclusions(courseInclusionList);

    }
    return setCourseMetaInformation(dto);

  }

  public static CourseSummaryDTO toCourseSummaryDTO(CourseSummary course) {
    CourseSummaryDTO dto = null;
    if (course != null) {
      dto = new CourseSummaryDTO();
      dto.setCourseImageUrl(course.getCourseImageUrl());
      dto.setCourseRating(course.getCourseRating());
      dto.setCourseOldSlug(course.getCourseSlug());
      dto.setCourseTitle(course.getCourseTitle());
      dto.setInstituteName(course.getInstituteName());
      dto.setInstituteSlug(course.getInstituteSlug());
      dto.setInstructorName(course.getInstructorName());
      dto.setInstructorSlug(course.getInstructorSlug());

      dto.setNoOfReviewers(course.getNoOfReviewers());
      dto.setPrice(course.getPrice());
      dto.setStartDate(course.getStartDate());
      dto.setStatus(course.getStatus());
      dto.setStudentsEnrolled(course.getStudentsEnrolled());
    }
    return dto;

  }

  public static CourseBatchDetailedDTO toCourseBatchDetailedDTO(CourseBatch cb) {
    CourseBatchDetailedDTO dto = null;
    if (cb != null) {
      dto = new CourseBatchDetailedDTO();
      dto.setBatchName(cb.getBatchName());
      dto.setClassTiming(cb.getClassTiming());
      dto.setCreatedAt(cb.getCreatedAt());
      dto.setDiscount(cb.getDiscount());
      dto.setDisplaySize(cb.getDisplaySize());
      dto.setDuration(cb.getDuration());
      dto.setId(cb.getId());
      dto.setMetaList(cb.getMetaList());
      dto.setNoOfAvailableSeats(cb.getNoOfAvailableSeats());
      dto.setNoOfSession(cb.getNoOfSession());
      dto.setRegularPrice(cb.getRegularPrice());
      dto.setSalePrice(cb.getSalePrice());
      dto.setStartDate(cb.getStartDate());
      dto.setStatus(cb.getStatus());
      dto.setUpdatedAt(cb.getUpdatedAt());
      dto.setValidity(cb.getValidity());
      dto.setValidityDisplay(cb.getValidityDisplay());

    }
    return dto;
  }

  public static CommentDTO toCommentDTO(Comment comment) {
    UserDTO userDTO = null;
    CommentDTO dto = null;
    if (comment != null) {
      dto = modelMapper.map(comment, CommentDTO.class);
      if (comment.getCommentedBy() != null) {
        userDTO = modelMapper.map(comment.getCommentedBy(), UserDTO.class);
      }
    }
    if (dto != null) {
      dto.setUser(userDTO);
    }
    return dto;

  }

  public static VideoChunksDTO toVideoChunksDTO(VideoChunks chunk) {
    VideoChunksDTO dto = new VideoChunksDTO();
    StringBuilder startTime = new StringBuilder();
    StringBuilder endTime = new StringBuilder();
    startTime.append(chunk.getStartHour() + ":");
    startTime.append(chunk.getStartMinute() + ":");
    startTime.append(chunk.getStartSecond());
    endTime.append(chunk.getEndHour() + ":");
    endTime.append(chunk.getEndMinute() + ":");
    endTime.append(chunk.getEndSecond());
    dto.setStartTime(startTime.toString());
    dto.setEndTime(endTime.toString());
    dto.setChunkPosition(chunk.getChunkPosition());
    dto.setId(chunk.getId());

    return dto;
  }

  public static VideoRequestDTO toVideoRequestDTO(VideoRequest request) {
    VideoRequestDTO dto = new VideoRequestDTO(request.getId(), request.getSourceVideoPath(),
        request.getRequestType(), request.getRequestCreationDate(),
        request.getRequestApprovalDate(), request.getRequestCreatedBy(), request.getApprover(),
        request.getRequestStatus(), request.getDestinationVideoPath(), request.isFree(),
        request.isDeleteOriginal(), request.getRequestReason(), request.getApproverComments(),
        request.getUnitId(), request.getOldWowzaLink(), request.getNewWowzaLink(),
        request.getCourseName(), request.getBatchName(), request.getInstituteName(),
        request.getTeacherName(), request.getTitle());

    Set<VideoChunksDTO> chunks = new LinkedHashSet<VideoChunksDTO>();
    if (request.getChunks() != null && request.getChunks().size() > 0) {
      for (VideoChunks chunk : request.getChunks()) {
        chunks.add(toVideoChunksDTO(chunk));
      }
      dto.setChunks(chunks);
    }

    return dto;
  }
  
  /**
   * 
   * @param discussion
   * @return
   */
  public static DiscussionDTO toDiscussionDTO(Discussion discussion) {
    DiscussionDTO dto = new DiscussionDTO();

    dto.setCategory(discussion.getCategory());
    dto.setContent(discussion.getContent());
    dto.setExamSegment(discussion.getExamSegment());
    dto.setId(discussion.getId());
    dto.setNoOfLikes(discussion.getNoOfLikes());
    dto.setSlug(discussion.getSlug());
    dto.setTitle(discussion.getTitle());
    dto.setCreatedBy(discussion.getUser().getFullName());
    dto.setUpdatedAt(discussion.getUpdatedAt());

    if(discussion.getUser()!=null) {

      if(discussion.getUser().getAvatar()!=null){
        dto.setAvatar(discussion.getUser().getAvatar());
      }
      else{
        dto.setAvatar("https://lh3.googleusercontent.com/Z4uHbjIjELCl1WaD_fGK7VnkE9sXzT_b4SLAmmapf7JbmtR9pNF17JW58z3vA7JsRuCGl0BiovQc7zuHiuOp6PZi7Cw");

      }

    }

    else{
      dto.setAvatar("https://lh3.googleusercontent.com/Z4uHbjIjELCl1WaD_fGK7VnkE9sXzT_b4SLAmmapf7JbmtR9pNF17JW58z3vA7JsRuCGl0BiovQc7zuHiuOp6PZi7Cw");

    }

    return dto;
  }

  /**
   * 
   * @param dto
   * @return
   */
  public static Discussion fromDiscussionDTO(DiscussionDTO dto) {
    Discussion discussion = new Discussion();

    discussion.setCategory(dto.getCategory());
    discussion.setContent(dto.getContent());
    discussion.setExamSegment(dto.getExamSegment());
    discussion.setId(dto.getId());
    discussion.setNoOfLikes(dto.getNoOfLikes());
    discussion.setSlug(dto.getSlug());
    discussion.setTitle(dto.getTitle());

    return discussion;
  }
  
  public static UserNeoCashHistoryDTO toUserNeoCashHistoryDTO(UserNeoCashHistory history)
  {
    UserNeoCashHistoryDTO dto=new UserNeoCashHistoryDTO();
    dto.setAction(history.getAction());
    dto.setAdditionalInfo(history.getAdditionalInfo());
    dto.setCreditedBy(history.getCreditedBy());
    dto.setExpiryDate(history.getExpiryDate());
    dto.setId(history.getId());
    dto.setNeoCashValue(history.getNeoCashValue());
    dto.setReason(history.getReason());
    dto.setUserId(history.getUserId());
    dto.setCreatedAt(history.getCreatedAt());
    
    return dto;
  }

  public static UnitDTO toUnitDTO(Unit unit)
  {
    UnitDTO dto=new UnitDTO();
    dto.setBatch(unit.getBatch());
    dto.setDescription(unit.getDescription());
    dto.setFree(unit.isFree());
    dto.setHasStarted(unit.isHasStarted());
    dto.setNoOfViews(unit.getNoOfViews());
    dto.setPosition(unit.getPosition());
    dto.setPrice(unit.getPrice());
    dto.setProduct(unit.isProduct());
    dto.setPubliclyBuyable(unit.isPubliclyBuyable());
    dto.setQueries(unit.getQueries());
    dto.setStatus(unit.getStatus());
    dto.setTitle(unit.getTitle());
    dto.setTopic(unit.getTopic());
    dto.setType(unit.getType());
    dto.setTypeId(unit.getTypeId());
    dto.setUnitId(unit.getUnitId());
    dto.setUnit(unit);

    return dto;
  }
}
