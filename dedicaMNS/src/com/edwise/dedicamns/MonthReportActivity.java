package com.edwise.dedicamns;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.edwise.dedicamns.asynctasks.AppData;
import com.edwise.dedicamns.beans.MonthReportBean;
import com.edwise.dedicamns.beans.MonthReportRecord;
import com.edwise.dedicamns.menu.MenuUtils;

public class MonthReportActivity extends Activity {
    private static final String LOGTAG = MonthReportActivity.class.toString();

    private MonthReportBean monthReport;

    private TextView title;
    private TextView accountsList;
    private TextView totalHours;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.month_report);
	AppData.setCurrentActivity(this);

	if (savedInstanceState != null) {
	    restoreFromChangeOrientation(savedInstanceState);
	} else {
	    monthReport = (MonthReportBean) getIntent().getSerializableExtra("monthReport");
	}

	title = (TextView) findViewById(R.id.MReportTitleTextView);
	accountsList = (TextView) findViewById(R.id.MReportAccountsTextView);
	totalHours = (TextView) findViewById(R.id.MReportTotalTextView);

	title.setText(monthReport.getMonthName() + " " + monthReport.getYear());

	fillAccountListTextView();
	fillTotalHoursTextView();
    }

    private void fillTotalHoursTextView() {
	totalHours.setText(getString(R.string.totalReport) + " " + monthReport.getTotal() + " "
		+ getString(R.string.hoursReport));
    }

    private void fillAccountListTextView() {
	for (MonthReportRecord record : monthReport.getMonthReportRecords()) {
	    accountsList.setText(accountsList.getText() + "\n - " + record.getProjectId() + ": "
		    + record.getHours() + " " + getString(R.string.hoursReport));
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

    private void restoreFromChangeOrientation(Bundle savedInstanceState) {
	monthReport = (MonthReportBean) savedInstanceState.getSerializable("monthReport");
    }

    @Override
    protected void onRestart() {
	super.onRestart();
	AppData.setCurrentActivity(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
	Log.d(LOGTAG, "onSaveInstanceState");

	outState.putSerializable("monthReport", monthReport);

	super.onSaveInstanceState(outState);
    }

}
