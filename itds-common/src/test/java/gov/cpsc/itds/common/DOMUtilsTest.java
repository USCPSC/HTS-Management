package gov.cpsc.itds.common;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import gov.cpsc.itds.common.cli.CommandLineHandler;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/** Unit test for simple App. */
public class DOMUtilsTest extends TestCase {
  private static final Logger logger = LoggerFactory.getLogger(DOMUtilsTest.class);

  /**
   * Create the test case
   *
   * @param testName name of the test case
   */
  public DOMUtilsTest(String testName) {
    super(testName);
    String[] args = {"-p", "EXAMPLE/transmitter/src/test/resources/itds.properties"};
    try {
      new CommandLineHandler(args);
    } catch (ParseException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  /** @return the suite of tests being tested */
  public static Test suite() {
    return new TestSuite(DOMUtilsTest.class);
  }

  /** get value happy case */
  public void testGetValue() {
    String result = "";
    String xml =
        "EXAMPLE";
    String xpath = "EXAMPLE";

    logger.debug("xml=" + xml);
    DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
    DocumentBuilder documentBuilder = null;
    Document document = null;
    try {
      documentBuilder = documentBuilderFactory.newDocumentBuilder();
      document = documentBuilder.parse(new InputSource(new StringReader(xml)));
    } catch (ParserConfigurationException | IOException e) {
      logger.error("Exception{}", e);
    } catch (SAXException e) {
      logger.error("", e);
    }
    try {
      result = DOMUtil.getNodeValue(document, xpath);
    } catch (DOMUtilException e) {
      // TODO Auto-generated catch block
      logger.error("", e);
    }
    logger.debug("result=" + result);
    assertTrue(result != null);
  }
  /** set value happy case */
  public void testSetValue() {
    String xml =
        "EXAMPLE";
    String xpath = "EXAMPLE";

    logger.debug("xml=" + xml);

    String xml2 = null;
    try {
      xml2 = DOMUtil.setNodeValue(xml, xpath, "!!!");
    } catch (DOMUtilException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    logger.debug("xml2=" + xml2);
    assertTrue(!xml.equals(xml2));
  }

  public void testSetSecurityToken() {
    String xml =
        "EXAMPLE";
    logger.debug("xml=" + xml);
    String securityToken = "xxx";
    try {
      String xml2 = DOMUtil.setNodeValue(xml, "EXAMPLE", securityToken);
      logger.debug("xml2=" + xml2);
    } catch (DOMUtilException e) {
      e.printStackTrace();
    }
  }

  public void testGetSecurityToken() {
    String xml =
        "EXAMPLE";
    String securityToken = "xxx";
    try {
      String xml2 = DOMUtil.getNodeValue(xml, "EXAMPLE");
      logger.debug("xml2=" + xml2);
    } catch (DOMUtilException e) {
      e.printStackTrace();
    }
  }
}
