/**
 * 
 */
package com.edwise.dedicamns.beans;

import java.io.Serializable;

/**
 * @author edwise
 * 
 */
public class ActivityDay implements Serializable {

    private static final String NBSP = "&nbsp;";

    /**
     * 
     */
    private static final long serialVersionUID = 4988047922232822611L;

    private String hours;
    private String projectId;
    private String subProjectId;
    private String subProject;
    private String task;

    private String idActivity;
    private boolean isUpdate;
    private boolean toRemove;

    public String getIdActivity() {
	return idActivity;
    }

    public void setIdActivity(String idActivity) {
	this.idActivity = idActivity;
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

    public String getSubProjectId() {
	return subProjectId;
    }

    public void setSubProjectId(String subProjectId) {
	this.subProjectId = subProjectId;
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
	if (!NBSP.equals(task)) {
	    this.task = task;
	}
    }

    public boolean isUpdate() {
	return isUpdate;
    }

    public void setUpdate(boolean isUpdate) {
	this.isUpdate = isUpdate;
    }

    public boolean isToRemove() {
	return toRemove;
    }

    public void setToRemove(boolean toRemove) {
	this.toRemove = toRemove;
    }

}
