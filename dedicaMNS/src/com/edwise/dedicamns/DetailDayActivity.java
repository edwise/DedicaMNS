package com.edwise.dedicamns;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.edwise.dedicamns.beans.DayRecord;

public class DetailDayActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.detail_day);

	DayRecord dayRecord = (DayRecord) getIntent().getSerializableExtra(
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
	}
    }

}
