package com.neostencil.responses;

import com.neostencil.projections.ProductProjection;
import com.neostencil.projections.UnitProjection;

public class UnitResponse {

  private ProductProjection product;
  private UnitProjection unit;

  public String getBatchName() {
    return batchName;
  }

  public void setBatchName(String batchName) {
    this.batchName = batchName;
  }

  private String batchName;

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

  /**
   * @return the unit
   */
  public UnitProjection getUnit() {
    return unit;
  }

  /**
   * @param unit the unit to set
   */
  public void setUnit(UnitProjection unit) {
    this.unit = unit;
  }



}
