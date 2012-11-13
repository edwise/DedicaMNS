package com.edwise.dedicamns.connections;

import android.app.Activity;


public interface WebConnection {

    boolean isOnline(Activity activity);
    
    Integer connectWeb(String userName, String password);
}
