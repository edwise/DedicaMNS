package com.edwise.dedicamns;

import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.edwise.dedicamns.asynctasks.MonthListAsyncTask;
import com.edwise.dedicamns.beans.BatchDataBean;
import com.edwise.dedicamns.menu.MenuUtils;
import com.edwise.dedicamns.mocks.DedicaHTMLParserMock;

public class BatchMenuActivity extends Activity {

    private Spinner monthSpinner = null;
    private Spinner projectSpinner = null;
    private Spinner subProjectSpinner = null;
    private EditText taskEditText = null;
    private Spinner typeHourSpinner = null;

    private ProgressDialog pDialog = null;

    @SuppressWarnings("unchecked")
    @Override
    public void onCreate(Bundle savedInstanceState) {
	Log.d(BatchMenuActivity.class.toString(), "onCreate");
	super.onCreate(savedInstanceState);
	setContentView(R.layout.batch_menu);

	List<String> listMonths = (List<String>) getIntent().getSerializableExtra("monthsList");
	List<String> listProjects = (List<String>) getIntent().getSerializableExtra("projectList");

	linkMonthSpinner(listMonths);
	linkProjectSpinner(listProjects);
	if (subProjectSpinner == null) {
	    linkSubProjectSpinner((String) this.projectSpinner.getSelectedItem());
	}
	linkTypeHourSpinner();
	taskEditText = (EditText) findViewById(R.id.batchTaskEditText);
    }

    private void linkTypeHourSpinner() {
	typeHourSpinner = (Spinner) findViewById(R.id.batchTypeHourSpinner);
	ArrayAdapter<String> typeHouradapter = new ArrayAdapter<String>(this,
		android.R.layout.simple_spinner_item, Arrays.asList(new String[] { "8:30 de L a J y 6:00 V",
			"8:00 L a V" })); // TODO meter en otro lao
	typeHourSpinner.setAdapter(typeHouradapter);
	typeHourSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
	    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
	    }

