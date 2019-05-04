package com.neostencil.model.entities;

import java.util.Set;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OrderColumn;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import com.neostencil.common.enums.AnswerSheetCategory;

@Entity
@Table(name = "ns_answer_sheets")
public class AnswerSheet extends DomainObject {

  @GeneratedValue(strategy = GenerationType.TABLE, generator = "answersheet_gen")
  @TableGenerator(name = "answersheet_gen", table = "answersheet_id_gen", pkColumnName = "GEN_NAME",
      valueColumnName = "GEN_VAL", pkColumnValue = "answersheet_Gen", initialValue = 300,
      allocationSize = 1)
  @Id
  int id;

  @Column(name = "sheet_title")
  String title;

  @Fetch(value = FetchMode.SUBSELECT)
  @ElementCollection(fetch = FetchType.EAGER)
  @JoinTable(name = "ns_answer_sheet_booklets", joinColumns = @JoinColumn(name = "answer_sheet_id"))
  @OrderColumn(name = "sequence", updatable = true)
  Set<Booklet> booklets;

  @Column(name = "answer_sheet_category")
  @Enumerated(EnumType.STRING)
  AnswerSheetCategory category;

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
   * @return the title
   */
  public String getTitle() {
    return title;
  }

  /**
   * @param title the title to set
   */
  public void setTitle(String title) {
    this.title = title;
  }

  /**
   * @return the booklets
   */
  public Set<Booklet> getBooklets() {
    return booklets;
  }

  /**
   * @param booklets the booklets to set
   */
  public void setBooklets(Set<Booklet> booklets) {
    this.booklets = booklets;
  }

  /**
   * @return the category
   */
  public AnswerSheetCategory getCategory() {
    return category;
  }

  /**
   * @param category the category to set
   */
  public void setCategory(AnswerSheetCategory category) {
    this.category = category;
  }



}
