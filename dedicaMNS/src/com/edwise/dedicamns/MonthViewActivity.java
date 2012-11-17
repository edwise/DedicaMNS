package com.edwise.dedicamns;

import java.util.List;

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
import android.widget.Toast;

import com.edwise.dedicamns.adapters.DayListAdapter;
import com.edwise.dedicamns.beans.DayRecord;
import com.edwise.dedicamns.menu.MenuUtils;

public class MonthViewActivity extends Activity {

    static final int DAY_REQUEST = 0;

    private ListView listView = null;
    private List<DayRecord> listDayRecord = null;
    private Parcelable listState = null;

    @SuppressWarnings("unchecked")
    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.month_view);

	listDayRecord = (List<DayRecord>) getIntent().getSerializableExtra("dayList");
	// TODO comprobacion antes de si viene el dato

	listView = (ListView) findViewById(R.id.listV_main);
	listView.setAdapter(new DayListAdapter(this, listDayRecord));

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
	case R.id.menu_about_us:
	    MenuUtils.goToAbout(this);
	    returned = true;
	default:
	    returned = super.onOptionsItemSelected(item);
	}

	return returned;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	Log.d(MonthViewActivity.class.toString(), "onActivityResult...");

	if (requestCode == DAY_REQUEST) {
	    if (resultCode == RESULT_OK) {
		DayRecord dayRecord = (DayRecord) data.getSerializableExtra("dayRecordModif");
		this.reDrawList(dayRecord);
	    } else {
		// TODO hacer algo??
		this.showToastMessage("Nada!!");
	    }
	}
    }

    private void showToastMessage(String message) {
	Toast toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
	toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER, 0, -10);
	toast.show();
    }

    private void reDrawList(DayRecord dayRecord) {
	// Repintar toda la lista, cambiando el dia que ha cambiado
	changeDayRecordInList(dayRecord);
	listView.setAdapter(new DayListAdapter(this, listDayRecord));
	listView.onRestoreInstanceState(listState);

	Log.d(MonthViewActivity.class.toString(), "changeDataList: done");
    }

    private void changeDayRecordInList(DayRecord dayRecord) {
	for (DayRecord oldDayRecord : listDayRecord) {
	    if (oldDayRecord.getDayNum() == dayRecord.getDayNum()) {
		if (dayRecord.isToRemove()) {
		    oldDayRecord.clearDay();
		} else {
		    oldDayRecord.copyDayData(dayRecord);
		}
		break;
	    }
	}
    }

}
