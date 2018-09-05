package com.ttw.itds.ui.shared.dto.inbox;

import java.io.IOException;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.Assert;
import org.junit.Test;

import com.ttw.itds.ui.shared.codec.JsonCodec;

public class TradePartiesTest {

	public TradePartiesTest() {
		super();
	}

	public TradeParties createSampleTradeParties() {
		TradeParties dto = new TradeParties();

		dto.setImporterId(100L);
		dto.setImporterNumber("a");
		dto.setImporterName("b");
		dto.setImporterAddressLine1("c");
		dto.setImporterAddressLine2("d");
		dto.setImporterCity("e");
		dto.setImporterStateCode("f");
		dto.setImporterZipCode("g");
		dto.setManufacturerId(110L); 
		dto.setMid("h");
		dto.setManufacturerName("j");
		dto.setManufacturerAddress("k");;
		dto.setManufacturerCity("l");
//		dto.setFilerCode(120L);
		dto.setFilerPointOfContact("m");
		dto.setFilerPhoneOfContact("n");
		dto.setFilerAddressLine1("o");
		dto.setFilerAddressLine2("p");
		dto.setFilerCity("q");
		dto.setFilerStateCode("r");
		dto.setFilerZipCode("s");
		dto.setConsigneeId(130);;
		dto.setConsigneeName("t");
		dto.setEin("u");
		dto.setConsigneeNumber("v");
		dto.setConsigneeAddressLine1("x");
		dto.setConsigneeAddressLine2("y");
		dto.setConsigneeCity("z");
		dto.setConsigneeStateCode("aa");
		dto.setConsigneeZipCode("bb");
		dto.setManufacturerCountry("cc");
		
		return dto;
	}
	
	@Test
	public void encode_TradeParties() throws IOException {
		TradeParties dto = createSampleTradeParties();
		String jsonText = JsonCodec.marshal(dto);

		Assert.assertNotNull(jsonText);
		
		Assert.assertTrue(jsonText.contains("c"));
		Assert.assertTrue(jsonText.contains("h"));
//		Assert.assertTrue(jsonText.contains("120"));
		Assert.assertTrue(jsonText.contains("p"));
		Assert.assertTrue(jsonText.contains("r"));
		Assert.assertTrue(jsonText.contains("y"));
		Assert.assertTrue(jsonText.contains("cc"));
	}

	@Test
	public void decode_TradeParties() throws IOException {
		TradeParties dto1 = createSampleTradeParties();
		String jsonText = JsonCodec.marshal(dto1);
		
		TradeParties dto2 = JsonCodec.unmarshal(TradeParties.class, jsonText);
		Assert.assertTrue(EqualsBuilder.reflectionEquals(dto1, dto2));
	}

}
