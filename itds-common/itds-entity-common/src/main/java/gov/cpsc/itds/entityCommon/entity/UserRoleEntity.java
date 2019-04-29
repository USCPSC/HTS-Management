package gov.cpsc.itds.entityCommon.entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "USER_ROLE")
public class UserRoleEntity extends BaseItdsEntity {

  private static final long serialVersionUID = 6713939486604672811L;

  public UserRoleEntity() {
    super();
  }

  @EmbeddedId private UserRoleEntityId id;

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
