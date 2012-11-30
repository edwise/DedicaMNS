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
    private int numMonth;
    private String year;
    private String project;
    private String subProject;
    private String task;
    private int typeHour;

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

    public int getTypeHour() {
	return typeHour;
    }

    public void setTypeHour(int typeHour) {
	this.typeHour = typeHour;
    }

    public int getNumMonth() {
	return numMonth;
    }

    public void setNumMonth(int numMonth) {
	this.numMonth = numMonth;
    }

    public String getYear() {
	return year;
    }

    public void setYear(String year) {
	this.year = year;
    }

}
