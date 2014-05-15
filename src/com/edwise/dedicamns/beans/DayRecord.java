package com.edwise.dedicamns.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DayRecord implements Serializable {

	/**
     * 
     */
	private static final long serialVersionUID = -749246389856673135L;

	private int dayNum;
	private String dayName;
	private String totalHours;
	private List<ActivityDay> activities = new ArrayList<ActivityDay>();

	private Boolean isWeekend = Boolean.FALSE;
	private Boolean isHoliday = Boolean.FALSE;

	private String dateForm;

	public int getDayNum() {
		return dayNum;
	}

	public void setDayNum(int dayNum) {
		this.dayNum = dayNum;
	}

	public String getDayName() {
		return dayName;
	}

	public void setDayName(String dayName) {
		this.dayName = dayName;
	}

	public String getHours() {
		return totalHours;
	}

	public void setHours(String hours) {
		this.totalHours = hours;
	}

	public List<ActivityDay> getActivities() {
		return activities;
	}

	public Boolean getIsWeekend() {
		return isWeekend;
	}

	public void setIsWeekend(Boolean isWeekend) {
		this.isWeekend = isWeekend;
	}

	public Boolean getIsHoliday() {
		return isHoliday;
	}

	public void setIsHoliday(Boolean isHoliday) {
		this.isHoliday = isHoliday;
	}

	public String getDateForm() {
		return dateForm;
	}

	public void setDateForm(String dateForm) {
		this.dateForm = dateForm;
	}

	public void clearDay() {
		this.totalHours = "00:00";
		this.activities.clear();
	}

	public void copyDayData(DayRecord dayRecord) {
		if (this != dayRecord) { // Si es el mismo objeto, no hacemos nada
			this.totalHours = dayRecord.getHours();
			this.activities.clear();
			this.activities = dayRecord.getActivities();
		}
	}
	
	public void copyDayDataCloned(DayRecord dayRecord) {
		this.totalHours = dayRecord.getHours();
		this.activities = new ArrayList<ActivityDay>();
		for (ActivityDay activityDay: dayRecord.getActivities()) {
			ActivityDay clonedActivityDay = new ActivityDay(activityDay);
			this.activities.add(clonedActivityDay);
		}
	}

	public boolean isEmptyDay() {
		return this.activities == null || this.activities.size() == 0;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dateForm == null) ? 0 : dateForm.hashCode());
		result = prime * result + ((dayName == null) ? 0 : dayName.hashCode());
		result = prime * result + dayNum;
		result = prime * result + ((isHoliday == null) ? 0 : isHoliday.hashCode());
		result = prime * result + ((isWeekend == null) ? 0 : isWeekend.hashCode());
		result = prime * result + ((totalHours == null) ? 0 : totalHours.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DayRecord other = (DayRecord) obj;
		if (dateForm == null) {
			if (other.dateForm != null)
				return false;
		} else if (!dateForm.equals(other.dateForm))
			return false;
		if (dayName == null) {
			if (other.dayName != null)
				return false;
		} else if (!dayName.equals(other.dayName))
			return false;
		if (dayNum != other.dayNum)
			return false;
		if (isHoliday == null) {
			if (other.isHoliday != null)
				return false;
		} else if (!isHoliday.equals(other.isHoliday))
			return false;
		if (isWeekend == null) {
			if (other.isWeekend != null)
				return false;
		} else if (!isWeekend.equals(other.isWeekend))
			return false;
		if (totalHours == null) {
			if (other.totalHours != null)
				return false;
		} else if (!totalHours.equals(other.totalHours))
			return false;
		return true;
	}

}
