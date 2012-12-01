/**
 * 
 */
package com.edwise.dedicamns.asynctasks;

import java.io.Serializable;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.edwise.dedicamns.BatchMenuActivity;
import com.edwise.dedicamns.MonthViewActivity;
import com.edwise.dedicamns.R;
import com.edwise.dedicamns.beans.DayRecord;
import com.edwise.dedicamns.beans.MonthListBean;
import com.edwise.dedicamns.connections.ConnectionException;
import com.edwise.dedicamns.connections.ConnectionFacade;
import com.edwise.dedicamns.connections.WebConnection;
import com.edwise.dedicamns.utils.DayUtils;

/**
 * @author edwise
 * 
 */
public class MonthListAsyncTask extends AsyncTask<Integer, Integer, Integer> {
    private static final int OK = 1;
    private static final int ERROR = -1;

    private static final String LOGTAG = MonthListAsyncTask.class.toString();

    private Activity activity;
    private MonthListBean monthList = null;
    private ProgressDialog pDialog = null;

    public MonthListAsyncTask(Activity activity, ProgressDialog pDialog) {
	this.activity = activity;
	this.pDialog = pDialog;
    }

    @Override
    protected Integer doInBackground(Integer... params) {
	Log.d(LOGTAG, "doInBackground...");
	WebConnection webConnection = ConnectionFacade.getWebConnection();

	Integer result = fillNeededDataCache(webConnection);

	try {
	    if (params.length > 1) {
		int month = params[0];
		String year = String.valueOf(params[1]);
		List<DayRecord> listDays = webConnection.getListDaysAndActivitiesForMonthAndYear(month, year,
			true);
		monthList = new MonthListBean(DayUtils.getMonthName(month), year, listDays);
	    } else {
		monthList = webConnection.getListDaysAndActivitiesForCurrentMonth();
	    }
	} catch (ConnectionException e) {
	    Log.e(LOGTAG, "Error al obtener la lista de datos diarios", e);
	    result = ERROR;
	}

	return result == 1 && monthList.getListDays() != null && monthList.getListDays().size() > 0 ? OK
		: ERROR;
    }

    private Integer fillNeededDataCache(WebConnection webConnection) {
	Integer result = OK;
	try {
	    webConnection.fillProyectsAndSubProyectsCached();
	    webConnection.fillMonthsAndYearsCached();
	} catch (ConnectionException e) {
	    Log.e(LOGTAG, "Error al obtener datos de cacheo (proyectos, meses y años)", e);
	    result = ERROR;
	}
	return result;
    }

    @Override
    protected void onPostExecute(Integer result) {
	Log.d(LOGTAG, "onPostExecute...");
	super.onPostExecute(result);

	if (result == OK) {
	    this.launchMonthActivity();
	    this.closeDialog();
	    finalizeActivityIfBatch();
	} else {
	    this.closeDialog();
	    showToastMessage(activity.getString(R.string.msgWebError));
	}
    }

    protected void finalizeActivityIfBatch() {
	if (this.activity instanceof BatchMenuActivity) {
	    this.activity.finish();
	}
    }

    private void closeDialog() {
	pDialog.dismiss();
	pDialog = null;
    }

    private void launchMonthActivity() {
	Intent intent = new Intent(this.activity, MonthViewActivity.class);
	intent.putExtra("monthList", (Serializable) monthList);

	this.activity.startActivity(intent);
    }

    private void showToastMessage(String message) {
	Toast toast = Toast.makeText(this.activity, message, Toast.LENGTH_LONG);
	toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, 20);
	toast.show();
    }

}