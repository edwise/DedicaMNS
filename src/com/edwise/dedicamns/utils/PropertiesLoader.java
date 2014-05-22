/**
 * 
 */
package com.edwise.dedicamns.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import android.app.Activity;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.util.Log;

/**
 * @author Edu
 * 
 */
public class PropertiesLoader {

	private static final String APP_PROPERTIES_FILE = "app.properties";
	
	public static final String MOCK_ACTIVATED = "app.mockActivated";
	public static final String REGENERATE_DB_ON_START = "app.regenerateDBOnStart";
	public static final String APP_SUPPORTED = "app.suppported";
	
	private static Properties properties;

	private PropertiesLoader() {
	}

	private static void loadProperties(Activity activity) {
		Resources resources = activity.getResources();
		AssetManager assetManager = resources.getAssets();

		try {
			InputStream inputStream = assetManager.open(APP_PROPERTIES_FILE);
			properties = new Properties();
			properties.load(inputStream);
		} catch (IOException e) {
			Log.e(PropertiesLoader.class.toString(), "Error al obtener el fichero de propiedades", e);
			throw new RuntimeException(e);
		}
	}

	public static String getProperty(String key, Activity activity) {
		if (properties == null) {
			loadProperties(activity);
		}
		return (String) properties.get(key);
	}
}
