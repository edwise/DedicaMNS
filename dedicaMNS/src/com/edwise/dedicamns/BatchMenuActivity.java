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

    private Spinner monthSpinner = null;
    private Spinner yearSpinner = null;
    private Spinner projectSpinner = null;
    private Spinner subProjectSpinner = null;
    private EditText taskEditText = null;
    private Spinner typeHourSpinner = null;

    private ProgressDialog pDialog = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
	Log.d(LOGTAG, "onCreate");
	super.onCreate(savedInstanceState);
	setContentView(R.layout.batch_menu);

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

	initDialog();
    }

    private void initDialog() {
	pDialog = new ProgressDialog(this);
	pDialog.setMessage("Ejecutando imputación en batch...");
	pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
	pDialog.setCancelable(false);
    }

    private void linkTypeHourSpinner() {
	typeHourSpinner = (Spinner) findViewById(R.id.batchTypeHourSpinner);
	ArrayAdapter<String> typeHouradapter = new ArrayAdapter<String>(this,
		android.R.layout.simple_spinner_item, Arrays.asList(DayUtils.TYPE_HOURS));
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
	ArrayAdapter<String> projectAdapter = new ArrayAdapter<String>(this,
		android.R.layout.simple_spinner_item, listProjects);
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
	ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
		listMonths);
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
	ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
		listYears);
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
	ArrayAdapter<String> subProjectAdapter = new ArrayAdapter<String>(this,
		android.R.layout.simple_spinner_item, subProjectArray);
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
	AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
	alertDialogBuilder.setTitle(getString(R.string.msgInputProcessToExecute));
	alertDialogBuilder.setMessage(getString(R.string.msgContinue));
	alertDialogBuilder.setPositiveButton(getString(R.string.msgAlertOK),
		new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int which) {
			launchBatchProcess();
		    }
		});
	alertDialogBuilder.setNegativeButton(getString(R.string.msgAlertCancel),
		new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int which) {
		    }
		});
	alertDialogBuilder.show();
    }

    private void launchBatchProcess() {
	showDialog();
	BatchDataBean batchData = fillDataBean();
	AsyncTask<BatchDataBean, Integer, Integer> batchTask = new BatchAsyncTask(this);
	batchTask.execute(batchData);
    }

    private void showToastMessage(String message) {
	Toast toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
	toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, 20);
	toast.show();
    }

    private void showDialog() {
	pDialog.show();
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

	showDialog(getString(R.string.msgGettingDataMonth));
	AsyncTask<Integer, Integer, Integer> monthListAsyncTask = new MonthListAsyncTask(this, pDialog);
	monthListAsyncTask.execute(1);
    }

    private void showDialog(String message) {
	pDialog = ProgressDialog.show(this, message, getString(R.string.msgPleaseWait), true);
    }

    private class BatchAsyncTask extends AsyncTask<BatchDataBean, Integer, Integer> {

	private Activity activity;
	private BatchDataBean batchData;

	public BatchAsyncTask(Activity activity) {
	    this.activity = activity;
	}

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
		int total = listDays.size();
		pDialog.setMax(total);
		int processed = 0;
		for (DayRecord day : listDays) {
		    webConnection.saveDayBatch(day);
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

	    if (result == 1) {
		this.closeDialog();
		this.showOkAlertDialog();
	    } else {
		this.closeDialog();
		showToastMessage(activity.getString(R.string.msgInputBatchError));
	    }
	}

	private void closeDialog() {
	    pDialog.dismiss();
	    pDialog = null;
	}

	private void showOkAlertDialog() {
	    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this.activity);
	    alertDialogBuilder.setTitle(activity.getString(R.string.msgInputBatchProccessFinished));
	    alertDialogBuilder.setMessage(activity.getString(R.string.msgSeeInputHours));
	    alertDialogBuilder.setPositiveButton(activity.getString(R.string.msgYES),
		    new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
			    launchMonthActivity();
			}
		    });
	    alertDialogBuilder.setNegativeButton(activity.getString(R.string.msgNO),
		    new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
			    finish();
			}
		    });
	    alertDialogBuilder.show();
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
	    Log.d(BatchAsyncTask.class.toString(), "onProgressUpdate...");
	    super.onProgressUpdate(values);

	    pDialog.setProgress(values[0]);
	}

	private void showToastMessage(String message) {
	    Toast toast = Toast.makeText(this.activity, message, Toast.LENGTH_LONG);
	    toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, 20);
	    toast.show();
	}
    }
}
