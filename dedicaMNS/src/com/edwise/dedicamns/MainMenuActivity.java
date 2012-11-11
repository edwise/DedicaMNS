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
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.edwise.dedicamns.asynctasks.MonthListAsyncTask;
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
	boolean returned = false;
	switch (item.getItemId()) {
	case R.id.menu_logout:
	    // TODO llamada a loginactivity, desactivando y borrando datos. (en clase generica para todos)
	    returned = true;
	case R.id.menu_settings:
	    // TODO llamada a settings, en clase generica para todos.
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
	private List<String> listProyects = null;
	private List<String> listMonths = null;

	public BatchMenuAsyncTask(Activity activity) {
	    this.activity = activity;
	}

	@Override
	protected Integer doInBackground(Integer... params) {
	    Log.d(BatchMenuAsyncTask.class.toString(), "doInBackground...");
	    // TODO desmockear
	    DedicaHTMLParserMock parser = DedicaHTMLParserMock.getInstance();
	    listProyects = parser.getArrayProjects();
	    listMonths = parser.getMonths();

	    return listProyects != null && listProyects.size() > 0 ? 1 : -1; // TODO
									     // constantes
									     // de
									     // error
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
		showToastMessage("Error de la web de dedicaciones");
	    }
	}

	private void closeDialog() {
	    pDialog.dismiss();
	    pDialog = null;
	}

	private void launchBatchMenuActivity() {
	    Intent intent = new Intent(this.activity, BatchMenuActivity.class);
	    intent.putExtra("projectList", (Serializable) listProyects);
	    intent.putExtra("monthsList", (Serializable) listMonths);

	    this.activity.startActivity(intent);
	}

	private void showToastMessage(String message) {
	    Toast toast = Toast.makeText(this.activity, message, Toast.LENGTH_LONG);
	    toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, 20);
	    toast.show();
	}

    }
}
