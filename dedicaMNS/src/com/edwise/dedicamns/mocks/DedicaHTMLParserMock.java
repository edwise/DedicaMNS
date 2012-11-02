package com.edwise.dedicamns.mocks;

import java.util.ArrayList;
import java.util.List;

import com.edwise.dedicamns.beans.DayRecord;
import com.edwise.dedicamns.utils.DayUtils;

/**
 * Mock para obtención y parseo de los html obtenidos.
 * 
 * @author edwise
 * 
 */
public class DedicaHTMLParserMock {

    private static DedicaHTMLParserMock htmlParser;

    public static DedicaHTMLParserMock getInstance() {
	if (htmlParser == null) {
	    htmlParser = new DedicaHTMLParserMock();
	}
	return htmlParser;
    }

    public List<DayRecord> getListFromHTML() {
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
		dayRecord.setProjectId("BBVA58");
	    }

	    list.add(dayRecord);
	}
    }

    final static String[] dayNames = { "Lunes", "Martes", "Miercoles",
	    "Jueves", "Viernes", "Sabado", "Domingo" };

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

    public boolean saveDay(DayRecord dayRecord) {
	boolean saved = false;

	saved = true;

	return saved;
    }

    public boolean removeDay(DayRecord dayRecord) {
	boolean removed = false;

	removed = true;

	return removed;
    }

    public List<String> getArrayProjects() {
	List<String> arrayProjects = new ArrayList<String>();

	arrayProjects.add("Selecciona proyecto...");
	arrayProjects.add("BBVA58");
	arrayProjects.add("Educared09");
	arrayProjects.add("BBVA68");
	arrayProjects.add("NIELHUEVO32");
	arrayProjects.add("ISBAN12");

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

}
