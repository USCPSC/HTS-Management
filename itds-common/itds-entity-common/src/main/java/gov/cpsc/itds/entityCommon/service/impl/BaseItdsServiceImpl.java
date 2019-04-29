package gov.cpsc.itds.entityCommon.service.impl;

import gov.cpsc.itds.entityCommon.shared.ApplicationConstants;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.supercsv.io.CsvListReader;
import org.supercsv.io.CsvListWriter;
import org.supercsv.io.ICsvListReader;
import org.supercsv.io.ICsvListWriter;
import org.supercsv.prefs.CsvPreference;

@PropertySource({"${"+ BaseItdsServiceImpl.SystemArgName + "}/itds-ui-appinfo.properties" })

public class BaseItdsServiceImpl {
	public static final String SystemArgName = "ItdsUi.Config";
	@Autowired 
	public Environment env;

	private static final Logger logger = LoggerFactory.getLogger(BaseItdsServiceImpl.class);

	public static final String emptyString = "";
	
	// utils for data conversion
	private static final SimpleDateFormat formatter = new SimpleDateFormat(ApplicationConstants.DateFormats.mmddyyyy);
	
	public static boolean isCreateFlag(String flag) { return "c".equalsIgnoreCase(flag)||"cc".equalsIgnoreCase(flag); }
	public static boolean isUpdateFlag(String flag) { return "u".equalsIgnoreCase(flag)||"uc".equalsIgnoreCase(flag); }
	public static boolean isDeleteFlag(String flag) { return "d".equalsIgnoreCase(flag)||"dc".equalsIgnoreCase(flag); }
	public static boolean isCreateReadingFlag(String flag) { return "cc".equalsIgnoreCase(flag); }
	public static boolean isUpdateReadingFlag(String flag) { return "uc".equalsIgnoreCase(flag); }
	public static boolean isDeleteReadingFlag(String flag) { return "cd".equalsIgnoreCase(flag)||"ud".equalsIgnoreCase(flag); }
	public static String toString(String val) {return (val == null)?"":val;}
	public static String toString(Integer val) {return (val == null)?"":val.toString();}
	public static String toString(Long val) {return (val == null)?"":val.toString();}
	public static String toString(Float val) {return (val == null)?"":val.toString();}
	public static String toString(Double val) {return (val == null)?"":val.toString();}
	public static String toString(Date val) { return (val == null)?"":formatter.format(val);}
	public static String toString(Date val, String format) {
		return (val == null)?"":DateFormatUtils.format(val, format);
		}
	public static String toString(Object val) {return (val == null)?"":val.toString();}
	public static Long toLong(String val) {return (val == null)?null:Long.parseLong(val);}
	public static Date toDate(String val) throws ParseException {return (val == null)?null:formatter.parse(val);}

	public static boolean isEmpty(Object val) {return toString(val).trim().length() ==0; }

	public static String toAddress(String line1, String line2, String city, String state, String zip) {
		StringBuilder sb = new StringBuilder();
		if(StringUtils.isNotBlank(line1)) {
			sb.append(line1).append("\n");
		}
		if(StringUtils.isNotBlank(line2)) {
			sb.append(line2).append("\n");
		}
		if(StringUtils.isNotBlank(city)) {
			sb.append(city).append(", ");
		}
		if(StringUtils.isNotBlank(state)) {
			sb.append(state);
		}
		if(StringUtils.isNotBlank(zip)) {
			sb.append(" ").append(zip);
		}
		return sb.toString();
	}
	
	public static String escapeSingleQuote(Object val) {
		if(val == null)
		{
			return "";
			
		}
		return toString(val).replaceAll("'", "''");
	}

	public String[] parseCsvString(String csvValue) {
		// split the csv value into list of strings
		ICsvListReader listReader = null;
		List<String> listRtn = new ArrayList<>();
		try {
			listReader = new CsvListReader(new StringReader(csvValue), CsvPreference.STANDARD_PREFERENCE);
			listRtn = listReader.read();

		} catch (Exception e) {
			logger.error("error parsing csv value {" + csvValue + "}. Error message:" + e.getMessage());
		} finally {
			if (listReader != null) {
				try {
					listReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
		String[] rtnArr = new String[listRtn.size()];
		rtnArr = listRtn.toArray(rtnArr);
		return rtnArr ;
	}

	public String toCsvString(String[] strArr) {
		// convert to csv value from list of strings
		if(strArr == null) {
			return "";
		}
		
		ICsvListWriter csvWriter = null;
		StringWriter strWriter = new StringWriter();
		String rtnString = null;
		try {
			csvWriter = new CsvListWriter(strWriter, CsvPreference.STANDARD_PREFERENCE);
			csvWriter.write(Arrays.asList(strArr));
			rtnString = strWriter.toString();
		} catch (Exception e) {
			logger.error("error converting to csv value {" + strArr + "}. Error message:" + e.getMessage());
		} finally {
			if (csvWriter != null) {
				try {
					csvWriter.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
		return rtnString;
	}
}
