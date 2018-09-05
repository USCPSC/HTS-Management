package com.ttw.itds.ui.shared.dto.email;

import com.ttw.itds.ui.shared.dto.ItdsUiBaseDto;
import com.ttw.itds.ui.shared.dto.inbox.EntryLine;

import java.util.List;

/**
 * Created by dlauber on 5/26/16.
 */
public class SendWorkflowEntryEmailDto extends ItdsUiBaseDto {

    private List<String> listEmailAddresses;
    private EntryLine entryLine;
    private CBPEntryDataDto cbpEntry;

    public SendWorkflowEntryEmailDto() {
    }

    public SendWorkflowEntryEmailDto(List<String> listEmailAddresses, EntryLine entryLine) {
        this.listEmailAddresses = listEmailAddresses;
        this.entryLine = entryLine;
    }

    public List<String> getListEmailAddresses() {
        return listEmailAddresses;
    }

    public void setListEmailAddresses(List<String> listEmailAddresses) {
        this.listEmailAddresses = listEmailAddresses;
    }

    public EntryLine getEntryLine() { return entryLine; }

    public void setEntryLine(EntryLine entryLine) { this.entryLine = entryLine; }

    public CBPEntryDataDto getCbpEntry() { return cbpEntry; }

    public void setCbpEntry(CBPEntryDataDto cbpEntry) { this.cbpEntry = cbpEntry; }

    @Override
    public String toString() {
        return "SendWorkflowEntryEmailDto{" + "listEmailAddresses=" + listEmailAddresses + ", entryLine=" + entryLine + '}';
    }
}
