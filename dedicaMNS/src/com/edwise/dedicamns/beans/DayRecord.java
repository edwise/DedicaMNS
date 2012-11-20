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

    private String addLink;
    private String editLink;
    private String removeLink;

    private Boolean isWeekend = Boolean.FALSE;
    private Boolean isHoliday = Boolean.FALSE;

    private boolean toRemove;

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

    // TODO quitar estos getters, que están por ahora para que funcionen ciertos mockeos.
    public String getProjectId() {
	return this.activities.size() == 0 ? null : this.activities.get(0).getProjectId();
    }

    public String getSubProject() {
	return this.activities.size() == 0 ? null : this.activities.get(0).getSubProject();
    }

    public String getTask() {
	return this.activities.size() == 0 ? null : this.activities.get(0).getTask();
    }

    public String getAddLink() {
	return addLink;
    }

    public void setAddLink(String addLink) {
	this.addLink = addLink;
    }

    public String getEditLink() {
	return editLink;
    }

    public void setEditLink(String editLink) {
	this.editLink = editLink;
    }

    public String getRemoveLink() {
	return removeLink;
    }

    public void setRemoveLink(String removeLink) {
	this.removeLink = removeLink;
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

    public int describeContents() {
	return 0;
    }

    public boolean isToRemove() {
	return toRemove;
    }

    public void setToRemove(boolean toRemove) {
	this.toRemove = toRemove;
    }

    public void clearDay() {
	this.totalHours = null;
	this.activities.clear();
    }

    public void copyDayData(DayRecord dayRecord) {
	this.totalHours = dayRecord.getHours();
	this.activities.clear();
	this.activities = dayRecord.getActivities();
    }
}
