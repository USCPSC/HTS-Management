package gov.cpsc.itds.common.properties;

import gov.cpsc.itds.common.cli.CommandLineHandler;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.apache.commons.cli.ParseException;

/** Unit test for simple App. */
public class SysPropertiesHandlerTest extends TestCase {
  /**
   * Create the test case
   *
   * @param testName name of the test case
   */
  public SysPropertiesHandlerTest(String testName) {
    super(testName);
    String[] args = {
      "-p", "EXAMPLE\\itds-common\\src\\test\\resources\\itds.properties"
    };
    try {
      new CommandLineHandler(args);
    } catch (ParseException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  /** @return the suite of tests being tested */
  public static Test suite() {
    return new TestSuite(SysPropertiesHandlerTest.class);
  }

  /** Happy Case */
  public void testPropertyFound() {
    assertTrue(SysPropertiesHandler.getProperty(SysProps.TEST_STRING).equals("abc"));
  }
  /** property not found Case */
  public void testPropertyNotFound() {
    assertTrue(SysPropertiesHandler.getProperty(SysProps.NOT_FOUND) == null);
  }
  /** property int found Case */
  public void testIntPropertyFound() {
    assertTrue(SysPropertiesHandler.getIntProperty(SysProps.TEST_INT) ==123);
  }
  /** property boolean found Case */
  public void testBooleanPropertyFound() {
    assertTrue(SysPropertiesHandler.getBooleanProperty(SysProps.TEST_ENABLED));
  }
}
