package com.ttw.itds.sharedservice.dto.rules;

import com.ttw.itds.ui.shared.dto.ItdsUiBaseDto;

/**
 * @author hzhao
 * national operation instance
 */
public class NationalOp extends ItdsUiBaseDto {
	private static final long serialVersionUID = -2598432533684068247L;
	
	private String id;
	private String code;
	private String name;
	private String category;
	private String description;
	private String actionMessage;
	private String icon;
	private String percentage;
	private String permanentRuleFlag;
	private String effectiveFromDate;
	private String effectiveToDate;
	private String productValue;
	private String productValueOperator;
	private String score;
	private String status;
	private String filterHts;
	private String filterImporter;
	private String filterConsignee;
	private String filterManufacturer;
	private String filterPortEntry;
	private String filterCountryOrigin;
	private String filterMot;
	private String filterEntryType;
	private String refCtpat;
	private String refIsaps;
	private String includeExcludeFlagHts;
	private String includeExcludeFlagImporter;
	private String includeExcludeFlagConsignee;
	private String includeExcludeFlagManufacturer;
	private String includeExcludeFlagPortEntry;
	private String includeExcludeFlagCountryOrigin;
	private String includeExcludeFlagMot;
	private String includeExcludeFlagEntryType;
	private String includeExcludeFlagCtpat;
	private String includeExcludeFlagIsaps;
	private String iconfilename;
	private String defaultWorkflowAction;
	private String productValueHigh;
	private String priority;
	private String atsRule;
	private String parentCode;

