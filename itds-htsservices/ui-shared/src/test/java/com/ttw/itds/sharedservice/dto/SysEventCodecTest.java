package com.ttw.itds.sharedservice.dto;

import java.util.Date;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Assert;
import org.junit.Test;

import com.ttw.itds.ui.shared.codec.JsonCodec;
import com.ttw.itds.ui.shared.codec.UtcDateTimeCodec;

/**
 * @author zlt633
 * @generated at : Apr 16, 2016 - 9:23:01 PM
 * @since: 1.0.0
 */
public class SysEventCodecTest {

	public SysEventCodecTest() {}
	

    
	
	private SysEvent createSampleSysEvent(){
		SysEvent dto = new SysEvent("ITDS-UI", "1.0.0");
		dto.setUserIdentifier("myname@foo.org");
		
		dto.setAction("Update");
		
		dto.setEventName("updateWorkflowStatus");
		dto.setEventDescription("update workflow status from X to Y");
		dto.setEventTimeStamp( UtcDateTimeCodec.encode( new Date() ) );
		
		return dto;
	}
	
	@Test
	public void encode_SysEvent(){
		SysEvent dto = createSampleSysEvent();
		String jsonText = JsonCodec.marshal(dto);
		System.out.println(jsonText);
		
		Assert.assertNotNull(jsonText);
		Assert.assertTrue(jsonText.contains("ITDS-UI"));
	}
	
	@Test
	public void decode_SysEvent(){
		SysEvent dto = createSampleSysEvent();
		String jsonText = JsonCodec.marshal(dto);
		System.out.println(jsonText);
		
		Assert.assertNotNull(jsonText);
		Assert.assertTrue(jsonText.contains("ITDS-UI"));
		
		SysEvent dto2 = JsonCodec.unmarshal(SysEvent.class, jsonText);
		Assert.assertNotNull(dto2);
		
		Assert.assertTrue(dto != dto2);
	}

}
