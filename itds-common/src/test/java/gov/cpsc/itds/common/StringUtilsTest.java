package gov.cpsc.itds.common;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/** Unit test for simple App. */
public class StringUtilsTest extends TestCase {
  /**
   * Create the test case
   *
   * @param testName name of the test case
   */
  public StringUtilsTest(String testName) {
    super(testName);
  }

  /** @return the suite of tests being tested */
  public static Test suite() {
    return new TestSuite(StringUtilsTest.class);
  }

  /** false */
  public void testFalse() {
    assertTrue(StringUtils.isEmptyTrim("x") == false);
  }
  /** true */
  public void testTrue() {
    assertTrue(StringUtils.isEmptyTrim("") == true);
  }
  /** true with spaces */
  public void testTrueWithSpaces() {
    assertTrue(StringUtils.isEmptyTrim("   ") == true);
  }
  /** true null */
  public void testTrueNull() {
    assertTrue(StringUtils.isEmptyTrim(null) == true);
  }
}
