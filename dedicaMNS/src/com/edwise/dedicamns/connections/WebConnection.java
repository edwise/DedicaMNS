package com.edwise.dedicamns.connections;

import java.util.List;

import android.app.Activity;

public interface WebConnection {

    boolean isOnline(Activity activity);

    Integer connectWeb(String userName, String password) throws ConnectionException;

    List<String> getMonths();

    List<String> getYears();

    List<String> getArrayProjects();
    
    List<String> getArraySubProjects(String projectId);

    void fillProyectsAndSubProyectsCached() throws ConnectionException;

    void fillMonthsAndYearsCached() throws ConnectionException;
}
