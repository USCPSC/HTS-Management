package gov.cpsc.itds.entityCommon.entity;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Embeddable
public class UserRoleEntityId  extends BaseItdsEntity{
	
	private static final long serialVersionUID = 6713939486604672811L;
	public UserRoleEntityId() {
		super();
	}
	
	@ManyToOne
	@JoinColumn(name = "USER_ID")
	private User username;
	public User getUsername() {    
		return username;    
	}
	public void setUsername(User username) {
		this.username = username;	
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
	
}
