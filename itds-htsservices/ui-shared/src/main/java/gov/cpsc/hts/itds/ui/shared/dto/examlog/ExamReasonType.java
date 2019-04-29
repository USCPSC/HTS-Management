
package gov.cpsc.hts.itds.ui.shared.dto.examlog;

import gov.cpsc.hts.itds.ui.shared.dto.ItdsUiBaseDto;

/**
 * @author hzhao
 *
 */
public class ExamReasonType extends ItdsUiBaseDto {
	private static final long serialVersionUID = 7784406721665428235L;

	private String reasonCode;
	private String reasonName;
	
	
	public String getReasonCode() {
		return reasonCode;
	}
	public void setReasonCode(String reasonCode) {
		this.reasonCode = reasonCode;
	}
	public String getReasonName() {
		return reasonName;
	}
	public void setReasonName(String reasonName) {
		this.reasonName = reasonName;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
}
