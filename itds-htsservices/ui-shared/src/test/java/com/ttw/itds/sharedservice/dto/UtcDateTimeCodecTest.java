package com.ttw.itds.sharedservice.dto;

import java.util.Date;

import org.junit.Assert;
import org.junit.Test;

import com.ttw.itds.ui.shared.codec.UtcDateTimeCodec;

/**
 * @author zlt633
 * @generated at : Apr 17, 2016 - 12:30:00 AM
 * @since: 1.0.0
 */
public class UtcDateTimeCodecTest {

	public UtcDateTimeCodecTest() {
		
	}
	
	@Test
	public void encode_Date(){
		Date date = new Date(1990-1900, 6, 11);
		String utcText = UtcDateTimeCodec.encode(date);
		Assert.assertNotNull(utcText);
		Assert.assertTrue("1990-07-11T04:00:00Z".equals(utcText));
	}
	
	@Test
	public void decode_Date(){
		Date date = new Date(1990-1900, 6, 11);
		String utcText = "1990-07-11T04:00:00Z";
		
		Date date2 = UtcDateTimeCodec.decode(utcText);
		Assert.assertNotNull(date2);
		Assert.assertTrue(date.getTime() == date2.getTime() );

	}
	
	
	@Test
	public void encode_Long(){
		Date date = new Date(1990-1900, 6, 11);
		String utcText = UtcDateTimeCodec.encode(date.getTime());
		System.out.println(utcText);
		Assert.assertNotNull(utcText);
		Assert.assertTrue("1990-07-11T04:00:00Z".equals(utcText));
	}

}
