package gov.cpsc.itds.common.properties;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalTime;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gov.cpsc.itds.common.cli.CommandLineHandler;

public class PropertiesHandler {

	private PropertiesHandler() {
		throw new IllegalStateException("PropertiesHandler class");
	}

	static Properties properties = new Properties();
	private static final Logger logger = LoggerFactory.getLogger(PropertiesHandler.class);
	private static boolean initialized=false;
	public static String PROPERTIES_FILE_NAME ="Itds.properties";

	private static void initialize(){
		if (initialized){
			return;
		}
		initialized=true;
		logger.debug("in static initializer");
		InputStream input = null;
		//first, try to get the properties file name from the command line parameters
		String fileName = CommandLineHandler.PROPERTIES_FILE_LOCATION;
		logger.debug("fileName="+fileName);
		//second, try to get the properties file name from the wildfly system property
		if (fileName==null) {
			String conf = System.getProperty("ItdsUi.config");
			fileName = conf + "/" + PROPERTIES_FILE_NAME;
		}
		fileName=handleProfiles(fileName);
		try(FileInputStream fis = new FileInputStream(fileName)) {
			logger.debug("about to load properties file");
			properties.load(fis);
			logger.debug("finished loading properties file");
		} catch (IOException ex) {
			logger.error("error opening properties file, exiting", ex);
			System.exit(1);
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					logger.error("error closing properties file, exiting", e);
					System.exit(1);
				}
			}
		}
	}
	
	public static String[] getArray(Enum key) {
		return getArray(key, ",");
	}
	
	public static String[] getArray(Enum key, String delimiter) {
		return getArray(""+key, delimiter);
	}
	
	public static String[] getArray(String key) {
		return getArray(""+key, ",");
	}
	
	public static String[] getArray(String key, String delimiter) {
		String[] result=null;
		if (getProperty(key)!=null){
			result=getProperty(key).split(delimiter);
		}
		return result;
	}
	

	public static String getProperty(Enum key) {
		return getProperty(""+key);
	}
	
	public static String getProperty(String key) {
		initialize();
		logger.debug("property name= {} --", key);
		String result = properties.getProperty(key);

		logger.debug("property value= {}", result);
		return result;
	}

	public static Long getLongProperty(Enum key) {
		return getLongProperty(""+key);
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
		return getIntProperty(""+key);
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
		return getBooleanProperty(""+key);
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
  this method defaults to no profile if no profile is specified in the JMV argument -Dprofile=
   */
	private static String handleProfiles(String fileName){
		String profile=System.getProperty("profile");
		if (profile == null) {
			return fileName;
		}
		String result=fileName.replace(".properties", "-"+System.getProperty("profile")+".properties");
		return result;
	}

}
