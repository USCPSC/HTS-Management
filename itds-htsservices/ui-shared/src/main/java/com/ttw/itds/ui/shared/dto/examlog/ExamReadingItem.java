/**
 * 
 */
package com.ttw.itds.ui.shared.dto.examlog;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.joda.time.DateTime;

import com.ttw.itds.ui.shared.dto.ItdsUiBaseDto;

/**
 * @author hzhao
 * @since 1.0.10
 *
 */
public class ExamReadingItem extends ItdsUiBaseDto {

	private static final long serialVersionUID = 3566031983664690814L;

	
	    private Long id;
	    private long productSampleId;
		private List<ExamReading> readings;
	    private Integer readingNo;
	    private DateTime readingTime;
	    private String sampleNumber;
	    private Integer fileIndex;
	    private String type;
	    private String duration;
	    private String units;
	    private String sigmaValue;
	    private String sequence;
	    private String sampleType;
	    private String result;
	    private String flags;
	    private String color;
	    private String sku;
	    private String location;
	    private String misc;
	    private String note;

		private String createUserId;
		private Date createTimestamp;
		private String lastUpdateUserId;
		private Date lastUpdateTimestamp;

		
	    public ExamReadingItem() {
			super();
			readings = new ArrayList<ExamReading>();
		}

		@Override
	    public int hashCode() {
	        int hash = 3;
	        hash = 29 * hash + Objects.hashCode(this.id);
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
	        final ExamReadingItem other = (ExamReadingItem) obj;
	        return Objects.equals(this.id, other.id);
	    }

	    @Override
	    public String toString() {
	        return "ExamReadingItemEntity{" + "id=" + id + ", readingNo=" + readingNo + ", readingTime=" + readingTime + ", sampleNumber=" + sampleNumber + ", fileIndex=" + fileIndex + ", type=" + type + ", duration=" + duration + ", units=" + units + ", sigmaValue=" + sigmaValue + ", sequence=" + sequence + ", sampleType=" + sampleType + ", result=" + result + ", flags=" + flags + ", color=" + color + ", sku=" + sku + ", location=" + location + ", misc=" + misc + ", note=" + note + '}';
	    }

		/**
		 * @return the id
		 */
		public Long getId() {
			return id;
		}

		/**
		 * @param id the id to set
		 */
		public void setId(Long id) {
			this.id = id;
		}

		/**
		 * @return the productSampleId
		 */
		public long getProductSampleId() {
			return productSampleId;
		}

		/**
		 * @param productSampleId the productSampleId to set
		 */
		public void setProductSampleId(long productSampleId) {
			this.productSampleId = productSampleId;
		}

		/**
		 * @return the readings
		 */
		public List<ExamReading> getReadings() {
			return readings;
		}

		/**
		 * @param readings the readings to set
		 */
		public void setReadings(List<ExamReading> readings) {
			this.readings = readings;
		}

		/**
		 * @return the readingNo
		 */
		public Integer getReadingNo() {
			return readingNo;
		}

		/**
		 * @param readingNo the readingNo to set
		 */
		public void setReadingNo(Integer readingNo) {
			this.readingNo = readingNo;
		}

		/**
		 * @return the readingTime
		 */
		public DateTime getReadingTime() {
			return readingTime;
		}

		/**
		 * @param readingTime the readingTime to set
		 */
		public void setReadingTime(DateTime readingTime) {
			this.readingTime = readingTime;
		}

		/**
		 * @return the sampleNumber
		 */
		public String getSampleNumber() {
			return sampleNumber;
		}

		/**
		 * @param sampleNumber the sampleNumber to set
		 */
		public void setSampleNumber(String sampleNumber) {
			this.sampleNumber = sampleNumber;
		}

		/**
		 * @return the fileIndex
		 */
		public Integer getFileIndex() {
			return fileIndex;
		}

		/**
		 * @param fileIndex the fileIndex to set
		 */
		public void setFileIndex(Integer fileIndex) {
			this.fileIndex = fileIndex;
		}

		/**
		 * @return the type
		 */
		public String getType() {
			return type;
		}

