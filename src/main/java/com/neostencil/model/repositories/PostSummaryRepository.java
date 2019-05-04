package com.neostencil.model.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.neostencil.common.enums.PublishStatus;
import com.neostencil.model.entities.PostSummary;

public interface PostSummaryRepository extends JpaRepository<PostSummary, String> {

	PostSummary findByPostSummaryId(int postSummarId);

	List<PostSummary> findByStatus(PublishStatus status);
}

