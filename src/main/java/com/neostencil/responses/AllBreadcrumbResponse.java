package com.neostencil.responses;

import com.neostencil.model.entities.BreadCrumb;

public class AllBreadcrumbResponse {

    BreadCrumb child;
    BreadCrumb parent;

  public BreadCrumb getChild() {
    return child;
  }

  public void setChild(BreadCrumb child) {
    this.child = child;
  }

  public BreadCrumb getParent() {
    return parent;
  }

  public void setParent(BreadCrumb parent) {
    this.parent = parent;
  }
}
