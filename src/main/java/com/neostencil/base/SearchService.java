package com.neostencil.base;

import com.neostencil.common.enums.PublishStatus;
import com.neostencil.model.entities.Course;
import com.neostencil.model.entities.Institute;
import com.neostencil.model.entities.Post;
import com.neostencil.model.entities.TeacherDetails;
import com.neostencil.model.entities.User;
import com.neostencil.responses.SearchResponse;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import org.apache.lucene.search.Query;
import org.hibernate.search.engine.ProjectionConstants;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.stereotype.Service;

@Service
public class SearchService {

  @PersistenceContext
  private EntityManager entityManager;

  public void initializeSearch() throws InterruptedException {
    FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
    fullTextEntityManager.createIndexer().startAndWait();
  }

  public SearchResponse performSearch(String search, int page, int size) {
    SearchResponse response = new SearchResponse();
    if (search == null || search.isEmpty()) {
      // return empty response if search string is null
      return response;
    }
    response.setTeachers(this.searchTeachers(search, page, size));
    response.setCourses(this.searchCourses(search, page, size));
    /* response.setInstitutes(this.searchInstitutes(search, page, size)); */
    response.setPosts(this.searchPosts(search, page, size));
    return response;
  }

  public List<Post> filterPublishedPosts(List<Post> posts) {
    List<Post> result = new LinkedList<Post>();

    if (posts != null && posts.size() > 0) {
      for (Post p : posts) {
        if (p != null && PublishStatus.publish.equals(p.getStatus())) {
          result.add(p);
        }
      }
    }
    return result;
  }

  public List<TeacherDetails> filterPublishedTeachers(List<TeacherDetails> teachers) {
    List<TeacherDetails> result = new LinkedList<TeacherDetails>();

    if (teachers != null && teachers.size() > 0) {
      for (TeacherDetails td : teachers) {
        if (td != null && PublishStatus.publish.equals(td.getStatus())) {
          result.add(td);
        }
      }
    }
    return result;
  }

  public List<Course> filterPublishedCourses(List<Course> courses) {
    List<Course> result = new LinkedList<Course>();

    if (courses != null && courses.size() > 0) {
      for (Course c : courses) {
        if (c != null && PublishStatus.publish.equals(c.getStatus())) {
          result.add(c);
        }
      }
    }
    return result;
  }

  public List<Institute> filterPublishedInstitutes(List<Institute> institutes) {
    List<Institute> result = new LinkedList<Institute>();

    if (institutes != null && institutes.size() > 0) {
      for (Institute i : institutes) {
        if (i != null && PublishStatus.publish.equals(i.getStatus())) {
          result.add(i);
        }
      }
    }
    return result;
  }

  @Transactional
  public List<Object> searchPosts(String search, int page, int size) {
    FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
    QueryBuilder qb =
        fullTextEntityManager.getSearchFactory().buildQueryBuilder().forEntity(Post.class).get();
    Query luceneQuery = qb.bool()
        .should(qb.keyword().onField("title").boostedTo(100).andField("text").matching(search)
            .createQuery())
        .must(qb.keyword().onField("status").matching(PublishStatus.publish).createQuery())
        .createQuery();

    javax.persistence.Query jpaQuery =
        fullTextEntityManager.createFullTextQuery(luceneQuery, Post.class);
    ((FullTextQuery) jpaQuery).setProjection("postSlug", "title", "featuredImage.url", "brief",
        "readTime", "noOfViews", ProjectionConstants.SCORE);
    // page = 0;
    if (page > 0 && size > 0) {
      jpaQuery.setFirstResult((page - 1) * size);
      jpaQuery.setMaxResults(size);
    }
    // execute search

    List<Object> postList = null;
    try {
      postList = jpaQuery.getResultList();
      postList = goodQualityResults(postList, 100);
    } catch (NoResultException nre) {
      ;// do nothing
    }

    return postList;
  }

  @Transactional
  public List<Object> searchTeachers(String search, int page, int size) {
    FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
    QueryBuilder qb = fullTextEntityManager.getSearchFactory().buildQueryBuilder()
        .forEntity(TeacherDetails.class).get();
    Query luceneQuery = qb.bool()
        .should(qb.keyword()
            .onFields("teacherDescription", "teacherExamSegment", "teacherCategory", "subjects",
                "slug", "teacherLocation", "institute.name")
            .andField("teacherName").boostedTo(100). matching(search).createQuery())
        .must(qb.keyword().onField("status").matching(PublishStatus.publish).createQuery())
        .createQuery();

    javax.persistence.Query jpaQuery =
        fullTextEntityManager.createFullTextQuery(luceneQuery, TeacherDetails.class);

    ((FullTextQuery) jpaQuery).setProjection("teacherName", "slug", "displayPictureUrl",
        "institute.name", "institute.instituteSlug", "subjects", ProjectionConstants.SCORE);
    // page = 0;
    if (page > 0 && size > 0) {
      jpaQuery.setFirstResult((page - 1) * size);
      jpaQuery.setMaxResults(size);
    }
    // execute search

    List<Object> teacherDetailsList = null;
    try {
      teacherDetailsList = jpaQuery.getResultList();
      teacherDetailsList = goodQualityResults(teacherDetailsList, 5);
    } catch (NoResultException nre) {
      ;// do nothing
    }

    // return filterPublishedTeachers(teacherDetailsList);
    return teacherDetailsList;
  }


