/**
 * 
 */
package com.edwise.dedicamns.asynctasks;

import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.edwise.dedicamns.LoginActivity;
import com.edwise.dedicamns.MainMenuActivity;
import com.edwise.dedicamns.R;
import com.edwise.dedicamns.connections.ConnectionException;
import com.edwise.dedicamns.connections.ConnectionFacade;
import com.edwise.dedicamns.utils.ErrorUtils;

/**
 * @author edwise
 * 
 */
public class ConnectionAsyncTask extends AsyncTask<Map<String, String>, Integer, Integer> implements
	LoginConstants {
    private static final String LOGTAG = ConnectionAsyncTask.class.toString();

    @Override
    protected Integer doInBackground(Map<String, String>... data) {
	Log.d(LOGTAG, "doInBackground: Comenzando asyncTask...");
	Map<String, String> dataLogin = data[0];
	saveSharedPreferences(dataLogin);

	int result = -1; // Si no hay conexión a internet, devolveremos -1
	if (ConnectionFacade.getWebConnection().isOnline(AppData.getCurrentActivity())) {
	    try {
		result = ConnectionFacade.getWebConnection().connectWeb(dataLogin.get(USER_TAG),
			dataLogin.get(PASS_TAG));
	    } catch (ConnectionException e) {
		Log.e(LOGTAG, "Error en la conexión al intentar logarse", e);
		result = -2;
	    }
	}

	return result;
    }

    private void saveSharedPreferences(Map<String, String> dataLogin) {
	SharedPreferences sharedPref = AppData.getCurrentActivity().getPreferences(Context.MODE_PRIVATE);
	if (dataLogin.get(CHECK_TAG).equals(LoginConstants.TRUE)) { // Guardamos todo
	    SharedPreferences.Editor editor = sharedPref.edit();
	    editor.putString(USER_TAG, dataLogin.get(USER_TAG));
	    editor.putString(PASS_TAG, dataLogin.get(PASS_TAG));
	    editor.putBoolean(REMEMBER_TAG, Boolean.valueOf(dataLogin.get(CHECK_TAG)));
	    editor.commit();
	} else { // borramos todo
	    sharedPref.edit().clear().commit();
	}
    }

    @Override
    protected void onPostExecute(Integer result) {
	Log.d(LOGTAG, "onPostExecute: Finalizando asyncTask...");

	if (ConnectionFacade.CONNECTION_OK.equals(result)) {
	    this.startNextActivity();
	} else {
	    showToastErrorConnection(ErrorUtils.getMessageError(result));
	}

	closeDialog();
    }

    private void closeDialog() {
	LoginActivity activity = (LoginActivity) AppData.getCurrentActivity();
	activity.closeDialog();	
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
	Log.d(LOGTAG, "onProgressUpdate: Actualizando progreso...");
    }

    private void startNextActivity() {
	Intent intent = new Intent(AppData.getCurrentActivity(), MainMenuActivity.class);

	AppData.getCurrentActivity().startActivity(intent);
	AppData.getCurrentActivity().finish();

	showToastConnected();

    }

    private void showToastConnected() {
	Toast toast = Toast.makeText(AppData.getCurrentActivity(),
		AppData.getCurrentActivity().getString(R.string.msgConnected), Toast.LENGTH_LONG);
	toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, 20);
	toast.show();
    }

    private void showToastErrorConnection(String msg) {
	Toast toast = Toast.makeText(AppData.getCurrentActivity(), msg, Toast.LENGTH_LONG);
	toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, 40);
	toast.show();
    }

}
