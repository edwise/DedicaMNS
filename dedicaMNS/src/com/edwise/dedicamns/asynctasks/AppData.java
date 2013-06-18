package com.edwise.dedicamns.asynctasks;

import android.app.Activity;

import com.edwise.dedicamns.db.DatabaseHelper;

public class AppData {

	private static Activity currentActivity;
	private static DatabaseHelper databaseHelper;

	public static Activity getCurrentActivity() {
		return currentActivity;
	}

	public static void setCurrentActivity(Activity currentActivity) {
		AppData.currentActivity = currentActivity;
	}

	public static DatabaseHelper getDatabaseHelper() {
		return databaseHelper;
	}

	public static void setDatabaseHelper(DatabaseHelper databaseHelper) {
		AppData.databaseHelper = databaseHelper;
	}

}
