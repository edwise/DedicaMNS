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
public class MonthYearBean implements Serializable {

	/**
     * 
     */
	private static final long serialVersionUID = -4982937356681183043L;

	private List<String> months = null;
	private List<String> years = null;

	public MonthYearBean(List<String> months, List<String> years) {
		this.months = months;
		this.years = years;
	}

	public List<String> getMonths() {
		return months;
	}

	public void setMonths(List<String> months) {
		this.months = months;
	}

	public List<String> getYears() {
		return years;
	}

	public void setYears(List<String> years) {
		this.years = years;
	}

}
