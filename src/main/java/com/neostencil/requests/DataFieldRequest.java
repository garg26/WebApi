package com.neostencil.requests;

public class DataFieldRequest {


  String fieldName;
  String label;
  String fieldValue;



  public DataFieldRequest() {
    super();
    // TODO Auto-generated constructor stub
  }

  public DataFieldRequest(String fieldName, String label, String fieldValue) {
    super();
    this.fieldName = fieldName;
    this.label = label;
    this.fieldValue = fieldValue;
  }

  /**
   * @return the fieldName
   */
  public String getFieldName() {
    return fieldName;
  }

  /**
   * @param fieldName the fieldName to set
   */
  public void setFieldName(String fieldName) {
    this.fieldName = fieldName;
  }

  /**
   * @return the label
   */
  public String getLabel() {
    return label;
  }

  /**
   * @param label the label to set
   */
  public void setLabel(String label) {
    this.label = label;
  }

  /**
   * @return the fieldValue
   */
  public String getFieldValue() {
    return fieldValue;
  }

  /**
   * @param fieldValue the fieldValue to set
   */
  public void setFieldValue(String fieldValue) {
    this.fieldValue = fieldValue;
  }


}
