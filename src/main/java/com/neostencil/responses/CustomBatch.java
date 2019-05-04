package com.neostencil.responses;

public class CustomBatch {

  private int batchId;

  private String batchName;

  /**
   * @return the batchId
   */
  public int getBatchId() {
    return batchId;
  }

  /**
   * @param batchId the batchId to set
   */
  public void setBatchId(int batchId) {
    this.batchId = batchId;
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

  public CustomBatch() {
    super();
    // TODO Auto-generated constructor stub
  }

  public CustomBatch(int batchId, String batchName) {
    super();
    this.batchId = batchId;
    this.batchName = batchName;
  }

  /* (non-Javadoc)
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + batchId;
    return result;
  }

  /* (non-Javadoc)
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    CustomBatch other = (CustomBatch) obj;
    if (batchId != other.batchId)
      return false;
    return true;
  }

  /**
   *
   */
  @Override
  public String toString() {
    return  batchName;
  }



}
