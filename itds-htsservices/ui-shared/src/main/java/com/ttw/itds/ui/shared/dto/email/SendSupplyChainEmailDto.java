package com.ttw.itds.ui.shared.dto.email;

import com.ttw.itds.ui.shared.dto.ItdsUiBaseDto;
import java.util.List;

/**
 *
 * @author rzauel
 */
public class SendSupplyChainEmailDto extends ItdsUiBaseDto {

    private static final long serialVersionUID = 1662287778079397306L;
    
    
    private List<String> listEmailAddresses;
    private List<SupplyChainEmailDataDto> listSupplyChainEmailDataDto;

    public SendSupplyChainEmailDto() {
    }

    public SendSupplyChainEmailDto(List<String> listEmailAddresses, List<SupplyChainEmailDataDto> listSupplyChainEmailDataDto) {
        this.listEmailAddresses = listEmailAddresses;
        this.listSupplyChainEmailDataDto = listSupplyChainEmailDataDto;
    }

    public List<String> getListEmailAddresses() {
        return listEmailAddresses;
    }

    public void setListEmailAddresses(List<String> listEmailAddresses) {
        this.listEmailAddresses = listEmailAddresses;
    }

    public List<SupplyChainEmailDataDto> getListSupplyChainEmailDataDto() {
        return listSupplyChainEmailDataDto;
    }

    public void setListSupplyChainEmailDataDto(List<SupplyChainEmailDataDto> listSupplyChainEmailDataDto) {
        this.listSupplyChainEmailDataDto = listSupplyChainEmailDataDto;
    }

    @Override
    public String toString() {
        return "SendSupplyChainEmailDto{" + "listEmailAddresses=" + listEmailAddresses + ", listSupplyChainEmailDataDto=" + listSupplyChainEmailDataDto + '}';
    }

}
