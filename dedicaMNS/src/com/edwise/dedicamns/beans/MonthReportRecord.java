/**
 * 
 */
package com.edwise.dedicamns.beans;

import java.io.Serializable;

/**
 * @author edwise
 * 
 */
public class MonthReportRecord implements Serializable {

	/**
     * 
     */
	private static final long serialVersionUID = -3398888705843950328L;

	private String projectId;
	private String hours;

	public MonthReportRecord(String projectId, String hours) {
		this.projectId = projectId;
		this.hours = hours;
	}

	public String getProjectId() {
		return projectId;
	}

	public String getHours() {
		return hours;
	}

}
