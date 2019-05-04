package com.neostencil.model.repositories;

import com.neostencil.model.entities.UserDevice;
import com.neostencil.model.entities.UserQuizSubmission;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserQuizSubmissionRepository extends JpaRepository<UserQuizSubmission, Integer> {

  UserQuizSubmission findByUserUserIdAndUnitUnitId(long userId,int unitId);

  List<UserQuizSubmission> findByQuizQuizIdOrderByMarksObtainedDesc(int quizId);

}