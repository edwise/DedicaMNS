package com.edwise.dedicamns;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.edwise.dedicamns.asynctasks.AppData;
import com.edwise.dedicamns.beans.ActivityDay;
import com.edwise.dedicamns.connections.ConnectionException;
import com.edwise.dedicamns.connections.ConnectionFacade;
import com.edwise.dedicamns.connections.WebConnection;
import com.edwise.dedicamns.menu.MenuUtils;
import com.edwise.dedicamns.utils.DayUtils;
import com.edwise.dedicamns.utils.ErrorUtils;
import com.edwise.dedicamns.utils.Time24HoursValidator;

public class DetailDayActivity extends Activity {
    private static final String LOGTAG = DetailDayActivity.class.toString();

    enum DetailDayActionEnum {
	SAVE, REMOVE;
    }

    private ActivityDay activityDay = null;
    private Integer dayNum = null;
    private String dateForm = null;
    private ProgressDialog pDialog = null;
    private String messageDialog = null;

    private EditText hoursEditText = null;
    private Spinner projectSpinner = null;
    private Spinner subProjectSpinner = null;
    private EditText taskEditText = null;

    private boolean isFirstChargeSubprojectSpinner;

    @Override
    public void onCreate(Bundle savedInstanceState) {
	Log.d(LOGTAG, "onCreate");
	super.onCreate(savedInstanceState);
	setContentView(R.layout.detail_day);
	AppData.setCurrentActivity(this);

	if (savedInstanceState != null && savedInstanceState.getBoolean("pDialogON")) {
	    String message = savedInstanceState.getString("messageDialog");
	    showDialog(message);
	}

	this.activityDay = (ActivityDay) getIntent().getSerializableExtra("activityDay");
	this.dateForm = getIntent().getStringExtra("dateForm");
	this.dayNum = getIntent().getIntExtra("dayNum", 0);
	if (activityDay != null) {
	    hideRemoveButtonIfNew();
	    hoursEditText = (EditText) findViewById(R.id.detailHoursEditText);
	    hoursEditText.setText(DayUtils.ZERO_HOUR.equals(activityDay.getHours()) ? "" : activityDay
		    .getHours());

	    isFirstChargeSubprojectSpinner = true;
	    linkProjectSpinner();
	    linkSubProjectSpinner((String) this.projectSpinner.getSelectedItem());

	    taskEditText = (EditText) findViewById(R.id.detailTaskEditText);
	    taskEditText.setText(activityDay.getTask());
	} else {
	    Log.e(LOGTAG, "ActivityDay ha venido nulo");
	    throw new UnsupportedOperationException("ActivityDay ha venido nulo!");
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
	    outState.putBoolean("pDialogON", true);
	    outState.putString("messageDialog", this.messageDialog);
	}
	super.onSaveInstanceState(outState);
    }

    public void closeDialog() {
	if (pDialog != null) {
	    pDialog.dismiss();
	    pDialog = null;
	}
    }

    private void hideRemoveButtonIfNew() {
	if (StringUtils.isBlank(activityDay.getProjectId())) {
	    Button removeButton = (Button) findViewById(R.id.detailRemoveButton);
	    removeButton.setVisibility(View.GONE);
	}
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

    private void linkProjectSpinner() {
	List<String> projectsArray = ConnectionFacade.getWebConnection().getArrayProjects();
	projectSpinner = (Spinner) findViewById(R.id.detailProjectSpinner);
	ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
		projectsArray);
	projectSpinner.setAdapter(adapter);
	String projectId = activityDay.getProjectId();
	if (StringUtils.isNotBlank(projectId)) {
	    int spinnerPosition = adapter.getPosition(projectId);
	    projectSpinner.setSelection(spinnerPosition);
	}
	projectSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
	    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
		if (isFirstChargeSubprojectSpinner) {
		    isFirstChargeSubprojectSpinner = false;
		} else {
		    linkSubProjectSpinner((String) parent.getItemAtPosition(pos));
		}
	    }

