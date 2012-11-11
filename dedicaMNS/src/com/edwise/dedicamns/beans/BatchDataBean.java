/**
 * 
 */
package com.edwise.dedicamns.beans;

import java.io.Serializable;

/**
 * @author edwise
 * 
 */
public class BatchDataBean implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 5151335699616242005L;
    
    private String month;
    private String project;
    private String subProject;
    private String task;
    private String typeHour;

    public BatchDataBean() {
    }

    public String getMonth() {
	return month;
    }

    public void setMonth(String month) {
	this.month = month;
    }

    public String getProject() {
	return project;
    }

    public void setProject(String project) {
	this.project = project;
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

    public String getTypeHour() {
	return typeHour;
    }

    public void setTypeHour(String typeHour) {
	this.typeHour = typeHour;
    }

}
