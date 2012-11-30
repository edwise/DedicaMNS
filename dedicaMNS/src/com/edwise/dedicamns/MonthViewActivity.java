package com.edwise.dedicamns;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.edwise.dedicamns.adapters.DayListAdapter;
import com.edwise.dedicamns.beans.DayRecord;
import com.edwise.dedicamns.beans.MonthListBean;
import com.edwise.dedicamns.menu.MenuUtils;

public class MonthViewActivity extends Activity {
    private static final String LOGTAG = MonthViewActivity.class.toString();

    static final int DAY_REQUEST = 0;

    private ListView listView = null;
    private MonthListBean monthList = null;
    private Parcelable listState = null;
    private TextView monthYearTextView = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
	Log.d(LOGTAG, "onCreate...");

	super.onCreate(savedInstanceState);
	setContentView(R.layout.month_view);

	// TODO un poco de refactor...
	monthList = (MonthListBean) getIntent().getSerializableExtra("monthList");

	monthYearTextView = (TextView) findViewById(R.id.monthAndYearTextView);
	monthYearTextView.setText(monthList.getMonthName() + " " + monthList.getYear());

	listView = (ListView) findViewById(R.id.listV_main);
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

}
