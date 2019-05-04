package com.neostencil.model.repositories;

import com.neostencil.common.enums.CommentType;
import com.neostencil.common.enums.PublishStatus;
import com.neostencil.model.entities.Comment;
import com.neostencil.projections.CommentProjection;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Pageable;

public interface CommentRepository extends JpaRepository<Comment, Integer> {

  List<Comment> findBySlugAndStatusAndTypeOrderByUpdatedAtAsc(String slug, PublishStatus status,
      CommentType type);
  
  Comment findByCommentId(int id);

  @Query("select c from Comment c where c.slug=:slug and c.type =:commentType and c.status='publish' and c.parentId=0 order by commentId desc ")
  Page<Comment> findCommentBySlug(@Param("slug") String slug,
      @Param("commentType") CommentType commentType, Pageable pageRequest);


  @Query("select c from Comment c where c.slug=:slug and c.type =:commentType and c.status='publish'")
  List<Comment> findCommentBySlug(@Param("slug") String slug, @Param("commentType") CommentType commentType);

  List<Comment> findBySlugAndStatusAndReviewTitleContainsAndTypeAndReviewRatingGreaterThanEqualOrderByCommentIdDesc(String slug, PublishStatus status,String reviewTitle,
      CommentType type,float rating);

  @Query("select c from Comment c where c.slug=:slug and c.type =:commentType and c.status='publish' and c.parentId=0 order by commentId desc ")
  Page<CommentProjection> findAllProjectedBySlug(@Param("slug") String slug,
      @Param("commentType") CommentType commentType, Pageable pageRequest);

  @Query("select c from Comment c where c.slug=:slug and c.type =:commentType order by commentId desc ")
  Page<CommentProjection> findAllByProjectedByOrderByCommentId(@Param("slug") String slug,
      @Param("commentType") CommentType commentType,Pageable pageRequest);

  Page<CommentProjection> findAllProjectedByParentIdOrderByCommentIdAsc(int parentId, Pageable pageRequest);

  }
