package gov.cpsc.hts.itds.ui.service.impl;

import java.util.ArrayList;
import java.util.Stack;

public class HtsFloaterStack {

	private class LineTracker {

		private HtsCsvLine line;
		private ArrayList<String> appliedCodes;

		public HtsCsvLine getLine() {return line;}
		public ArrayList<String> getAppliedCodes() {return appliedCodes;}

		public LineTracker(HtsCsvLine newLine) { 
			line = newLine;
			appliedCodes = new ArrayList<String>();
		}

		public boolean appliedToAncestor(String htsCode) {
			// see if the code or any of its ancestors is in the list
			// if so, return true else false
			for (int strlen = htsCode.length(); strlen > 0; strlen -= 2) {
				if (appliedCodes.contains(htsCode.substring(0,strlen)))
					return true;
			}
			return false;
		}
	}

	private Stack<LineTracker> floaterStack;

	public HtsFloaterStack( ) {
		floaterStack = new Stack<LineTracker>();
	}
	
	// this only is called for non-floaters - it's job is to return the prefix string needed by 
	// identifying and concatenating the correct floaters that are active in the stack
	StringBuffer getAtIndent(HtsCsvLine nonFloaterLine) {
		StringBuffer descriptionFragment = new StringBuffer("");
		int lineIndent = nonFloaterLine.getIndentation();
		// pop any floaters that are no longer relevant
		while (!floaterStack.empty() && floaterStack.peek().getLine().getIndentation() >= lineIndent) {
			floaterStack.pop();
			if (floaterStack.empty()) break;
		}
		// now concatenate  floaters and return the string
		for (LineTracker lt : floaterStack) {
			// has this floater ever been applied to an ancestor of this code?
			// if not, then append the floater to the description fragment to be returned
			if (!lt.appliedToAncestor(nonFloaterLine.getHtsCode())) {
				descriptionFragment.append(lt.getLine().getDescriptionFragment());
				lt.getAppliedCodes().add(nonFloaterLine.getHtsCode());
			}
		}
		return descriptionFragment;
	}
	
	void add(HtsCsvLine floaterLine) {
		int newIndent = floaterLine.getIndentation();
		// walk down stack and remove all items at indent >= the new floaterLine indent
		// business rule is that any floater with indent >= new floater is knocked out, otherwise floater is added to stack
		while (!floaterStack.empty() && floaterStack.peek().getLine().getIndentation() >= newIndent) {
			floaterStack.pop();
			if (floaterStack.empty()) break;
		}
		floaterStack.push(new LineTracker(floaterLine));
	}
	
}
