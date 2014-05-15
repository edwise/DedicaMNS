/**
 * 
 */
package com.edwise.dedicamns.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author edwise
 * 
 */
public class MonthReportBean implements Serializable {

	/**
     * 
     */
	private static final long serialVersionUID = -3947266002963067502L;

	private String monthName;
	private String year;
	private List<MonthReportRecord> monthReportRecords;
	private String total;

	public MonthReportBean(String monthName, String year) {
		this.monthName = monthName;
		this.year = year;
		this.monthReportRecords = new ArrayList<MonthReportRecord>();
	}

	public String getMonthName() {
		return monthName;
	}

	public String getYear() {
		return year;
	}

	public List<MonthReportRecord> getMonthReportRecords() {
		return monthReportRecords;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public void addMonthReportRecord(MonthReportRecord monthReportRecord) {
		this.monthReportRecords.add(monthReportRecord);
	}
}
