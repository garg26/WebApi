package com.neostencil.common;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CommonUtil {

  public static String IMAGE_UPLOADED_FOLDER = "src/main/resources/static/images/";
  public static String bucketName = "ns-web-storage.appspot.com";


  public static String convertStringIntoDateFormat(Date date) {
    SimpleDateFormat convertFormatter = new SimpleDateFormat("dd MMM yyyy");

    return convertFormatter.format(date);

  }

  public static String convertEpochTimeIntoDateFormat(String dateInString) {

    DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
    SimpleDateFormat convertFormatter = new SimpleDateFormat("dd MMM yyyy");
    String format = null;
    try {

      Date date = formatter.parse(dateInString);

      if (date != null) {
        format = convertFormatter.format(date);
      }


    } catch (ParseException e) {
      e.printStackTrace();
    }

    return format;

  }

  public static String convertStringFormatIntoDateFormat(String dateInString) {
    DateFormat formatter = new SimpleDateFormat("DD MMM YYYY");
    SimpleDateFormat convertFormatter = new SimpleDateFormat("dd/mm/yyyy");
    String format = null;
    try {

      Date date = formatter.parse(dateInString);

      if (date != null) {
        format = convertFormatter.format(date);
      }


    } catch (ParseException e) {
      e.printStackTrace();
    }

    return format;

  }

  public static Timestamp convertStringFormatIntoTimeStamp(String date){
    try {
      DateFormat formatter;
      formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
      // you can change format of date
      Date Str_date = formatter.parse(date + " 00:00:00.000");
      java.sql.Timestamp timeStampDate = new Timestamp(Str_date.getTime());

      return timeStampDate;
    } catch (ParseException e) {
      System.out.println("Exception :" + e);
      return null;
    }
  }

  public static String getImageType(String base64) {
    String[] header = base64.split("[;]");
    if(header == null || header.length == 0) return null;
    return header[0].split("[/]")[1];
  }

  public static String comapreTime(String time1,String time2){
    SimpleDateFormat format = new SimpleDateFormat("hh:mm:ss");
    Date date1 = null;
    Date date2 = null;
    try {
      date1 = format.parse(time1);
      date2 = format.parse(time2);
    } catch (ParseException e) {
      e.printStackTrace();
    }

    long diff = date1.getTime() - date2.getTime();

    long diffSeconds = diff / 1000 % 60;
    long diffMinutes = diff / (60 * 1000) % 60;
    long diffHours = diff / (60 * 60 * 1000) % 24;
    long diffDays = diff / (24 * 60 * 60 * 1000);

    return String.format("%02d:%02d:%02d", diffHours, diffMinutes, diffSeconds);
  }

}
