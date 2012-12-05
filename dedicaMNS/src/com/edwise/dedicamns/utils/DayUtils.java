package com.edwise.dedicamns.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import android.util.SparseArray;

import com.edwise.dedicamns.beans.ActivityDay;
import com.edwise.dedicamns.beans.BatchDataBean;
import com.edwise.dedicamns.beans.DayRecord;

public class DayUtils {

    public static final String ZERO_HOUR = "00:00";
    public static final String NBSP = "&nbsp;";
    public static final int HOUR_MINUTES = 60;

    public static final int MAX_SIZE_STRING = 15;

    public static final String[] TYPE_HOURS = new String[] { "8:30 de L a J y 6:00 V", "8:00 L a V" };
    public static final int TYPE_COOL_FRIDAY = 0; // 8:30 de L a J y 6:00 V
    public static final int TYPE_BAD_FRIDAY = 1; // 8:00 L a V

    private static final SparseArray<String> MONTH_NAMES = new SparseArray<String>();
    static {
	MONTH_NAMES.put(1, "Enero");
	MONTH_NAMES.put(2, "Febrero");
	MONTH_NAMES.put(3, "Marzo");
	MONTH_NAMES.put(4, "Abril");
	MONTH_NAMES.put(5, "Mayo");
	MONTH_NAMES.put(6, "Junio");
	MONTH_NAMES.put(7, "Julio");
	MONTH_NAMES.put(8, "Agosto");
	MONTH_NAMES.put(9, "Septiembre");
	MONTH_NAMES.put(10, "Octubre");
	MONTH_NAMES.put(11, "Noviembre");
	MONTH_NAMES.put(12, "Diciembre");
    }

    public static boolean isWeekend(String dayName) {
	return dayName.equalsIgnoreCase("Dom") || dayName.equalsIgnoreCase("Sáb");
    }

    public static boolean isFriday(String dayName) {
	return dayName.equalsIgnoreCase("Vie");
    }

    public static String replaceAcutes(String name) {
	String nameWithoutAcutes = name;
	if (name.contains("acute;")) {
	    nameWithoutAcutes = nameWithoutAcutes.replace("&aacute;", "á");
	    nameWithoutAcutes = nameWithoutAcutes.replace("&eacute;", "é");
	    nameWithoutAcutes = nameWithoutAcutes.replace("&iacute;", "í");
	    nameWithoutAcutes = nameWithoutAcutes.replace("&oacute;", "ó");
	    nameWithoutAcutes = nameWithoutAcutes.replace("&uacute;", "ú");
	}

	return nameWithoutAcutes;
    }

    public static String createDateString(int day, String numMonth, String year) {
	StringBuilder dateString = new StringBuilder();

	// Formato: 22/11/2012 0:00:00
	dateString.append(StringUtils.leftPad(String.valueOf(day), 2, '0')).append("/")
		.append(StringUtils.leftPad(numMonth, 2, '0')).append("/").append(year).append(" 0:00:00");
	return dateString.toString();
    }

    public static String getNumSubProject(String subProjectName) {
	String numString = subProjectName.substring(0, subProjectName.indexOf("-"));

	return numString.trim();
    }

    public static String getTaskNameWithoutNBSP(String taskName) {
	String resultString = null;
	if (taskName.indexOf(NBSP) >= 0) {
	    resultString = taskName.substring(0, taskName.indexOf(NBSP)).trim();
	} else {
	    resultString = taskName.trim();
	}

	return resultString;
    }

    public static List<DayRecord> fillListDaysWithOnlyActivityDay(List<DayRecord> listDays,
	    BatchDataBean batchData) {
	List<DayRecord> workingDays = new ArrayList<DayRecord>();

	ActivityDay activityDayNormal = createActivityFromBatchData(batchData, "8:00");
	ActivityDay activityDayCool = createActivityFromBatchData(batchData, "8:30");
	ActivityDay activityDayCoolFriday = createActivityFromBatchData(batchData, "6:00");

	for (DayRecord day : listDays) {
	    if (!day.getIsWeekend() && !day.getIsHoliday()) {
		workingDays.add(day);
		switch (batchData.getTypeHour()) {
		case TYPE_COOL_FRIDAY:
		    if (isFriday(day.getDayName())) {
			day.getActivities().add(activityDayCoolFriday);
		    } else {
			day.getActivities().add(activityDayCool);
		    }
		    break;
		case TYPE_BAD_FRIDAY:
		default:
		    day.getActivities().add(activityDayNormal);
		    break;
		}
	    }
	}

	return workingDays;
    }

    private static ActivityDay createActivityFromBatchData(BatchDataBean batchData, String hours) {
	ActivityDay activityDay = new ActivityDay();
	activityDay.setHours(hours);
	activityDay.setProjectId(batchData.getProject());
	activityDay.setSubProject(batchData.getSubProject());
	activityDay.setSubProjectId(getNumSubProject((String) batchData.getSubProject()));
	activityDay.setTask(batchData.getTask());
	activityDay.setUpdate(false);
	activityDay.setToRemove(false);

	return activityDay;
    }

    public static String getMonthName(int numMonth) {
	return MONTH_NAMES.get(numMonth);
    }

    public static String addHours(String hour1, String hour2) {
	String[] hour1Splitted = hour1.split(":");
	String[] hour2Splitted = hour2.split(":");
	Integer hours = Integer.valueOf(hour1Splitted[0]) + Integer.valueOf(hour2Splitted[0]);
	Integer minutes = Integer.valueOf(hour1Splitted[1]) + Integer.valueOf(hour2Splitted[1]);
	if (minutes >= HOUR_MINUTES) {
	    hours = hours + (minutes / HOUR_MINUTES);
	    minutes = minutes % HOUR_MINUTES;
	}

	return StringUtils.leftPad(String.valueOf(hours), 2, '0') + ":"
		+ StringUtils.leftPad(String.valueOf(minutes), 2, '0');
    }

    public static String limitSizeString(String text) {
	String textResult = null;
	if (text != null && text.length() > MAX_SIZE_STRING) {
	    textResult = text.substring(0, MAX_SIZE_STRING) + "...";
	} else {
	    textResult = text;
	}

	return textResult;
    }

    public static String getSomeProjectIds(List<ActivityDay> activities) {
	StringBuilder projectIds = new StringBuilder();

	if (activities.size() > 0) {
	    projectIds.append(activities.get(0).getProjectId());
	    if (activities.size() > 1) {
		projectIds.append(", ").append(activities.get(1).getProjectId());
		if (activities.size() > 2) {
		    projectIds.append(", ...");
		}
	    }
	}

	return projectIds.toString();
    }
}
