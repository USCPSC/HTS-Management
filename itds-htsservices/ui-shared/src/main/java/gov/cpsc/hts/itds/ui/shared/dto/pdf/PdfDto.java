package gov.cpsc.hts.itds.ui.shared.dto.pdf;

import java.util.ArrayList;
import java.util.List;

import gov.cpsc.hts.itds.ui.shared.dto.ItdsUiBaseDto;

/**
 *
 */
public class PdfDto extends ItdsUiBaseDto {

    private static final long serialVersionUID = 1L;

    private String actionRequested;
    private String addlActionRequested;
    private String cbpFax;
    private String collectionDate;
    private String commentsToCbp;
    private String consigneeAddress;
    private String consigneeName;
    private String countryOfOrigin;
    private String customsBroker;
    private String customsBrokerAddress;
    private String customsBrokerEmail;
    private String customsBrokerPhone;
    private String distributionCC;
    private String entryDate;
    private String entryNumber;
    private String examinationSite;
    private String firmAddress;
    private String firmName;
    private String formDate;
    private String formType;
    private String fromAddress;
    private String fromOffice;
    private String htsNumbers;
    private String importerAddress;
    private String importerEmail;
    private String importerName;
    private String importerNumber;
    private String individualName;
    private String individualTitle;
    private String investigator;
    private String investigatorEmail;
    private String investigatorPhone;
    private String investigatorTitle;
    private String invoiceDate;
    private String invoiceNum;
    private String manufacturerNumbers;
    private String modelItems;
    private String officerEmail;
    private String officerFax;
    private String officerName;
    private String officerPhoneNumber;
    private String officerTitle;
    private String portOfEntry;
    private String products;
    private String programOperation;
    private String samples;
    private String statute;
    private String storageLocation;
    private String toAttn;
    private String toPort;

    private List<PdfSampleDto> listPdfSampleDto = new ArrayList<>();

	public List<PdfSampleDto> getListPdfSampleDto() {
        return listPdfSampleDto;
    }

    public void setListPdfSampleDto(List<PdfSampleDto> listPdfSampleDto) {
        this.listPdfSampleDto = listPdfSampleDto;
    }

    public String getFormType() {
        return formType;
    }

    public void setFormType(String formType) {
        this.formType = formType;
    }

