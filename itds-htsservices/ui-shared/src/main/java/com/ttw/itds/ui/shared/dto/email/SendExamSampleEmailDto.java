package com.ttw.itds.ui.shared.dto.email;

import com.ttw.itds.ui.shared.dto.examlog.Exam;
import com.ttw.itds.ui.shared.dto.examlog.ExamProductSample;
import com.ttw.itds.ui.shared.dto.inbox.EntryLine;

import java.util.List;

/**
 * Created by dlauber on 8/18/16.
 */
public class SendExamSampleEmailDto {
    private List<String> listEmailAddresses;
    private String examId;
    private List<ExamProductSample> examProductSamples;
    private String emailHeader;

    public SendExamSampleEmailDto () {}

    public List<String> getListEmailAddresses() {
        return listEmailAddresses;
    }

    public void setListEmailAddresses(List<String> listEmailAddresses) {
        this.listEmailAddresses = listEmailAddresses;
    }

    public String getExamId() {
        return examId;
    }

    public void setExamId(String examId) {
        this.examId = examId;
    }

    public List<ExamProductSample> getExamProductSamples() {
        return examProductSamples;
    }

    public void setExamProductSamples(List<ExamProductSample> examProductSamples) {
        this.examProductSamples = examProductSamples;
    }

    public String getEmailHeader() {
        return emailHeader;
    }

    public void setEmailHeader(String emailHeader) {
        this.emailHeader = emailHeader;
    }

}
