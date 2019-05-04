package com.neostencil.responses;

import com.neostencil.projections.ProductProjection;

public class CourseProductResponse {

  private ProductProjection product;
  private String courseName;

  public String getBatchName() {
    return batchName;
  }

  public void setBatchName(String batchName) {
    this.batchName = batchName;
  }

  private String batchName;

  /**
   * @return the courseName
   */
  public String getCourseName() {
    return courseName;
  }

  /**
   * @param courseName the courseName to set
   */
  public void setCourseName(String courseName) {
    this.courseName = courseName;
  }

  /**
   * @return the product
   */
  public ProductProjection getProduct() {
    return product;
  }

  /**
   * @param product the product to set
   */
  public void setProduct(ProductProjection product) {
    this.product = product;
  }



}
