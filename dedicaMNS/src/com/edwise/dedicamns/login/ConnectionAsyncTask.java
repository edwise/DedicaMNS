/**
 * 
 */
package com.edwise.dedicamns.login;

import java.util.Map;

import android.os.AsyncTask;
import android.util.Log;

/**
 * @author edwise
 * 
 */
public class ConnectionAsyncTask extends AsyncTask<Map<String, String>, Integer, Integer> {

    @Override
    protected Integer doInBackground(Map<String, String>... data) {
	Log.d(ConnectionAsyncTask.class.toString(), "doInBackground: Comenzando asyncTask...");
	
	return 1; // TODO enum con tipos de retorno, errores, etc
    }

    @Override
    protected void onPostExecute(Integer result) {
	Log.d(ConnectionAsyncTask.class.toString(), "onPostExecute: Finalizando asyncTask...");	
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
	Log.d(ConnectionAsyncTask.class.toString(), "onProgressUpdate: Actualizando progreso...");
    }

}
