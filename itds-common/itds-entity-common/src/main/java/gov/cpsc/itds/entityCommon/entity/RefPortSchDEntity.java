package gov.cpsc.itds.entityCommon.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;

/**
 * @author hzhao
 * Created new table, based on old port_ref table
 */
@Entity
@Table(name = "REF_PORT_SCH_D")
public class RefPortSchDEntity extends BaseItdsEntity{

    @Size(max = 27)
    @Column(name = "CREATE_TIMESTAMP")
    private String createTimestamp;
    @Size(max = 25)
    @Column(name = "CREATE_USER_ID")
    private String createUserId;
    @Size(max = 27)
    @Column(name = "LAST_UPDATE_TIMESTAMP")
    private String lastUpdateTimestamp;
    @Size(max = 25)
    @Column(name = "LAST_UPDATE_USER_ID")
    private String lastUpdateUserId;
	@Id
	@Column(name = "SCH_D_CODE", nullable = false)
	private String portCode;
	@Column(name = "SCH_D_PLACE", nullable = false)
	private String portName;
	@Column(name = "SCH_D_STATE", nullable = false)
	private String state;
    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY, mappedBy="refPorts")
    private Set<User> users = new HashSet<>();

	private static final long serialVersionUID = 1449382955222L;

	public RefPortSchDEntity() {
	}

	public String getPortCode() {
		return portCode;
	}
	public void setPortCode(String portCode) {
		this.portCode = portCode;
	}

	public String getPortName() {
		return portName;
	}
	public void setPortName(String portName) {
		this.portName = portName;
	}

	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}

    public String getCreateTimestamp() {
        return createTimestamp;
    }

    public void setCreateTimestamp(String createTimestamp) {
        this.createTimestamp = createTimestamp;
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    public String getLastUpdateTimestamp() {
        return lastUpdateTimestamp;
    }

    public void setLastUpdateTimestamp(String lastUpdateTimestamp) {
        this.lastUpdateTimestamp = lastUpdateTimestamp;
    }

    public String getLastUpdateUserId() {
        return lastUpdateUserId;
    }

    public void setLastUpdateUserId(String lastUpdateUserId) {
        this.lastUpdateUserId = lastUpdateUserId;
    }

    public Set<User> getUsers() { return users; }

    public void setUsers(Set<User> users) { this.users = users; }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 19 * hash + Objects.hashCode(this.portCode);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final RefPortSchDEntity other = (RefPortSchDEntity) obj;
        if (!Objects.equals(this.portCode, other.portCode)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "RefPortSchDEntity{" + "createTimestamp=" + createTimestamp + ", createUserId=" + createUserId + ", lastUpdateTimestamp=" + lastUpdateTimestamp + ", lastUpdateUserId=" + lastUpdateUserId + ", portCode=" + portCode + ", portName=" + portName + ", state=" + state + '}';
    }
}
