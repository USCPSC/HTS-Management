package gov.cpsc.itds.common.logging;

import java.io.PrintStream;
import org.slf4j.Logger;

public class LoggingProxy {

  public static void setLoggingProxy(Logger logger){
    System.setOut(LoggingProxy.createLoggingDebugProxy(System.out, logger));
    System.setErr(LoggingProxy.createLoggingErrorProxy(System.err, logger));
  }

  private static PrintStream createLoggingDebugProxy(final PrintStream realPrintStream, Logger logger) {
    return new PrintStream(realPrintStream) {
      public void print(final String string) {
        logger.debug(string);
      }
      public void println(final String o){
        logger.debug(o.toString());
      }
    };
  }
  private static PrintStream createLoggingErrorProxy(final PrintStream realPrintStream, Logger logger) {
    return new PrintStream(realPrintStream) {
      public void print(final String string) {
        logger.error(string);
      }
      public void println(final String o){ logger.error(o.toString());
      }
    };
  }
}
