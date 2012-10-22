package com.edwise.dedicamns;

import java.io.Serializable;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.edwise.dedicamns.beans.DayRecord;
import com.edwise.dedicamns.mocks.DedicaHTMLParserMock;

public class DetailDayActivity extends Activity {
    private static final String MESSAGE_SAVE_OK = "Guardado registro de horas correctamente";
    private static final String MESSAGE_REMOVE_OK = "Borrado registro de horas correctamente";
    private static final String DIALOG_SAVING = "Guardando datos";
    private static final String DIALOG_REMOVING = "Borrando datos";

    enum DetailDayActionEnum {
	SAVE, REMOVE;
    }

    private DayRecord dayRecord = null;
    private ProgressDialog pDialog = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
	Log.d(DetailDayActivity.class.toString(), "onCreate");
	super.onCreate(savedInstanceState);
	setContentView(R.layout.detail_day);
	
	List<String> spinnerArray = DedicaHTMLParserMock.getInstance().getArrayProjects();

	this.dayRecord = (DayRecord) getIntent().getSerializableExtra(
		"dayRecord");
	if (dayRecord != null) {
	    TextView view = (TextView) findViewById(R.id.detailDayInfoTextView);
	    view.setText(dayRecord.getDayNum() + " - " + dayRecord.getDayName());
	    EditText text = (EditText) findViewById(R.id.detailHoursEditText);
	    text.setText(dayRecord.getHours());
	    
	    Spinner spinner = (Spinner) findViewById(R.id.detailProjectSpinner);
	    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
		        android.R.layout.simple_spinner_dropdown_item,
		            spinnerArray);
	    // Specify the layout to use when the list of choices appears
	    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    spinner.setAdapter(adapter);
	    spinner.setSelection(3); // TODO dayRecord.getProjectId()
	    
	    spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
		public void onItemSelected(AdapterView<?> parent, View view,
			int pos, long id) {
		    // TODO obtener el seleccionado con parent.getItemAtPosition(pos)
		}

		public void onNothingSelected(AdapterView<?> parent) {
		    // TODO Auto-generated method stub
		}
	    });
	    
	    text = (EditText) findViewById(R.id.detailSubprojectEditText);
	    text.setText(dayRecord.getSubProject());
	    text = (EditText) findViewById(R.id.detailTaskEditText);
	    text.setText(dayRecord.getTask());
	} else {
	    dayRecord = new DayRecord();
	}
    }

    public void doSaveDay(View view) {
	Log.d(DetailDayActivity.class.toString(), "doSaveDay");

	// TODO rellenar el dayRecord con los nuevos datos. Ojo, validaciones!
	showDialog(DetailDayActionEnum.SAVE);

	AsyncTask<DetailDayActionEnum, Integer, Integer> connectionAsyncTask = new SaveRemoveDayAsyncTask(
		this);
	connectionAsyncTask.execute(DetailDayActionEnum.SAVE);
    }

    public void doRemoveDay(View view) {
	Log.d(DetailDayActivity.class.toString(), "doRemoveDay");

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
	pDialog = ProgressDialog.show(this, messageDialog,
		"Por favor, espera...", true);
    }

    private class SaveRemoveDayAsyncTask extends
	    AsyncTask<DetailDayActionEnum, Integer, Integer> {

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

	    this.startMonthActivity();

	    this.closeDialog();
	}

	private void closeDialog() {
	    pDialog.dismiss();
	    pDialog = null;
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
	    Log.d(SaveRemoveDayAsyncTask.class.toString(),
		    "onProgressUpdate...");

	    super.onProgressUpdate(values);
	}

	private void startMonthActivity() {
	    Intent intent = new Intent(this.activity, MonthViewActivity.class);

	    // TODO revisar si volver a obtener esto o que hacer...
	    List<DayRecord> listDays = DedicaHTMLParserMock.getInstance()
		    .getListFromHTML();

	    intent.putExtra("dayList", (Serializable) listDays);
	    this.activity.startActivity(intent);
	    this.activity.finish();

	    String message = MESSAGE_SAVE_OK;
	    if (this.action == DetailDayActionEnum.REMOVE) {
		message = MESSAGE_REMOVE_OK;
	    }

	    showToastMessage(message);
	}

	private void showToastMessage(String message) {
	    Toast toast = Toast.makeText(this.activity, message,
		    Toast.LENGTH_LONG);
	    toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, 20);
	    toast.show();
	}
    }
}
