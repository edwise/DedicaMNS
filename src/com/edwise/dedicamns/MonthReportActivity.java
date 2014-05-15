package com.edwise.dedicamns;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.edwise.dedicamns.asynctasks.AppData;
import com.edwise.dedicamns.beans.MonthReportBean;
import com.edwise.dedicamns.beans.MonthReportRecord;
import com.edwise.dedicamns.menu.MenuUtils;

public class MonthReportActivity extends Activity {
	private static final String LOGTAG = MonthReportActivity.class.toString();

	private static final String STRING_SPACE = " ";
	private static final String STRING_TEXT_PLAIN = "text/plain";
	private static final String STRING_DOUBLE_ENTER = "\n\n";

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

		title.setText(monthReport.getMonthName() + STRING_SPACE + monthReport.getYear());

		fillAccountListTextView();
		fillTotalHoursTextView();
	}

	private void fillTotalHoursTextView() {
		StringBuilder totalHoursSB = new StringBuilder();
		totalHoursSB.append(getString(R.string.totalReport)).append(STRING_SPACE);
		totalHoursSB.append(monthReport.getTotal()).append(STRING_SPACE);
		totalHoursSB.append(getString(R.string.hoursReport));

		totalHours.setText(totalHoursSB.toString());
	}

	private void fillAccountListTextView() {
		for (MonthReportRecord record : monthReport.getMonthReportRecords()) {
			StringBuilder textAccount = new StringBuilder();
			textAccount.append(accountsList.getText()).append("\n - ");
			textAccount.append(record.getProjectId()).append(": ");
			textAccount.append(record.getHours()).append(STRING_SPACE);
			textAccount.append(getString(R.string.hoursReport));

			accountsList.setText(textAccount.toString());
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

	public void doShareMonthReport(View view) {
		Intent sendIntent = new Intent();
		sendIntent.setAction(Intent.ACTION_SEND);
		sendIntent.putExtra(Intent.EXTRA_TEXT, getDataReport());
		sendIntent.setType(STRING_TEXT_PLAIN);
		startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.shareIn)));
	}

	private String getDataReport() {
		StringBuilder dataReport = new StringBuilder();
		dataReport.append(title.getText()).append(STRING_DOUBLE_ENTER).append(accountsList.getText())
				.append(STRING_DOUBLE_ENTER).append(totalHours.getText());

		return dataReport.toString();
	}
}
