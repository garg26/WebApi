package com.neostencil.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.neostencil.responses.quizresponse.ExportQuizStats;
import org.springframework.stereotype.Service;

import com.neostencil.model.entities.Product;
import com.neostencil.requests.DataFieldRequest;
import com.neostencil.responses.CouponDTO;
import com.neostencil.responses.OrderDTO;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvException;

@Service
public class CSVUtility {


  public static void writeCouponsToCsv(Writer writer, List<CouponDTO> coupons,
      List<DataFieldRequest> dataFields) {
    try {

      /*
       * ColumnPositionMappingStrategy<Coupon> mapStrategy = new
       * ColumnPositionMappingStrategy<Coupon>();
       *
       * mapStrategy.setType(Coupon.class);
       */
      /*
       * mapStrategy.generateHeader(); List<String> columnList = new LinkedList<String>(); if
       * (dataFields != null && dataFields.size() > 0) { for (DataFieldRequest r : dataFields) { if
       * (r != null && r.getFieldName() != null) { columnList.add(r.getFieldName()); } } } String[]
       * columns = new String[columnList.size()]; columns = columnList.toArray(columns);
       * mapStrategy.setColumnMapping(columns);
       */

      StatefulBeanToCsv btcsv = new StatefulBeanToCsvBuilder(writer)
          .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER).withSeparator(',').build();

      btcsv.write(coupons);
      writer.flush();
      writer.close();

    } catch (CsvException ex) {

      // LOGGER.error("Error mapping Bean to CSV", ex);
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  public static void writeProductsToCsv(Writer writer, List<Product> products,
      List<DataFieldRequest> dataFields) {
    try {

      /*
       * ColumnPositionMappingStrategy<Coupon> mapStrategy = new
       * ColumnPositionMappingStrategy<Coupon>();
       *
       * mapStrategy.setType(Coupon.class);
       */
      /*
       * mapStrategy.generateHeader(); List<String> columnList = new LinkedList<String>(); if
       * (dataFields != null && dataFields.size() > 0) { for (DataFieldRequest r : dataFields) { if
       * (r != null && r.getFieldName() != null) { columnList.add(r.getFieldName()); } } } String[]
       * columns = new String[columnList.size()]; columns = columnList.toArray(columns);
       * mapStrategy.setColumnMapping(columns);
       */

      StatefulBeanToCsv btcsv = new StatefulBeanToCsvBuilder(writer)
          .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER).withSeparator(',').build();

      btcsv.write(products);
      writer.flush();
      writer.close();

    } catch (CsvException ex) {

      // LOGGER.error("Error mapping Bean to CSV", ex);
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  public static void writeUserQuizSubmission(Writer writer, List<ExportQuizStats> orderList,
      List<DataFieldRequest> dataFields) {

    try {

      StatefulBeanToCsv<ExportQuizStats> beanToCsv = new StatefulBeanToCsvBuilder<ExportQuizStats>(writer).build();
      beanToCsv.write(orderList);
      writer.flush();
      writer.close();

    } catch (CsvException ex) {
      ex.printStackTrace();

    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  public static void writeOrdersToCsv(Writer writer, List<OrderDTO> orderList,
                                      List<DataFieldRequest> dataFields) {

    try {

      StatefulBeanToCsv<OrderDTO> beanToCsv = new StatefulBeanToCsvBuilder<OrderDTO>(writer).build();
      beanToCsv.write(orderList);
      writer.flush();
      writer.close();

    } catch (CsvException ex) {
      ex.printStackTrace();

    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  public static Map<String, String> getRedirectionMap() {
    Map<String, String> redirectionMap = new HashMap<String, String>();
    BufferedReader br = null;
    CSVReader reader = null;
    try {

      InputStream in = CSVUtility.class.getResourceAsStream("/PostRedirectionURLs_New.csv");

      br = new BufferedReader(new InputStreamReader(in));

      if (br != null) {
        reader = new CSVReader(br);
      }

      String[] row;
      if (reader != null) {
        while ((row = reader.readNext()) != null) {
          String key = row[1];
          String value = row[4];
          redirectionMap.put(key, value);
        }
        reader.close();
        in = CSVUtility.class.getResourceAsStream("/CourseRedirectionURLs_New.csv");
        br = new BufferedReader(new InputStreamReader(in));

        reader = new CSVReader(br);

        while ((row = reader.readNext()) != null) {
          String key = row[0];
          String value = row[1];

          if (key != null && !key.isEmpty() && key.indexOf(".com") != -1
              && value.indexOf(".com") != -1) {
            if (key.endsWith("/")) {
              key = key.substring(key.indexOf(".com") + 5, key.length() - 1);
            } else {
              key = key.substring(key.indexOf(".com") + 5, key.length());
            }

            value = value.substring(value.indexOf(".com") + 5, value.length() - 1);
            redirectionMap.put(key, value);
          }

        }
      }
      reader.close();
      in = CSVUtility.class.getResourceAsStream("/InstituteRedirectionURLs.csv");
      br = new BufferedReader(new InputStreamReader(in));

      reader = new CSVReader(br);

      while ((row = reader.readNext()) != null) {
        String key = row[0];
        String value = "institute/" + row[1];
        redirectionMap.put(key, value);

      }
      reader.close();

      in = CSVUtility.class.getResourceAsStream("/TeacherRedirectionURLs.csv");
      br = new BufferedReader(new InputStreamReader(in));

      reader = new CSVReader(br);

      while ((row = reader.readNext()) != null) {
        String key = row[0];
        String value = "teacher/" + row[1];
        redirectionMap.put(key, value);
      }
      reader.close();

      in = CSVUtility.class.getResourceAsStream("/ContentRedirectionURLs.csv");
      br = new BufferedReader(new InputStreamReader(in));

      reader = new CSVReader(br);

      while ((row = reader.readNext()) != null) {
        String key = row[0];
        String value = row[1];

        if (key != null && !key.isEmpty() && key.indexOf(".com") != -1
            && value.indexOf(".com") != -1) {
          key = key.substring(key.indexOf(".com") + 5, key.length() - 1);
          value = value.substring(value.indexOf(".com") + 5, value.length() - 1);
          redirectionMap.put(key, value);
        }

      }

    } catch (Exception e) {
      e.printStackTrace();
    }

    return redirectionMap;

  }

}


