/**
 * 
 */
package com.ttw.itds.ui.shared.dto.search;

import java.util.List;

import com.ttw.itds.ui.shared.dto.ItdsUiBaseDto;

/**
 * @author hzhao
 *
 */
public class SearchReturn extends ItdsUiBaseDto {
	private static final long serialVersionUID = -4444630317462972684L;
	
	private Long count; // total count
	//private List ids;   //  Ids up to a configured max
	private List<? extends ItdsUiBaseDto> data;  // one or more pages of actual data

	
	
	/**
	 * @return the count
	 */
	public Long getCount() {
		return count;
	}
	/**
	 * @param count the count to set
	 */
	public void setCount(Long count) {
		this.count = count;
	}
//	/**
//	 * @return the ids
//	 */
//	public List getIds() {
//		return ids;
//	}
//	/**
//	 * @param ids the ids to set
//	 */
//	public void setIds(List ids) {
//		this.ids = ids;
//	}
	/**
	 * @return the data
	 */
	public List<? extends ItdsUiBaseDto> getData() {
		return data;
	}
	/**
	 * @param entryLines the data to set
	 */
	public void setData(List<? extends ItdsUiBaseDto> entryLines) {
		this.data = entryLines;
	}

}
