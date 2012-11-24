package com.edwise.dedicamns.connections;

import java.util.List;

import android.app.Activity;

import com.edwise.dedicamns.beans.DayRecord;
import com.edwise.dedicamns.beans.MonthListBean;

public interface WebConnection {

    boolean isOnline(Activity activity);

    Integer connectWeb(String userName, String password) throws ConnectionException;

    List<String> getMonths();

    List<String> getYears();

    List<String> getArrayProjects();

    List<String> getArraySubProjects(String projectId);

    void fillProyectsAndSubProyectsCached() throws ConnectionException;

    void fillMonthsAndYearsCached();

    MonthListBean getListDaysForMonth() throws ConnectionException;

    Integer saveDay(DayRecord dayRecord) throws ConnectionException;

    Integer removeDay(DayRecord dayRecord) throws ConnectionException;
}
