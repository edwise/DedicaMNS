package com.edwise.dedicamns;

import java.io.Serializable;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import com.edwise.dedicamns.beans.DayRecord;
import com.edwise.dedicamns.mocks.DedicaHTMLParserMock;

public class MainMenuActivity extends Activity {
    
    private ProgressDialog pDialog = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);
    }
    
    // TODO metodo al proceso batch

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
    
    public void doShowListMonth(View view) {
	Log.d(MainMenuActivity.class.toString(), "showListMonth");

	showDialog();
	AsyncTask<Integer, Integer, Integer> monthListAsyncTask = new MonthListAsyncTask(
		this);
	monthListAsyncTask.execute(1);
    }
    
    private void showDialog() {
	pDialog = ProgressDialog.show(this, "Obteniendo datos del mes",
		"Por favor, espera...", true);
    }
    
    private class MonthListAsyncTask extends
    	AsyncTask<Integer, Integer, Integer> {

	private Activity activity;
	private List<DayRecord> listDays = null;
	
	public MonthListAsyncTask(Activity activity) {
	    this.activity = activity;
	}
	
	@Override
	protected Integer doInBackground(Integer... params) {
	    Log.d(MonthListAsyncTask.class.toString(), "doInBackground...");
	    // TODO desmockear
	    DedicaHTMLParserMock parser = DedicaHTMLParserMock.getInstance();
	    listDays = parser.getListFromHTML();
	    
	    return listDays != null && listDays.size() > 0?1:-1; // TODO constantes de error
	}

	@Override
	protected void onPostExecute(Integer result) {
	    Log.d(MonthListAsyncTask.class.toString(), "onPostExecute...");
	    super.onPostExecute(result);
	    
	    if (result == 1) {
		this.launchMonthActivity();
		this.closeDialog();
	    }
	    else {
		this.closeDialog();
		showToastMessage("Error de la web de dedicaciones");
	    }
	}
	
	private void closeDialog() {
	    pDialog.dismiss();
	    pDialog = null;
	}
	
	private void launchMonthActivity() {
	    Intent intent = new Intent(this.activity, MonthViewActivity.class);
	    intent.putExtra("dayList", (Serializable) listDays);

	    this.activity.startActivity(intent);     	    
	}

	private void showToastMessage(String message) {
	    Toast toast = Toast.makeText(this.activity, message,
		    Toast.LENGTH_LONG);
	    toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, 20);
	    toast.show();
	}
	
    }
}
