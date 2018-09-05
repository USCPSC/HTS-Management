package com.ttw.itds.ui.shared.dto.email;

import com.ttw.itds.ui.shared.dto.ItdsUiBaseDto;

import java.util.List;

/**
 * Created by dlauber on 6/1/16.
 */
public class SendNonExisMapEmailDto extends ItdsUiBaseDto {
    private List<String> listEmailAddresses;
    private List<CBPEntryDataDto> listNonExisMapEmailDto;

    public SendNonExisMapEmailDto() {}

    public SendNonExisMapEmailDto(List<String> listEmailAddresses, List<CBPEntryDataDto> listNonExisMapEmailDto) {
        this.listEmailAddresses = listEmailAddresses;
        this.listNonExisMapEmailDto = listNonExisMapEmailDto;
    }

    public List<String> getListEmailAddresses() {
        return listEmailAddresses;
    }

    public void setListEmailAddresses(List<String> listEmailAddresses) {
        this.listEmailAddresses = listEmailAddresses;
    }

    public List<CBPEntryDataDto> getListNonExisMapEmailDto() {
        return listNonExisMapEmailDto;
    }

    public void setListNonExisMapEmailDto(List<CBPEntryDataDto> listNonExisMapEmailDto) {
        this.listNonExisMapEmailDto = listNonExisMapEmailDto;
    }

    @Override
    public String toString() {
        return "SendNonExisMapEmailDto{" + "listEmailAddresses=" + listEmailAddresses + ", listNonExisMapEmailDto=" + listNonExisMapEmailDto + '}';
    }
}

