package gov.cpsc.hts.itds.ui.domain.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "REF_ROLE")
public class RefRoleEntity extends BaseItdsEntity {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "ROLE_NAME")
    private String roleName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "ROLE_DESCRIPTION")
    private String roleDescription;
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
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinTable(name = "ROLE_APPLICATION_ACTION", joinColumns = @JoinColumn(name = "ROLE_NAME"), inverseJoinColumns = @JoinColumn(name = "ACTION_CODE"))
    private Set<RefApplicationAction> refApplicationActions = new HashSet<>();

    public RefRoleEntity() {
    }

    public RefRoleEntity(String roleName) {
        this.roleName = roleName;
    }

    public RefRoleEntity(String roleName, String roleDescription) {
        this.roleName = roleName;
        this.roleDescription = roleDescription;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleDescription() {
        return roleDescription;
    }

    public void setRoleDescription(String roleDescription) {
        this.roleDescription = roleDescription;
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

    public Set<RefApplicationAction> getRefApplicationActions() {
        return refApplicationActions;
    }

    public void setRefApplicationActions(Set<RefApplicationAction> refApplicationActions) { this.refApplicationActions = refApplicationActions; }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + Objects.hashCode(this.roleName);
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
        final RefRoleEntity other = (RefRoleEntity) obj;
        if (!Objects.equals(this.roleName, other.roleName)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "RefRole{" + "roleName=" + roleName + ", roleDescription=" + roleDescription + ", createTimestamp=" + createTimestamp + ", createUserId=" + createUserId + ", lastUpdateTimestamp=" + lastUpdateTimestamp + ", lastUpdateUserId=" + lastUpdateUserId + ", refApplicationActions=" + refApplicationActions + '}';
    }
}
