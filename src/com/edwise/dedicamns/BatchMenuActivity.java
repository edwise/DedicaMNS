package com.edwise.dedicamns;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.edwise.dedicamns.asynctasks.AppData;
import com.edwise.dedicamns.asynctasks.MonthListAsyncTask;
import com.edwise.dedicamns.beans.BatchDataBean;
import com.edwise.dedicamns.beans.DayRecord;
import com.edwise.dedicamns.connections.ConnectionException;
import com.edwise.dedicamns.connections.ConnectionFacade;
import com.edwise.dedicamns.connections.WebConnection;
import com.edwise.dedicamns.menu.MenuUtils;
import com.edwise.dedicamns.utils.DayUtils;

public class BatchMenuActivity extends Activity {
	private static final String LOGTAG = BatchMenuActivity.class.toString();

	private static final int NO_DIALOG = 0;
	private static final int INIT_ALERT_DIALOG = 1;
	private static final int MONTH_ALERT_DIALOG = 2;
	private static final int BATCH_PROGRESS_DIALOG = 1;
	private static final int MONTH_PROGRESS_DIALOG = 2;

	private static final int DEFAULT_MAX_PROGRESS_DIALOG = 100;

	private Spinner monthSpinner = null;
	private Spinner yearSpinner = null;
	private Spinner projectSpinner = null;
	private Spinner subProjectSpinner = null;
	private EditText taskEditText = null;
	private Spinner typeHourSpinner = null;

