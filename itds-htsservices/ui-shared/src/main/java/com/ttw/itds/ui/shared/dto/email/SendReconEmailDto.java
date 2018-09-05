package com.ttw.itds.ui.shared.dto.email;

import com.ttw.itds.ui.shared.dto.ItdsUiBaseDto;

import java.util.List;

/**
 * Created by dlauber on 5/27/16.
 */
public class SendReconEmailDto extends ItdsUiBaseDto {
    private List<String> listEmailAddresses;
    private List<ReconRowDto> reconRows;

    public SendReconEmailDto() {}

    public SendReconEmailDto(List<String> listEmailAddresses, List<ReconRowDto> reconRows) {
        this.listEmailAddresses = listEmailAddresses;
        this.reconRows = reconRows;
    }

    public List<String> getListEmailAddresses() {
        return listEmailAddresses;
    }

    public void setListEmailAddresses(List<String> listEmailAddresses) {
            this.listEmailAddresses = listEmailAddresses;
    }

    public List<ReconRowDto> getReconRows() {
        return reconRows;
    }

    public void setReconRows(List<ReconRowDto> reconRows) {
        this.reconRows = reconRows;
    }

    @Override
    public String toString() {
        return "SendReconEmailDto{" + "listEmailAddresses=" + listEmailAddresses + ", reconRows=" + reconRows + '}';
    }
}
