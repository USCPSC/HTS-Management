package com.ttw.itds.sharedservice.dto.rules;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ttw.itds.ui.shared.dto.ItdsUiBaseDto;

/**
 * @author zlt633
 * @generated at : Feb 8, 2016 - 11:26:47 PM
 * @since: 1.0.0
 */
public class ScoredItem extends ItdsUiBaseDto {
	private static final long serialVersionUID = -979041667834968748L;
	public ScoredItem() {
		super();
	}
	
	/*
	 * IntegerScore method here is a instruction how to round number, nothing else
	 */
	@JsonIgnore
	public Integer getIntegerScore(){
		if( score == null || !score.isNaN() ){
			return null;
		} else {
			return (int)Math.round(score);
		}
	}
	
	/**
	 * ZTANG: unique name for the rule category regardless case.
	 */
	private String ruleCategoryName;
	public String getRuleCategoryName() {	return ruleCategoryName;	}
	public void setRuleCategoryName(String ruleCategoryName) {	this.ruleCategoryName = ruleCategoryName;	}
	
	private String ruleTypeName;
	public String getRuleTypeName() {	return ruleTypeName;	}
	public void setRuleTypeName(String ruleName) {	this.ruleTypeName = ruleName;	}
	
	/**
	 * ZTANG: unique name for the rule instance regardless case.
	 */
	private String ruleInstanceName;	
	public String getRuleInstanceName() {	return ruleInstanceName;	}
	public void setRuleInstanceName(String ruleInstanceName) {	this.ruleInstanceName = ruleInstanceName;	}
	
	
	private String ruleInstanceDisplayName;
	public String getRuleInstanceDisplayName() {	return ruleInstanceDisplayName;	}
	public void setRuleInstanceDisplayName(String ruleInstanceDisplayName) {
		this.ruleInstanceDisplayName = ruleInstanceDisplayName;
	}

	
	private Object ruleSourceObject;
	public Object getRuleSourceObject() {	return ruleSourceObject;	}
	public void setRuleSourceObject(Object ruleSourceObject) {
		this.ruleSourceObject = ruleSourceObject;
	}

	/**
	 * ZTANG: Some scoring activity requires to provide why/reason.
	 * This field is optional for most scoring activity
	 */
	private String reason;
	public String getReason() {	return reason;	}
	public void setReason(String reason) {	this.reason = reason;	}



	private Double rawScore;
	private Double score;
	private Double weight;  // rule category weight

	private String rawScoreString; // why only String get marshall/unmarshall correctly???
	private String scoreString;
	private String weightString;
	public Double getRawScore() {
		return rawScore;
	}

	public Double getScore() {
		return score;
	}

	public Double getWeight() {
		return weight;
	}

	public String getRawScoreString() {
		return rawScoreString;
	}

	public String getScoreString() {
		return scoreString;
	}

	public String getWeightString() {
		return weightString;
	}

	public void setRawScore(Double rawScore) {
		this.rawScore = rawScore;
	}

	public void setScore(Double score) {
		this.score = score;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	public void setRawScoreString(String rawScoreString) {
		this.rawScoreString = rawScoreString;
	}

	public void setScoreString(String scoreString) {
		this.scoreString = scoreString;
	}

	public void setWeightString(String weightString) {
		this.weightString = weightString;
	}


}
