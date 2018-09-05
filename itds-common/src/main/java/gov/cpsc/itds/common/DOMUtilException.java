package gov.cpsc.itds.common;

public class DOMUtilException extends Exception {

  public DOMUtilException(String xpath) {
    super(xpath);
  }

  public DOMUtilException(Exception e) {
    super(e);
  }
}
