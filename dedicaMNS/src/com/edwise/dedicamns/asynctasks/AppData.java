package com.edwise.dedicamns.asynctasks;

import android.app.Activity;

public class AppData {

    private static Activity currentActivity;

    public static Activity getCurrentActivity() {
	return currentActivity;
    }

    public static void setCurrentActivity(Activity currentActivity) {
	AppData.currentActivity = currentActivity;
    }

}
