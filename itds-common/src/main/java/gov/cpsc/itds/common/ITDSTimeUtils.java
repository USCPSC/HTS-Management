package gov.cpsc.itds.common;

import java.time.LocalTime;

public class ITDSTimeUtils {

  private ITDSTimeUtils() {

    throw new IllegalStateException("DataManagerTimeUtils");
  }

  /*
   * The first parameter is the earlier time
   * and the second parameter is the later time
   */
  public static boolean isNowBetween(LocalTime time1, LocalTime time2) {
    boolean result = false;
    result = LocalTime.now().isAfter(time1) && LocalTime.now().isBefore(time2);
    return result;
  }

  /*
   * The first parameter is the earlier time
   * and the second parameter is the later time
   */
  public static boolean isNowBetween(String time1, String time2) {
    return isNowBetween(LocalTime.parse(time1), LocalTime.parse(time2));
  }
}
