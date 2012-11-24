package com.edwise.dedicamns;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.edwise.dedicamns.asynctasks.MonthListAsyncTask;
import com.edwise.dedicamns.connections.ConnectionException;
import com.edwise.dedicamns.connections.ConnectionFacade;
import com.edwise.dedicamns.connections.WebConnection;
import com.edwise.dedicamns.menu.MenuUtils;

public class MainMenuActivity extends Activity {
    private static final String LOGTAG = MainMenuActivity.class.toString();

    private ProgressDialog pDialog = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.main_menu);
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

    public void doShowListMonth(View view) {
	Log.d(MainMenuActivity.class.toString(), "doShowListMonth");

	showDialog("Obteniendo datos del mes");
	AsyncTask<Integer, Integer, Integer> monthListAsyncTask = new MonthListAsyncTask(this, pDialog);
	monthListAsyncTask.execute(1);
    }

    public void doShowBatchMenu(View view) {
	Log.d(MainMenuActivity.class.toString(), "doShowBatchMenu");

	showDialog("Obteniendo datos necesarios");
	AsyncTask<Integer, Integer, Integer> batchMenuAsyncTask = new BatchMenuAsyncTask(this);
	batchMenuAsyncTask.execute(1);
    }

    private void showDialog(String message) {
	pDialog = ProgressDialog.show(this, message, "Por favor, espera...", true);
    }

    private class BatchMenuAsyncTask extends AsyncTask<Integer, Integer, Integer> {

	private Activity activity;

	public BatchMenuAsyncTask(Activity activity) {
	    this.activity = activity;
	}

	@Override
	protected Integer doInBackground(Integer... params) {
	    Log.d(BatchMenuAsyncTask.class.toString(), "doInBackground...");

	    WebConnection webConnection = ConnectionFacade.getWebConnection();
	    Integer result = 1;
	    try {
		webConnection.fillProyectsAndSubProyectsCached();
		webConnection.fillMonthsAndYearsCached();
	    } catch (ConnectionException e) {
		Log.e(LOGTAG, "Error al obtener datos de cacheo (proyectos, meses y años)", e);
		result = -1;
	    }

	    // TODO constantes de error
	    return result;
	}

	@Override
	protected void onPostExecute(Integer result) {
	    Log.d(BatchMenuAsyncTask.class.toString(), "onPostExecute...");
	    super.onPostExecute(result);

	    if (result == 1) {
		this.launchBatchMenuActivity();
		this.closeDialog();
	    } else {
		this.closeDialog();
		showToastMessage("Error en la conexión con la web de dedicaciones");
	    }
	}

	private void closeDialog() {
	    pDialog.dismiss();
	    pDialog = null;
	}

	private void launchBatchMenuActivity() {
	    Intent intent = new Intent(this.activity, BatchMenuActivity.class);

	    this.activity.startActivity(intent);
	}

	private void showToastMessage(String message) {
	    Toast toast = Toast.makeText(this.activity, message, Toast.LENGTH_LONG);
	    toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, 20);
	    toast.show();
	}

    }
}
