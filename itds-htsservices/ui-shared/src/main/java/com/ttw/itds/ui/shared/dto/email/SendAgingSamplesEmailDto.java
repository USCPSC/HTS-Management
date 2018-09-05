package com.ttw.itds.ui.shared.dto.email;

import com.ttw.itds.ui.shared.dto.examlog.ExamProductSample;

import java.util.List;

/**
 * Created by dlauber on 6/6/16.
 */
public class SendAgingSamplesEmailDto {
    private List<String> listEmailAddresses;
    private List<ExamProductSample> agingSamples;

    public SendAgingSamplesEmailDto () {}

    public List<String> getListEmailAddresses() {
        return listEmailAddresses;
    }

    public void setListEmailAddresses(List<String> listEmailAddresses) {
        this.listEmailAddresses = listEmailAddresses;
    }

    public List<ExamProductSample> getAgingSamples() {
        return agingSamples;
    }

    public void setAgingSamples(List<ExamProductSample> agingSamples) {
        this.agingSamples = agingSamples;
    }

    @Override
    public String toString() {
        return "SendAgingSamplesEmailDto{" + "listEmailAddresses=" + listEmailAddresses + ", agingSamples=" + agingSamples + '}';
    }
}
