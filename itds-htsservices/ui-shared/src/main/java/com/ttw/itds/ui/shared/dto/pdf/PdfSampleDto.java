package com.ttw.itds.ui.shared.dto.pdf;

import com.ttw.itds.ui.shared.dto.ItdsUiBaseDto;

/**
 *
 */
public class PdfSampleDto extends ItdsUiBaseDto {

    private static final long serialVersionUID = 1L;

    private String sampleNo;
    private String itemModel;
    private String productDescription;
    private String reason;

    public String getSampleNo() {
        return sampleNo;
    }

    public void setSampleNo(String sampleNo) {
        this.sampleNo = sampleNo;
    }

    public String getItemModel() {
        return itemModel;
    }

    public void setItemModel(String itemModel) {
        this.itemModel = itemModel;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

}
