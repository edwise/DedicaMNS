package com.edwise.dedicamns;

import java.io.Serializable;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.edwise.dedicamns.beans.DayRecord;
import com.edwise.dedicamns.mocks.DedicaHTMLParserMock;

public class DetailDayActivity extends Activity {

    enum DetailDayActionEnum {
	SAVE, REMOVE;
    }

    private DayRecord dayRecord = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
	Log.d(DetailDayActivity.class.toString(), "onCreate");
	super.onCreate(savedInstanceState);
	setContentView(R.layout.detail_day);

	this.dayRecord = (DayRecord) getIntent().getSerializableExtra(
		"dayRecord");
	if (dayRecord != null) {
	    TextView view = (TextView) findViewById(R.id.detailDayInfoTextView);
	    view.setText(dayRecord.getDayNum() + " - " + dayRecord.getDayName());
	    EditText text = (EditText) findViewById(R.id.detailHoursEditText);
	    text.setText(dayRecord.getHours());
	    text = (EditText) findViewById(R.id.detailProjectEditText);
	    text.setText(dayRecord.getProjectId());
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

	AsyncTask<DetailDayActionEnum, Integer, Integer> connectionAsyncTask = new SaveRemoveDayAsyncTask(
		this.dayRecord, this);
	connectionAsyncTask.execute(DetailDayActionEnum.SAVE);
    }

    public void doRemoveDay(View view) {
	Log.d(DetailDayActivity.class.toString(), "doRemoveDay");

	AsyncTask<DetailDayActionEnum, Integer, Integer> connectionAsyncTask = new SaveRemoveDayAsyncTask(
		this.dayRecord, this);
	connectionAsyncTask.execute(DetailDayActionEnum.REMOVE);
    }

    private class SaveRemoveDayAsyncTask extends
	    AsyncTask<DetailDayActionEnum, Integer, Integer> {

	private DayRecord dayRecord;
	private Activity activity;
	private DetailDayActionEnum action;

	public SaveRemoveDayAsyncTask(DayRecord dayRecord, Activity activity) {
	    this.dayRecord = dayRecord;
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
	    List<DayRecord> listDays = DedicaHTMLParserMock.getInstance().getListFromHTML();

	    intent.putExtra("dayList", (Serializable) listDays);
	    this.activity.startActivity(intent);
	    this.activity.finish();

	    String message = "Guardado registro de horas correctamente";
	    if (this.action == DetailDayActionEnum.REMOVE) {
		message = "Borrado registro de horas correctamente";
	    }

	    Toast toast = Toast.makeText(this.activity, message,
		    Toast.LENGTH_LONG);
	    toast.setGravity(Gravity.CENTER, 0, 100);
	    toast.show();
	}
    }
}
