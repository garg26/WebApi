package com.neostencil.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import javax.persistence.*;

@Entity
@Table(name = "ns_user_lecture_stats")
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class UserLectureStats extends DomainObject {

  @Column(name = "batch_id")
  private int batchId;

  @Column(name = "on_first_frame", columnDefinition = "TEXT")
  private String onFirstFrame;


  @Column(name = "chunk_count", columnDefinition = "int default 0")
  private int chunkCount;

  @Column(name = "on_complete", columnDefinition = "TEXT")
  private String onComplete;

  @Column(name = "on_idle", columnDefinition = "TEXT")
  private String onIdle;

  @Column(name = "on_pause", columnDefinition = "TEXT")
  private String onPause;

  @Column(name = "resume_from")
  private double resumeFrom;

  @Column(name="duration")
  private long duration;
  
  @EmbeddedId
  private UserLectureCompositeKey id;

  public String getOnPause() {
    return onPause;
  }

  public void setOnPause(String onPause) {
    this.onPause = onPause;
  }


  public UserLectureCompositeKey getId() {
    return id;
  }

  public void setId(UserLectureCompositeKey id) {
    this.id = id;
  }

  public int getBatchId() {
    return batchId;
  }

  public void setBatchId(int batchId) {
    this.batchId = batchId;
  }

  public String getOnFirstFrame() {
    return onFirstFrame;
  }

  public void setOnFirstFrame(String onFirstFrame) {
    this.onFirstFrame = onFirstFrame;
  }

  public String getOnComplete() {
    return onComplete;
  }

  public void setOnComplete(String onComplete) {
    this.onComplete = onComplete;
  }

  public int getChunkCount() {
    return chunkCount;
  }

  public void setChunkCount(int chunkCount) {
    this.chunkCount = chunkCount;
  }

  public String getOnIdle() {
    return onIdle;
  }

  public void setOnIdle(String onIdle) {
    this.onIdle = onIdle;
  }

  /**
   * @return the duration
   */
  public long getDuration() {
    return duration;
  }

  /**
   * @param duration the duration to set
   */
  public void setDuration(long duration) {
    this.duration = duration;
  }

  /**
   * @return the resumeFrom
   */
  public double getResumeFrom() {
    return resumeFrom;
  }

  /**
   * @param resumeFrom the resumeFrom to set
   */
  public void setResumeFrom(double resumeFrom) {
    this.resumeFrom = resumeFrom;
  }
}
