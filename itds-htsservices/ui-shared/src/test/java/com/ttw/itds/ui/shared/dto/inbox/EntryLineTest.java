package com.ttw.itds.ui.shared.dto.inbox;

import java.io.IOException;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.Assert;
import org.junit.Test;

import com.ttw.itds.ui.shared.codec.JsonCodec;

/**
 * @author cpan
 */
public class EntryLineTest {

	public EntryLineTest() {
		super();
	}

	public EntryLine createSampleEntryLine() {
		EntryLine dto = new EntryLine();

		dto.setEntryNumber("a");
		dto.setEntryLineNumber("b");
		dto.setHtsNumber("c");
		dto.setImporterName("e");
		dto.setManufacturerName("g");
//		dto.setFilerName("i");
		dto.setPortName("k");
		dto.setReleaseDate("l");
		dto.setArrivalDate("m");
		dto.setRamScore(1001);
		dto.setNationalOp("o");
		dto.setInvestigatorFirstName("q");
		dto.setInvestigatorLastName("r");
		dto.setWorkflowStatus("s");
//		dto.setEntryId(100L);
//		dto.setHtsId(110L);
//		dto.setImporterId(120L);
//		dto.setManufacturerId(130L);
//		dto.setFilerId(140L);
//		dto.setPortId(150L);
		dto.setPortCode("aaa");
//		dto.setInvestigatorId(160L);
//		dto.setExamId(170L);
		dto.setHasExam(true);
		
		return dto;
	}

	@Test
	public void encode_EntryLine() throws IOException {
		EntryLine dto = createSampleEntryLine();
		String jsonText = JsonCodec.marshal(dto);

		Assert.assertNotNull(jsonText);
		
		Assert.assertTrue(jsonText.contains("c"));
		Assert.assertTrue(jsonText.contains("k"));
//		Assert.assertTrue(jsonText.contains("170"));
		Assert.assertTrue(jsonText.contains("aaa"));
	}

	@Test
	public void decode_EntryLine() throws IOException {
		EntryLine dto1 = createSampleEntryLine();
		String jsonText = JsonCodec.marshal(dto1);
		
		EntryLine dto2 = JsonCodec.unmarshal(EntryLine.class, jsonText);
		Assert.assertTrue(EqualsBuilder.reflectionEquals(dto1, dto2));
	}
}