	private String createUserId;
	private String createTimestamp;
	private String lastUpdateUserId;
	private String lastUpdateTimestamp;
	
	
	/*
	 * Added 9/15/2016 to fit into RAM 2.0 DB design
	 */
	private String categoryName;
	private RuleType ruleType;
	private String ruleDataJson;
	/*
	 * End of addition
	 */

	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getId() {
		return id;
	}
	public String getCode() {
		return code;
	}
	public String getName() {
		return name;
	}
	public String getCategory() {
		return category;
	}
	public String getDescription() {
		return description;
	}
	public String getActionMessage() {
		return actionMessage;
	}
	public String getIcon() {
		return icon;
	}
	public String getPercentage() {
		return percentage;
	}
	public String getPermanentRuleFlag() {
		return permanentRuleFlag;
	}
	public String getEffectiveFromDate() {
		return effectiveFromDate;
	}
	public String getEffectiveToDate() {
		return effectiveToDate;
	}
	public String getProductValue() {
		return productValue;
	}
	public String getProductValueOperator() {
		return productValueOperator;
	}
	public String getScore() {
		return score;
	}
	public String getStatus() {
		return status;
	}
	public String getFilterHts() {
		return filterHts;
	}
	public String getFilterImporter() {
		return filterImporter;
	}
	public String getFilterConsignee() {
		return filterConsignee;
	}
	public String getFilterManufacturer() {
		return filterManufacturer;
	}
	public String getFilterPortEntry() {
		return filterPortEntry;
	}
	public String getFilterCountryOrigin() {
		return filterCountryOrigin;
	}
	public String getFilterMot() {
		return filterMot;
	}
	public String getFilterEntryType() {
		return filterEntryType;
	}
	public String getRefCtpat() {
		return refCtpat;
	}
	public String getRefIsaps() {
		return refIsaps;
	}
	public String getIncludeExcludeFlagHts() {
		return includeExcludeFlagHts;
	}
	public String getIncludeExcludeFlagImporter() {
		return includeExcludeFlagImporter;
	}
	public String getIncludeExcludeFlagConsignee() {
		return includeExcludeFlagConsignee;
	}
	public String getIncludeExcludeFlagManufacturer() {
		return includeExcludeFlagManufacturer;
	}
	public String getIncludeExcludeFlagPortEntry() {
		return includeExcludeFlagPortEntry;
	}
	public String getIncludeExcludeFlagCountryOrigin() {
		return includeExcludeFlagCountryOrigin;
	}
	public String getIncludeExcludeFlagMot() {
		return includeExcludeFlagMot;
	}
	public String getIncludeExcludeFlagEntryType() {
		return includeExcludeFlagEntryType;
	}
	public String getIncludeExcludeFlagCtpat() {
		return includeExcludeFlagCtpat;
	}
	public String getIncludeExcludeFlagIsaps() {
		return includeExcludeFlagIsaps;
	}
	public String getIconfilename() {
		return iconfilename;
	}
	public String getDefaultWorkflowAction() {
		return defaultWorkflowAction;
	}
	public String getProductValueHigh() {
		return productValueHigh;
	}
	public String getPriority() {
		return priority;
	}
	public String getAtsRule() {
		return atsRule;
	}
	public String getParentCode() {
		return parentCode;
	}
	public String getCreateUserId() {
		return createUserId;
	}
	public String getCreateTimestamp() {
		return createTimestamp;
	}
	public String getLastUpdateUserId() {
		return lastUpdateUserId;
	}
	public String getLastUpdateTimestamp() {
		return lastUpdateTimestamp;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public void setActionMessage(String actionMessage) {
		this.actionMessage = actionMessage;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public void setPercentage(String percentage) {
		this.percentage = percentage;
	}
	public void setPermanentRuleFlag(String permanentRuleFlag) {
		this.permanentRuleFlag = permanentRuleFlag;
	}
	public void setEffectiveFromDate(String effectiveFromDate) {
		this.effectiveFromDate = effectiveFromDate;
	}
	public void setEffectiveToDate(String effectiveToDate) {
		this.effectiveToDate = effectiveToDate;
	}
	public void setProductValue(String productValue) {
		this.productValue = productValue;
	}
	public void setProductValueOperator(String productValueOperator) {
		this.productValueOperator = productValueOperator;
	}
	public void setScore(String score) {
		this.score = score;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public void setFilterHts(String filterHts) {
		this.filterHts = filterHts;
	}
	public void setFilterImporter(String filterImporter) {
		this.filterImporter = filterImporter;
	}
	public void setFilterConsignee(String filterConsignee) {
		this.filterConsignee = filterConsignee;
	}
	public void setFilterManufacturer(String filterManufacturer) {
		this.filterManufacturer = filterManufacturer;
	}
	public void setFilterPortEntry(String filterPortEntry) {
		this.filterPortEntry = filterPortEntry;
	}
	public void setFilterCountryOrigin(String filterCountryOrigin) {
		this.filterCountryOrigin = filterCountryOrigin;
	}
	public void setFilterMot(String filterMot) {
		this.filterMot = filterMot;
	}
	public void setFilterEntryType(String filterEntryType) {
		this.filterEntryType = filterEntryType;
	}
	public void setRefCtpat(String refCtpat) {
		this.refCtpat = refCtpat;
	}
	public void setRefIsaps(String refIsaps) {
		this.refIsaps = refIsaps;
	}
	public void setIncludeExcludeFlagHts(String includeExcludeFlagHts) {
		this.includeExcludeFlagHts = includeExcludeFlagHts;
	}
	public void setIncludeExcludeFlagImporter(String includeExcludeFlagImporter) {
		this.includeExcludeFlagImporter = includeExcludeFlagImporter;
	}
	public void setIncludeExcludeFlagConsignee(String includeExcludeFlagConsignee) {
		this.includeExcludeFlagConsignee = includeExcludeFlagConsignee;
	}
	public void setIncludeExcludeFlagManufacturer(String includeExcludeFlagManufacturer) {
		this.includeExcludeFlagManufacturer = includeExcludeFlagManufacturer;
	}
	public void setIncludeExcludeFlagPortEntry(String includeExcludeFlagPortEntry) {
		this.includeExcludeFlagPortEntry = includeExcludeFlagPortEntry;
	}
	public void setIncludeExcludeFlagCountryOrigin(String includeExcludeFlagCountryOrigin) {
		this.includeExcludeFlagCountryOrigin = includeExcludeFlagCountryOrigin;
	}
	public void setIncludeExcludeFlagMot(String includeExcludeFlagMot) {
		this.includeExcludeFlagMot = includeExcludeFlagMot;
	}
	public void setIncludeExcludeFlagEntryType(String includeExcludeFlagEntryType) {
		this.includeExcludeFlagEntryType = includeExcludeFlagEntryType;
	}
	public void setIncludeExcludeFlagCtpat(String includeExcludeFlagCtpat) {
		this.includeExcludeFlagCtpat = includeExcludeFlagCtpat;
	}
	public void setIncludeExcludeFlagIsaps(String includeExcludeFlagIsaps) {
		this.includeExcludeFlagIsaps = includeExcludeFlagIsaps;
	}
	public void setIconfilename(String iconfilename) {
		this.iconfilename = iconfilename;
	}
	public void setDefaultWorkflowAction(String defaultWorkflowAction) {
		this.defaultWorkflowAction = defaultWorkflowAction;
	}
	public void setProductValueHigh(String productValueHigh) {
		this.productValueHigh = productValueHigh;
	}
	public void setPriority(String priority) {
		this.priority = priority;
	}
	public void setAtsRule(String atsRule) {
		this.atsRule = atsRule;
	}
	public void setParentCode(String parentCode) {
		this.parentCode = parentCode;
	}
	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}
	public void setCreateTimestamp(String createTimestamp) {
		this.createTimestamp = createTimestamp;
	}
	public void setLastUpdateUserId(String lastUpdateUserId) {
		this.lastUpdateUserId = lastUpdateUserId;
	}
	public void setLastUpdateTimestamp(String lastUpdateTimestamp) {
		this.lastUpdateTimestamp = lastUpdateTimestamp;
	}

	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public RuleType getRuleType() {
		return ruleType;
	}
	public void setRuleType(RuleType ruleType) {
		this.ruleType = ruleType;
	}
	public String getRuleDataJson() {
		return ruleDataJson;
	}
	public void setRuleDataJson(String ruleDataJson) {
		this.ruleDataJson = ruleDataJson;
	}
}
