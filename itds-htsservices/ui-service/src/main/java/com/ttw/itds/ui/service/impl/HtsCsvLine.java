package com.ttw.itds.ui.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.ttw.itds.ui.service.exceptions.CpscHtsMgmtCodeEnum;
import com.ttw.itds.ui.service.exceptions.CpscHtsMgmtException;

public class HtsCsvLine {
	
	private int lineNumber;
	private String htsCode;
	private int indentation;
	private String descriptionFragment; // may include newline, LF, CR chars
	private String rawInput;
	private boolean isHeader;
	
	public HtsCsvLine(String rawInput, int lineNumber) {
		this(rawInput, lineNumber, false);
	}
	
	private HtsCsvLine(String rawInput, int lineNumber, boolean dryRun) {
		setRawInput(rawInput);
		setLineNumber(lineNumber);
		setHeader(lineNumber == 1 && (rawInput == null || rawInput.isEmpty() || 
				rawInput.length() < 9 || rawInput.charAt(0) != '"' || !Character.isDigit(rawInput.charAt(1))));
		if (!dryRun) {
			parseRawInputAndCalculate();
		}
	}
	
	private int getLineNumber() {
		return lineNumber;
	}
	private void setLineNumber(int lineNumber) {
		this.lineNumber = lineNumber;
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
	private void setRawInput(String rawInput) {
		this.rawInput = rawInput;
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
		} else if (rawInput.endsWith("\"")) {
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
		StringBuffer curVal = new StringBuffer();
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
						} else {
							curVal.append(ch);
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
						curVal = new StringBuffer();
						startCollectChar = false;
					} else if (ch == '\r') {
						continue;
					} else if (ch == '\n') {
						break;
					} else {
						curVal.append(ch);
					}
				}
			}
		} catch (Exception e) {
			throw new CpscHtsMgmtException(CpscHtsMgmtCodeEnum.EXCEPTION_PARSING_LINE, lineNumber, "failed to parse line", e);
		}
		result.add(curVal.toString());
		return result;
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
	
	public boolean likelyToBeData() throws CpscHtsMgmtException {
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
