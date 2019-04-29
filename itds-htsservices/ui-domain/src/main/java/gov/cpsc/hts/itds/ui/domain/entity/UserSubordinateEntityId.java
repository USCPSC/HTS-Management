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
public class UserSubordinateEntityId  extends BaseItdsEntity{
	
	private static final long serialVersionUID = 6713939486604672811L;
	public UserSubordinateEntityId() {
		super();
	}
	
	@ManyToOne
	@JoinColumn(name = "SUPERVISOR_USERNAME")
	private User supervisorUsername;
	public User getSupervisorUsername() {    
		return supervisorUsername;    
	}
	public void setSupervisorUsername(User supervisorUsername) {
		this.supervisorUsername = supervisorUsername;	
	}

	@ManyToOne
	@JoinColumn(name = "SUBORDINATE_USERNAME")
	private User subordinateUsername;
	public User getSubordinateUsername() {    
		return subordinateUsername;    
	}
	public void setSubordinateUsername(User subordinateUsername) {
		this.subordinateUsername = subordinateUsername;	
	}

}
