package com.ttw.itds.ui.shared.dto.search;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;

import com.ttw.itds.ui.shared.dto.ItdsUiBaseDto;

/**
 * 
 * @author cpan
 *
 */
public class SearchCriteria extends ItdsUiBaseDto {
	private static final Logger logger = Logger.getLogger(SearchCriteria.class);
	
	private String type; // STRING, FLOAT, BOOLEAN, DATE, WORKFLOW_STATUS
	private String concat; // null if only one or first one in the searchFor list 
	private String field; // JPA attribute name
	private String fieldDisp; // user friendly field name
	private String op;  // LIKE, =, <, >, <>
	private String opDisp; // user friendly operator name
	private Object value;
	private Object valueDisp;
	
	@Override
	public String toString() {
		return "SearchCriteria [type=" + type + ", concat=" + concat + ", field=" + field + ", fieldDisp=" + fieldDisp
				+ ", op=" + op + ", opDisp=" + opDisp + ", value=" + value + ", valueDisp=" + valueDisp + "]";
	}
	public String getType() {
		return type;
	}
	public void setType(String fieldType) {
		this.type = fieldType;
	}

	public String getConcat() {
		return concat;
	}
	public void setConcat(String concat) {
		this.concat = concat;
	}

	public String getField() {
		return field;
	}
	public void setField(String field) {
		this.field = field;
	}

	public String getFieldDisp() {
		return fieldDisp;
	}
	public void setFieldDisp(String fieldDisp) {
		this.fieldDisp = fieldDisp;
	}
	
	public String getOp() {
		return op;
	}
	public void setOp(String op) {
		this.op = op;
	}

	public String getOpDisp() {
		return opDisp;
	}
	public void setOpDisp(String opDisp) {
		this.opDisp = opDisp;
	}

	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
	
	public Object getValueDisp() {
		return valueDisp;
	}
	public void setValueDisp(Object valueDisp) {
		this.valueDisp = valueDisp;
	}
	
}
