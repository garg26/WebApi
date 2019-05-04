package com.neostencil.projections;

import com.neostencil.common.enums.CommentType;
import com.neostencil.common.enums.PublishStatus;
import com.neostencil.model.entities.CommentAttachment;
import com.neostencil.model.entities.User;
import java.util.Set;
import org.springframework.beans.factory.annotation.Value;

public interface CommentProjection {

  int getCommentId();
  int getReply_count();
  int getCount();
  int getParentId();
  String getSlug();
  String getSlugType();
  String getName();
  String getEmail();
  String getUrl();
  String getDate();
  String getText();
  Set<CommentAttachment> getAttachments();

  @Value("#{target.commentedBy?.avatar}")
  String getAvatar();

  CommentType getType();
  PublishStatus getStatus();

}
