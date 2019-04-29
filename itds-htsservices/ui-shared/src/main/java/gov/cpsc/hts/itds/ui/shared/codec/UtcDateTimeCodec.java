package gov.cpsc.hts.itds.ui.shared.codec;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * @author zlt633
 * @generated at : Apr 17, 2016 - 12:10:30 AM
 * @since: 1.0.0
 */
public class UtcDateTimeCodec {

	private static final String UTC_Pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'";
	
	public static String encode(Date date){
		if( date == null ){
			throw new IllegalArgumentException("Date object cannot be NULL");
		}
		DateTimeFormatter formatter = DateTimeFormat.forPattern(UTC_Pattern);
		return formatter.withZoneUTC().print(date.getTime());		
	}
	
	
	public static String encode(long milisecond){
		DateTimeFormatter formatter = DateTimeFormat.forPattern(UTC_Pattern);
		return formatter.withZoneUTC().print(milisecond);		
	}
	
	
	public static Date decode(String utcText){
		if( StringUtils.isEmpty(utcText) ){
			throw new IllegalArgumentException("UTC datetime string cannot be NULL or EMPTY");
		}
		DateTimeFormatter formatter = DateTimeFormat.forPattern(UTC_Pattern);
		DateTime dateTime =  formatter.withZoneUTC().parseDateTime(utcText);
		return new Date(dateTime.getMillis());
	}
	

}
