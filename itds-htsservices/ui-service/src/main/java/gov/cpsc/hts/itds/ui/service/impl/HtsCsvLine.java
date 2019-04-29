package gov.cpsc.hts.itds.ui.service.impl;

import java.util.ArrayList;
import java.util.List;

import gov.cpsc.hts.itds.ui.service.exceptions.CpscHtsMgmtCodeEnum;
import gov.cpsc.hts.itds.ui.service.exceptions.CpscHtsMgmtException;

public class HtsCsvLine {
	
	private int lineNumber;
	private String htsCode;
	private int indentation;
	private String descriptionFragment; // may include newline, LF, CR chars
	private String rawInput;
	private boolean isHeader;
	
	public HtsCsvLine(String rawInput, int lineNumber) {
		this.rawInput = rawInput;
		this.lineNumber = lineNumber;
		setHeader(lineNumber == 1 && (rawInput == null || rawInput.isEmpty() || 
				rawInput.length() < 9 || rawInput.charAt(0) != '"' || !Character.isDigit(rawInput.charAt(1))));
		parseRawInputAndCalculate();
	}
	
	public String getHtsCode() {
		return htsCode;
	}
	private void setHtsCode(String htsCode) {
		if (htsCode == null) {
			this.htsCode = "";
		} else {
			this.htsCode = htsCode.replace(".", "").trim(); // trim is for trailing space in spreadsheet row 4608
		}
	}
	public int getIndentation() {
		return indentation;
	}
	private void setIndentation(int indentation) {
		this.indentation = indentation;
	}
	public String getDescriptionFragment() {
		return descriptionFragment;
	}
	private void setDescriptionFragment(String descriptionFragment) {
		this.descriptionFragment = descriptionFragment;
	}
	public String getRawInput() {
		return rawInput;
	}
	public boolean isHeader() {
		return isHeader;
	}
	public void setHeader(boolean isHeader) {
		this.isHeader = isHeader;
	}
	
	public void appendSpilloverLine(String spilloverLine) {
		if (rawInput.length() < 10000) { // safety precaution
			rawInput += spilloverLine;
		}
		parseRawInputAndCalculate();
	}
	
	public boolean isFloater() {
		return (getHtsCode() == null ? true : getHtsCode().isEmpty());
	}
	
	public int getCodeType() {
		return (getHtsCode() == null ? 0 : getHtsCode().length()); // 0 == floater
	}
	
	private void parseRawInputAndCalculate() throws CpscHtsMgmtException {
		if (isHeader()) {
			setIndentation(0);
			setHtsCode("");
			setDescriptionFragment("");
		} else {
			rawInput = sanitizeRawInput(rawInput);
			if (rawInput != null && rawInput.endsWith("\"")) {
				List<String> cells = parseRawInput();
				if (cells == null || cells.isEmpty() || cells.size() < 3) {
					throw new CpscHtsMgmtException(CpscHtsMgmtCodeEnum.EXCEPTION_PARSING_LINE, lineNumber, "line has too few cells");
				}
				try {
					// in the published USITC CSVs seen so far, the following regexp matches no line other than the header line:
					// ^"[0-9\.]*","[^0-9]
					setIndentation(Integer.parseInt(cells.get(1))); // -1 to prevent the descriptionParts from being changed
				} catch (NumberFormatException nfe) {
					throw new CpscHtsMgmtException(CpscHtsMgmtCodeEnum.EXCEPTION_PARSING_INDENT, lineNumber, cells.get(1), nfe);
				}
				setHtsCode(cells.get(0));
				setDescriptionFragment(cells.get(2));
			} // else do nothing, because appendSpilloverLine has not yet been called for the last time
		}
	}
	
	public String sanitizeRawInput(String lineOrFragment) {
		if (lineOrFragment != null) {
			int len = lineOrFragment.length();
			if (lineOrFragment.endsWith(",") && len > 2) {
				int numOfTrailingCommasSeen = 1;
				int i = len - 2;
				while (i >= 0 && lineOrFragment.charAt(i) == ',') {
					numOfTrailingCommasSeen++;
					i--;
				}
				String a = lineOrFragment.substring(0, len - numOfTrailingCommasSeen);
				StringBuilder b = new StringBuilder(",\"\"");
				while (numOfTrailingCommasSeen > 1) {
					b.append(",\"\"");
					numOfTrailingCommasSeen--;
				}
				lineOrFragment = a + b.toString();
			}
		}
		return lineOrFragment;
	}
	
