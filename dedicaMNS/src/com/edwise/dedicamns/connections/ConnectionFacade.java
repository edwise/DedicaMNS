/**
 * 
 */
package com.edwise.dedicamns.connections;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import android.app.Activity;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.util.Log;

import com.edwise.dedicamns.connections.impl.MNSWebConnectionImpl;
import com.edwise.dedicamns.connections.impl.MockWebConnectionImpl;

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
		Resources resources = activity.getResources();
		AssetManager assetManager = resources.getAssets();
		boolean isMock = true;
		try {
			InputStream inputStream = assetManager.open("app.properties");
			Properties properties = new Properties();
			properties.load(inputStream);

			isMock = Boolean.valueOf((String) properties.get("app.mockActivated"));
			Log.d(ConnectionFacade.class.toString(), "Es mock: " + isMock);
		} catch (IOException e) {
			Log.e(ConnectionFacade.class.toString(), "Error al obtener el fichero de propiedades", e);
			throw new RuntimeException(e);
		}
		return isMock;
	}
}
