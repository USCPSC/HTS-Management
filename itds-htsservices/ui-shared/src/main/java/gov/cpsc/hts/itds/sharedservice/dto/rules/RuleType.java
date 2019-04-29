package gov.cpsc.hts.itds.sharedservice.dto.rules;


import java.io.Serializable;

/**
 * @author ztang
 * generated at : Jan 28, 2016 - 10:01:37 AM
 */
public class RuleType implements Serializable{
	private static final long serialVersionUID = -6950345114988040667L;
	
	public RuleType() {
		super();
	}
	
	private String categoryName;
	public String getCategoryName() {	return categoryName;	}
	public void setCategoryName(String categoryName) {	this.categoryName = categoryName;	}
		
	private String name;
	public String getName() {	return name;	}
	public void setName(String name) {	this.name = name;	}

	private String displayName;
	public String getDisplayName() {return displayName;	}
	public void setDisplayName(String displayName) {this.displayName = displayName;	}
	
	private String description;
	public String getDescription() {return description;	}
	public void setDescription(String description) {	this.description = description;	}
	
	private String ruleDataDisplayHeaders;

	public String getRuleDataDisplayHeaders() {
		return ruleDataDisplayHeaders;
	}
	public void setRuleDataDisplayHeaders(String ruleDataDisplayHeaders) {
		this.ruleDataDisplayHeaders = ruleDataDisplayHeaders;
	}
	
	
}
