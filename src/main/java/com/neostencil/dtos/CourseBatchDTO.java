package com.neostencil.dtos;

/**
 * For transferring batch information over the network
 * 
 * @author shilpa
 *
 */
public class CourseBatchDTO {

  private int id;

  private String batchName;

  /**
   * @return the id
   */
  public int getId() {
    return id;
  }

  /**
   * @param id the id to set
   */
  public void setId(int id) {
    this.id = id;
  }

  /**
   * @return the batchName
   */
  public String getBatchName() {
    return batchName;
  }

  /**
   * @param batchName the batchName to set
   */
  public void setBatchName(String batchName) {
    this.batchName = batchName;
  }


}