	    public void onNothingSelected(AdapterView<?> parent) {
	    }
	});
    }

    private void linkSubProjectSpinner(String projectSelected) {
	List<String> subProjectArray = null;
	subProjectArray = ConnectionFacade.getWebConnection().getArraySubProjects(projectSelected);
	if (subProjectSpinner == null) {
	    subProjectSpinner = (Spinner) findViewById(R.id.detailSubprojectSpinner);
	}
	ArrayAdapter<String> subProjectAdapter = new ArrayAdapter<String>(this,
		android.R.layout.simple_spinner_item, subProjectArray);
	subProjectSpinner.setAdapter(subProjectAdapter);
	String subProject = activityDay.getSubProject();
	if (StringUtils.isNotBlank(subProject) && isFirstChargeSubprojectSpinner) {
	    int spinnerPosition = subProjectAdapter.getPosition(subProject);
	    subProjectSpinner.setSelection(spinnerPosition);
	}
	subProjectSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
	    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
	    }

	    public void onNothingSelected(AdapterView<?> parent) {
	    }
	});
    }

    public void doSaveActivity(View view) {
	Log.d(LOGTAG, "doSaveActivity");

	if (!Time24HoursValidator.validateTime(hoursEditText.getText().toString().trim())) {
	    showToastMessage(getString(R.string.msgErrorFormatHour));
	} else if (!validateSpinnerProjectSelected()) {
	    showToastMessage(getString(R.string.msgErrorSelectProject));
	} else {
	    showDialog(DetailDayActionEnum.SAVE);
	    fillActivityDay();
	    AsyncTask<DetailDayActionEnum, Integer, Integer> connectionAsyncTask = new SaveRemoveActivityAsyncTask();
	    connectionAsyncTask.execute(DetailDayActionEnum.SAVE);
	}
    }

    private void showToastMessage(String message) {
	Toast toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
	toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, 20);
	toast.show();
    }

    private boolean validateSpinnerProjectSelected() {
	return projectSpinner.getSelectedItemPosition() != 0;
    }

    private void fillActivityDay() {
	activityDay.setHours(DayUtils.formatHour(hoursEditText.getText().toString().trim()));
	activityDay.setProjectId((String) this.projectSpinner.getSelectedItem());
	activityDay.setSubProject((String) this.subProjectSpinner.getSelectedItem());
	activityDay.setSubProjectId(DayUtils.getNumSubProject((String) this.subProjectSpinner
		.getSelectedItem()));
	activityDay.setTask(this.taskEditText.getText().toString().trim());
    }

    public void doRemoveActivity(View view) {
	Log.d(LOGTAG, "doRemoveActivity");

	showDialog(DetailDayActionEnum.REMOVE);

	AsyncTask<DetailDayActionEnum, Integer, Integer> connectionAsyncTask = new SaveRemoveActivityAsyncTask();
	connectionAsyncTask.execute(DetailDayActionEnum.REMOVE);
    }

    private void showDialog(DetailDayActionEnum actionEnum) {
	String message = getString(R.string.msgSaving);
	if (actionEnum == DetailDayActionEnum.REMOVE) {
	    message = getString(R.string.msgRemoving);
	}
	showDialog(message);
    }

    private void showDialog(String message) {
	this.messageDialog = message;

	pDialog = ProgressDialog.show(this, message, getString(R.string.msgPleaseWait), true);
    }

    @Override
    public void onBackPressed() {
	Log.d(LOGTAG, "onBackPressed");
	super.onBackPressed();

	Intent returnIntent = new Intent();
	setResult(RESULT_CANCELED, returnIntent);
	finish();
    }

    private class SaveRemoveActivityAsyncTask extends AsyncTask<DetailDayActionEnum, Integer, Integer> {

	private DetailDayActionEnum action;

	@Override
	protected Integer doInBackground(DetailDayActionEnum... action) {
	    Log.d(SaveRemoveActivityAsyncTask.class.toString(), "doInBackground...");
	    WebConnection webConnection = ConnectionFacade.getWebConnection();
	    this.action = action[0];
	    Integer result = 0;
	    switch (this.action) {
	    case SAVE:
		try {
		    result = webConnection.saveDay(activityDay, dateForm, dayNum);
		} catch (ConnectionException e) {
		    Log.e(LOGTAG, "Error al guardar datos de una actividad", e);
		    result = -2;
		}
		break;

	    case REMOVE:
		activityDay.setToRemove(true);
		try {
		    result = webConnection.removeDay(activityDay);
		} catch (ConnectionException e) {
		    Log.e(LOGTAG, "Error al borrar datos de una actividad", e);
		    result = -2;
		}
		break;
	    default:
		break;
	    }

	    return result;
	}

	@Override
	protected void onPostExecute(Integer result) {
	    Log.d(SaveRemoveActivityAsyncTask.class.toString(), "onPostExecute...");

	    if (result == 1) {
		this.returnGeneralDayActivity();
		this.closeDialog();
	    } else {
		this.closeDialog();
		showToastMessage(ErrorUtils.getMessageError(result));
	    }
	}

	private void closeDialog() {
	    DetailDayActivity activity = (DetailDayActivity) AppData.getCurrentActivity();
	    activity.closeDialog();
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
	    Log.d(SaveRemoveActivityAsyncTask.class.toString(), "onProgressUpdate...");

	    super.onProgressUpdate(values);
	}

	private void returnGeneralDayActivity() {
	    String message = getString(R.string.msgSaveOK);
	    if (this.action == DetailDayActionEnum.REMOVE) {
		message = getString(R.string.msgRemoveOK);
	    }

	    Intent returnIntent = new Intent();
	    returnIntent.putExtra("activityModif", activityDay);
	    setResult(RESULT_OK, returnIntent);

	    showToastMessage(message);

	    finish();
	}

	private void showToastMessage(String message) {
	    Toast toast = Toast.makeText(AppData.getCurrentActivity(), message, Toast.LENGTH_LONG);
	    toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, 20);
	    toast.show();
	}
    }
}
