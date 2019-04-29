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
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Size;

@Entity
@Table(name = "CPSC_HTS_MANAGEMENT_FILE")
public class CpscHtsManagementFileEntity extends BaseItdsEntity {

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
		    
		    @Size(max = 1000)
		    @Column(name = "SOURCE", nullable = false)
		    private String source;
			public String getSource() {    
				return source;    
			}
			public void setSource(String source) {
				this.source = source;	
			}
		    
		    @Column(name = "SIZE", nullable = false)
		    private Integer size;
			public Integer getSize() {    
				return size;    
			}
			public void setSize(Integer size) {
				this.size = size;	
			}
			
			@Column(name = "ROW_COUNT", nullable = true)
			private Integer rowCount;
			public Integer getRowCount() {    
				return rowCount;    
			}
			public void setRowCount(Integer rowCount) {
				this.rowCount = rowCount;	
			}
		    
			@Column(name = "CODE_COUNT", nullable = true)
			private Integer codeCount;
			public Integer getCodeCount() {    
				return codeCount;    
			}
			public void setCodeCount(Integer codeCount) {
				this.codeCount = codeCount;	
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
		    @Column(name = "NOTES", nullable = true)
		    private String notes;
			public String getNotes() {    
				return notes;    
			}
			public void setNotes(String notes) {
				this.notes = notes;	
			}
		        
			@Column(name = "CREATION_DATE", nullable = false)
			private Date creationDate;
			public Date getCreationDate() {    
				return creationDate;    
			}
			public void setCreationDate(Date creationDate) {
				this.creationDate = creationDate;	
			}
			
			@Lob
		    @Column(name = "CSV", nullable = true)
		    private byte[] csv;
			public byte[] getCsv() {    
				return csv;    
			}
			public void setCsv(byte[] csv) {
				this.csv = csv;	
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
		        final CpscHtsManagementFileEntity other = (CpscHtsManagementFileEntity) obj;
		        return Objects.equals(this.id, other.id);
		    }

		    @Override
		    public String toString() {
		        return "CpscHtsManagementFileEntity{" + "id=" + id + ", username=" + username + ", source=" + source 
		        		+ ", size=" + size + ", rowCount=" + rowCount + ", codeCount=" + codeCount 
		        		+ ", reviewStatus=" + reviewStatus + ", notes=" + notes + ", creationDate=" + creationDate + '}';
		    }		    
		    
}
