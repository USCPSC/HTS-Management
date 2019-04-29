package gov.cpsc.hts.itds.sharedservice.dto.rules;

/**
 * @author hzhao
 */
public class ComputedNationalOpItem {

	public ComputedNationalOpItem() {
		super();
	}
	
	// result of nationalOperation calculation
	private String nationalOpCode;
	public String getNationalOpCode() {
		return nationalOpCode;
	}
	public void setNationalOpCode(String nationalOpCode) {
		this.nationalOpCode = nationalOpCode;
	}

	// the request object
	private AtomicOperation opSourceObject;
	public AtomicOperation getRuleSourceObject() {	return opSourceObject;	}
	public void setRuleSourceObject(AtomicOperation opSourceObject) {
		this.opSourceObject = opSourceObject;
	}

	private String reason;
	public String getReason() {	return reason;	}
	public void setReason(String reason) {	this.reason = reason;	}
	
}
