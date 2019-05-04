package com.neostencil.model.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 
 * @author shilpa
 *
 */
@Entity
@Table(name = "ns_video_chunks")
public class VideoChunks {

  @Id
  @Column(name = "ID")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "video_chunk_seq")
  @SequenceGenerator(name = "video_chunk_seq", sequenceName = "video_chunk_seq", allocationSize = 1)
  private long id;

  @Column(name = "chunk_position")
  private int chunkPosition;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "request_id")
  private VideoRequest videoRequest;

  @Column(name = "start_hour")
  private int startHour;

  @Column(name="start_minute")
  private int startMinute;
  
  @Column(name="start_second")
  private int startSecond;
  
  @Column(name="end_hour")
  private int endHour;
  
  @Column(name="end_minute")
  private int endMinute;
  
  @Column(name="end_second")
  private int endSecond;
  /**
   * @return the id
   */
  public long getId() {
    return id;
  }

  /**
   * @param id the id to set
   */
  public void setId(long id) {
    this.id = id;
  }

  /**
   * @return the chunkPosition
   */
  public int getChunkPosition() {
    return chunkPosition;
  }

  /**
   * @param chunkPosition the chunkPosition to set
   */
  public void setChunkPosition(int chunkPosition) {
    this.chunkPosition = chunkPosition;
  }

  /**
   * @return the videoRequest
   */
  public VideoRequest getVideoRequest() {
    return videoRequest;
  }

  /**
   * @param videoRequest the videoRequest to set
   */
  public void setVideoRequest(VideoRequest videoRequest) {
    this.videoRequest = videoRequest;
  }

  /**
   * @return the startHour
   */
  public int getStartHour() {
    return startHour;
  }

  /**
   * @param startHour the startHour to set
   */
  public void setStartHour(int startHour) {
    this.startHour = startHour;
  }

  
  /**
   * @return the startSecond
   */
  public int getStartSecond() {
    return startSecond;
  }

  /**
   * @param startSecond the startSecond to set
   */
  public void setStartSecond(int startSecond) {
    this.startSecond = startSecond;
  }

  /**
   * @return the endHour
   */
  public int getEndHour() {
    return endHour;
  }

  /**
   * @param endHour the endHour to set
   */
  public void setEndHour(int endHour) {
    this.endHour = endHour;
  }

   /**
   * @return the endSecond
   */
  public int getEndSecond() {
    return endSecond;
  }

  /**
   * @param endMinute the endMinute to set
   */
  public void setEndMinute(int endMinute) {
    this.endMinute = endMinute;

  }

  /**
   * @return the startMinute
   */
  public int getStartMinute() {
    return startMinute;
  }

  /**
   * @param startMinute the startMinute to set
   */
  public void setStartMinute(int startMinute) {
    this.startMinute = startMinute;
  }

  /**
   * @return the endMinute
   */
  public int getEndMinute() {
    return endMinute;
  }

  /**
   * @param endSecond the endSecond to set
   */
  public void setEndSecond(int endSecond) {
    this.endSecond = endSecond;

  }

}
