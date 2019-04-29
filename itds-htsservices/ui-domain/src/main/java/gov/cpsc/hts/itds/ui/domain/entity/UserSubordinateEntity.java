package gov.cpsc.hts.itds.ui.domain.entity;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
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

@Entity
@Table(name = "USER_SUBORDINATE")
public class UserSubordinateEntity  extends BaseItdsEntity{

	private static final long serialVersionUID = 6713939486604672811L;
	public UserSubordinateEntity() {
		super();
		// add defaults
		this.setCreateTimestamp(new Date());
		this.setCreateUserId("SYSTEM");
		this.setLastUpdateTimestamp(new Date());
		this.setLastUpdateUserId("SYSTEM");
		this.setStartEffectiveTimestamp(new Date());
		this.endEffectiveTimestamp = new Date();
	}

	@EmbeddedId
	private UserSubordinateEntityId id;

	@Column(name = "START_EFFECTIVE_TIMESTAMP", nullable = false)
	private Date startEffectiveTimestamp;
	public Date getStartEffectiveTimestamp() {
		return startEffectiveTimestamp;
	}
	public void setStartEffectiveTimestamp(Date startEffectiveTimestamp) {
		this.startEffectiveTimestamp = startEffectiveTimestamp;
	}

	@Column(name = "END_EFFECTIVE_TIMESTAMP", nullable = false)
	private Date endEffectiveTimestamp;
	public Date getEndEffectiveTimestamp() {
		return endEffectiveTimestamp;
	}
	public void setIsActive(Date endEffectiveTimestamp) {
		this.endEffectiveTimestamp = endEffectiveTimestamp;
	}

	@Column(name = "CREATE_USER_ID")
	private String createUserId = "SYSTEM";
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

	@Column(name = "LAST_UPDATE_USER_ID")
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
