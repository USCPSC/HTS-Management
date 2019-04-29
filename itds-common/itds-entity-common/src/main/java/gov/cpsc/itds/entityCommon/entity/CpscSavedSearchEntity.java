package gov.cpsc.itds.entityCommon.entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

//import org.hibernate.annotations.Type;

/**
 * @author cpan
 */
@Entity
@Table(name = "CPSC_SAVED_SEARCH")
public class CpscSavedSearchEntity extends BaseItdsEntity{
	
	private static final long serialVersionUID = 546847423700180370L;

	public CpscSavedSearchEntity() {
		super();
	}
	
	@Id
	@Column(name="ID")
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="USERNAME")
	private String username;
	
	@Column(name="SEARCH_NAME")
	private String searchName;
	
	@Column(name="DISPLAY_ORDER")
	private Integer displayOrder;
	
	@Column(name="SEARCH_DESC")
	private String searchDesc;
	
	@Column(name="SEARCH_AVAILABILITY")
	private String searchAvailability;
	
	@Column(name="SEARCH_TARGET")
	private String searchTarget;
	
	@Column(name="SEARCH_DEFINITION")
	private String searchCriteriaDefinition;
	
	@Column(name="SORT_DESC")
	private String sortDesc;
	
	@Column(name="SORT_DEFINITION")
	private String sortDefinition;

	@Column(name="STATUS")
	private String status;
	
	@Column(name="CREATE_TIMESTAMP")
	private Date createTimestamp;
	
	@Column(name="CREATE_USER_ID")
	private String createUserId;
	
	@Column(name="LAST_UPDATE_TIMESTAMP")
	private Date lastUpdateTimestamp;
	
	@Column(name="LAST_UPDATE_USER_ID")
	private String lastUpdateUserId;
	
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getSearchName() {
		return searchName;
	}

	public void setSearchName(String searchName) {
		this.searchName = searchName;
	}

	public Integer getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(Integer displayOrder) {
		this.displayOrder = displayOrder;
	}

	public String getSearchDesc() {
		return searchDesc;
	}

	public void setSearchDesc(String searchDesc) {
		this.searchDesc = searchDesc;
	}

	public String getSearchAvailability() {
		return searchAvailability;
	}

	public void setSearchAvailability(String searchAvailability) {
		this.searchAvailability = searchAvailability;
	}

	public String getSearchTarget() {
		return searchTarget;
	}

	public void setSearchTarget(String searchTarget) {
		this.searchTarget = searchTarget;
	}

	public String getSearchCriteriaDefinition() {
		return searchCriteriaDefinition;
	}

	public void setSearchCriteriaDefinition(String searchCriteriaDefinition) {
		this.searchCriteriaDefinition = searchCriteriaDefinition;
	}

	public String getSortDesc() {
		return sortDesc;
	}

	public void setSortDesc(String sortDesc) {
		this.sortDesc = sortDesc;
	}

	public String getSortDefinition() {
		return sortDefinition;
	}

	public void setSortDefinition(String sortDefinition) {
		this.sortDefinition = sortDefinition;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getCreateTimestamp() {
		return createTimestamp;
	}

	public void setCreateTimestamp(Date createTimestamp) {
		this.createTimestamp = createTimestamp;
	}

	public String getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}

	public Date getLastUpdateTimestamp() {
		return lastUpdateTimestamp;
	}

	public void setLastUpdateTimestamp(Date lastUpdateTimestamp) {
		this.lastUpdateTimestamp = lastUpdateTimestamp;
	}

	public String getLastUpdateUserId() {
		return lastUpdateUserId;
	}

	public void setLastUpdateUserId(String lastUpdateUserId) {
		this.lastUpdateUserId = lastUpdateUserId;
	}
	
	
}
