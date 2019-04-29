
package gov.cpsc.hts.itds.ui.shared.codec;


import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;

/**
 * @author zlt633
 * @generated at : Feb 9, 2016 - 11:22:37 PM
 * @since: 1.0.0
 */
public class Base64Codec {
	
	
	private static final String Charset_Name= "UTF-8";

	/**
	 * 
	 * @param plainText
	 * @return base64 encoded text for input
	 */
	public static final String encode(String plainText) {
		if( plainText == null || plainText.length()<1){
			throw new IllegalArgumentException("Text to be encoded must NOt null or empty");
		}
		Encoder encoder = Base64.getUrlEncoder();
		try {
			byte[] bytes = plainText.getBytes(Charset_Name);
			return new String(encoder.encode(bytes), Charset_Name);
		} catch (UnsupportedEncodingException e) {
			//Should not be here, not UTF-8? then where are we
			throw new RuntimeException(e);
		}
	}
	
	
	public static final String decode(String base64Text) {
		if( base64Text == null || base64Text.length()<1){
			throw new IllegalArgumentException("Text to be decoded must NOt null or empty");
		}
		Decoder decoder = Base64.getUrlDecoder();
		try {
			byte[] bytes = base64Text.getBytes(Charset_Name);
			return new String(decoder.decode(bytes), Charset_Name);
		} catch (UnsupportedEncodingException e) {
			//Should not be here, not UTF-8? then where are we
			throw new RuntimeException(e);
		}
	}

}
