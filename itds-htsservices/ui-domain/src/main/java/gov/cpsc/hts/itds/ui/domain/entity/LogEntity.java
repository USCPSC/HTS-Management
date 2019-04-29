package gov.cpsc.hts.itds.ui.domain.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import gov.cpsc.hts.itds.ui.shared.codec.CustomDateTimeDeserializer;
import gov.cpsc.hts.itds.ui.shared.codec.CustomDateTimeSerializer;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

@Entity
@Table(name = "LOG")
@Deprecated
public class LogEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Long id;
    @Size(max = 25)
    @Column(name = "USERNAME")
    private String username;
    @Column(name = "DATE_CREATED", insertable=false, updatable=false)
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    private DateTime dateCreated;
    @Size(max = 50)
    @Column(name = "LOGGER_NAME")
    private String loggerName;
    @Size(max = 10)
    @Column(name = "LEVEL")
    private String level;
    @Size(max = 255)
    @Column(name = "MESSAGE")
    private String message;

    public LogEntity() {
    }

    public LogEntity(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public DateTime getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(DateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getLoggerName() {
        return loggerName;
    }

    public void setLoggerName(String loggerName) {
        this.loggerName = loggerName;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.id);
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
        final LogEntity other = (LogEntity) obj;
        return Objects.equals(this.id, other.id);
    }
    
    @Override
    public String toString() {
        return "LogEntity{" + "id=" + id + ", username=" + username + ", dateCreated=" + dateCreated + ", loggerName=" + loggerName + ", level=" + level + ", message=" + message + '}';
    }
}
