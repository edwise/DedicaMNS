package com.edwise.dedicamns;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.edwise.dedicamns.adapters.DayListAdapter;
import com.edwise.dedicamns.beans.DayRecord;

public class MonthViewActivity extends Activity {

    @SuppressWarnings("unchecked")
    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.month_view);

	List<DayRecord> listDayRecord  = (List<DayRecord>) getIntent().getSerializableExtra("dayList");
	// TODO comprobacion antes de si viene el dato
	
	final ListView listView = (ListView) findViewById(R.id.listV_main);        
        listView.setAdapter(new DayListAdapter(this, listDayRecord));
        
//        list.setOnItemClickListener(new OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> a, View v, int position, long id) { 
//        	Object o = list.getItemAtPosition(position);
//        	ItemDetails obj_itemDetails = (ItemDetails)o;
//        	Toast.makeText(ListViewImagesActivity.this, "You have chosen : " + " " + obj_itemDetails.getName(), Toast.LENGTH_LONG).show();
//            }  
//        });
    }

}
