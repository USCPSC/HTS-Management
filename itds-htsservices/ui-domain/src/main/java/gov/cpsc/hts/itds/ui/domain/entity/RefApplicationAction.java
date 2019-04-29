package gov.cpsc.hts.itds.ui.domain.entity;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "REF_APPLICATION_ACTION")
@NamedQueries({
    @NamedQuery(name = "RefApplicationAction.findAll", query = "SELECT r FROM RefApplicationAction r")})
public class RefApplicationAction implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "ACTION_CODE")
    private String actionCode;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "ACTION_NAME")
    private String actionName;
    @Size(max = 255)
    @Column(name = "ACTION_DESCRIPTION")
    private String actionDescription;
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

    public RefApplicationAction() {
    }

    public RefApplicationAction(String actionCode) {
        this.actionCode = actionCode;
    }

    public RefApplicationAction(String actionCode, String actionName) {
        this.actionCode = actionCode;
        this.actionName = actionName;
    }

    public String getActionCode() {
        return actionCode;
    }

    public void setActionCode(String actionCode) {
        this.actionCode = actionCode;
    }

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public String getActionDescription() {
        return actionDescription;
    }

    public void setActionDescription(String actionDescription) {
        this.actionDescription = actionDescription;
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

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 83 * hash + Objects.hashCode(this.actionCode);
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
        final RefApplicationAction other = (RefApplicationAction) obj;
        if (!Objects.equals(this.actionCode, other.actionCode)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "RefApplicationAction{" + "actionCode=" + actionCode + ", actionName=" + actionName + ", actionDescription=" + actionDescription + ", createTimestamp=" + createTimestamp + ", createUserId=" + createUserId + ", lastUpdateTimestamp=" + lastUpdateTimestamp + ", lastUpdateUserId=" + lastUpdateUserId + '}';
    }
}
