package gov.cpsc.hts.itds.ui.service.impl;

import java.util.Collection;
import java.util.HashMap;

import javax.inject.Inject;

public class HtsCsvLineMap {
	
    private HtsFloaterStack floaterStack;
	private HashMap<String, HtsCsvLine> inputHashMap;
	private static final String DELIMITER = ";";
	
	public HtsCsvLineMap() {
		this.floaterStack = new HtsFloaterStack();
		this.inputHashMap = new HashMap<String, HtsCsvLine>();
	}
	
	/**
	 * @param line may be null if previous floater was reset to null 
	 */
	public void admit(HtsCsvLine line) {
		if (!line.isHeader()) {
			if (line.isFloater()) {
				floaterStack.add(line);
			} else {
				line.applyFloaterString(floaterStack.getAtIndent(line).toString(), DELIMITER);
				inputHashMap.put(line.getHtsCode(), line);
			}
		}
	}
	
	public HtsCsvLine get(String code) {
		return inputHashMap.get(code);
	}
	
}
