package gov.cpsc.itds.common.properties;

import gov.cpsc.itds.common.StringUtils;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalTime;
import java.util.Properties;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gov.cpsc.itds.common.cli.CommandLineHandler;

public class PropertiesHandler {

	private PropertiesHandler() {
		throw new IllegalStateException("PropertiesHandler class");
	}

	static Properties properties = new Properties();
	private static final Logger logger = LoggerFactory.getLogger(PropertiesHandler.class);
	private static boolean initialized = false;
	public static String PROPERTIES_FILE_NAME = "Itds.properties";
	private static String fileNameEquals ="fileName={}";

	private static void initialize() {
		if (initialized) {
			return;
		}
		initialized = true;
		logger.debug("in initialize");
		// first, try to get the properties file name from the command line parameters
		String fileName = CommandLineHandler.PROPERTIES_FILE_LOCATION;
		logger.debug("checking from application argument -p");
		logger.debug(fileNameEquals, fileName);
		// second, try to get the properties file name from the wildfly system property
		if (fileName == null) {
			logger.debug("checking wildfly system property");
			String conf = System.getProperty("ItdsUi.config");
			logger.debug("System.getProperty(\"ItdsUi.Config\")={}", System.getProperty("ItdsUi.Config"));
			fileName = conf + "/" + PROPERTIES_FILE_NAME;
			logger.debug(fileNameEquals, fileName);
		}
		fileName = handleProfiles(fileName);
		// first try to get properties from the file system
		logger.debug("trying file system");
		loadFromFileSystem(fileName);
		// if can't, then try to get it from the jar file
		if (properties.isEmpty()) {
			logger.debug("trying jar file");
			loadFromJar(fileName);
		}
		if (properties.isEmpty()) {
			logger.debug("properties is empty");
			System.exit(1);
		}
	}

	private static void loadFromJar(String fileName) {
		logger.debug(fileNameEquals, fileName);
		try (InputStream is = PropertiesHandler.class.getResourceAsStream(fileName))
		{
			if (is!=null){
				loadFromInputStream(is);
			}
		} catch (IOException ex) {
			logger.debug("properties file not found in jar file");
		}
	}

	private static void loadFromInputStream(InputStream is) throws IOException{
		try(InputStreamReader isr = new InputStreamReader(is);
				BufferedReader br = new BufferedReader(isr)) {
			logger.debug("about to load properties file from jar file");
			properties.load(is);
			logger.debug("finished loading properties file");
		} catch (IOException e){
			logger.debug("properties file not found in jar file");
			throw new IOException(e);
		}
	}

	private static void loadFromFileSystem(String fileName) {
		try (FileInputStream fis = new FileInputStream(fileName)) {
			logger.debug("about to load properties file");
			properties.load(fis);
			logger.debug("finished loading properties file");
		} catch (IOException ex) {
			logger.debug("properties file not found on file system");
		}

	}

	public static String[] getArray(Enum key) {
		return getArray(key, ",");
	}

	public static String[] getArray(Enum key, String delimiter) {
		return getArray("" + key, delimiter);
	}

	public static String[] getArray(String key) {
		return getArray("" + key, ",");
	}

	public static String[] getArray(String key, String delimiter) {
		String[] result = null;
		String value=getProperty(key);
		if (value != null) {
			result = value.split(delimiter);
		}
		return result;
	}

	public static String getProperty(Enum key) {
		return getProperty("" + key);
	}

	//returns null if property is not found
	public static String getProperty(String key) {
		initialize();
		logger.debug("property name= {} --", key);
		String result = properties.getProperty(key);
		if (result != null) {
			Pattern pattern = Pattern.compile("\\{\\{(.*?)\\}\\}");
			Matcher matchPattern = pattern.matcher(result);
			String macroVariable = null;
			while (matchPattern.find()) {
				macroVariable = matchPattern.group(1);
			}
			// if there are {} call recursively, this time with the macro expression as the key
			if (!StringUtils.isEmptyTrim(macroVariable)) {
				// checking a second time, because we actually want double curly brackets
				String macroValue = getProperty(macroVariable);
				result = result.replace("{{" + macroVariable + "}}", macroValue);
			}
		}
		logger.debug("property value= {}", result);
		return result;
	}

	public static Long getLongProperty(Enum key) {
		return getLongProperty("" + key);
	}

	/**
	 * @deprecated (use getLongProperty instead)
	 */
	@Deprecated
	public static Long getPropertyLong(String key) {
		return getLongProperty(key);
	}

	public static long getLongProperty(String key) {
		logger.debug("Converting the property to long key--{}", key);
		return Long.parseLong(getProperty(key));
	}

	public static int getIntProperty(Enum key) {
		return getIntProperty("" + key);
	}

	public static int getIntProperty(String key) {
		int result = -1;
		try {
			result = Integer.parseInt(getProperty(key));
		} catch (NumberFormatException nfe) {
			logger.error("error parsing int property, returning -1, exiting", nfe);
			System.exit(1);
		}
		return result;
	}

	public static boolean getBooleanProperty(Enum key) {
		return getBooleanProperty("" + key);
	}

	public static boolean getBooleanProperty(String key) {
		boolean result = true;
		try {
			result = Boolean.parseBoolean(getProperty(key));
		} catch (NumberFormatException nfe) {
			logger.error("error parsing int property, returning -1, exiting", nfe);
			System.exit(1);
		}
		return result;
	}

	/*
	 * This expects properties formatted as hh:mm in 24 hour time
	 */
	public static LocalTime getLocalTimeProperty(String key) {
		LocalTime result = null;
		String strValue = getProperty(key);
		if (strValue != null) {
			result = LocalTime.parse(strValue);
		}
		return result;
	}

	/*
	 * this method defaults to no profile if no profile is specified in the JMV
	 * argument -Dprofile=
	 */
	private static String handleProfiles(String fileName) {
		logger.debug("in handleProfile()");
		logger.debug(fileNameEquals, fileName);
		String profile = System.getProperty("profile");
		logger.debug("profile={}", profile);
		if (profile == null) {
			return fileName;
		}
		String result = fileName.replace(".properties", "-" + System.getProperty("profile") + ".properties");
		logger.debug("result={}", result);
		return result;
	}

}
