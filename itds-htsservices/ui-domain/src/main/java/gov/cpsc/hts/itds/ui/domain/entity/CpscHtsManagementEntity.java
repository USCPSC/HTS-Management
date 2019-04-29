package gov.cpsc.hts.itds.ui.domain.entity;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import gov.cpsc.hts.itds.ui.shared.codec.CustomDateTimeDeserializer;
import gov.cpsc.hts.itds.ui.shared.codec.CustomDateTimeSerializer;

@Entity
@Table(name = "CPSC_HTS_MANAGEMENT_SCRATCH")
public class CpscHtsManagementEntity extends BaseItdsEntity implements Comparable<CpscHtsManagementEntity> {

    private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
//@Basic(optional = false)
	@Column(name = "ID", nullable = false)
	private Long id;
	public Long getId() {    
		return id;    
	}
	public void setId(Long id) {
		this.id = id;	
	}
    
    @Size(max = 25)
    @Column(name = "USERNAME", nullable = true)
    private String username;
	public String getUsername() {    
		return username;    
	}
	public void setUsername(String username) {
		this.username = username;	
	}
    
    @Size(max = 10)
//    @Column(name = "HTS", nullable = false)
    @Column(name = "CODE", nullable = false)
    private String htsCode;
	public String getHtsCode() {    
		return htsCode;    
	}
	public void setHtsCode(String htsCode) {
		this.htsCode = htsCode;	
	}
	
	@Column(name = "CODE_TYPE", nullable = false)
	private Integer codeType;
	public Integer getCodeType() {    
		return codeType;    
	}
	public void setCodeType(Integer codeType) {
		this.codeType = codeType;	
	}
    
    @Size(max = 3000)
    @Column(name = "CPSC_DESCRIPTION", nullable = true)
    private String cdescription;
	public String getCdescription() {    
		return cdescription;    
	}
	public void setCdescription(String cdescription) {
		this.cdescription = cdescription;	
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
    private String description;
	public String getDescription() {    
		return description;    
	}
	public void setDescription(String description) {
		this.description = description;	
	}
    
	@Column(name="JURISDICTION", nullable = true)
	private Boolean jurisdiction;    
	public Boolean getJurisdiction() { 
		return jurisdiction; 
	}
	public void setJurisdiction(Boolean jurisdiction) { 
		this.jurisdiction = jurisdiction; 
	}
    
	@Column(name="JURISDICTION_MODIFIED", nullable = true)
	private Boolean jurisdictionModified;
	public Boolean getJurisdictionModified() { 
		return jurisdictionModified; 
	}
	public void setJurisdictionModified(Boolean jurisdictionModified) { 
		this.jurisdictionModified = jurisdictionModified; 
	}
    
	@Column(name="TARGETED", nullable = true)
	private Boolean targeted;    
	public Boolean getTargeted() { 
		return targeted; 
	}
	public void setTargeted(Boolean targeted) { 
		this.targeted = targeted; 
	}
    
	@Column(name="TARGETED_MODIFIED", nullable = true)
	private Boolean targetedModified;
	public Boolean getTargetedModified() { 
		return targetedModified; 
	}
	public void setTargetedModified(Boolean targetedModified) { 
		this.targetedModified = targetedModified; 
	}
    
	@Column(name="MODIFIED", nullable = true)
	private Boolean modified;    
	public Boolean getModified() { 
		return modified; 
	}
	public void setModified(Boolean modified) { 
		this.modified = modified; 
	}
    
	@Transient
	private Boolean sunset;    
	public Boolean getSunset() { 
		return sunset; 
	}
	public void setSunset(Boolean sunset) { 
		this.sunset = sunset; 
	}
    
	@Column(name="INHERITS_CHANGE", nullable = true)
	private Boolean inheritsChange;    
	public Boolean getInheritsChange() { 
		return inheritsChange; 
	}
	public void setInheritsChange(Boolean inheritsChange) { 
		this.inheritsChange = inheritsChange; 
	}
    
    @Size(max = 25)
    @Column(name = "CHANGE_STATUS", nullable = true)
    private String changeStatus;
	public String getChangeStatus() {    
		return changeStatus;    
	}
	public void setChangeStatus(String changeStatus) {
		this.changeStatus = changeStatus;	
	}
	
    @Size(max = 25)
	@Column(name = "REVIEW_STATUS", nullable = true)
	private String reviewStatus;
	public String getReviewStatus() {    
		return reviewStatus;    
	}
	public void setReviewStatus(String reviewStatus) {
		this.reviewStatus = reviewStatus;	
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
        
	@Transient
	public List<CpscHtsManagementEntity> children;
	public List<CpscHtsManagementEntity> getChildren() {    
		return children;    
	}
	public void setChildren(List<CpscHtsManagementEntity> children) {
		this.children = children;	
	}

	public enum HierarchyProp {
		JURISDICTION, 
		JURISDICTION_MODIFIED, 
		TARGETED, 
		TARGETED_MODIFIED, 
		MODIFIED, 
		SUNSET,
		INHERITS_CHANGE // a short-circuit option for efficiency
	}	
	
	public boolean ifNotNullSetFalse(HierarchyProp flag) {
		boolean result = false;
		Boolean currentValue = forwardNullableGet(flag);
		if (Boolean.TRUE.equals(currentValue)) { // currentValue != null && currentValue
			forwardNullableSet(flag, false);
			result = true;
		}
		return result;
	}
	
	public boolean setAndReturnIfChanged(HierarchyProp flag, boolean newValue) {
		boolean changed = false;
		Boolean currentValue = forwardNullableGet(flag);
		if (newValue) {
			if (!Boolean.TRUE.equals(currentValue)) {
				forwardNullableSet(flag, newValue);
				changed = true;
			}
		} else {
			if (!Boolean.FALSE.equals(currentValue)) {
				forwardNullableSet(flag, newValue);
				changed = true;
			}
		}
		return changed;
	}
	
	private Boolean forwardNullableGet(HierarchyProp flag) {
		Boolean result = null;
		switch (flag) {
		case JURISDICTION: 
			result = getJurisdiction();
			break;
		case TARGETED: 
			result = getTargeted();
			break;
		default: // do nothing, so null will be returned
		}
		return result;
	}
	
	private Boolean forwardNullableSet(HierarchyProp flag, Boolean newValue) {
		Boolean previousValue = null;
		switch (flag) {
		case JURISDICTION: 
			previousValue = getJurisdiction();
			setJurisdiction(newValue);
			break;
		case TARGETED: 
			previousValue = getTargeted();
			setTargeted(newValue);
			break;
		default: // do nothing, so null will be returned
		}
		return previousValue;
	}
	
	public static boolean isPlaceholder(CpscHtsManagementEntity chme) {
		boolean result = false;
		if (chme != null) {
			result = chme.getDescription() == null || chme.getDescription().isEmpty();
		}
		return result;
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
        final CpscHtsManagementEntity other = (CpscHtsManagementEntity) obj;
        return Objects.equals(this.id, other.id);
    }
    
    public int compareTo(CpscHtsManagementEntity other) {
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
        return "CpscHtsManagementEntity{" + "id=" + id + ", username=" + username + ", htsCode=" + htsCode + ", codeType=" + codeType 
        		+ ", description=" + description + ", cdescription=" + cdescription + ", jurisdiction=" + jurisdiction 
        		+ ", jurisdictionModified=" + jurisdictionModified + ", targeted=" + targeted + ", targetedModified=" + targetedModified 
        		+ ", changeStatus=" + changeStatus + ", reviewStatus=" + reviewStatus + ", source=" + source + ", notes=" + notes + '}';
    }
    
    

}
