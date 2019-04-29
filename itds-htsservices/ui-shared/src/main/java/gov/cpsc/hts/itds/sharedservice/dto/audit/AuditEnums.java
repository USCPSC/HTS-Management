package gov.cpsc.hts.itds.sharedservice.dto.audit;


/**
 * @author zlt633
 * @generated at : Dec 30, 2015 - 11:13:34 PM
 * @since: 1.0.0
 */
public class AuditEnums {

	public enum NetworkAccessPointEnum {
		MACHINE_NAME,
		IP_ADDRESS,
		TELEPHONE_NUMBER;
	}
	
	
	public enum EventActionEnum {
		Create,
		Read,
		Update,
		Delete, 
		Execute;
	}
}