		/**
		 * @param type the type to set
		 */
		public void setType(String type) {
			this.type = type;
		}

		/**
		 * @return the duration
		 */
		public String getDuration() {
			return duration;
		}

		/**
		 * @param duration the duration to set
		 */
		public void setDuration(String duration) {
			this.duration = duration;
		}

		/**
		 * @return the units
		 */
		public String getUnits() {
			return units;
		}

		/**
		 * @param units the units to set
		 */
		public void setUnits(String units) {
			this.units = units;
		}

		/**
		 * @return the sigmaValue
		 */
		public String getSigmaValue() {
			return sigmaValue;
		}

		/**
		 * @param sigmaValue the sigmaValue to set
		 */
		public void setSigmaValue(String sigmaValue) {
			this.sigmaValue = sigmaValue;
		}

		/**
		 * @return the sequence
		 */
		public String getSequence() {
			return sequence;
		}

		/**
		 * @param sequence the sequence to set
		 */
		public void setSequence(String sequence) {
			this.sequence = sequence;
		}

		/**
		 * @return the sampleType
		 */
		public String getSampleType() {
			return sampleType;
		}

		/**
		 * @param sampleType the sampleType to set
		 */
		public void setSampleType(String sampleType) {
			this.sampleType = sampleType;
		}

		/**
		 * @return the result
		 */
		public String getResult() {
			return result;
		}

		/**
		 * @param result the result to set
		 */
		public void setResult(String result) {
			this.result = result;
		}

		/**
		 * @return the flags
		 */
		public String getFlags() {
			return flags;
		}

		/**
		 * @param flags the flags to set
		 */
		public void setFlags(String flags) {
			this.flags = flags;
		}

		/**
		 * @return the color
		 */
		public String getColor() {
			return color;
		}

		/**
		 * @param color the color to set
		 */
		public void setColor(String color) {
			this.color = color;
		}

		/**
		 * @return the sku
		 */
		public String getSku() {
			return sku;
		}

		/**
		 * @param sku the sku to set
		 */
		public void setSku(String sku) {
			this.sku = sku;
		}

		/**
		 * @return the location
		 */
		public String getLocation() {
			return location;
		}

		/**
		 * @param location the location to set
		 */
		public void setLocation(String location) {
			this.location = location;
		}

		/**
		 * @return the misc
		 */
		public String getMisc() {
			return misc;
		}

		/**
		 * @param misc the misc to set
		 */
		public void setMisc(String misc) {
			this.misc = misc;
		}

		/**
		 * @return the note
		 */
		public String getNote() {
			return note;
		}

		/**
		 * @param note the note to set
		 */
		public void setNote(String note) {
			this.note = note;
		}

		/**
		 * @return the createUserId
		 */
		public String getCreateUserId() {
			return createUserId;
		}

		/**
		 * @param createUserId the createUserId to set
		 */
		public void setCreateUserId(String createUserId) {
			this.createUserId = createUserId;
		}

		/**
		 * @return the createTimestamp
		 */
		public Date getCreateTimestamp() {
			return createTimestamp;
		}

		/**
		 * @param createTimestamp the createTimestamp to set
		 */
		public void setCreateTimestamp(Date createTimestamp) {
			this.createTimestamp = createTimestamp;
		}

		/**
		 * @return the lastUpdateUserId
		 */
		public String getLastUpdateUserId() {
			return lastUpdateUserId;
		}

		/**
		 * @param lastUpdateUserId the lastUpdateUserId to set
		 */
		public void setLastUpdateUserId(String lastUpdateUserId) {
			this.lastUpdateUserId = lastUpdateUserId;
		}

		/**
		 * @return the lastUpdateTimestamp
		 */
		public Date getLastUpdateTimestamp() {
			return lastUpdateTimestamp;
		}

		/**
		 * @param lastUpdateTimestamp the lastUpdateTimestamp to set
		 */
		public void setLastUpdateTimestamp(Date lastUpdateTimestamp) {
			this.lastUpdateTimestamp = lastUpdateTimestamp;
		}

		/**
		 * @return the serialversionuid
		 */
		public static long getSerialversionuid() {
			return serialVersionUID;
		}
	}

