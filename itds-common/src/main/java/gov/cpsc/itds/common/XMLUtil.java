package gov.cpsc.itds.common;

import java.io.StringReader;
import java.io.StringWriter;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.ErrorListener;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class XMLUtil implements ErrorListener, ErrorHandler {
  private static final Logger logger = LoggerFactory.getLogger(DOMUtil.class);

  public static String formatXml(String xml){
    String result="";
    try {
      result=new XMLUtil().prettyPrint(xml);
    } catch (ParserConfigurationException e) {
      //e.printStackTrace();
    } catch (SAXException e) {
      //e.printStackTrace();
    }
    return result;
  }
  /*
  this returns the empty string if there is an error
   */
  private String prettyPrint(String xml) throws ParserConfigurationException, SAXException {
    String result="";

    SAXParserFactory parserFactory = SAXParserFactory.newInstance();
    SAXParser parser = parserFactory.newSAXParser();
    parser.getXMLReader().setErrorHandler(this);
    SAXSource xmlInput = new SAXSource(parser.getXMLReader(), new InputSource(new StringReader(xml)));

    StringWriter stringWriter = new StringWriter();
    StreamResult xmlOutput = new StreamResult(stringWriter);
    TransformerFactory transformerFactory = TransformerFactory.newInstance();
    transformerFactory.setAttribute("indent-number", 4);

    try {
      Transformer transformer = transformerFactory.newTransformer();
      transformer.setErrorListener(this);
      transformer.setOutputProperty(OutputKeys.INDENT, "yes");
      transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
      transformer.transform(xmlInput, xmlOutput);
      result=xmlOutput.getWriter().toString();
    } catch (Exception ex) {
      //this method swallows the exception
    }

    return result;
  }


  @Override
  public void warning(TransformerException exception) throws TransformerException {
    //throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public void error(TransformerException exception) throws TransformerException {
    //throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public void fatalError(TransformerException exception) throws TransformerException {
    //throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public void warning(SAXParseException exception) throws SAXException {
    // Do nothing
  }

  @Override
  public void error(SAXParseException exception) throws SAXException {
    // Do nothing
  }

  @Override
  public void fatalError(SAXParseException exception) throws SAXException {
    // Rethrow the exception
    throw exception;
  }

}