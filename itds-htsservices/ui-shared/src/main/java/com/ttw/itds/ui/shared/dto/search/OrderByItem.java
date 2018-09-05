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
public class OrderByItem extends ItdsUiBaseDto {
	@Override
	public String toString() {
		return "OrderByItem [field=" + field + ", fieldDisp=" + fieldDisp + ", sortRank=" + sortRank
				+ ", sortDirection=" + sortDirection + ", sortDisp=" + sortDisp + "]";
	}
	private static final Logger logger = Logger.getLogger(OrderByItem.class);
	
	private String field; // JPA attribute name
	private String fieldDisp; // user friendly field name
	private String sortRank;  // first sort order, second sort order, third, 
	private String sortDirection; // asc or desc
	private String sortDisp; // user friendly sort description
	
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
	
	public String getSortRank() {
		return sortRank;
	}
	public void setSortRank(String sortRank) {
		this.sortRank = sortRank;
	}

	public String getSortDirection() {
		return sortDirection;
	}
	public void setSortDirection(String sortDirection) {
		this.sortDirection = sortDirection;
	}

	public String getSortDisp() {
		return sortDisp;
	}
	public void setSortDisp(String sortDisp) {
		this.sortDisp = sortDisp;
	}
	
/*	
	private static final Logger logger = Logger.getLogger(OrderByItem.class);
	
	private String field;
	private String order; // ASC, DESC

	public String getField() {
		return field;
	}
	public void setField(String field) {
		this.field = field;
	}

	public String getOrder() {
		return order;
	}
	public void setOrder(String order) {
		this.order = order;
	}
*/
}