	/**
	 * Interprets a raw input line from the uploaded/imported CSV spreadsheet and outputs a list
	 * whose elements correspond to the important cells in the raw line. The elements are also
	 * cleaned.
	 * 
	 * In order to ensure that undesirable characters (e.g. whitespace characters other than the
	 * space character itself, U+0020) never enter the scratch table's 3 description columns from
	 * the raw content of the spreadsheet, this method must clean the Strings returned. For example, 
	 * the JSON that is returned to the client in a REST get response will be invalid if it contains
	 * U+0009 (the horizonal tab).
	 * 
	 * @return a list of Strings corresponding to the useful cells of the imported spreadsheet
	 * @throws CpscHtsMgmtException
	 */
	private List<String> parseRawInput() throws CpscHtsMgmtException {
		List<String> result = new ArrayList<>();
		if (rawInput == null) {
			return result;
		}
		rawInput = rawInput.trim();
		if (rawInput.isEmpty()) {
			return result;
		}
		char customQuote = '"';
		StringBuilder curVal = new StringBuilder();
		boolean inQuotes = false;
		boolean startCollectChar = false;
		boolean doubleQuotesInColumn = false;
		char[] chars = rawInput.toCharArray();		
		try {
			for (char ch : chars) {
				if (inQuotes) {
					startCollectChar = true;
					if (ch == customQuote) {
						inQuotes = false;
						doubleQuotesInColumn = false;
					} else {
						if (ch == '\"') {
							if (!doubleQuotesInColumn) {
								curVal.append(ch);
								doubleQuotesInColumn = true;
							}
						} else if (isSanitizeable(ch)) {
							curVal.append(sanitized(ch));
						}
					}
				} else {
					char separators = ',';
					if (ch == customQuote) {
						inQuotes = true;
						if (chars[0] != '"' && customQuote == '\"') {
							curVal.append('"');
						}
						if (startCollectChar) {
							curVal.append('"');
						}
					} else if (ch == separators) {
						result.add(curVal.toString());
						curVal = new StringBuilder();
						startCollectChar = false;
					} else if (ch == '\r') {
						continue;
					} else if (ch == '\n') {
						break;
					} else if (isSanitizeable(ch)) {
						curVal.append(sanitized(ch));
					}
				}
			}
		} catch (Exception e) {
			throw new CpscHtsMgmtException(CpscHtsMgmtCodeEnum.EXCEPTION_PARSING_LINE, lineNumber, "failed to parse line", e);
		}
		result.add(curVal.toString());
		return result;
	}
	
	private boolean isSanitizeable(char ch) {
		// return FALSE for the Unicode control characters, EXCEPT the 6 control characters that are also whitespace:
		return !(ch <= 0x08 || (ch >= 0x0e && ch <= 0x1f) || (ch >= 0x7f && ch <= 0x84) || (ch >= 0x86 && ch <= 0x9f));
	}
	
	private char sanitized(char ch) {
		// if "low" whitespace other than space, return space:
		if ((ch >= 0x09 && ch <= 0x0d) || ch == 0x85 || ch == 0xa0) {
			return ' '; // 0x20
		} else {
			return ch;
		}
	}
	
	public void applyFloaterString(String applicableFloaterPrefix, String delimiter) {
		if (applicableFloaterPrefix != null && !applicableFloaterPrefix.isEmpty()) {
			String myDescFrag = getDescriptionFragment();
			if (myDescFrag != null && !myDescFrag.isEmpty()) {
				setDescriptionFragment(applicableFloaterPrefix + delimiter + myDescFrag);
			} else {
				setDescriptionFragment(applicableFloaterPrefix);
			}
		}
	}
	
	private boolean likelyToBeData() throws CpscHtsMgmtException {
		List<String> parsedLine = parseRawInput();
    	if (parsedLine == null || parsedLine.isEmpty() || parsedLine.get(0) == null || parsedLine.get(0).isEmpty() 
    			|| parsedLine.get(0).length() < 2) {
    		return false;
    	} else {
    		return parsedLine.get(0).charAt(0) == '"' &&
    				(Character.isDigit(parsedLine.get(0).charAt(1)) || parsedLine.get(0).charAt(1) == '"');
    	}
	}

}
