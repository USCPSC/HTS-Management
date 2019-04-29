package gov.cpsc.hts.itds.ui.service.impl;

public class HtsGlobalStateSummary {
	
	private final HtsGlobalStateEnum state;
	private final String summary;
	
	public HtsGlobalStateSummary(HtsGlobalStateEnum state, String summary) {
		this.state = state;
		this.summary = summary;
	}

	public HtsGlobalStateEnum getState() {
		return state;
	}

	public String getSummary() {
		return summary;
	}

}
