package gov.cpsc.itds.common;

import java.time.LocalTime;

import gov.cpsc.itds.common.ITDSTimeUtils;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/** Unit test for DiskSpaceUtil */
public class DataManagerTimeUtilsTest extends TestCase {
  /**
   * Create the test case
   *
   * @param testName name of the test case
   */
  public DataManagerTimeUtilsTest(String testName) {
    super(testName);
  }

  /** @return the suite of tests being tested */
  public static Test suite() {
    return new TestSuite(DataManagerTimeUtilsTest.class);
  }

  /** Happy Case */
  public void testIsNowBetweenPreviousHourAndNextHourLocalTime() {
    LocalTime now = LocalTime.now();
    LocalTime previousHour = now.minusHours(1);
    LocalTime nextHour = now.plusHours(1);

    boolean between = ITDSTimeUtils.isNowBetween(previousHour, nextHour);

    assertTrue(between);
  }
  /** Happy Case This will fail if run between 2am and 3am */
  public void testIsNowBetweenPreviousHourAndNextHourString() {
    boolean notBetween = !ITDSTimeUtils.isNowBetween("02:00", "03:00");
    assertTrue(notBetween);
  }
}
