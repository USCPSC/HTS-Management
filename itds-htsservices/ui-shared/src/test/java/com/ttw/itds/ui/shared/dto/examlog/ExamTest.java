package com.ttw.itds.ui.shared.dto.examlog;

import java.io.IOException;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.Assert;
import org.junit.Test;

import com.ttw.itds.ui.shared.codec.JsonCodec;

/**
 * @author hzhao
 * @since: 1.0.0
 */
public class ExamTest {

	public ExamTest() {
		super();
	}

	public Exam createSampleExam() {
		Exam dto = new Exam();

		dto.setExamId("111");
		dto.setExamDate("222");
		dto.setInvestigator("333");
		dto.setImporterName("444");
		dto.setImporterNumber("555");
		dto.setEntryNumber("666");
		dto.setCountryOfOrigin("777");
		dto.setPortName("888");
		dto.setSampleNumber("999");
		dto.setNumOfProductsScreened("aaa");
		dto.setProductDescription("bbb");
		dto.setReasonsForScreening("ccc");
		dto.setLead("ddd");
		dto.setCadmium("eee");
		dto.setFtirPhthalates("fff");
		dto.setCbpActionRequested("ggg");
		dto.setDispositionDate("hhh");
		dto.setRemarks("iii");
		dto.setActivityTime("jjj");
		dto.setTravelTime("kkk");
		dto.setDeclinedIndicator("lll");
		dto.setModelItemNumber("mmm");
		dto.setLabelMissingIncomplete("nnn");
		dto.setBrokerImporterNotified("ooo");

		return dto;
	}

	@Test
	public void encode_Exam() throws IOException {
		Exam dto = createSampleExam();
		String jsonText = JsonCodec.marshal(dto);

		Assert.assertNotNull(jsonText);

		Assert.assertTrue(jsonText.contains("examId"));
		Assert.assertTrue(jsonText.contains("111"));
	}

	@Test
	public void decode_Exam() throws IOException {
		Exam dto = createSampleExam();
		String jsonText = JsonCodec.marshal(dto);

		Exam dto2 = JsonCodec.unmarshal(Exam.class, jsonText);

		Assert.assertTrue(EqualsBuilder.reflectionEquals(dto, dto2));
	}
}
