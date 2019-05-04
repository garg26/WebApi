package com.neostencil.responses;

import java.util.LinkedList;
import java.util.List;

/**
 * 
 * @author shilpa
 *
 */
public class NeoCashHistoryResponse {
  
  private double currentNeoCashBalance;
  private List<UserNeoCashHistoryDTO> historyList=new LinkedList<UserNeoCashHistoryDTO>();
  
  /**
   * @return the currentNeoCashBalance
   */
  public double getCurrentNeoCashBalance() {
    return currentNeoCashBalance;
  }
  /**
   * @param currentNeoCashBalance the currentNeoCashBalance to set
   */
  public void setCurrentNeoCashBalance(double currentNeoCashBalance) {
    this.currentNeoCashBalance = currentNeoCashBalance;
  }
  /**
   * @return the historyList
   */
  public List<UserNeoCashHistoryDTO> getHistoryList() {
    return historyList;
  }
  /**
   * @param historyList the historyList to set
   */
  public void setHistoryList(List<UserNeoCashHistoryDTO> historyList) {
    this.historyList = historyList;
  }
  
  

}
