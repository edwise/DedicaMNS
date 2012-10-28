package com.edwise.dedicamns.beans;

import java.io.Serializable;

public class DayRecord implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -749246389856673135L;
    
    private int dayNum;
    private String dayName;
    private String hours;
    private String projectId;
    private String subProject;
    private String task;
    private String addLink;
    private String editLink;
    private String removeLink;
    private Boolean isWeekend;
    private Boolean isHoliday;

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
	return hours;
    }

    public void setHours(String hours) {
	this.hours = hours;
    }

    public String getProjectId() {
	return projectId;
    }

    public void setProjectId(String projectId) {
	this.projectId = projectId;
    }

    public String getSubProject() {
	return subProject;
    }

    public void setSubProject(String subProject) {
	this.subProject = subProject;
    }

    public String getTask() {
	return task;
    }

    public void setTask(String task) {
	this.task = task;
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
	this.hours = null;
	this.projectId = null;
	this.subProject = null;
	this.task = null;
    }
    
    public void copyDayData(DayRecord dayRecord) {
	this.hours =  dayRecord.getHours();
	this.projectId = dayRecord.getProjectId();
	this.subProject = dayRecord.getSubProject();
	this.task = dayRecord.getTask();
    }
}
