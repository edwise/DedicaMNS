/**
 * 
 */
package com.edwise.dedicamns.menu;

import android.app.Activity;
import android.content.Intent;

import com.edwise.dedicamns.AboutActivity;
import com.edwise.dedicamns.LoginActivity;
import com.edwise.dedicamns.MainMenuActivity;
import com.edwise.dedicamns.asynctasks.LoginConstants;

/**
 * @author edwise
 * 
 */
public class MenuUtils {

	public static void doLogout(Activity source) {
		Intent intent = new Intent(source, MainMenuActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra(LoginConstants.IS_LOGOUT_TAG, true);
		source.startActivity(intent);
		source.finish();
	}

	public static void doDirectLogout(Activity source) {
		Intent intent = new Intent(source, LoginActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra(LoginConstants.IS_LOGOUT_TAG, true);
		source.startActivity(intent);
		source.finish();
	}

	public static void goToAbout(Activity source) {
		Intent intentAbout = new Intent(source, AboutActivity.class);
		source.startActivity(intentAbout);
	}
}
