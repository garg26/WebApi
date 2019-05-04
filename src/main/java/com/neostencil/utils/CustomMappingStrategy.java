package com.neostencil.utils;

import com.opencsv.bean.ColumnPositionMappingStrategy;

public class CustomMappingStrategy<OrderDTO> extends ColumnPositionMappingStrategy<OrderDTO> {

  private static final String[] HEADER = new String[] {"OrderID", "Date", "Order Status",
      "Student Name", "Student Email", "Collection By", "Address", "Phone Number", "Course",
      "Gross Sales", "Net Sales", "Quantity", "Institute"};

  @Override
  public String[] generateHeader(OrderDTO orderDTO) {
    return HEADER;
  }

}
