/**
 * 
 */
package com.edwise.dedicamns.connections.impl;

import android.app.Activity;

import com.edwise.dedicamns.connections.WebConnection;

/**
 * @author edwise
 *
 */
public class MockWebConnectionImpl implements WebConnection {

    /* (non-Javadoc)
     * @see com.edwise.dedicamns.connections.WebConnection#isOnline(android.app.Activity)
     */
    public boolean isOnline(Activity activity) {
	return true;
    }

    /* (non-Javadoc)
     * @see com.edwise.dedicamns.connections.WebConnection#connectWeb(java.lang.String, java.lang.String)
     */
    public Integer connectWeb(String userName, String password) {
	return 200;
    }

}
