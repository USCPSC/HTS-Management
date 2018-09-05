package gov.cpsc.itds.common.cli;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommandLineHandler {
  private static final Logger logger = LoggerFactory.getLogger(CommandLineHandler.class);
  public static String PROPERTIES_FILE_LOCATION = null;

  public CommandLineHandler(String[] args) throws ParseException {
    CommandLineParser parser = new DefaultParser();
    Options options = new Options();
    options.addOption("p", true, "properties file location");
    CommandLine cmd = null;
    cmd = parser.parse(options, args);
    if (cmd.hasOption("p")) {
      PROPERTIES_FILE_LOCATION = cmd.getOptionValue("p");
      logger.debug("PROPERTIES_FILE_LOCATION={}", PROPERTIES_FILE_LOCATION);
    }
  }
}
