/**
 * 
 */
package com.edwise.dedicamns.connections;

import android.app.Activity;

import com.edwise.dedicamns.connections.impl.MNSWebConnectionImpl;
import com.edwise.dedicamns.connections.impl.MockWebConnectionImpl;
import com.edwise.dedicamns.utils.PropertiesLoader;

/**
 * @author edwise
 * 
 */
public class ConnectionFacade {

	public static final Integer CONNECTION_OK = 200;

	private static WebConnection webConnection;

	public static WebConnection getWebConnection() {
		return webConnection;
	}

	public static void createWebConnection(Activity activity) {
		boolean isMock = getMockProperty(activity);

		if (isMock) {
			webConnection = new MockWebConnectionImpl();
		} else {
			webConnection = new MNSWebConnectionImpl();
		}
	}

	protected static boolean getMockProperty(Activity activity) {
		return Boolean.valueOf(PropertiesLoader.getProperty(PropertiesLoader.MOCK_ACTIVATED, activity));
	}
}
