package com.edwise.dedicamns.connections;

import java.util.List;

import android.app.Activity;

import com.edwise.dedicamns.beans.ActivityDay;
import com.edwise.dedicamns.beans.DayRecord;
import com.edwise.dedicamns.beans.MonthListBean;
import com.edwise.dedicamns.beans.MonthReportBean;

public interface WebConnection {

	boolean isOnline(Activity activity);

	Integer connectWeb(String userName, String password) throws ConnectionException;

	List<String> getMonths();

	List<String> getYears();

	List<String> getArrayProjects();

	List<String> getArraySubProjects(String projectId);

	void populateDBProjectsAndSubprojects() throws ConnectionException;
	
	void fillProyectsAndSubProyectsCached() throws ConnectionException;

	void fillMonthsAndYearsCached();

	MonthListBean getListDaysAndActivitiesForCurrentMonth() throws ConnectionException;

	Integer saveDay(ActivityDay activityDay, String dateForm, int dayNum, boolean isBatchMontly) throws ConnectionException;

	Integer saveDayBatch(DayRecord dayRecord, boolean isBatchMontly) throws ConnectionException;

	Integer removeDay(ActivityDay activityDay) throws ConnectionException;

	List<DayRecord> getListDaysAndActivitiesForMonthAndYear(int month, String year, boolean withActivities)
			throws ConnectionException;

	MonthReportBean getMonthReport() throws ConnectionException;
	
	boolean recreateDBOnStart();
}
