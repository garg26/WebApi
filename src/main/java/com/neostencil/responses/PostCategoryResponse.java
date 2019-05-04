package com.neostencil.responses;

import java.util.LinkedHashMap;
import java.util.LinkedList;

public class PostCategoryResponse {

  LinkedHashMap<String, LinkedList<String>> postCategories;

  public LinkedHashMap<String, LinkedList<String>> getPostCategories() {
    return postCategories;
  }

  public void setPostCategories(LinkedHashMap<String, LinkedList<String>> postCategories) {
    this.postCategories = postCategories;
  }
}
