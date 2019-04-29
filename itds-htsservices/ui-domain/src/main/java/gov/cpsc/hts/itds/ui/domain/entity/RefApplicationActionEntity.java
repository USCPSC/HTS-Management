package gov.cpsc.hts.itds.ui.domain.entity;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import gov.cpsc.hts.itds.ui.domain.entity.BaseItdsEntity;

@Entity
@Table(name = "REF_APPLICATION_ACTION")
public class RefApplicationActionEntity  extends BaseItdsEntity{
	
	private static final long serialVersionUID = 6713939486604672811L;
	public RefApplicationActionEntity() {
		super();
	}
	
	@Id
	@Column(name = "ACTION_CODE")
	private String actionCode;
	public String getActionCode() {    
		return actionCode;    
	}
	public void setActionCode(String actionCode) {
		this.actionCode = actionCode;	
	}

	@Column(name = "ACTION_NAME")
	private String actionName;
	public String getActionName() {    
		return actionName;    
	}
	public void setActionName(String actionName) {
		this.actionName = actionName;	
	}

	@Column(name = "ACTION_DESCRIPTION")
	private String actionDescription;
	public String getActionDescription() {    
		return actionDescription;    
	}
	public void setActionDescription(String actionDescription) {
		this.actionDescription = actionDescription;	
	}

	@Column(name = "CREATE_USER_ID", nullable = false)
	private String createUserId;
	public String getCreateUserId() {    
		return createUserId;    
	}
	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId; 
	}

	@Column(name = "CREATE_TIMESTAMP")
	private Date createTimestamp;
	public Date getCreateTimestamp() {    
		return createTimestamp;    
	}
	public void setCreateTimestamp(Date createTimestamp) {
		this.createTimestamp = createTimestamp; 
	}

	@Column(name = "LAST_UPDATE_USER_ID", nullable = false)
	private String lastUpdateUserId;
	public String getLastUpdateUserId() {    
		return lastUpdateUserId;    
	}
	public void setLastUpdateUserId(String lastUpdateUserId) {
		this.lastUpdateUserId = lastUpdateUserId; 
	}

	@Column(name = "LAST_UPDATE_TIMESTAMP")
	private Date lastUpdateTimestamp;
	public Date getLastUpdateTimestamp() {    
		return lastUpdateTimestamp;    
	}
	public void setLastUpdateTimestamp(Date lastUpdateTimestamp) {
		this.lastUpdateTimestamp = lastUpdateTimestamp; 
	}

}
