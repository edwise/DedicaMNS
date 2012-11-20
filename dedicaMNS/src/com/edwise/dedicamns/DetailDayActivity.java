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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.edwise.dedicamns.beans.ActivityDay;
import com.edwise.dedicamns.beans.DayRecord;
import com.edwise.dedicamns.connections.ConnectionFacade;
import com.edwise.dedicamns.menu.MenuUtils;
import com.edwise.dedicamns.mocks.DedicaHTMLParserMock;
import com.edwise.dedicamns.utils.Time24HoursValidator;

public class DetailDayActivity extends Activity {
    private static final String LOGTAG = DetailDayActivity.class.toString();

    private static final String MESSAGE_SAVE_OK = "Guardado registro de horas correctamente";
    private static final String MESSAGE_REMOVE_OK = "Borrado registro de horas correctamente";
    private static final String DIALOG_SAVING = "Guardando datos";
    private static final String DIALOG_REMOVING = "Borrando datos";

    enum DetailDayActionEnum {
	SAVE, REMOVE;
    }

    private DayRecord dayRecord = null;
    private ProgressDialog pDialog = null;

    private TextView dayNumTextView = null;
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

	this.dayRecord = (DayRecord) getIntent().getSerializableExtra("dayRecord");
	if (dayRecord != null) {
	    dayNumTextView = (TextView) findViewById(R.id.detailDayInfoTextView);
	    dayNumTextView.setText(dayRecord.getDayNum() + " - " + dayRecord.getDayName());
	    hoursEditText = (EditText) findViewById(R.id.detailHoursEditText);
	    hoursEditText.setText(dayRecord.getHours());

	    isFirstChargeSubprojectSpinner = true;
	    linkProjectSpinner();
	    linkSubProjectSpinner((String) this.projectSpinner.getSelectedItem());

	    taskEditText = (EditText) findViewById(R.id.detailTaskEditText);
	    taskEditText.setText(dayRecord.getTask());
	} else {
	    dayRecord = new DayRecord();
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
	case R.id.menu_about_us:
	    MenuUtils.goToAbout(this);
	    returned = true;
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
	if (StringUtils.isNotBlank(dayRecord.getProjectId())) {
	    int spinnerPosition = adapter.getPosition(dayRecord.getProjectId());
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
	if (StringUtils.isNotBlank(dayRecord.getSubProject()) && isFirstChargeSubprojectSpinner) {
	    int spinnerPosition = subProjectAdapter.getPosition(dayRecord.getSubProject());
	    subProjectSpinner.setSelection(spinnerPosition);
	}
	subProjectSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
	    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
	    }

	    public void onNothingSelected(AdapterView<?> parent) {
	    }
	});
    }

    public void doSaveDay(View view) {
	Log.d(LOGTAG, "doSaveDay");

	if (!Time24HoursValidator.validateTime(hoursEditText.getText().toString().trim())) {
	    showToastMessage("El formato de horas es incorrecto");
	} else if (!validateSpinnerProjectSelected()) {
	    showToastMessage("Debe seleccionar algún proyecto");
	} else {
	    showDialog(DetailDayActionEnum.SAVE);
	    fillDayRecord();
	    AsyncTask<DetailDayActionEnum, Integer, Integer> connectionAsyncTask = new SaveRemoveDayAsyncTask(
		    this);
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

    private void fillDayRecord() {
	this.dayRecord.setHours(hoursEditText.getText().toString().trim());
	ActivityDay activityDay = null;
	if (this.dayRecord.getActivities().size() == 0) {
	    activityDay = new ActivityDay();
	    this.dayRecord.getActivities().add(activityDay);
	} else {
	    activityDay = this.dayRecord.getActivities().get(0);
	}
	activityDay.setHours(this.dayRecord.getHours());
	activityDay.setProjectId((String) this.projectSpinner.getSelectedItem());
	activityDay.setSubProject((String) this.subProjectSpinner.getSelectedItem());
	activityDay.setTask(this.taskEditText.getText().toString().trim());
    }

    public void doRemoveDay(View view) {
	Log.d(LOGTAG, "doRemoveDay");

	showDialog(DetailDayActionEnum.REMOVE);

	AsyncTask<DetailDayActionEnum, Integer, Integer> connectionAsyncTask = new SaveRemoveDayAsyncTask(
		this);
	connectionAsyncTask.execute(DetailDayActionEnum.REMOVE);
    }

    private void showDialog(DetailDayActionEnum actionEnum) {
	String messageDialog = DIALOG_SAVING;
	if (actionEnum == DetailDayActionEnum.REMOVE) {
	    messageDialog = DIALOG_REMOVING;
	}
	pDialog = ProgressDialog.show(this, messageDialog, "Por favor, espera...", true);
    }

    @Override
    public void onBackPressed() {
	Log.d(LOGTAG, "onBackPressed");
	super.onBackPressed();

	Intent returnIntent = new Intent();
	setResult(RESULT_CANCELED, returnIntent);
	finish();
    }

    private class SaveRemoveDayAsyncTask extends AsyncTask<DetailDayActionEnum, Integer, Integer> {

	private Activity activity;
	private DetailDayActionEnum action;

	public SaveRemoveDayAsyncTask(Activity activity) {
	    this.activity = activity;
	}

	@Override
	protected Integer doInBackground(DetailDayActionEnum... action) {
	    Log.d(SaveRemoveDayAsyncTask.class.toString(), "doInBackground...");
	    DedicaHTMLParserMock parser = DedicaHTMLParserMock.getInstance();
	    this.action = action[0];
	    boolean ok = false;
	    switch (this.action) {
	    case SAVE:
		ok = parser.saveDay(dayRecord);
		break;

	    case REMOVE:
		dayRecord.setToRemove(true);
		ok = parser.removeDay(dayRecord);
		break;
	    default:
		break;
	    }
	    // TODO revisar si devolver boolean o tener varios tipos de error
	    return ok ? 1 : -1;
	}

	@Override
	protected void onPostExecute(Integer result) {
	    Log.d(SaveRemoveDayAsyncTask.class.toString(), "onPostExecute...");

	    if (result == 1) {
		this.returnMonthActivity();
		this.closeDialog();
	    } else {
		this.closeDialog();
		showToastMessage("Error de la web de dedicaciones");
	    }
	}

	private void closeDialog() {
	    pDialog.dismiss();
	    pDialog = null;
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
	    Log.d(SaveRemoveDayAsyncTask.class.toString(), "onProgressUpdate...");

	    super.onProgressUpdate(values);
	}

	private void returnMonthActivity() {
	    String message = MESSAGE_SAVE_OK;
	    if (this.action == DetailDayActionEnum.REMOVE) {
		message = MESSAGE_REMOVE_OK;
	    }

	    Intent returnIntent = new Intent();
	    returnIntent.putExtra("dayRecordModif", dayRecord);
	    setResult(RESULT_OK, returnIntent);

	    showToastMessage(message);

	    finish();
	}

	private void showToastMessage(String message) {
	    Toast toast = Toast.makeText(this.activity, message, Toast.LENGTH_LONG);
	    toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, 20);
	    toast.show();
	}
    }
}
