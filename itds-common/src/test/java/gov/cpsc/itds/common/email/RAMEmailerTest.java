package gov.cpsc.itds.common.email;

import org.apache.commons.cli.ParseException;
import org.apache.commons.mail.EmailException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gov.cpsc.itds.common.cli.CommandLineHandler;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/** Unit test for simple App. */
public class RAMEmailerTest extends TestCase {
  private static final Logger logger = LoggerFactory.getLogger(RAMEmailerTest.class);

  /**
   * Create the test case
   *
   * @param testName name of the test case
   */
  public RAMEmailerTest(String testName) {
    super(testName);
    String[] args = {"-p", "src/test/resources/itds.properties"};
    try {
      new CommandLineHandler(args);
    } catch (ParseException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  /** @return the suite of tests being tested */
  public static Test suite() {
    return new TestSuite(RAMEmailerTest.class);
  }

  /** semd email happy case */
  public void testSendEmail() {
    try {
      new RAMEmailer().sendEmail("EXAMPLE@EXAMPLE.org", "test", "test", false);
      assertTrue(true);
    } catch (EmailException e) {
      logger.error("", e);
      assertTrue(false);
    }
  }
}
