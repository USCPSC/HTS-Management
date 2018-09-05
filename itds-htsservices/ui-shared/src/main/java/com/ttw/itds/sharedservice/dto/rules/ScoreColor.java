package com.ttw.itds.sharedservice.dto.rules;

import com.ttw.itds.ui.shared.dto.ItdsUiBaseDto;

/**
 * @author hzhao
 *
 */
public class ScoreColor extends ItdsUiBaseDto {

	private static final long serialVersionUID = 5519735103406594751L;
	
	private String color;
    private String rgb;
    private String textRgb;
    private Integer percentile;
    private Integer minScore;
    private String description;
    private String ramScoreVersion;
	/**
	 * @return the color
	 */
	public String getColor() {
		return color;
	}
	/**
	 * @param color the color to set
	 */
	public void setColor(String color) {
		this.color = color;
	}
	/**
	 * @return the rgb
	 */
	public String getRgb() {
		return rgb;
	}
	/**
	 * @param rgb the rgb to set
	 */
	public void setRgb(String rgb) {
		this.rgb = rgb;
	}
	/**
	 * @return the textRgb
	 */
	public String getTextRgb() {
		return textRgb;
	}
	/**
	 * @param textRgb the textRgb to set
	 */
	public void setTextRgb(String textRgb) {
		this.textRgb = textRgb;
	}
	/**
	 * @return the percentile
	 */
	public Integer getPercentile() {
		return percentile;
	}
	/**
	 * @param percentile the percentile to set
	 */
	public void setPercentile(Integer percentile) {
		this.percentile = percentile;
	}
	/**
	 * @return the minScore
	 */
	public Integer getMinScore() {
		return minScore;
	}
	/**
	 * @param minScore the minScore to set
	 */
	public void setMinScore(Integer minScore) {
		this.minScore = minScore;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return the ramScoreVersion
	 */
	public String getRamScoreVersion() {
		return ramScoreVersion;
	}
	/**
	 * @param ramScoreVersion the ramScoreVersion to set
	 */
	public void setRamScoreVersion(String ramScoreVersion) {
		this.ramScoreVersion = ramScoreVersion;
	}
	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
