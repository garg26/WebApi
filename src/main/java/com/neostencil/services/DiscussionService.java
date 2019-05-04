package com.neostencil.services;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.neostencil.common.enums.ExamSegmentTypes;
import com.neostencil.model.entities.Discussion;
import com.neostencil.model.entities.User;
import com.neostencil.model.repositories.DiscussionRepository;
import com.neostencil.model.repositories.UserRepository;
import com.neostencil.responses.DiscussionDTO;
import com.neostencil.security.JwtTokenUtil;
import com.neostencil.utils.CommonAssembler;

/**
 * 
 * @author shilpa
 *
 */
@Service
public class DiscussionService {

  @Autowired
  DiscussionRepository repository;

  @Autowired
  JwtTokenUtil jwtUtil;

  @Autowired
  UserRepository userRepository;


  public void createDiscussion(DiscussionDTO dto) {

    if (dto != null) {
      Discussion discussion = CommonAssembler.fromDiscussionDTO(dto);
      String slug = getSlugFromTitle(discussion.getTitle(), discussion.getExamSegment());
      discussion.setSlug(slug);
      discussion.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
      discussion.setUpdatedAt(discussion.getCreatedAt());
     
      long loggedInUserId=jwtUtil.getLoggedInUserID();
      User user=userRepository.findByUserId(loggedInUserId);
      
      discussion.setUser(user);
      
      repository.saveAndFlush(discussion);
    }
  }



  private String getSlugFromTitle(String title, ExamSegmentTypes examSegment) {
    StringBuilder slug = new StringBuilder();
    slug.append(examSegment.name().toLowerCase());
    slug.append("-");

    if (title != null && !title.isEmpty()) {
      slug.append(((title.replaceAll("[-+.^:,]", "")).toLowerCase()).replaceAll(" ", "-"));
    }
    return slug.toString();
  }


  public List<Discussion> fetchAllDiscussions() {
    List<Discussion> discussions = repository.findAllByOrderByUpdatedAtDesc();


    return discussions;
  }

  public DiscussionDTO fetchDiscussionBySlug(String slug) {
    DiscussionDTO response = null;
    List<Discussion> discussionList = repository.findAllBySlug(slug);
    if (discussionList != null && !discussionList.isEmpty()) {
      if (discussionList.size() == 1) {
        Discussion d = discussionList.get(0);
        response = CommonAssembler.toDiscussionDTO(d);

      }
    }
    return response;
  }

  public long likeDiscussion(long id) {
    Optional<Discussion> optional = repository.findById(id);
    Discussion discussion = optional.get();
    long totalLikes = discussion.getNoOfLikes() + 1;
    discussion.setNoOfLikes(totalLikes);
    repository.saveAndFlush(discussion);

    return totalLikes;

  }

  public List<Discussion> fetchUserDiscussions() {

    long loggedInUserId = jwtUtil.getLoggedInUserID();
    User user = userRepository.findByUserId(loggedInUserId);
    List<Discussion> discussions = repository.findAllByUserOrderByUpdatedAtDesc(user);

    return discussions;
  }
}
