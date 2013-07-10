package com.edwise.dedicamns;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.edwise.dedicamns.asynctasks.AppData;
import com.edwise.dedicamns.asynctasks.LoginConstants;
import com.edwise.dedicamns.asynctasks.MonthListAsyncTask;
import com.edwise.dedicamns.asynctasks.PopulateDBAsyncTask;
import com.edwise.dedicamns.connections.ConnectionException;
import com.edwise.dedicamns.connections.ConnectionFacade;
import com.edwise.dedicamns.connections.WebConnection;
import com.edwise.dedicamns.db.DatabaseHelper;
import com.edwise.dedicamns.menu.MenuUtils;

public class MainMenuActivity extends Activity {
	private static final String LOGTAG = MainMenuActivity.class.toString();

	private static final int NO_DIALOG = 0;
	private static final int RELOAD_DB_DIALOG = 1;

	private ProgressDialog pDialog = null;
	private String messageDialog = null;
	private AlertDialog alertDialog = null;
	private int alertDialogActiveType = NO_DIALOG;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_menu);
		AppData.setCurrentActivity(this);

		restoreFromChangeOrientation(savedInstanceState);

		boolean isLogout = getIntent().getBooleanExtra(LoginConstants.IS_LOGOUT_TAG, false);
		if (isLogout) { // es logout, nos vamos al login, y cerramos esta
			goToLogout();
		}
		else {
			// No es logout, creamos la bd si no está creada
			if (AppData.getDatabaseHelper() == null) {
				initDB();
			}
		}
	}

	private void initDB() {
		DatabaseHelper dbHelper = new DatabaseHelper(this);
		AppData.setDatabaseHelper(dbHelper);

		if (dbHelper.isEmptyDB()) {
			reloadProjects();
		}
	}

	private void reloadProjects() {
		showDialog(getString(R.string.populatingDB));
		AsyncTask<Integer, Integer, Integer> populateDBAsyncTask = new PopulateDBAsyncTask();
		populateDBAsyncTask.execute(1);
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		AppData.setCurrentActivity(this);
	}

	private void restoreFromChangeOrientation(Bundle savedInstanceState) {
		if (savedInstanceState != null) {
			if (savedInstanceState.getBoolean("pDialogON")) {
				String message = savedInstanceState.getString("messageDialog");
				showDialog(message);
			}
			int alertDialogType = savedInstanceState.getInt("alertDialogType");
			if (alertDialogType != NO_DIALOG) {
				launchReloadDB();
			}
		}
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		Log.d(LOGTAG, "onSaveInstanceState");
		if (pDialog != null) {
			pDialog.cancel();
			outState.putBoolean("pDialogON", true);
			outState.putString("messageDialog", this.messageDialog);
		} else if (alertDialogActiveType != NO_DIALOG) {
			alertDialog.dismiss();
			outState.putInt("alertDialogType", alertDialogActiveType);
		}
		super.onSaveInstanceState(outState);
	}

	public void closeDialog() {
		if (pDialog != null) {
			pDialog.dismiss();
			pDialog = null;
		}
	}

	private void goToLogout() {
		Intent intent = new Intent(this, LoginActivity.class);
		intent.putExtra(LoginConstants.IS_LOGOUT_TAG, true);
		startActivity(intent);
		finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		boolean returned = false;
		switch (item.getItemId()) {
		case R.id.menu_logout:
			// En este caso podemos ir directos al LoginActivity, no hace falta pasar por la que ya estamos
			MenuUtils.doDirectLogout(this);
			returned = true;
			break;
		case R.id.menu_about_us:
			MenuUtils.goToAbout(this);
			returned = true;
			break;
		default:
			returned = super.onOptionsItemSelected(item);
		}

		return returned;
	}

	public void doShowListMonth(View view) {
		Log.d(LOGTAG, "doShowListMonth");
		showDialog(getString(R.string.msgGettingMonthData));
		Calendar today = Calendar.getInstance();

		AsyncTask<Integer, Integer, Integer> monthListAsyncTask = new MonthListAsyncTask();
		monthListAsyncTask.execute(new Integer[] { today.get(Calendar.MONTH) + 1, today.get(Calendar.YEAR) });
	}

	public void doShowBatchMenu(View view) {
		Log.d(LOGTAG, "doShowBatchMenu");

		showDialog(getString(R.string.msgGettingData)); 
		AsyncTask<Integer, Integer, Integer> batchMenuAsyncTask = new BatchMenuAsyncTask();
		batchMenuAsyncTask.execute(1);
	}

	public void doReloadBD(View view) {
		Log.d(LOGTAG, "doReloadBD");

		launchReloadDB();
	}

	private void launchReloadDB() {
		this.alertDialogActiveType = RELOAD_DB_DIALOG;
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder.setTitle(getString(R.string.msgreloadProjectsToExecute));
		alertDialogBuilder.setMessage(getString(R.string.msgContinue));
		alertDialogBuilder.setPositiveButton(getString(R.string.msgAlertOK), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				MainMenuActivity.this.alertDialogActiveType = NO_DIALOG;
				reloadProjects();
			}
		});
		alertDialogBuilder.setNegativeButton(getString(R.string.msgAlertCancel), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				MainMenuActivity.this.alertDialogActiveType = NO_DIALOG;
			}
		});
		alertDialog = alertDialogBuilder.show();
	}

	private void showDialog(String message) {
		this.messageDialog = message;

		pDialog = ProgressDialog.show(this, message, getString(R.string.msgPleaseWait), true);
	}

	private class BatchMenuAsyncTask extends AsyncTask<Integer, Integer, Integer> {

		@Override
		protected Integer doInBackground(Integer... params) {
			Log.d(BatchMenuAsyncTask.class.toString(), "doInBackground...");

			WebConnection webConnection = ConnectionFacade.getWebConnection();
			Integer result = 1;
			try {
				webConnection.fillProyectsAndSubProyectsCached();
				webConnection.fillMonthsAndYearsCached();
			} catch (ConnectionException e) {
				Log.e(BatchMenuAsyncTask.class.toString(),
						"Error al obtener datos de cacheo (proyectos, meses y años)", e);
				result = -1;
			}

			return result;
		}

		@Override
		protected void onPostExecute(Integer result) {
			Log.d(BatchMenuAsyncTask.class.toString(), "onPostExecute...");
			super.onPostExecute(result);

			if (result == 1) {
				this.launchBatchMenuActivity();
				this.closeDialog();
			} else {
				this.closeDialog();
				showToastMessage(getString(R.string.msgWebError));
			}
		}

		private void closeDialog() {
			MainMenuActivity activity = (MainMenuActivity) AppData.getCurrentActivity();
			activity.closeDialog();
		}

		private void launchBatchMenuActivity() {
			Intent intent = new Intent(AppData.getCurrentActivity(), BatchMenuActivity.class);

			AppData.getCurrentActivity().startActivity(intent);
		}

		private void showToastMessage(String message) {
			Toast toast = Toast.makeText(AppData.getCurrentActivity(), message, Toast.LENGTH_LONG);
			toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, 20);
			toast.show();
		}

	}
}
