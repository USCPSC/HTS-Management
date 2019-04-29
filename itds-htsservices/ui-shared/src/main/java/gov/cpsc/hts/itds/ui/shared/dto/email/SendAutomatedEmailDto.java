package gov.cpsc.hts.itds.ui.shared.dto.email;

import java.util.List;

import gov.cpsc.hts.itds.ui.shared.dto.inbox.EntryLine;

/**
 * Created by dlauber on 6/6/16.
 */
public class SendAutomatedEmailDto {
    private List<String> listEmailAddresses;
    private List<EntryLine> inboxEntryLines;
    private String emailHeader;

    public SendAutomatedEmailDto () {}

    public List<String> getListEmailAddresses() {
        return listEmailAddresses;
    }

    public void setListEmailAddresses(List<String> listEmailAddresses) {
        this.listEmailAddresses = listEmailAddresses;
    }

    public List<EntryLine> getInboxEntryLines() {
        return inboxEntryLines;
    }

    public void setInboxEntryLines(List<EntryLine> inboxEntryLines) {
        this.inboxEntryLines = inboxEntryLines;
    }

    public String getEmailHeader() {
        return emailHeader;
    }

    public void setEmailHeader(String emailHeader) {
        this.emailHeader = emailHeader;
    }

    @Override
    public String toString() {
        return "SendAutomatedEmailDto{" + "listEmailAddresses=" + listEmailAddresses + ", inboxEntryLines=" + inboxEntryLines + '}';
    }
}
