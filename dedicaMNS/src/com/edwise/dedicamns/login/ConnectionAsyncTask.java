/**
 * 
 */
package com.edwise.dedicamns.login;

import java.util.Map;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

/**
 * @author edwise
 * 
 */
public class ConnectionAsyncTask extends AsyncTask<Map<String, String>, Integer, Integer> {
    
    private ProgressDialog pDialog;
    private Activity loginActivity;
    
    public ConnectionAsyncTask(Activity activity, ProgressDialog pDialog) {
	this.loginActivity = activity;
	this.pDialog = pDialog;
    }

    @Override
    protected Integer doInBackground(Map<String, String>... data) {
	Log.d(ConnectionAsyncTask.class.toString(), "doInBackground: Comenzando asyncTask...");
	Map<String, String> dataLogin = data[0];
	// TODO Guardar el sharedpreferences si está el check, si no borrarlo!
	saveSharedPreferences(dataLogin);
	
	// TODO conexión real
	try {
	    for (int i = 0; i < 3; i++) {
		Thread.sleep(1000);
		publishProgress(i+1);
	    }
	} catch (InterruptedException e) {
	    Log.e(ConnectionAsyncTask.class.toString(), "doInBackground: Error en Thread.sleep()...", e);
	    e.printStackTrace();
	}
	
	return 1; // TODO enum con tipos de retorno, errores, etc
    }

    private void saveSharedPreferences(Map<String, String> dataLogin) {
	SharedPreferences sharedPref = loginActivity.getPreferences(Context.MODE_PRIVATE);
	if (dataLogin.get("check").equals("true")) { // Guardamos todo
	    SharedPreferences.Editor editor = sharedPref.edit();
	    editor.putString("user", dataLogin.get("user")); // TODO constantes y demás!!
	    editor.putString("pass", dataLogin.get("pass"));
	    editor.putBoolean("remember", Boolean.valueOf(dataLogin.get("check")));
	    editor.commit();
	}
	else { // borramos todo
	    sharedPref.edit().clear().commit();	    
	}
    }

    @Override
    protected void onPostExecute(Integer result) {
	Log.d(ConnectionAsyncTask.class.toString(), "onPostExecute: Finalizando asyncTask...");	
	
	if (true) { // TODO comprobaciones de resultado
	    this.startNextActivity();
	}    
	
	pDialog.dismiss();
	pDialog = null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
	Log.d(ConnectionAsyncTask.class.toString(), "onProgressUpdate: Actualizando progreso...");
    }

    
    private void startNextActivity() {
	// TODO esto es un mock, que vaya a la pantalla de lista
	Toast toast = Toast.makeText(this.loginActivity,
		"Conectado!", Toast.LENGTH_LONG);
	toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL, 0, 50);
	toast.show();
    }
}
