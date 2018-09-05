package com.ttw.itds.sharedservice.dto;

import java.io.IOException;
import java.util.Date;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.junit.Assert;
import org.junit.Test;

import com.ttw.itds.sharedservice.dto.audit.AuditRecord;
import com.ttw.itds.sharedservice.dto.audit.AuditEnums.EventActionEnum;
import com.ttw.itds.sharedservice.dto.audit.AuditEnums.NetworkAccessPointEnum;
import com.ttw.itds.ui.shared.codec.JsonCodec;
import com.ttw.itds.ui.shared.codec.UtcDateTimeCodec;

/**
 * @author zlt633
 * @generated at : Dec 30, 2015 - 10:02:26 PM
 * @since: 1.0.0
 */
public class AuditRecordTest {

	public AuditRecordTest() {
		super();
	}
	
	public AuditRecord createSampleAuditRecord(){
		AuditRecord dto = new AuditRecord();
		dto.setGeneratorAppName("ITDS-UI");
		dto.setGeneratorAppVersion("1.0.0");
		
		dto.setUserId("jsmith");
		dto.setEventAction(EventActionEnum.Read);
		dto.setEventTimeStamp( UtcDateTimeCodec.encode( new Date() ) );
		
		
		dto.setNetworkAccessPointEnum(NetworkAccessPointEnum.IP_ADDRESS);
		dto.setNetworkAccessAddress("127.0.0.1");	
		dto.setQueryContextPath("/user/doFoo");
		dto.setQueryParameter("key1=foo;key2=bazz");
		
		return dto;
	}
	
	@Test
	public void encode_AuditRecord() throws IOException{
		AuditRecord dto = createSampleAuditRecord();
		String jsonText = JsonCodec.marshal(dto);
		System.out.println(jsonText);
		Assert.assertNotNull(jsonText);
		
		Assert.assertTrue(jsonText.contains("ITDS-UI"));
		Assert.assertTrue(jsonText.contains(EventActionEnum.Read.toString()));
		Assert.assertTrue(jsonText.contains("127.0.0.1"));
		
		Assert.assertTrue(jsonText.contains("doFoo"));
		Assert.assertTrue(jsonText.contains("key2=bazz"));
	}
	
	@Test
	public void decode_AuditRecord(){
		AuditRecord dto = createSampleAuditRecord();
		String jsonText = JsonCodec.marshal(dto);
		
		AuditRecord dto2 = JsonCodec.unmarshal(AuditRecord.class, jsonText);
		
		Assert.assertEquals(dto.getGeneratorAppName(), dto2.getGeneratorAppName());
		Assert.assertTrue(EqualsBuilder.reflectionEquals(dto, dto2));
	}

}
