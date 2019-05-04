package com.neostencil.model.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Store;

/**
 * 
 * @author shilpa
 *
 */
@Entity
@Table(name="ns_parent_student_linkages")
public class ParentStudentLinkage {

  @Id
  @Column(name = "ID")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ps_linkage_seq")
  @SequenceGenerator(name = "ps_linkage_seq", sequenceName = "ps_linkage_seq", allocationSize = 1)
  @Field(name = "psLinkageId", index = Index.YES, analyze = Analyze.YES, store = Store.YES)
  private Long linkageId;
  
  @Column(name="parent_id")
  private long parentId;
  
  @Column(name="student_id")
  private long studentId;
  
  @Column(name="no_of_absences")
  private int noOfAbsences;

  /**
   * @return the parentId
   */
  public long getParentId() {
    return parentId;
  }

  /**
   * @param parentId the parentId to set
   */
  public void setParentId(long parentId) {
    this.parentId = parentId;
  }

  /**
   * @return the studentId
   */
  public long getStudentId() {
    return studentId;
  }

  /**
   * @param studentId the studentId to set
   */
  public void setStudentId(long studentId) {
    this.studentId = studentId;
  }

  /**
   * @return the linkageId
   */
  public Long getLinkageId() {
    return linkageId;
  }

  /**
   * @param linkageId the linkageId to set
   */
  public void setLinkageId(Long linkageId) {
    this.linkageId = linkageId;
  }

  /**
   * @return the noOfAbsences
   */
  public int getNoOfAbsences() {
    return noOfAbsences;
  }

  /**
   * @param noOfAbsences the noOfAbsences to set
   */
  public void setNoOfAbsences(int noOfAbsences) {
    this.noOfAbsences = noOfAbsences;
  }
  
  
}
