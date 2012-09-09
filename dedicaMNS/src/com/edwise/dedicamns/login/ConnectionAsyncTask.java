/**
 * 
 */
package com.edwise.dedicamns.login;

import java.util.Map;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
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
	try {
	    for (int i = 0; i < 3; i++) {
		Thread.sleep(3000);
		publishProgress(i+1);
	    }
	} catch (InterruptedException e) {
	    Log.e(ConnectionAsyncTask.class.toString(), "doInBackground: Error en Thread.sleep()...", e);
	    e.printStackTrace();
	}
	
	return 1; // TODO enum con tipos de retorno, errores, etc
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
	// TODO al mock!
	Toast.makeText(this.loginActivity,
		"Conectado!", Toast.LENGTH_LONG).show();
    }
}
