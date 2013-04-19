/**
 * 
 */
package com.edwise.dedicamns.beans;

import java.io.Serializable;
import java.util.List;

/**
 * @author edwise
 * 
 */
public class MonthListBean implements Serializable {

	/**
     * 
     */
	private static final long serialVersionUID = 1494769003330926569L;

	private String monthName;
	private String year;
	private List<DayRecord> listDays = null;

	public MonthListBean(String monthName, String year, List<DayRecord> listDays) {
		this.monthName = monthName;
		this.year = year;
		this.listDays = listDays;
	}

	public List<DayRecord> getListDays() {
		return listDays;
	}

	public String getMonthName() {
		return monthName;
	}

	public String getYear() {
		return year;
	}

}
