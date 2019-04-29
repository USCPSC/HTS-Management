package gov.cpsc.hts.itds.ui.domain.entity;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import gov.cpsc.hts.itds.ui.domain.entity.BaseItdsEntity;

@Embeddable
public class RoleApplicationActionEntityId  extends BaseItdsEntity{
	
	private static final long serialVersionUID = 6713939486604672811L;
	public RoleApplicationActionEntityId() {
		super();
	}
	
	@ManyToOne
	@JoinColumn(name = "ROLE_NAME")
	private RefRoleEntity roleName;
	public RefRoleEntity getRoleName() {    
		return roleName;    
	}
	public void setRoleName(RefRoleEntity roleName) {
		this.roleName = roleName;	
	}

	@ManyToOne
	@JoinColumn(name = "ACTION_CODE")
	private RefApplicationActionEntity actionCode;
	public RefApplicationActionEntity getActionCode() {    
		return actionCode;    
	}
	public void setActionCode(RefApplicationActionEntity actionCode) {
		this.actionCode = actionCode;	
	}

}
