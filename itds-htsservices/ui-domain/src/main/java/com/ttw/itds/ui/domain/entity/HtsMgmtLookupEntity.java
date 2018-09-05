package com.ttw.itds.ui.domain.entity;

import java.util.Date;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import org.joda.time.LocalDateTime;

@Entity
@Table(name = "REF_HTS_ALL")
public class HtsMgmtLookupEntity extends BaseItdsEntity implements Comparable<HtsMgmtLookupEntity> {

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
	
    @Size(max = 10)
    @Column(name = "HTS", nullable = false)
    private String htsCode;
	public String getHtsCode() {    
		return htsCode;    
	}
	public void setHtsCode(String htsCode) {
		this.htsCode = htsCode;	
	}
    
    @Size(max = 3000)
    @Column(name = "CPSC_DESCRIPTION", nullable = true)
    private String cpscDescription;
	public String getCpscDescription() {    
		return cpscDescription;    
	}
	public void setCpscDescription(String cpscDescription) {
		this.cpscDescription = cpscDescription;	
	}
    
    @Size(max = 3000)
    @Column(name = "CPSC_SHORT_DESCRIPTION", nullable = true)
    private String cpscShortDescription;
	public String getCpscShortDescription() {    
		return cpscShortDescription;    
	}
	public void setCpscShortDescription(String cpscShortDescription) {
		this.cpscShortDescription = cpscShortDescription;	
	}
    
    @Size(max = 3000)
    @Column(name = "ITC_SHORT_DESCRIPTION", nullable = true)
    private String itcDescription;
	public String getItcDescription() {    
		return itcDescription;    
	}
	public void setItcDescription(String itcDescription) {
		this.itcDescription = itcDescription;	
	}
    
	@Column(name="JURISDICTION", nullable = true)
	private Boolean jurisdiction;    
	public Boolean getJurisdiction() { 
		return jurisdiction; 
	}
	public void setJurisdiction(Boolean jurisdiction) { 
		this.jurisdiction = jurisdiction; 
	}
    
	@Column(name="TARGETED", nullable = true)
	private Boolean targeted;    
	public Boolean getTargeted() { 
		return targeted; 
	}
	public void setTargeted(Boolean targeted) { 
		this.targeted = targeted; 
	}
    
	@Column(name="INHERITS_CHANGE", nullable = true)
	private Boolean inheritsChange;    
	public Boolean getInheritsChange() { 
		return inheritsChange; 
	}
	public void setInheritsChange(Boolean inheritsChange) { 
		this.inheritsChange = inheritsChange; 
	}
    
    @Size(max = 1000)
    @Column(name = "SOURCE", nullable = true)
    private String source;
	public String getSource() {    
		return source;    
	}
	public void setSource(String source) {
		this.source = source;	
	}
    
    @Size(max = 1000)
    @Column(name = "NOTES", nullable = true)
    private String notes;
	public String getNotes() {    
		return notes;    
	}
	public void setNotes(String notes) {
		this.notes = notes;	
	}
        
	@Column(name = "START_DATE", nullable = true)
	private Date startDate;
	public Date getStartDate() {    
		return startDate;    
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;	
	}
    
	@Column(name = "END_DATE", nullable = true)
	private Date endDate;
	public Date getEndDate() {    
		return endDate;    
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;	
	}
	
	@Column(name = "CREATE_TIMESTAMP", nullable = true)
	private Date createTS;
	public Date getCreateTS() {
		return createTS;
	}
	public void setCreateTS(Date createTS) {
		this.createTS = createTS;
	}
    
    @Size(max = 25)
    @Column(name = "CREATE_USER_ID", nullable = true)
    private String createUserId;
	public String getCreateUserId() {    
		return createUserId;    
	}
	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;	
	}

	@Column(name = "LAST_UPDATE_TIMESTAMP", nullable = true)
	private Date lastUpdateTS;
	public Date getLastUpdateTS() {
		return lastUpdateTS;
	}
	public void setLastUpdateTS(Date lastUpdateTS) {
		this.lastUpdateTS = lastUpdateTS;
	}
	
    @Size(max = 25)
    @Column(name = "LAST_UPDATE_USER_ID", nullable = true)
    private String lastUpdateUserId;
	public String getLastUpdateUserId() {    
		return lastUpdateUserId;    
	}
	public void setLastUpdateUserId(String lastUpdateUserId) {
		this.lastUpdateUserId = lastUpdateUserId;	
	}
    
	@Column(name = "SEQUENCE_ID", nullable = false)
	private Integer sequenceId;
	public Integer getSequenceId() {    
		return sequenceId;    
	}
	public void setSequenceId(Integer sequenceId) {
		this.sequenceId = sequenceId;	
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
        final HtsMgmtLookupEntity other = (HtsMgmtLookupEntity) obj;
        return Objects.equals(this.id, other.id);
    }
    
    public int compareTo(HtsMgmtLookupEntity other) {
    	int result = htsCode.compareTo(other.getHtsCode());
    	if (result == 0) {
    		if (id != null && other.getId() != null) {
    			result = id.compareTo(other.getId());
    		} else if (id == null && other.getId() == null) {
    			result = 0;
    		} else { // one is from db, other is generated
    			result = 0;
    		}
    	}
    	return result;
    }

    @Override
    public String toString() {
        return "HtsMgmtLookupEntity{" + "id=" + id + ", htsCode=" + htsCode + ", cpscDescription=" + cpscDescription 
        		+ ", itcDescription=" + itcDescription + ", jurisdiction=" + jurisdiction + ", targeted=" + targeted 
        		+ ", source=" + source + ", notes=" + notes + ", startDate=" + startDate + ", endDate=" + endDate + '}';
    }
	

}