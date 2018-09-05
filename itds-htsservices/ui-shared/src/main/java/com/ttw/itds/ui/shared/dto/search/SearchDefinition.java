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
 * @author hzhao
 * Specify JPQL search condition.
 * Search, filter, pagination input
 * 
 * searchResultsIDList = search Id List<Ids> getCount(search, returnIds?, maxIds<3000): execute the search; return records count. If Ids are required, return the Ids for 1..maxIds, up to 3000(a configuration parameter);
   select count(id) from inbox where entryDate > now-3days order by abc
   select top 3000 id from inbox where entryDate > now-3days order by abc
   
filteredSearchResultsIDList = filter Id List<Ids> getCount(filter, IdList, maxIds<3000): execute the filter against IdList; return records count. If Ids are required, return the Ids for 1..maxIds, up to 3000(a configuration parameter);
   select top 3000 id from inbox 
   where portCode like '12%' and id in (searchResultsIDList)

   select top 3000 id from inbox 
   where portCode like '12%' and id in (select id from tempSearchResultsTable) -- if too mary rows >3000
   
   multipageIDList = filteredSearchResultsIDList[startIndx, startIndex+rangeSize125]
getPaginationBufferData(multipageIDList: a subset of filteredSearchResultsIDList): retreive the data from the table using IdList; 
   select * from inbox 
   where id in (multipageIDList)

 */
public class SearchDefinition extends ItdsUiBaseDto {
	private static final Logger logger = Logger.getLogger(SearchDefinition.class);
		
	public static final String ACTION_SEARCH = "SEARCH";
	public static final String ACTION_FILTER = "FILTER";
	public static final String ACTION_GET = "GET";
	public static final String ACTION_COUNT = "COUNT";
	
	private String action; //SEARCH, FILTER, GET, COUNT
	private List ids; // help filtering ?  
	
	private Long searchId;
	private String username;
	private String searchName;
	private String searchDesc;
	private String searchAvailability;
	private String searchTarget;
	private int displayOrder;
	
	private List<SearchCriteria> searchCriteriaList;
//	private List<SearchCriteria> fitlerItems;
	private List<OrderByItem> orderBys; // inherent order defined by the query
	private List<OrderByItem> adhocOrderBys; // allow user to re-sort by clicking data table headings
	
	private String orderBysDesc;
//		private List<Object> searchReturnIds; 
//		private List<Object> filterReturnIds; 
	private Integer maxLength;  // TODO: from config? or each user defines their own?
	
	
	
	@Override
	public String toString() {
		final int maxLen = 15;
		return "SearchDefinition [action=" + action + ", ids="
				+ (ids != null ? ids.subList(0, Math.min(ids.size(), maxLen)) : null) + ", searchId=" + searchId
				+ ", username=" + username + ", searchName=" + searchName + ", searchDesc=" + searchDesc
				+ ", searchAvailability=" + searchAvailability + ", searchTarget=" + searchTarget + ", displayOrder="
				+ displayOrder + ", searchCriteriaList="
				+ (searchCriteriaList != null
						? searchCriteriaList.subList(0, Math.min(searchCriteriaList.size(), maxLen)) : null)
				+ ", orderBys=" + (orderBys != null ? orderBys.subList(0, Math.min(orderBys.size(), maxLen)) : null)
				+ ", adhocOrderBys="
				+ (adhocOrderBys != null ? adhocOrderBys.subList(0, Math.min(adhocOrderBys.size(), maxLen)) : null)
				+ ", orderBysDesc=" + orderBysDesc + ", maxLength=" + maxLength + "]";
	}
	public List<SearchCriteria> getSearchCriteriaList() {
		return searchCriteriaList;
	}
	public void setSearchCriteriaList(List<SearchCriteria> searchCriteriaList) {
		this.searchCriteriaList = searchCriteriaList;
	}
/*
	public List<SearchCriteria> getFitlerItems() {
		return fitlerItems;
	}
	public void setFitlerItems(List<SearchCriteria> fitlerItems) {
		this.fitlerItems = fitlerItems;
	}
*/
	public List<OrderByItem> getOrderBys() {
		return orderBys;
	}
	public void setOrderBys(List<OrderByItem> orderBys) {
		this.orderBys = orderBys;
	}

	public String getOrderBysDesc() {
		return orderBysDesc;
	}
	public void setOrderBysDesc(String orderBysDesc) {
		this.orderBysDesc = orderBysDesc;
	}

	// for filter use?
	public List getIds() {
		return ids;
	}
	public void setIds(List ids) {
		this.ids = ids;
	}

	public Integer getMaxLength() {
		return maxLength;
	}
	public void setMaxLength(Integer maxLength) {
		this.maxLength = maxLength;
	}

	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}

	public static String getActionGet() {
		return ACTION_GET;
	}

	public String getSearchName() {
		return searchName;
	}
	public void setSearchName(String searchName) {
		this.searchName = searchName;
	}
	
	public String getSearchDesc() {
		return searchDesc;
	}
	public void setSearchDesc(String searchDesc) {
		this.searchDesc = searchDesc;
	}
	
	public String getSearchAvailability() {
		return searchAvailability;
	}
	public void setSearchAvailability(String searchAvailability) {
		this.searchAvailability = searchAvailability;
	}
	
	public String getSearchTarget() {
		return searchTarget;
	}
	public void setSearchTarget(String searchTarget) {
		this.searchTarget = searchTarget;
	}
	
	public int getDisplayOrder() {
		return displayOrder;
	}
	public void setDisplayOrder(int displayOrder) {
		this.displayOrder = displayOrder;
	}

	public Long getSearchId() {
		return searchId;
	}
	public void setSearchId(Long searchId) {
		this.searchId = searchId;
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public List<OrderByItem> getAdhocOrderBys() {
		return adhocOrderBys;
	}
	public void setAdhocOrderBys(List<OrderByItem> adhocOrderBys) {
		this.adhocOrderBys = adhocOrderBys;
	}
}