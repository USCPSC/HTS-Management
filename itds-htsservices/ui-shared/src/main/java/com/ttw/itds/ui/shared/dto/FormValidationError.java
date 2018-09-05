/**
 * 
 */
package com.ttw.itds.ui.shared.dto;

/**
 * @author hzhao
 *
 */
public class FormValidationError extends ItdsUiBaseDto {
	private static final long serialVersionUID = -7069758586537196398L;

	private String group, field, index, message;
	
	

	public FormValidationError(String group, String field, String index, String message) {
		super();
		this.group = group;
		this.field = field;
		this.index = index;
		this.message = message;
	}

	public FormValidationError(String group, String field, int index, String message) {
		super();
		this.group = group;
		this.field = field;
		this.index = index+"";
		this.message = message;
	}

	public FormValidationError() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the group
	 */
	public String getGroup() {
		return group;
	}

	/**
	 * @param group the group to set
	 */
	public void setGroup(String group) {
		this.group = group;
	}

	/**
	 * @return the field
	 */
	public String getField() {
		return field;
	}

	/**
	 * @param field the field to set
	 */
	public void setField(String field) {
		this.field = field;
	}

	/**
	 * @return the index
	 */
	public String getIndex() {
		return index;
	}

	/**
	 * @param index the index to set
	 */
	public void setIndex(String index) {
		this.index = index;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	
}
