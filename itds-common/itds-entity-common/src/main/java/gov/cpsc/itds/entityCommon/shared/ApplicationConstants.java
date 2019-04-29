/**
 * 
 */

/**
 * @author cpan
 *
 */

package gov.cpsc.itds.entityCommon.shared;

public interface ApplicationConstants {

	public interface DateFormats {
		public static final String mmddyyyy = "MM/dd/yyyy";
		public static final String[] datePatterns = new String[]{mmddyyyy};
	}

	public interface InboxUserInterface {
		public static final int DEFAULT_START_INDEX_INT = 0;
		public static final String DEFAULT_START_INDEX_STRING = String.valueOf(DEFAULT_START_INDEX_INT);
		public static final int DEFAULT_NUMBER_OF_RECORDS_INT = 10;
		public static final String DEFAULT_NUMBER_OF_RECORDS_STRING = String.valueOf(DEFAULT_NUMBER_OF_RECORDS_INT);
	}
	
	
}
