package com.neostencil.model.entities;

import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class OrderNotes {

  @Column(name = "note_content")
  private String noteContent;

  @Column(name = "added_by")
  private String addedBy;

  @Column(name = "noteDate")
  private Timestamp noteDate;

  /**
   * @return the noteContent
   */
  public String getNoteContent() {
    return noteContent;
  }

  /**
   * @param noteContent the noteContent to set
   */
  public void setNoteContent(String noteContent) {
    this.noteContent = noteContent;
  }

  /**
   * @return the addedBy
   */
  public String getAddedBy() {
    return addedBy;
  }

  /**
   * @param addedBy the addedBy to set
   */
  public void setAddedBy(String addedBy) {
    this.addedBy = addedBy;
  }

  /**
   * @return the noteDate
   */
  public Timestamp getNoteDate() {
    return noteDate;
  }

  /**
   * @param noteDate the noteDate to set
   */
  public void setNoteDate(Timestamp noteDate) {
    this.noteDate = noteDate;
  }



}
