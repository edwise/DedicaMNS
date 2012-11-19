/**
 * 
 */
package com.edwise.dedicamns.connections.impl;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;

import com.edwise.dedicamns.beans.ActivityDay;
import com.edwise.dedicamns.beans.DayRecord;
import com.edwise.dedicamns.connections.WebConnection;
import com.edwise.dedicamns.utils.DayUtils;

/**
 * @author edwise
 * 
 */
public class MockWebConnectionImpl implements WebConnection {

    private List<String> arrayProjects = null;
    private List<String> months = null;
    private List<String> years = null;

    /*
     * (non-Javadoc)
     * 
     * @see com.edwise.dedicamns.connections.WebConnection#isOnline(android.app.Activity )
     */
    public boolean isOnline(Activity activity) {
	return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.edwise.dedicamns.connections.WebConnection#connectWeb(java.lang.String , java.lang.String)
     */
    public Integer connectWeb(String userName, String password) {
	return 200;
    }

    public List<String> getMonths() {
	return months;
    }

    public List<String> getYears() {
	return years;
    }

    public List<String> getArrayProjects() {
	return arrayProjects;
    }

    public List<String> getArraySubProjects(String projectId) {
	List<String> arraySubProjects = new ArrayList<String>();
	if (projectId != null && projectId.equals("BBVA58")) {
	    arraySubProjects.add("1 - Tarea mierda");
	    arraySubProjects.add("2 - Marronacos");
	    arraySubProjects.add("3 - Calentar silla");
	} else {
	    arraySubProjects.add("0 - Sin cuenta");
	}

	return arraySubProjects;
    }

    public void fillProyectsAndSubProyectsCached() {
	arrayProjects = new ArrayList<String>();

	arrayProjects.add("Selecciona proyecto...");
	arrayProjects.add("BBVA58");
	arrayProjects.add("Educared09");
	arrayProjects.add("BBVA68");
	arrayProjects.add("NIELHUEVO32");
	arrayProjects.add("ISBAN12");
    }

    public void fillMonthsAndYearsCached() {
	months = new ArrayList<String>();
	months.add("Enero");
	months.add("Octubre");
	months.add("Noviembre");
	months.add("Diciembre");

	years = new ArrayList<String>();
	years.add("2011");
	years.add("2012");
	years.add("2013");
    }

    public List<DayRecord> getListDaysForMonth() {
	List<DayRecord> list = new ArrayList<DayRecord>();

	fillListMock(list);

	return list;
    }

    private void fillListMock(List<DayRecord> list) {
	for (int i = 1; i < 31; i++) {
	    DayRecord dayRecord = new DayRecord();
	    dayRecord.setDayNum(i);
	    dayRecord.setDayName(generateDayName(i));

	    if (i < 10 && !DayUtils.isWeekend(dayRecord.getDayName())) {
		dayRecord.setHours("8:30");

		ActivityDay activityDay = new ActivityDay();
		activityDay.setHours("8:30");
		activityDay.setProjectId("BBVA58");
		activityDay.setSubProject("3 - Calentar silla");
		activityDay.setSubProjectId("3");

		dayRecord.getActivities().add(activityDay);
	    }

	    list.add(dayRecord);
	}
    }

    final static String[] dayNames = { "Lunes", "Martes", "Miercoles", "Jueves", "Viernes", "Sabado",
	    "Domingo" };

    private String generateDayName(int i) {
	String dayName = null;

	if (i <= 7) {
	    dayName = dayNames[i - 1];
	} else {
	    int ind = i % 7;
	    if (ind == 0) {
		dayName = dayNames[6];
	    } else {
		dayName = dayNames[ind - 1];
	    }
	}

	return dayName;
    }

}
