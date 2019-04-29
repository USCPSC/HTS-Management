package gov.cpsc.hts.itds.ui.domain.entity;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Immutable;

import gov.cpsc.hts.itds.ui.domain.entity.BaseItdsEntity;

@Entity
@Table(name = "ref_hts")
@Immutable
public class RefHtsEntity extends BaseItdsEntity{
	private static final long serialVersionUID = 1449382955468L;
	
	public RefHtsEntity() {
		super();
	}
	
	@Id
	@Column(name = "HTS", nullable = false)
	private String hts;
	public String getHts() {    
		return hts;    
	}
	public void setHts(String hts) {
		this.hts = hts;	
	}

	
	@Column(name = "SEQUENCE_ID")
	private Integer sequenceId;
	public Integer getSequenceId() {    
		return sequenceId;    
	}
	public void setSequenceId(Integer sequenceId) {
		this.sequenceId = sequenceId;	
	}

	
	@Column(name = "HTS_DESCRIPTION")
	private String description;
	public String getDescription() {    
		return description;    
	}
	public void setDescription(String description) {
		this.description = description;	
	}

	
	@Column(name = "UOMS")
	private String uoms;
	public String getUoms() {    
		return uoms;    
	}
	public void setUoms(String uoms) {
		this.uoms = uoms;	
	}

	
	@Column(name = "DUTY_RATES")
	private String dutyRates;
	public String getDutyRates() {    
		return dutyRates;    
	}
	public void setDutyRates(String dutyRates) {
		this.dutyRates = dutyRates;	
	}

	
	@Column(name = "SPECIAL_RATES")
	private String specialRates;
	public String getSpecialRates() {    
		return specialRates;    
	}
	public void setSpecialRates(String specialRates) {
		this.specialRates = specialRates;	
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