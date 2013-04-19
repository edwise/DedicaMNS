package com.edwise.dedicamns;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.edwise.dedicamns.asynctasks.AppData;

public class AboutActivity extends Activity {
	private static final String LOGTAG = AboutActivity.class.toString();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d(LOGTAG, "onCreate");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
		AppData.setCurrentActivity(this);

		fillTextViewVersion();
	}

	private void fillTextViewVersion() {
		String versionName = getVersionNameApp();

		if (versionName != null) {
			TextView textViewAbout = (TextView) findViewById(R.id.AboutVersionTextView);
			textViewAbout.setText("Dedica MNS: " + versionName + " Version");
		}
	}

	private String getVersionNameApp() {
		String versionName = null;
		PackageInfo pinfo;
		try {
			pinfo = getPackageManager().getPackageInfo(getPackageName(), 0);
			versionName = pinfo.versionName;
		} catch (NameNotFoundException e) {
			Log.e(LOGTAG, "Error al obtener la versión de la app!", e);
		}
		return versionName;
	}

	@Override
	public void onBackPressed() {
		Log.d(LOGTAG, "onBackPressed");
		super.onBackPressed();

		finish();
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		AppData.setCurrentActivity(this);
	}

}
