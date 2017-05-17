package com.helix.utils;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

public class DateUtil {

  public static String getDate(String dateAsString) {

    DateTime dateTime = DateTime.parse(dateAsString, DateTimeFormat.forPattern("YYYY-MM-dd"));
    String month = dateTime.toString("MMM");
    String date = dateTime.toString("dd");
    return date + " " + month + " " + dateTime.getYear();
  }
}
