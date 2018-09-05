package com.ttw.itds.ui.shared.dto.inbox;

import java.io.IOException;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.Assert;
import org.junit.Test;

import com.ttw.itds.ui.shared.codec.JsonCodec;

public class EntrySummaryTest {

	public EntrySummary createSampleEntrySummary() {
		EntrySummary dto = new EntrySummary();
	
		// entry
		dto.setEntryId(100L);
		dto.setEntryDate("a");
		dto.setEntryNumber("b");
		dto.setEntryValue(99.99D);
		dto.setNumberOfLines(3);
		dto.setBol("c");
		dto.setArrivalDateEstimate("d");
		dto.setArrivalDateActual("e");
		dto.setLocationOfGoods("f");
		dto.setCarrierId(105L);
		dto.setCarrierName("g");
		dto.setMot("h");
		dto.setVessel("i");
		dto.setForeignPortId(110L);
		dto.setForeignPortCode("j"); 
		dto.setForeignPortName("k");
		dto.setPortId(120L);
		dto.setPortCode("l"); 
		dto.setPortName("m");
		dto.setUnladingPortId(130L);
		dto.setUnladingPortCode("n"); 
		dto.setUnladingPortName("o");
		dto.setFtz("p");
		dto.setOgc("q");
		dto.setImporterId(140L);
//		dto.setFilerCode(150L);
		
		return dto;
	}
	
	@Test
	public void encode_EntrySummary() throws IOException {
		EntrySummary dto = createSampleEntrySummary();
		String jsonText = JsonCodec.marshal(dto);

		Assert.assertNotNull(jsonText);
		
		Assert.assertTrue(jsonText.contains("b"));
		Assert.assertTrue(jsonText.contains("99.99"));
		Assert.assertTrue(jsonText.contains("k"));
//		Assert.assertTrue(jsonText.contains("150"));
		Assert.assertTrue(jsonText.contains("n"));
	}

	@Test
	public void decode_EntrySummary() throws IOException {
		EntrySummary dto1 = createSampleEntrySummary();
		String jsonText = JsonCodec.marshal(dto1);
		
		EntrySummary dto2 = JsonCodec.unmarshal(EntrySummary.class, jsonText);
		Assert.assertTrue(EqualsBuilder.reflectionEquals(dto1, dto2));
	}

}
