package gov.cpsc.hts.itds.ui.domain.entity;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import gov.cpsc.hts.itds.ui.domain.entity.RefRoleEntity;
import gov.cpsc.hts.itds.ui.domain.entity.User;

import gov.cpsc.hts.itds.ui.shared.codec.CustomDateTimeDeserializer;
import gov.cpsc.hts.itds.ui.shared.codec.CustomDateTimeSerializer;

@Entity
@Table(name = "CPSC_USER")
public class User extends BaseItdsEntity {

    private static final long serialVersionUID = -4156175327807991133L;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 25)
    @Id
    private String username;

    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Size(max = 255)
    private String email;

    @Size(max = 255)
    private String password;

    @Size(max = 255)
    @Column(name = "PASSWORD_SALT")
    private String psswordSalt;

    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    @Column(name = "LAST_LOGIN_TIMESTAMP")
    private DateTime lastlogintimestamp;

    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    @Column(name = "LAST_LOGOUT_TIMESTAMP")
    private DateTime lastlogouttimestamp;

    @Column(name = "INBOX_ROWS_SHOWN")
    private Integer inboxrowsshown;

    @Column(name = "ACTIVE")
    private Boolean active;

    @Size(max = 100)
    @Column(name = "FIRST_NAME")
    private String firstname;

    @Size(max = 100)
    @Column(name = "LAST_NAME")
    private String lastname;

    @Size(max = 15)
    @Column(name = "ORGANIZATION_CODE")
    private String orgcode;

    @Size(max = 255)
    @Column(name = "EXIS_LOCATION")
    private String exisLocation;

    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    @Column(name = "CREATE_TIMESTAMP")
    private DateTime createTimestamp;

    @Size(max = 25)
    @Column(name = "CREATE_USER_ID")
    private String createuserid;

    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    @Column(name = "LAST_UPDATE_TIMESTAMP")
    private DateTime lastupdateTimestamp;

    @Size(max = 25)
    @Column(name = "LAST_UPDATE_USER_ID")
    private String lastupdateuserid;

    @Column(name = "MSG_FILTER")
    private String defaultUserMsgFilter;
    public String getDefaultUserMsgFilter() {
		return defaultUserMsgFilter;
	}
	public void setDefaultUserMsgFilter(String defaultUserMsgFilter) {
		this.defaultUserMsgFilter = defaultUserMsgFilter;
	}
	@Column(name = "READ_FILTER")
	private Boolean unReadFilter;
	
	public Boolean getUnReadFilter() {
		if(unReadFilter == null ) {
			unReadFilter = false;
		}
		return unReadFilter;
	}
	public void setUnReadFilter(Boolean unReadFilter) {
		this.unReadFilter = unReadFilter;
	}
	@ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "USER_ROLE", joinColumns = @JoinColumn(name = "USER_ID"), inverseJoinColumns = @JoinColumn(name = "ROLE_NAME"))
    private Set<RefRoleEntity> refRoles = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "USER_SUBORDINATE", joinColumns = @JoinColumn(name = "SUBORDINATE_USERNAME"), inverseJoinColumns = @JoinColumn(name = "SUPERVISOR_USERNAME"))
    private Set<User> supervisors = new HashSet<>();

	public User() {
    }

    public User(String username) {
        this.username = username;
    }

    public User(String username, Set<RefRoleEntity> refRoles){
        this.username = username;
        this.refRoles = refRoles;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPsswordSalt() {
        return psswordSalt;
    }

    public void setPsswordSalt(String psswordSalt) {
        this.psswordSalt = psswordSalt;
    }

    public Integer getInboxrowsshown() {
        return inboxrowsshown;
    }

    public void setInboxrowsshown(Integer inboxrowsshown) {
        this.inboxrowsshown = inboxrowsshown;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getOrgcode() {
        return orgcode;
    }

    public void setOrgcode(String orgcode) {
        this.orgcode = orgcode;
    }

    public String getExisLocation() {
        return exisLocation;
    }

    public void setExisLocation(String exisLocation) {
        this.exisLocation = exisLocation;
    }

    public String getCreateuserid() {
        return createuserid;
    }

    public void setCreateuserid(String createuserid) {
        this.createuserid = createuserid;
    }

    public String getLastupdateuserid() {
        return lastupdateuserid;
    }

    public void setLastupdateuserid(String lastupdateuserid) {
        this.lastupdateuserid = lastupdateuserid;
    }

    public DateTime getLastlogintimestamp() {
        return lastlogintimestamp;
    }

    public void setLastlogintimestamp(DateTime lastlogintimestamp) {
        this.lastlogintimestamp = lastlogintimestamp;
    }

    public DateTime getLastlogouttimestamp() {
        return lastlogouttimestamp;
    }

    public void setLastlogouttimestamp(DateTime lastlogouttimestamp) {
        this.lastlogouttimestamp = lastlogouttimestamp;
    }

    public DateTime getCreateTimestamp() {
        return createTimestamp;
    }

    public void setCreateTimestamp(DateTime createTimestamp) {
        this.createTimestamp = createTimestamp;
    }

    public DateTime getLastupdateTimestamp() {
        return lastupdateTimestamp;
    }

    public void setLastupdateTimestamp(DateTime lastupdateTimestamp) {
        this.lastupdateTimestamp = lastupdateTimestamp;
    }

    public Set<RefRoleEntity> getRefRoles() {
        return refRoles;
    }

    public void setRefRoles(Set<RefRoleEntity> refRoles) {
        this.refRoles = refRoles;
    }

    public Set<User> getSupervisors() {
        return supervisors;
    }

    public void setSupervisors(Set<User> supervisors) {
        this.supervisors = supervisors;
    }

    @PrePersist
    protected void onCreate() {
        createTimestamp = new DateTime();
        lastupdateTimestamp = new DateTime();
        createuserid = lastupdateuserid;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.username);
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
        final User other = (User) obj;
        return Objects.equals(this.username, other.username);
    }

    @Override
    public String toString() {
        return "User{" + "username=" + username + ", email=" + email + ", password=" + password + ", psswordSalt=" + psswordSalt + ", supervisors=" + supervisors + ", lastlogintimestamp=" + lastlogintimestamp + ", lastlogouttimestamp=" + lastlogouttimestamp + ", inboxrowsshown=" + inboxrowsshown + ", active=" + active + ", firstname=" + firstname + ", lastname=" + lastname + ", orgcode=" + orgcode + ", exisLocation=" + exisLocation + ", createTimestamp=" + createTimestamp + ", createuserid=" + createuserid + ", lastupdateTimestamp=" + lastupdateTimestamp + ", lastupdateuserid=" + lastupdateuserid + ", refRoles=" + refRoles + '}';
    }
}
