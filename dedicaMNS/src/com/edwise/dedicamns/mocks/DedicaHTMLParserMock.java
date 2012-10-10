package com.edwise.dedicamns.mocks;

import java.util.ArrayList;
import java.util.List;

import com.edwise.dedicamns.beans.DayRecord;

public class DedicaHTMLParserMock {

    public static List<DayRecord> getListFromHTML() {
	List<DayRecord> list = new ArrayList<DayRecord>();

	fillListMock(list);

	return list;
    }

    private static void fillListMock(List<DayRecord> list) {
	for (int i = 1; i < 31; i++) {
	    DayRecord dayRecord = new DayRecord();
	    dayRecord.setDayNum(i);
	    dayRecord.setDayName(generateDayName(i));

	    if (i < 10 && !isWeekend(dayRecord.getDayName())) {
		dayRecord.setHours("8:30");
		dayRecord.setProjectId("BBVA58");
	    }

	    list.add(dayRecord);
	}
    }

    final static String[] dayNames = { "Lunes", "Martes", "Miercoles",
	    "Jueves", "Viernes", "Sabado", "Domingo" };

    private static String generateDayName(int i) {
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

    private static boolean isWeekend(String dayName) {
	return dayName.equals("Domingo") || dayName.equals("Sabado");
    }
}
