package gov.cpsc.itds.common.properties;

import org.apache.commons.cli.ParseException;

import gov.cpsc.itds.common.cli.CommandLineHandler;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/** Unit test for simple App. */
public class PropertiesHandlerTest extends TestCase {
  /**
   * Create the test case
   *
   * @param testName name of the test case
   */
  public PropertiesHandlerTest(String testName) {
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
    return new TestSuite(PropertiesHandlerTest.class);
  }

  /** Happy Case */
  public void testPropertyFound() {
    assertTrue(PropertiesHandler.getProperty("string") != null);
  }
  /** property not found Case */
  public void testPropertyNotFound() {
    assertTrue(PropertiesHandler.getProperty("sftpURLxx") == null);
  }
  /** property int found Case */
  public void testIntPropertyFound() {
    assertTrue(PropertiesHandler.getIntProperty("int") > 0);
  }
  /*
   * localtime should return null if property not found
   */
  public void testLocalTimePropertyNotFound() {
    assertNull(PropertiesHandler.getLocalTimeProperty("xxx"));
  }
  /*
   * localtime should return not null if property found
   */
  public void testLocalTimePropertyFound() {
    assertNotNull(PropertiesHandler.getLocalTimeProperty("time"));
  }

    /*
     * macro
     */
/*
    public void testMacroFound() {
    	assertEquals(PropertiesHandler.getProperty("macro"), "string/suffix1");
    }
*/

  public void testProfile(){
    String profile=PropertiesHandler.getProperty("profile");
    assertTrue(profile.equals("dev"));
  }

}
