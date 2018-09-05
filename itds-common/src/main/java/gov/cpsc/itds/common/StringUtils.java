package gov.cpsc.itds.common;

public class StringUtils {

  private StringUtils() {
    throw new IllegalStateException("StringUtils");
  }

  public static boolean isEmptyTrim(String string) {
    return string == null || string.trim().isEmpty();
  }
}
