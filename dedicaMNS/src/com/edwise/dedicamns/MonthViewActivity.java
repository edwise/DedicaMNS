package com.edwise.dedicamns;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.edwise.dedicamns.adapters.DayListAdapter;
import com.edwise.dedicamns.beans.DayRecord;

public class MonthViewActivity extends Activity {
    
    static final int DAY_REQUEST = 0;
    
    private ListView listView = null;

    @SuppressWarnings("unchecked")
    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.month_view);

	List<DayRecord> listDayRecord = (List<DayRecord>) getIntent()
		.getSerializableExtra("dayList");
	// TODO comprobacion antes de si viene el dato

	listView = (ListView) findViewById(R.id.listV_main);
	listView.setAdapter(new DayListAdapter(this, listDayRecord));

	listView.setOnItemClickListener(new OnItemClickListener() {
	    public void onItemClick(AdapterView<?> adapterView, View view,
		    int position, long id) {
		Object o = listView.getItemAtPosition(position);
		DayRecord dayRecord = (DayRecord) o;

		Intent intent = new Intent(MonthViewActivity.this,
			DetailDayActivity.class);
		intent.putExtra("dayRecord", dayRecord);
		startActivityForResult(intent, DAY_REQUEST);
	    }

	});
    }
    
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	Log.d(MonthViewActivity.class.toString(), "onActivityResult...");
	
	if (requestCode == DAY_REQUEST) {
	    if (resultCode == RESULT_OK) {
		DayRecord dayRecord = (DayRecord) data.getSerializableExtra("dayRecordModif");
		if (dayRecord.isToRemove()) {
		    // TODO borrar datos del list
		    this.showToastMessage("Borrar del list...");
		}
		else {
		    // TODO actualizar datos del list
		    this.changeDataList(dayRecord);
		}
	    }
	    else {
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
    
    private void changeDataList(DayRecord dayRecord) {
	// TODO!!!!  Esto no vale, no es persistente!!!
//	int visiblePosition = listView.getFirstVisiblePosition();	
	View v = listView.getChildAt(3);
	TextView someTextView = (TextView) v.findViewById(R.id.projectId);
	someTextView.setText("Changed!!");		
	
	Log.d(MonthViewActivity.class.toString(), "changeDataList: done");
    }

}
