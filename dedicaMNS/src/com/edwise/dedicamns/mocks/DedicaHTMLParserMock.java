package com.edwise.dedicamns.mocks;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import android.util.Log;

import com.edwise.dedicamns.beans.BatchDataBean;
import com.edwise.dedicamns.beans.DayRecord;
import com.edwise.dedicamns.beans.ProjectSubprojectBean;
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
    
    public Integer connectWeb() {
	try {
	    TimeUnit.SECONDS.sleep(3);
	} catch (InterruptedException e) {
	    Log.e(DedicaHTMLParserMock.class.toString(),
		    "connectWeb: Error en TimeUnit...", e);
	    e.printStackTrace();
	}
	
	return 200;
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
		dayRecord.setSubProject("3 - Calentar silla");
	    }
	    else {
		// Si esta vacio
		dayRecord.setSubProject(ProjectSubprojectBean.SUBPROJECT_DEFAULT);
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

	// TODO llamar aquí a un cacheador de subrpoyectos??
	
	return arrayProjects;
    }

    public List<String> getArraySubProjects(String projectId) {
	// TODO habrá que hacer una carga en algún lado con todos los subproyectos, y luego de ahi sacar los que sean...
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

    public List<String> getMonths() {
	List<String> months = new ArrayList<String>();

	months.add("Octubre 2012");
	months.add("Noviembre 2012");
	months.add("Diciembre 2012");
	months.add("Enero 2013");
	
	return months;
    }
    
    public int proccesBatch(BatchDataBean batchData) {
	// TODO procesado de batch: obtener web del mes, e ir imputando cada dia con los datos pasados
	try {
	    TimeUnit.SECONDS.sleep(5);
	} catch (InterruptedException e) {
	    Log.e(DedicaHTMLParserMock.class.toString(),
		    "proccesBatch: Error en TimeUnit...", e);
	    e.printStackTrace();
	}
	
	return 1;
    }
    
}
