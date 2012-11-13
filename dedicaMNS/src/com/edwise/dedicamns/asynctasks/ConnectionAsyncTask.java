/**
 * 
 */
package com.edwise.dedicamns.asynctasks;

import java.util.Map;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.edwise.dedicamns.MainMenuActivity;
import com.edwise.dedicamns.connections.ConnectionFacade;

/**
 * @author edwise
 * 
 */
public class ConnectionAsyncTask extends AsyncTask<Map<String, String>, Integer, Integer> {

    private ProgressDialog pDialog;
    private Activity activity;

    public ConnectionAsyncTask(Activity activity, ProgressDialog pDialog) {
	this.activity = activity;
	this.pDialog = pDialog;
    }

    @Override
    protected Integer doInBackground(Map<String, String>... data) {
	Log.d(ConnectionAsyncTask.class.toString(), "doInBackground: Comenzando asyncTask...");
	Map<String, String> dataLogin = data[0];
	saveSharedPreferences(dataLogin);

	int result = -1; // Si no hay conexión a internet, devolveremos -1
	if (ConnectionFacade.getWebConnection().isOnline(activity)) {
	    result = ConnectionFacade.getWebConnection().connectWeb(dataLogin.get("user"),
		    dataLogin.get("pass"));
	}

	// TODO enum con tipos de retorno, errores, etc
	return result;
    }

    private void saveSharedPreferences(Map<String, String> dataLogin) {
	SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
	if (dataLogin.get("check").equals("true")) { // Guardamos todo
	    SharedPreferences.Editor editor = sharedPref.edit();
	    editor.putString("user", dataLogin.get("user")); // TODO constantes
							     // y demás!!
	    editor.putString("pass", dataLogin.get("pass"));
	    editor.putBoolean("remember", Boolean.valueOf(dataLogin.get("check")));
	    editor.commit();
	} else { // borramos todo
	    sharedPref.edit().clear().commit();
	}
    }

    @Override
    protected void onPostExecute(Integer result) {
	Log.d(ConnectionAsyncTask.class.toString(), "onPostExecute: Finalizando asyncTask...");

	// TODO realizar la conexión, preparar un mock que de ok o algo así...
	if (result == 200) { // TODO comprobaciones de resultado y constantes!
	    this.startNextActivity();
	} else {
	    showToastErrorConnection();
	}

	closeDialog();
    }

    private void closeDialog() {
	pDialog.dismiss();
	pDialog = null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
	Log.d(ConnectionAsyncTask.class.toString(), "onProgressUpdate: Actualizando progreso...");
    }

    private void startNextActivity() {
	Intent intent = new Intent(this.activity, MainMenuActivity.class);

	this.activity.startActivity(intent);
	this.activity.finish();

	showToastConnected();

    }

    private void showToastConnected() {
	Toast toast = Toast.makeText(this.activity, "Conectado!", Toast.LENGTH_LONG);
	toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, 20);
	toast.show();
    }

    private void showToastErrorConnection() {
	Toast toast = Toast.makeText(this.activity,
		"Error en la conexión: revisa que tu usuario y password sean correctos", Toast.LENGTH_LONG);
	toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, 20);
	toast.show();
    }

}
