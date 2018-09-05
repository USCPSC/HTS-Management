package com.ttw.itds.ui.domain.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.ttw.itds.ui.shared.codec.CustomDateTimeDeserializer;
import com.ttw.itds.ui.shared.codec.CustomDateTimeSerializer;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

@Entity
@Table(name = "SYS_PROPERTY")
public class SysPropertyEntity extends BaseItdsEntity {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "NAME")
    private String name;
    
    @Column(name = "VALUE")
    private String value;
    
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    @Column(name = "CREATE_TIMESTAMP")
    private DateTime createTimestamp;
    
    @Size(max = 255)
    @Column(name = "CREATE_USER_ID")
    private String createUserId;
    
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    @Column(name = "LAST_UPDATE_TIMESTAMP")
    private DateTime lastUpdateTimestamp;

    @Size(max = 255)
    @Column(name = "LAST_UPDATE_USER_ID")
    private String lastUpdateUserId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public DateTime getCreateTimestamp() {
        return createTimestamp;
    }

    public void setCreateTimestamp(DateTime createTimestamp) {
        this.createTimestamp = createTimestamp;
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    public DateTime getLastUpdateTimestamp() {
        return lastUpdateTimestamp;
    }

    public void setLastUpdateTimestamp(DateTime lastUpdateTimestamp) {
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
        int hash = 3;
        hash = 97 * hash + Objects.hashCode(this.name);
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
        final SysPropertyEntity other = (SysPropertyEntity) obj;
        return Objects.equals(this.name, other.name);
    }

    @Override
    public String toString() {
        return "SysPropertyEntity{" + "name=" + name + ", value=" + value + ", createTimestamp=" + createTimestamp + ", createUserId=" + createUserId + ", lastUpdateTimestamp=" + lastUpdateTimestamp + ", lastUpdateUserId=" + lastUpdateUserId + '}';
    }

}
