/**
 * 
 */
package com.edwise.dedicamns.menu;

import android.app.Activity;
import android.content.Intent;

import com.edwise.dedicamns.AboutActivity;
import com.edwise.dedicamns.LoginActivity;

/**
 * @author edwise
 * 
 */
public class MenuUtils {

    public static void doLogout(Activity source) {
	Intent intent = new Intent(source, LoginActivity.class);
	intent.putExtra("isLogout", true);
	source.startActivity(intent);
	source.finish();
    }

    public static void goToAbout(Activity source) {
	Intent intentAbout = new Intent(source, AboutActivity.class);
	source.startActivity(intentAbout);
    }
}
