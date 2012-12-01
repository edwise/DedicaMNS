package com.edwise.dedicamns;

import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.edwise.dedicamns.adapters.DayListAdapter;
import com.edwise.dedicamns.asynctasks.MonthListAsyncTask;
import com.edwise.dedicamns.beans.DayRecord;
import com.edwise.dedicamns.beans.MonthListBean;
import com.edwise.dedicamns.connections.ConnectionFacade;
import com.edwise.dedicamns.menu.MenuUtils;

public class MonthViewActivity extends Activity {
    private static final String LOGTAG = MonthViewActivity.class.toString();

    static final int DAY_REQUEST = 0;

    private ListView listView = null;
    private MonthListBean monthList = null;
    private Parcelable listState = null;
    private Spinner monthSpinner = null;
    private Spinner yearSpinner = null;

    private ProgressDialog pDialog = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
	Log.d(LOGTAG, "onCreate...");

	super.onCreate(savedInstanceState);
	setContentView(R.layout.month_view);

	monthList = (MonthListBean) getIntent().getSerializableExtra("monthList");

	List<String> listMonths = ConnectionFacade.getWebConnection().getMonths();
	List<String> listYears = ConnectionFacade.getWebConnection().getYears();
	linkMonthSpinner(listMonths);
	linkYearsSpinner(listYears);

	this.markMonthYearSelected(monthList.getMonthName(), monthList.getYear());

	listView = (ListView) findViewById(R.id.listV_main);
	initListView();
    }

    private void initListView() {
	listView.setAdapter(new DayListAdapter(this, monthList.getListDays()));

	listView.setOnItemClickListener(new OnItemClickListener() {
	    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
		listState = listView.onSaveInstanceState();

		DayRecord dayRecord = (DayRecord) listView.getItemAtPosition(position);
		Intent intent = new Intent(MonthViewActivity.this, DetailDayActivity.class);
		intent.putExtra("dayRecord", dayRecord);
		startActivityForResult(intent, DAY_REQUEST);
	    }

	});
    }

    private void linkMonthSpinner(List<String> listMonths) {
	monthSpinner = (Spinner) findViewById(R.id.listMonthsSpinner);
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
	yearSpinner = (Spinner) findViewById(R.id.listYearsSpinner);
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

    @SuppressWarnings("unchecked")
    private void markMonthYearSelected(String month, String year) {
	ArrayAdapter<String> yearSpinnerAdapter = (ArrayAdapter<String>) yearSpinner.getAdapter();
	int spinnerPosition = yearSpinnerAdapter.getPosition(year);
	yearSpinner.setSelection(spinnerPosition);

	ArrayAdapter<String> monthSpinnerAdapter = (ArrayAdapter<String>) monthSpinner.getAdapter();
	spinnerPosition = monthSpinnerAdapter.getPosition(month);
	monthSpinner.setSelection(spinnerPosition);
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

    public void doUpdateList(View view) {
	Log.d(LOGTAG, "doUpdateList: Click en actualizar...");
	showDialog(getString(R.string.msgGettingMonthData));

	Integer month = this.monthSpinner.getSelectedItemPosition() + 1;
	Integer year = Integer.valueOf((String) this.yearSpinner.getSelectedItem());

	AsyncTask<Integer, Integer, Integer> monthListAsyncTask = new MonthListAsyncTask(this, pDialog);
	monthListAsyncTask.execute(new Integer[] { month, year, MonthListAsyncTask.IS_UPDATE_LIST });
    }

    private void showDialog(String message) {
	pDialog = ProgressDialog.show(this, message, getString(R.string.msgPleaseWait), true);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	Log.d(LOGTAG, "onActivityResult...");

	if (requestCode == DAY_REQUEST) {
	    if (resultCode == RESULT_OK) {
		DayRecord dayRecord = (DayRecord) data.getSerializableExtra("dayRecordModif");
		this.reDrawList(dayRecord);
	    }
	}
    }

    private void reDrawList(DayRecord dayRecord) {
	// Repintado de toda la lista, cambiando el dia que ha cambiado
	changeDayRecordInList(dayRecord);
	listView.setAdapter(new DayListAdapter(this, monthList.getListDays()));
	listView.onRestoreInstanceState(listState);

	Log.d(LOGTAG, "changeDataList: done");
    }

    private void changeDayRecordInList(DayRecord dayRecord) {
	for (DayRecord oldDayRecord : monthList.getListDays()) {
	    if (oldDayRecord.getDayNum() == dayRecord.getDayNum()) {
		if (dayRecord.getActivities().size() > 0 && dayRecord.getActivities().get(0).isToRemove()) {
		    oldDayRecord.clearDay();
		} else {
		    oldDayRecord.copyDayData(dayRecord);
		}
		break;
	    }
	}
    }

    public void updateList(MonthListBean monthListBean) {
	this.monthList = monthListBean;
	initListView();
    }

}
