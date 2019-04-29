package gov.cpsc.hts.itds.ui.shared.codec;

import java.io.IOException;
import java.io.StringReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;

/**
 * @author zlt633
 * @generated at : Dec 12, 2015 - 10:34:42 AM
 * @since: 1.0.0
 */

@JsonSerialize
public class JsonCodec {
	
	private static final Logger logger = LoggerFactory.getLogger(JsonCodec.class);

    private static ObjectMapper jsonMapper;
    public static ObjectMapper getJSonMapper() {
        if( jsonMapper == null ){
            jsonMapper = new ObjectMapper();
            AnnotationIntrospector annoIntrospector = new JacksonAnnotationIntrospector();        
            jsonMapper.setAnnotationIntrospector(annoIntrospector);
            
            jsonMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);        
            jsonMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
            jsonMapper.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, true);
            jsonMapper.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, false);        
            jsonMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
            
            // 1/8/2016 commented out to exclude java class name in JSON output
            //jsonMapper.enableDefaultTyping(DefaultTyping.JAVA_LANG_OBJECT);
        }
        return jsonMapper;
    }
   
    
    public static String marshal(Object dto) {
        if( dto == null ){
            throw new IllegalArgumentException("Object to be marshalled must not be null");
        }
        try {
			return getJSonMapper().writeValueAsString(dto);
		} catch (JsonProcessingException e) {
			logger.error(e.getMessage(), e);
		}
        return null;
    }    
    
        
    public static <T> T unmarshal(Class<T> clazz, byte[] jsonPayload ){
        if( jsonPayload == null || jsonPayload.length < 1){
            throw new IllegalArgumentException("JSon payload to be unmarshalled must not be empty");
        }
        try {
			return getJSonMapper().readValue(jsonPayload, clazz);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
        return null;
    }
    
    public static <T> T unmarshal(Class<T> clazz, String jsonPayload ){
        if( jsonPayload == null || jsonPayload.length() < 1){
            throw new IllegalArgumentException("JSon payload to be unmarshalled must not be empty");
        }
        StringReader reader = new StringReader(jsonPayload);
        try {
			return getJSonMapper().readValue(reader, clazz);
		} catch (IOException e) {
			logger.debug(e.getMessage(), e);
			//logger.error(e.getMessage(), e);
		}
        return null;
    }
     
    

}