	    public void onNothingSelected(AdapterView<?> parent) {
	    }

	});
    }

    private void linkProjectSpinner(List<String> listProjects) {
	projectSpinner = (Spinner) findViewById(R.id.batchProjectSpinner);
	ArrayAdapter<String> projectAdapter = new ArrayAdapter<String>(this,
		android.R.layout.simple_spinner_item, listProjects);
	projectSpinner.setAdapter(projectAdapter);
	projectSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
	    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
		linkSubProjectSpinner((String) parent.getItemAtPosition(pos));
	    }

	    public void onNothingSelected(AdapterView<?> parent) {
	    }

	});
    }

    private void linkMonthSpinner(List<String> listMonths) {
	monthSpinner = (Spinner) findViewById(R.id.batchMonthsSpinner);
	ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
		listMonths);
	monthSpinner.setAdapter(adapter);
	monthSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
	    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
	    }

	    public void onNothingSelected(AdapterView<?> parent) {
	    }

	});
    }

    private void linkSubProjectSpinner(String projectSelected) {
	List<String> subProjectArray = null;
	subProjectArray = DedicaHTMLParserMock.getInstance().getArraySubProjects(projectSelected);
	// Cargar el combo de subproyectos
	if (subProjectSpinner == null) {
	    subProjectSpinner = (Spinner) findViewById(R.id.batchSubprojectSpinner);
	}
	ArrayAdapter<String> subProjectAdapter = new ArrayAdapter<String>(this,
		android.R.layout.simple_spinner_item, subProjectArray);
	subProjectSpinner.setAdapter(subProjectAdapter);
	subProjectSpinner.setSelection(0);
	subProjectSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
	    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
	    }

	    public void onNothingSelected(AdapterView<?> parent) {
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
	    // TODO llamada a acerca de, en clase generica para todos.
	    returned = true;
	default:
	    returned = super.onOptionsItemSelected(item);
	}

	return returned;
    }

    public void doLaunchBatch(View view) {
	Log.d(BatchMenuActivity.class.toString(), "doLaunchBatch");

	if (!validateSpinnerProjectSelected()) {
	    showToastMessage("Debe seleccionar algún proyecto");
	} else {
	    launchBatchProcessWithAlertDialog();
	}
    }

    private boolean validateSpinnerProjectSelected() {
	return projectSpinner.getSelectedItemPosition() != 0;
    }

    private void launchBatchProcessWithAlertDialog() {
	AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
	alertDialogBuilder.setTitle("Se va a ejecutar el proceso de imputación");
	alertDialogBuilder.setMessage("¿Continuar?");
	alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
	    public void onClick(DialogInterface dialog, int which) {
		launchBatchProcess();
	    }
	});
	alertDialogBuilder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
	    public void onClick(DialogInterface dialog, int which) {
	    }
	});
	alertDialogBuilder.show();
    }

    private void launchBatchProcess() {
	showDialog();
	BatchDataBean batchData = fillDataBean();
	AsyncTask<BatchDataBean, Integer, Integer> batchTask = new BatchAsyncTask(this);
	batchTask.execute(batchData);
    }

    private void showToastMessage(String message) {
	Toast toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
	toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, 20);
	toast.show();
    }

    private void showDialog() {
	pDialog = ProgressDialog.show(this, "Ejecutando imputación en batch", "Por favor, espera...", true);
    }

    private BatchDataBean fillDataBean() {
	BatchDataBean batchData = new BatchDataBean();
	batchData.setMonth((String) this.monthSpinner.getSelectedItem());
	batchData.setProject((String) this.projectSpinner.getSelectedItem());
	batchData.setSubProject((String) this.subProjectSpinner.getSelectedItem());
	batchData.setTask(this.taskEditText.getText().toString().trim());
	batchData.setTypeHour((String) this.typeHourSpinner.getSelectedItem());

	return batchData;
    }

    public void doBack(View view) {
	Log.d(BatchMenuActivity.class.toString(), "doBack");
	finish();
    }

    private void launchMonthActivity() {
	Log.d(BatchMenuActivity.class.toString(), "launchMonthActivity");

	showDialog("Obteniendo datos del mes");
	AsyncTask<Integer, Integer, Integer> monthListAsyncTask = new MonthListAsyncTask(this, pDialog);
	monthListAsyncTask.execute(1);
    }

    private void showDialog(String message) {
	pDialog = ProgressDialog.show(this, message, "Por favor, espera...", true);
    }

    private class BatchAsyncTask extends AsyncTask<BatchDataBean, Integer, Integer> {

	private Activity activity;
	private BatchDataBean batchData;

	public BatchAsyncTask(Activity activity) {
	    this.activity = activity;
	}

	@Override
	protected Integer doInBackground(BatchDataBean... batchData) {
	    Log.d(BatchAsyncTask.class.toString(), "doInBackground...");
	    DedicaHTMLParserMock parser = DedicaHTMLParserMock.getInstance();
	    this.batchData = batchData[0];
	    // TODO revisar si devolver boolean o tener varios tipos de error
	    return parser.proccesBatch(this.batchData);
	}

	@Override
	protected void onPostExecute(Integer result) {
	    Log.d(BatchAsyncTask.class.toString(), "onPostExecute...");

	    if (result == 1) {
		this.closeDialog();
		this.showOkAlertDialog();
	    } else {
		this.closeDialog();
		showToastMessage("Error en la imputación!");
		// TODO mostrar otro AlertDialog?
	    }
	}

	private void closeDialog() {
	    pDialog.dismiss();
	    pDialog = null;
	}

	private void showOkAlertDialog() {
	    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this.activity);
	    alertDialogBuilder.setTitle("Proceso de imputación terminado!");
	    alertDialogBuilder.setMessage("¿Desea ver el listado mensual de horas imputadas?");
	    alertDialogBuilder.setPositiveButton("SI", new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int which) {
		    launchMonthActivity();
		}
	    });
	    alertDialogBuilder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int which) {
		    finish();
		}
	    });
	    alertDialogBuilder.show();
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
	    Log.d(BatchAsyncTask.class.toString(), "onProgressUpdate...");
	    // TODO intentar actualizar esto...
	    super.onProgressUpdate(values);
	}

	private void showToastMessage(String message) {
	    Toast toast = Toast.makeText(this.activity, message, Toast.LENGTH_LONG);
	    toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, 20);
	    toast.show();
	}
    }
}
