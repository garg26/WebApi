package com.neostencil.responses;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author shilpa
 *
 */
public class AllDiscussionsResponse {

  private Map<String, List<DiscussionDTO>> discussionMap;

  /**
   * @return the discussionMap
   */
  public Map<String, List<DiscussionDTO>> getDiscussionMap() {
    return discussionMap;
  }

  /**
   * @param discussionMap the discussionMap to set
   */
  public void setDiscussionMap(Map<String, List<DiscussionDTO>> discussionMap) {
    this.discussionMap = discussionMap;
  }

}
