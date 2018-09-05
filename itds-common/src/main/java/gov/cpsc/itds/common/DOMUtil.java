package gov.cpsc.itds.common;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class DOMUtil {
  private static final Logger logger = LoggerFactory.getLogger(DOMUtil.class);

  private DOMUtil() {
    throw new IllegalStateException("DOMUtil");
  }

  public static String getNodeValue(Document document, String xpath, int maxLength)
      throws DOMUtilException {
    String result = null;
    result = getNodeValue(document, xpath);
    if (result != null) {
      result = result.substring(0, maxLength);
    }
    return result;
  }

  public static String getNodeValue(Document document, String xpath) throws DOMUtilException {
    String result = null;
    Element element = null;
    try {
      element = (Element) document.getElementsByTagName(xpath).item(0);
      if (element != null) {
        result = element.getTextContent();
      }
    } catch (Exception e) {
      throw new DOMUtilException(e);
    }
    return result;
  }

  public static Date getDate(Document document, String xpath) throws DOMUtilException {
    Date result = null;
    String sDate = getNodeValue(document, xpath);
    try {
      result = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss-XXX").parse(sDate);
    } catch (ParseException e) {
      logger.error("Exception getting date", e);
      throw new DOMUtilException(e);
    }
    return result;
  }

  public static void setNodeValue(Document document, String xpath, String value)
      throws DOMUtilException {
    Element element = null;
    try {
      element = (Element) document.getElementsByTagName(xpath).item(0);
    } catch (Exception e) {
      logger.error("", e);
      throw new DOMUtilException(xpath);
    }
    if (element == null) {
      logger.error("element=null");
      throw new DOMUtilException(xpath);
    }
    element.setTextContent(value);
  }

  public static String getNodeValue(String xml, String nodeName) throws DOMUtilException {
    String result = null;
    Document document = createDocument(xml);
    if (document != null) {
      result = DOMUtil.getNodeValue(document, nodeName);
    }
    return result;
  }

  public static String setNodeValue(String xml, String nodeName, String value)
      throws DOMUtilException {
    String result = null;
    Document document = createDocument(xml);
    if (document != null) {
      DOMUtil.setNodeValue(document, nodeName, value);
      result = DOMUtil.convertToString(document);
    }
    return result;
  }

  public static Document createDocument(String xml) {
    DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
    DocumentBuilder documentBuilder = null;
    Document document = null;
    try {
      documentBuilder = documentBuilderFactory.newDocumentBuilder();
      document = documentBuilder.parse(new InputSource(new StringReader(xml)));
    } catch (ParserConfigurationException | IOException | SAXException e) {
      logger.error("Exception{}", e);
    }
    return document;
  }

  public static String convertToString(Document doc) throws DOMUtilException {
    StringWriter sw = new StringWriter();
    try {
      TransformerFactory tf = TransformerFactory.newInstance();
      tf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
      Transformer transformer;
      transformer = tf.newTransformer();
      transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
      transformer.setOutputProperty(OutputKeys.METHOD, "xml");
      transformer.setOutputProperty(OutputKeys.INDENT, "yes");
      transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
      transformer.transform(new DOMSource(doc), new StreamResult(sw));
    } catch (TransformerException e) {
      logger.error("error in convertToString()", e);
      throw new DOMUtilException(e);
    }
    return sw.toString();
  }
}
