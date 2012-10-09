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
	    dayRecord.setDayName("Lunes");
	    
	    if (i<10) {
		dayRecord.setHours("8:30");
		dayRecord.setProjectId("BBVA58");
	    }
	    
	    list.add(dayRecord);
	}
    }
}