    public String getFromAddress() {
        return fromAddress;
    }

    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }

    public String getImporterName() {
        return importerName;
    }

    public void setImporterName(String importerName) {
        this.importerName = importerName;
    }

    public String getImporterAddress() {
        return importerAddress;
    }

    public void setImporterAddress(String importerAddress) {
        this.importerAddress = importerAddress;
    }

    public String getOfficerName() {
        return officerName;
    }

    public void setOfficerName(String officerName) {
        this.officerName = officerName;
    }

    public String getIndividualName() {
        return individualName;
    }

    public void setIndividualName(String individualName) {
        this.individualName = individualName;
    }

    public String getIndividualTitle() {
        return individualTitle;
    }

    public void setIndividualTitle(String individualTitle) {
        this.individualTitle = individualTitle;
    }

    public String getFirmName() {
        return firmName;
    }

    public void setFirmName(String firmName) {
        this.firmName = firmName;
    }

    public String getFirmAddress() {
        return firmAddress;
    }

    public void setFirmAddress(String firmAddress) {
        this.firmAddress = firmAddress;
    }

    public String getEntryNumber() {
        return entryNumber;
    }

    public void setEntryNumber(String entryNumber) {
        this.entryNumber = entryNumber;
    }

    public String getActionRequested() {
        return actionRequested;
    }

    public void setActionRequested(String actionRequested) {
        this.actionRequested = actionRequested;
    }

    public String getAddlActionRequested() {
        return addlActionRequested;
    }

    public void setAddlActionRequested(String addlActionRequested) {
        this.addlActionRequested = addlActionRequested;
    }

    public String getToPort() {
        return toPort;
    }

    public void setToPort(String toPort) {
        this.toPort = toPort;
    }

    public String getToAttn() {
        return toAttn;
    }

    public void setToAttn(String toAttn) {
        this.toAttn = toAttn;
    }

    public String getFromOffice() {
        return fromOffice;
    }

    public void setFromOffice(String fromOffice) {
        this.fromOffice = fromOffice;
    }

    public String getProducts() {
        return products;
    }

    public void setProducts(String products) {
        this.products = products;
    }

    public String getSamples() {
        return samples;
    }

    public void setSamples(String samples) {
        this.samples = samples;
    }

    public String getModelItems() {
        return modelItems;
    }

    public void setModelItems(String modelItems) {
        this.modelItems = modelItems;
    }

    public String getImporterNumber() {
        return importerNumber;
    }

    public void setImporterNumber(String importerNumber) {
        this.importerNumber = importerNumber;
    }

    public String getInvestigator() {
        return investigator;
    }

    public void setInvestigator(String investigator) {
        this.investigator = investigator;
    }

    public String getInvestigatorTitle() {
        return investigatorTitle;
    }

    public void setInvestigatorTitle(String investigatorTitle) {
        this.investigatorTitle = investigatorTitle;
    }

    public String getInvestigatorPhone() {
        return investigatorPhone;
    }

    public void setInvestigatorPhone(String investigatorPhone) {
        this.investigatorPhone = investigatorPhone;
    }

    public String getInvestigatorEmail() {
        return investigatorEmail;
    }

    public void setInvestigatorEmail(String investigatorEmail) {
        this.investigatorEmail = investigatorEmail;
    }

    public String getConsigneeName() {
        return consigneeName;
    }

    public void setConsigneeName(String consigneeName) {
        this.consigneeName = consigneeName;
    }

    public String getConsigneeAddress() {
        return consigneeAddress;
    }

    public void setConsigneeAddress(String consigneeAddress) {
        this.consigneeAddress = consigneeAddress;
    }

    public String getInvoiceNum() {
        return invoiceNum;
    }

    public void setInvoiceNum(String invoiceNum) {
        this.invoiceNum = invoiceNum;
    }

    public String getPortOfEntry() {
        return portOfEntry;
    }

    public void setPortOfEntry(String portOfEntry) {
        this.portOfEntry = portOfEntry;
    }

    public String getExaminationSite() {
        return examinationSite;
    }

    public void setExaminationSite(String examinationSite) {
        this.examinationSite = examinationSite;
    }

    public String getStorageLocation() {
        return storageLocation;
    }

    public void setStorageLocation(String storageLocation) {
        this.storageLocation = storageLocation;
    }

    public String getProgramOperation() {
        return programOperation;
    }

    public void setProgramOperation(String programOperation) {
        this.programOperation = programOperation;
    }

    public String getCustomsBroker() {
        return customsBroker;
    }

    public void setCustomsBroker(String customsBroker) {
        this.customsBroker = customsBroker;
    }

    public String getCustomsBrokerAddress() {
        return customsBrokerAddress;
    }

    public void setCustomsBrokerAddress(String customsBrokerAddress) {
        this.customsBrokerAddress = customsBrokerAddress;
    }

    public String getCountryOfOrigin() {
        return countryOfOrigin;
    }

    public void setCountryOfOrigin(String countryOfOrigin) {
        this.countryOfOrigin = countryOfOrigin;
    }

    public String getStatute() {
        return statute;
    }

    public void setStatute(String statute) {
        this.statute = statute;
    }

    public String getCommentsToCbp() {
        return commentsToCbp;
    }

    public void setCommentsToCbp(String commentsToCbp) {
        this.commentsToCbp = commentsToCbp;
    }

    public String getDistributionCC() {
        return distributionCC;
    }

    public void setDistributionCC(String distributionCC) {
        this.distributionCC = distributionCC;
    }

    public String getOfficerTitle() {
        return officerTitle;
    }

    public void setOfficerTitle(String officerTitle) {
        this.officerTitle = officerTitle;
    }

    public String getOfficerPhoneNumber() {
        return officerPhoneNumber;
    }

    public void setOfficerPhoneNumber(String officerPhoneNumber) {
        this.officerPhoneNumber = officerPhoneNumber;
    }

    public String getOfficerFax() {
        return officerFax;
    }

    public void setOfficerFax(String officerFax) {
        this.officerFax = officerFax;
    }

    public String getOfficerEmail() {
        return officerEmail;
    }

    public void setOfficerEmail(String officerEmail) {
        this.officerEmail = officerEmail;
    }

    public String getManufacturerNumbers() {
        return manufacturerNumbers;
    }

    public void setManufacturerNumbers(String manufacturerNumbers) {
        this.manufacturerNumbers = manufacturerNumbers;
    }

    public String getHtsNumbers() {
        return htsNumbers;
    }

    public void setHtsNumbers(String htsNumbers) {
        this.htsNumbers = htsNumbers;
    }

    public String getImporterEmail() {
        return importerEmail;
    }

    public void setImporterEmail(String importerEmail) {
        this.importerEmail = importerEmail;
    }

    public String getCustomsBrokerPhone() {
        return customsBrokerPhone;
    }

    public void setCustomsBrokerPhone(String customsBrokerPhone) {
        this.customsBrokerPhone = customsBrokerPhone;
    }

    public String getCustomsBrokerEmail() {
        return customsBrokerEmail;
    }

    public void setCustomsBrokerEmail(String customsBrokerEmail) {
        this.customsBrokerEmail = customsBrokerEmail;
    }

    public String getCbpFax() {
        return cbpFax;
    }

    public void setCbpFax(String cbpFax) {
        this.cbpFax = cbpFax;
    }

	public String getFormDate() {
		return formDate;
	}

	public void setFormDate(String formDate) {
		this.formDate = formDate;
	}

	public String getEntryDate() {
		return entryDate;
	}

	public void setEntryDate(String entryDate) {
		this.entryDate = entryDate;
	}

	public String getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(String invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public String getCollectionDate() {
		return collectionDate;
	}

	public void setCollectionDate(String collectionDate) {
		this.collectionDate = collectionDate;
	}
    
}
