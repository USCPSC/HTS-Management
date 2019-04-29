package gov.cpsc.hts.itds.ui.shared.dto.email;

import java.util.List;

import gov.cpsc.hts.itds.ui.shared.dto.examlog.ExamProductSample;

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
