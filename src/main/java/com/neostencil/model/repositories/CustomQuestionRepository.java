package com.neostencil.model.repositories;

import com.neostencil.model.entities.Question;
import com.neostencil.model.entities.Quiz;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;
import java.util.RandomAccess;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public class CustomQuestionRepository extends SimpleJpaRepository<Question, Integer>{

  private EntityManager entityManager;

  public CustomQuestionRepository(EntityManager em) {
    super(Question.class, em);
    this.entityManager = em;
  }

  @Transactional()
  public List<Question> save(List<Question> questionList) {

    for(int i=0;i<questionList.size();i++){
      Question question = questionList.get(i);
      entityManager.persist(question);
      if(i%20==0){
        entityManager.flush();
        entityManager.clear();
      }
    }
    entityManager.flush();
    entityManager.clear();
    return questionList;
  }


  public List<Question> findQuestionsByQuizId(Quiz quiz) {
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Question> cquery = criteriaBuilder.createQuery(getDomainClass());
    Root<Question> root = cquery.from(getDomainClass());

    cquery.select(root).where(criteriaBuilder.equal(root.<String>get("quiz"),quiz));
    cquery.orderBy(criteriaBuilder.asc(root.get("position")));
    TypedQuery<Question> query = entityManager.createQuery(cquery);

    List<Question> resultList = query.getResultList();

    if(quiz.isShuffleQuestion()) {
      Collections.shuffle(resultList);
    }

    return resultList;
  }

  public Question findQuestionById(int questionId) {
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Question> cquery = criteriaBuilder.createQuery(getDomainClass());
    Root<Question> root = cquery.from(getDomainClass());

    cquery.select(root).where(criteriaBuilder.equal(root.<String>get("questionId"),questionId));

    TypedQuery<Question> query = entityManager.createQuery(cquery);

    return query.getSingleResult();
  }



}