  private List<Object> goodQualityResults(List<Object> originalResults, float qualityFactor)
  {
    float maxScore = 0;
    float tmpQualityFactor = 100000;
    if(qualityFactor > 0)
    {
      tmpQualityFactor = qualityFactor;
    }
    List<Object> rv = new ArrayList<>();
    for(Object tempOrig: originalResults)
    {
      Object[] temp = (Object[]) tempOrig;
      float score = (float) temp[temp.length-1];
      if (score > maxScore)
      {
        maxScore = score;
      }
    }

    for(Object tempOrig: originalResults)
    {
      Object[] temp = (Object[]) tempOrig;
      float score = (float) temp[temp.length-1];
      if (score > maxScore/tmpQualityFactor)
      {
        rv.add(tempOrig);
      }
    }
    return rv;
  }

  @Transactional
  public List<Object> searchCourses(String search, int page, int size) {
    FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
    QueryBuilder qb =
        fullTextEntityManager.getSearchFactory().buildQueryBuilder().forEntity(Course.class).get();

    Query luceneQuery = qb.bool()
        .should(qb.phrase().onField("instructors").andField("courseTitle").boostedTo(20)
            .andField("institute.name").andField("institute.instituteSlug").andField("instructorName").boostedTo(10)
            .andField("courseSubject").sentence(search).createQuery())
        .must(qb.keyword().onField("status").matching(PublishStatus.publish).createQuery())
        .createQuery();

    javax.persistence.Query jpaQuery =
        fullTextEntityManager.createFullTextQuery(luceneQuery, Course.class);


    ((FullTextQuery) jpaQuery).setProjection("courseTitle", "courseOldSlug", "instructors",
        "instructors.slug", "institute.name", "institute.instituteSlug", "startDate",
        "studentsEnrolled", "price", "courseImageUrl", "courseRating","instructorName", ProjectionConstants.SCORE);



    // page = 0;
    if (page > 0 && size > 0) {
      jpaQuery.setFirstResult((page - 1) * size);
      jpaQuery.setMaxResults(size);
    }
    // execute search

    List<Object> courseList = null;
    try {
      courseList = jpaQuery.getResultList();
      courseList = goodQualityResults(courseList, 0);
    } catch (NoResultException nre) {
      ;// do nothing
    }

    // return filterPublishedCourses(courseList);
    return courseList;
  }

  /*
   * @Transactional public List<Object> searchInstitutes(String search, int page, int size) {
   * FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
   * QueryBuilder qb = fullTextEntityManager.getSearchFactory().buildQueryBuilder()
   * .forEntity(Institute.class).get();
   *
   * Query luceneQuery = qb.bool()
   * .should(qb.keyword().fuzzy().withEditDistanceUpTo(1).withPrefixLength(1)
   * .onFields("name","url","description","image","ownerName","address","otherInformation",
   * "instituteSlug") .matching(search).createQuery())
   * .must(qb.keyword().onField("status").matching(PublishStatus.publish).createQuery())
   * .createQuery();
   *
   *
   *//*
     * Query luceneQuery = qb.keyword().fuzzy().withEditDistanceUpTo(1).withPrefixLength(1)
     * .onFields("name", "url", "description", "image", "ownerName", "address", "facebookUrl",
     * "linkedinUrl", "twitterUrl", "googlePlusUrl", "otherInformation", "instituteSlug")
     * .matching(search).createQuery();
     *//*
       * javax.persistence.Query jpaQuery = fullTextEntityManager.createFullTextQuery(luceneQuery,
       * Institute.class);
       *
       * ((FullTextQuery) jpaQuery).setProjection("name"); // page = 0; if (page > 0 && size > 0) {
       * jpaQuery.setFirstResult((page - 1) * size); jpaQuery.setMaxResults(size); } // execute
       * search
       *
       * List<Object> institutesList = null; try { institutesList = jpaQuery.getResultList(); }
       * catch (NoResultException nre) { ;// do nothing }
       *
       * // return filterPublishedInstitutes(institutesList); return institutesList; }
       */

  @Transactional
  public List<Object> searchUsers(String search, int page, int size) {
    FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
    QueryBuilder qb =
        fullTextEntityManager.getSearchFactory().buildQueryBuilder().forEntity(User.class).get();
    Query luceneQuery = qb.keyword().fuzzy().withEditDistanceUpTo(1).withPrefixLength(1)
        .onFields("userId", "emailId", "userName", "firstname", "lastname", "fullName")
        .matching(search).createQuery();

    javax.persistence.Query jpaQuery =
        fullTextEntityManager.createFullTextQuery(luceneQuery, User.class);

    ((FullTextQuery) jpaQuery).setProjection("emailId", "userId", ProjectionConstants.SCORE);
    // page = 0;
    if (page > 0 && size > 0) {
      jpaQuery.setFirstResult((page - 1) * size);
      jpaQuery.setMaxResults(size);
    }
    // execute search

    List<Object> userList = null;
    try {
      userList = jpaQuery.getResultList();
      userList = goodQualityResults(userList, 10);
    } catch (NoResultException nre) {
      ;// do nothing
    }

    return userList;
  }

  @Transactional
  public List<Post> searchPostByTag(String search, int page, int size) {
    FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
    QueryBuilder qb =
        fullTextEntityManager.getSearchFactory().buildQueryBuilder().forEntity(Post.class).get();
    Query luceneQuery = qb.bool()
        .should(qb.keyword().onFields("tags.name").matching(search).createQuery()).createQuery();


    javax.persistence.Query jpaQuery =
        fullTextEntityManager.createFullTextQuery(luceneQuery, Post.class);
    // page = 0;
    if (page > 0 && size > 0) {
      jpaQuery.setFirstResult((page - 1) * size);
      jpaQuery.setMaxResults(size);
    }
    // execute search

    List<Post> postList = null;
    try {
      postList = jpaQuery.getResultList();
    } catch (NoResultException nre) {
      ;// do nothing
    }

    return postList;
  }

}