	private ProgressDialog pDialog = null;
	private AlertDialog alertDialog = null;
	private int alertDialogActiveType = NO_DIALOG;
	private int progressDialogActiveType = NO_DIALOG;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d(LOGTAG, "onCreate");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.batch_menu);
		AppData.setCurrentActivity(this);

		restoreFromChangeOrientation(savedInstanceState);

		List<String> listMonths = ConnectionFacade.getWebConnection().getMonths();
		List<String> listYears = ConnectionFacade.getWebConnection().getYears();
		List<String> listProjects = ConnectionFacade.getWebConnection().getArrayProjects();

		linkMonthSpinner(listMonths);
		linkYearsSpinner(listYears);
		linkProjectSpinner(listProjects);
		linkSubProjectSpinner((String) this.projectSpinner.getSelectedItem());
		linkTypeHourSpinner();
		taskEditText = (EditText) findViewById(R.id.batchTaskEditText);

		markTodayMonthYearSelected();
	}

	private void restoreFromChangeOrientation(Bundle savedInstanceState) {
		if (savedInstanceState != null) {
			int pDialogType = savedInstanceState.getInt("pDialogOnType");
			if (pDialogType != NO_DIALOG) {
				showProgressDialogFromType(pDialogType);
			}
			int alertDialogType = savedInstanceState.getInt("alertDialogType");
			if (alertDialogType != NO_DIALOG) {
				showAlertDialogFromType(alertDialogType);
			}
		}
	}

	private void showAlertDialogFromType(int alertDialogType) {
		switch (alertDialogType) {
		case INIT_ALERT_DIALOG:
			launchBatchProcessWithAlertDialog();
			break;
		case MONTH_ALERT_DIALOG:
			showOkAlertDialog();
			break;
		default:
			throw new IllegalArgumentException();
		}
	}

	private void showProgressDialogFromType(int pDialogType) {
		switch (pDialogType) {
		case BATCH_PROGRESS_DIALOG:
			showDialogWithProgress();
			break;
		case MONTH_PROGRESS_DIALOG:
			showMonthProgressDialog();
			break;
		default:
			throw new IllegalArgumentException();
		}
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		AppData.setCurrentActivity(this);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		Log.d(LOGTAG, "onSaveInstanceState");
		if (pDialog != null) {
			pDialog.cancel();
			outState.putInt("pDialogOnType", progressDialogActiveType);
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
			this.progressDialogActiveType = NO_DIALOG;
		}
	}

	private void linkTypeHourSpinner() {
		typeHourSpinner = (Spinner) findViewById(R.id.batchTypeHourSpinner);
		ArrayAdapter<String> typeHouradapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
				Arrays.asList(DayUtils.TYPE_HOURS));
		typeHourSpinner.setAdapter(typeHouradapter);
		typeHourSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
			}

			public void onNothingSelected(AdapterView<?> parent) {
			}

		});
	}

	private void linkProjectSpinner(List<String> listProjects) {
		projectSpinner = (Spinner) findViewById(R.id.batchProjectSpinner);
		ArrayAdapter<String> projectAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
				listProjects);
		projectSpinner.setAdapter(projectAdapter);
		projectSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
				linkSubProjectSpinner((String) parent.getItemAtPosition(pos));
			}

			public void onNothingSelected(AdapterView<?> parent) {
			}

		});
	}

	private void linkMonthSpinner(List<String> listMonths) {
		monthSpinner = (Spinner) findViewById(R.id.batchMonthsSpinner);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listMonths);
		monthSpinner.setAdapter(adapter);
		monthSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
			}

			public void onNothingSelected(AdapterView<?> parent) {
			}

		});
	}

	private void linkYearsSpinner(List<String> listYears) {
		yearSpinner = (Spinner) findViewById(R.id.batchYearsSpinner);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listYears);
		yearSpinner.setAdapter(adapter);
		yearSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
			}

			public void onNothingSelected(AdapterView<?> parent) {
			}

		});
	}

	private void linkSubProjectSpinner(String projectSelected) {
		List<String> subProjectArray = null;
		subProjectArray = ConnectionFacade.getWebConnection().getArraySubProjects(projectSelected);
		// Cargar el combo de subproyectos
		if (subProjectSpinner == null) {
			subProjectSpinner = (Spinner) findViewById(R.id.batchSubprojectSpinner);
		}
		ArrayAdapter<String> subProjectAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
				subProjectArray);
		subProjectSpinner.setAdapter(subProjectAdapter);
		subProjectSpinner.setSelection(0);
		subProjectSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
			}

			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
	}

	@SuppressWarnings("unchecked")
	private void markTodayMonthYearSelected() {
		Calendar today = Calendar.getInstance();

		ArrayAdapter<String> yearSpinnerAdapter = (ArrayAdapter<String>) yearSpinner.getAdapter();
		int spinnerPosition = yearSpinnerAdapter.getPosition(String.valueOf(today.get(Calendar.YEAR)));
		yearSpinner.setSelection(spinnerPosition);

		monthSpinner.setSelection(today.get(Calendar.MONTH)); // Van de 0 a 11 las dos cosas...
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
			MenuUtils.doLogout(this);
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

	public void doLaunchBatch(View view) {
		Log.d(LOGTAG, "doLaunchBatch");

		if (!validateSpinnerProjectSelected()) {
			showToastMessage(getString(R.string.msgSelectOneProject));
		} else {
			launchBatchProcessWithAlertDialog();
		}
	}

	private boolean validateSpinnerProjectSelected() {
		return projectSpinner.getSelectedItemPosition() != 0;
	}

	private void launchBatchProcessWithAlertDialog() {
		this.alertDialogActiveType = INIT_ALERT_DIALOG;
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder.setTitle(getString(R.string.msgInputProcessToExecute));
		alertDialogBuilder.setMessage(getString(R.string.msgContinue));
		alertDialogBuilder.setPositiveButton(getString(R.string.msgAlertOK), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				BatchMenuActivity.this.alertDialogActiveType = NO_DIALOG;
				launchBatchProcess();
			}
		});
		alertDialogBuilder.setNegativeButton(getString(R.string.msgAlertCancel), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				BatchMenuActivity.this.alertDialogActiveType = NO_DIALOG;
			}
		});
		alertDialog = alertDialogBuilder.show();
	}

	private void showOkAlertDialog() {
		this.alertDialogActiveType = MONTH_ALERT_DIALOG;
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AppData.getCurrentActivity());
		alertDialogBuilder.setTitle(AppData.getCurrentActivity().getString(R.string.msgInputBatchProccessFinished));
		alertDialogBuilder.setMessage(AppData.getCurrentActivity().getString(R.string.msgSeeInputHours));
		alertDialogBuilder.setPositiveButton(AppData.getCurrentActivity().getString(R.string.msgYES),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						BatchMenuActivity.this.alertDialogActiveType = NO_DIALOG;
						launchMonthActivity();
					}
				});
		alertDialogBuilder.setNegativeButton(AppData.getCurrentActivity().getString(R.string.msgNO),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						BatchMenuActivity.this.alertDialogActiveType = NO_DIALOG;
						finish();
					}
				});
		alertDialog = alertDialogBuilder.show();
	}

	private void launchBatchProcess() {
		showDialogWithProgress();
		BatchDataBean batchData = fillDataBean();
		AsyncTask<BatchDataBean, Integer, Integer> batchTask = new BatchAsyncTask();
		batchTask.execute(batchData);
	}

	private void showToastMessage(String message) {
		Toast toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
		toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, 20);
		toast.show();
	}

	private void showDialogWithProgress() {
		pDialog = new ProgressDialog(this);
		pDialog.setMessage(getString(R.string.msgInputProcessExecuting));
		pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		pDialog.setCancelable(false);
		pDialog.show();
		this.progressDialogActiveType = BATCH_PROGRESS_DIALOG;
	}

	private BatchDataBean fillDataBean() {
		BatchDataBean batchData = new BatchDataBean();
		batchData.setMonth((String) this.monthSpinner.getSelectedItem());
		batchData.setNumMonth(this.monthSpinner.getSelectedItemPosition() + 1);
		batchData.setYear((String) this.yearSpinner.getSelectedItem());
		batchData.setProject((String) this.projectSpinner.getSelectedItem());
		batchData.setSubProject((String) this.subProjectSpinner.getSelectedItem());
		batchData.setTask(this.taskEditText.getText().toString().trim());
		batchData.setTypeHour(Integer.valueOf(this.typeHourSpinner.getSelectedItemPosition()));

		return batchData;
	}

	public void doBack(View view) {
		Log.d(LOGTAG, "doBack");
		finish();
	}

	private void launchMonthActivity() {
		Log.d(LOGTAG, "launchMonthActivity");

		showMonthProgressDialog();
		AsyncTask<Integer, Integer, Integer> monthListAsyncTask = new MonthListAsyncTask();
		monthListAsyncTask.execute(1);
	}

	private void showMonthProgressDialog() {
		pDialog = ProgressDialog.show(this, getString(R.string.msgGettingDataMonth), getString(R.string.msgPleaseWait),
				true);
		this.progressDialogActiveType = MONTH_PROGRESS_DIALOG;
	}

	private class BatchAsyncTask extends AsyncTask<BatchDataBean, Integer, Integer> {

		private BatchDataBean batchData;
		private int totalDays;

		@Override
		protected Integer doInBackground(BatchDataBean... batchData) {
			Log.d(BatchAsyncTask.class.toString(), "doInBackground...");
			WebConnection webConnection = ConnectionFacade.getWebConnection();
			this.batchData = batchData[0];

			Integer result = 0;
			try {
				List<DayRecord> listDays = webConnection.getListDaysAndActivitiesForMonthAndYear(
						this.batchData.getNumMonth(), this.batchData.getYear(), false);
				listDays = DayUtils.fillListDaysWithOnlyActivityDay(listDays, this.batchData);

				totalDays = listDays.size();
				BatchMenuActivity activity = (BatchMenuActivity) AppData.getCurrentActivity();
				activity.pDialog.setMax(totalDays);

				int processed = 0;
				for (DayRecord day : listDays) {
					webConnection.saveDayBatch(day, true);
					processed++;
					publishProgress(processed);
				}
				result = 1;
			} catch (ConnectionException e) {
				Log.e(LOGTAG, "Error en el proceso de imputación de mes", e);
				result = -1;
			}
			return result;
		}

		@Override
		protected void onPostExecute(Integer result) {
			Log.d(BatchAsyncTask.class.toString(), "onPostExecute...");

			this.closeDialog();
			if (result == 1) {
				BatchMenuActivity activity = (BatchMenuActivity) AppData.getCurrentActivity();
				activity.showOkAlertDialog();
			} else {
				showToastMessage(AppData.getCurrentActivity().getString(R.string.msgInputBatchError));
			}
		}

		private void closeDialog() {
			BatchMenuActivity activity = (BatchMenuActivity) AppData.getCurrentActivity();
			activity.closeDialog();
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			Log.d(BatchAsyncTask.class.toString(), "onProgressUpdate...");
			super.onProgressUpdate(values);

			BatchMenuActivity activity = (BatchMenuActivity) AppData.getCurrentActivity();
			if (activity.pDialog.getMax() == DEFAULT_MAX_PROGRESS_DIALOG) {
				activity.pDialog.setMax(totalDays);
			}
			activity.pDialog.setProgress(values[0]);
		}

		private void showToastMessage(String message) {
			Toast toast = Toast.makeText(AppData.getCurrentActivity(), message, Toast.LENGTH_LONG);
			toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, 20);
			toast.show();
		}
	}
}
