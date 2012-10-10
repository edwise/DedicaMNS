package com.edwise.dedicamns;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.edwise.dedicamns.adapters.DayListAdapter;
import com.edwise.dedicamns.beans.DayRecord;

public class MonthViewActivity extends Activity {

    @SuppressWarnings("unchecked")
    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.month_view);

	List<DayRecord> listDayRecord = (List<DayRecord>) getIntent()
		.getSerializableExtra("dayList");
	// TODO comprobacion antes de si viene el dato

	final ListView listView = (ListView) findViewById(R.id.listV_main);
	listView.setAdapter(new DayListAdapter(this, listDayRecord));

	listView.setOnItemClickListener(new OnItemClickListener() {
	    public void onItemClick(AdapterView<?> adapterView, View view,
		    int position, long id) {
		Object o = listView.getItemAtPosition(position);
		DayRecord dayRecord = (DayRecord) o;

		Intent intent = new Intent(MonthViewActivity.this,
			DetailDayActivity.class);
		intent.putExtra("dayRecord", dayRecord);
		startActivity(intent);
	    }

	});
    }

}
