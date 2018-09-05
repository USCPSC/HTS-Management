package com.ttw.itds.ui.domain.entity;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Size;

@Entity
@Table(name = "CPSC_HTS_MGMT_HIST")
public class CpscHtsMgmtHistEntity extends BaseItdsEntity {

    private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "ID", nullable = false)
	private Long id;
	public Long getId() {    
		return id;    
	}
	public void setId(Long id) {
		this.id = id;	
	}
    
    @Size(max = 25)
    @Column(name = "USERNAME", nullable = false)
    private String username;
	public String getUsername() {    
		return username;    
	}
	public void setUsername(String username) {
		this.username = username;	
	}
    
    @Size(max = 25)
    @Column(name = "CATEGORY", nullable = false)
    private String category;
	public String getCategory() {    
		return category;    
	}
	public void setCategory(String category) {
		this.category = category;	
	}
    
    @Size(max = 100)
    @Column(name = "INCIDENT", nullable = true)
    private String incident;
	public String getIncident() {    
		return incident;    
	}
	public void setIncident(String incident) {
		this.incident = incident;	
	}
    
	@Column(name = "EVENT_DATE", nullable = false)
	private Date eventDate;
	public Date getEventDate() {    
		return eventDate;    
	}
	public void setEventDate(Date eventDate) {
		this.eventDate = eventDate;	
	}
	
    @Column(name = "SIZE", nullable = true)
    private Integer size;
	public Integer getSize() {    
		return size;    
	}
	public void setSize(Integer size) {
		this.size = size;	
	}
	
    @Size(max = 100)
    @Column(name = "STATE_TRANSITION", nullable = true)
    private String stateTransition;
	public String getStateTransition() {    
		return stateTransition;    
	}
	public void setStateTransition(String stateTransition) {
		this.stateTransition = stateTransition;	
	}
    
    @Size(max = 100)
    @Column(name = "STATE_RESTORATION", nullable = true)
    private String stateRestoration;
	public String getStateRestoration() {    
		return stateRestoration;    
	}
	public void setStateRestoration(String stateRestoration) {
		this.stateRestoration = stateRestoration;	
	}
    
    @Size(max = 100)
    @Column(name = "SOURCE", nullable = true)
    private String source;
	public String getSource() {    
		return source;    
	}
	public void setSource(String source) {
		this.source = source;	
	}
        
    @Size(max = 100)
    @Column(name = "NOTES", nullable = true)
    private String notes;
	public String getNotes() {    
		return notes;    
	}
	public void setNotes(String notes) {
		this.notes = notes;	
	}
        
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + Objects.hashCode(this.id);
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
        final CpscHtsMgmtHistEntity other = (CpscHtsMgmtHistEntity) obj;
        return Objects.equals(this.id, other.id);
    }

    @Override
    public String toString() {
        return "CpscHtsMgmtHistEntity{" + "id=" + id + ", username=" + username + ", category=" + category + ", incident=" + incident
        		+ ", eventDate=" + eventDate + ", size=" + size + ", stateTransition=" + stateTransition 
        		+ ", stateRestoration=" + stateRestoration + ", source=" + source + ", notes=" + notes + '}';
    }
}
