package com.neostencil.responses;

import com.neostencil.model.entities.VideoRequest;

/**
 * 
 * @author shilpa
 *
 */
public class VideoChunksDTO {
  
  private long id;

  private int chunkPosition;

  private VideoRequest videoRequest;

  private String startTime;
  
  private String endTime;
  
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
   * @return the startTime
   */
  public String getStartTime() {
    return startTime;
  }

  /**
   * @param startTime the startTime to set
   */
  public void setStartTime(String startTime) {
    this.startTime = startTime;
  }

  /**
   * @return the endTime
   */
  public String getEndTime() {
    return endTime;
  }

  /**
   * @param endTime the endTime to set
   */
  public void setEndTime(String endTime) {
    this.endTime = endTime;
  }
  
}
