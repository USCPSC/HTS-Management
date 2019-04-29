package gov.cpsc.hts.itds.ui.shared.codec;

import java.io.IOException;

import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.joda.time.format.DateTimeFormat;

/**
 * Custom Jackson deserializer for displaying Joda DateTime objects.
 */
public class CustomDateTimeDeserializer extends JsonDeserializer<DateTime> {

    @Override
    public DateTime deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException {
        JsonToken t = jp.getCurrentToken();
        if (t == JsonToken.VALUE_STRING) {
            String str = jp.getText().trim();
            return ISODateTimeFormat.dateTimeParser().parseDateTime(str);
        }
        if (t == JsonToken.VALUE_NUMBER_INT) {
            return new DateTime(jp.getLongValue());
        }
        throw ctxt.mappingException(handledType());
    }
    
    public static DateTime readIsoString(String inputString) throws Exception {
        String str = inputString.trim();
        DateTime dateTime = ISODateTimeFormat.dateTimeParser().parseDateTime(str);
        return dateTime;
    }
    
    public static DateTime readDateTimeStringPattern1(String inputString) throws Exception {
        String str = inputString.trim();
        DateTime dateTime;

        if (str.contains("/"))
            dateTime = DateTimeFormat.forPattern("MM/dd/yy HH:mm").parseDateTime(str);
        else {
            try {
                dateTime = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm").parseDateTime(str);
            } catch (IllegalArgumentException e) {
                dateTime = DateTimeFormat.forPattern("MM-dd-yyyy HH:mm").parseDateTime(str);
            }
        }
        return dateTime;
    }

}
