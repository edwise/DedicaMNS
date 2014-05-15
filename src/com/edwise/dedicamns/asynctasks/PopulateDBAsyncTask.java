/**
 * 
 */
package com.edwise.dedicamns.asynctasks;

import android.os.AsyncTask;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.edwise.dedicamns.MainMenuActivity;
import com.edwise.dedicamns.R;
import com.edwise.dedicamns.connections.ConnectionException;
import com.edwise.dedicamns.connections.ConnectionFacade;
import com.edwise.dedicamns.connections.WebConnection;

/**
 * @author edwise
 * 
 */
public class PopulateDBAsyncTask extends AsyncTask<Integer, Integer, Integer> {
	private static final String LOGTAG = PopulateDBAsyncTask.class.toString();

	private static final int OK = 1;
	private static final int ERROR = -2;

	@Override
	protected Integer doInBackground(Integer... params) {
		Log.d(LOGTAG, "doInBackground...");
		int result = OK;
		WebConnection webConnection = ConnectionFacade.getWebConnection();		

		try {
			AppData.getDatabaseHelper().deleteAllTablesData();
			webConnection.populateDBProjectsAndSubprojects();
		} catch (ConnectionException e) {
			Log.e(LOGTAG, "Error en la conexión al recuperar los proyectos y cargarlos en la BD", e);
			result = ERROR;
		}

		return result;
	}

	@Override
	protected void onPostExecute(Integer result) {
		Log.d(LOGTAG, "onPostExecute...");
		super.onPostExecute(result);

		if (result == OK) {
			((MainMenuActivity) AppData.getCurrentActivity()).closeDialog();
			showToastMessage(AppData.getCurrentActivity().getString(R.string.msgProjectsReloaded));
		} else {
			// Borramos todos los , por si se ha quedado a medias...
			AppData.getDatabaseHelper().deleteAllTablesData();
			((MainMenuActivity) AppData.getCurrentActivity()).closeDialog();
			showToastMessage(AppData.getCurrentActivity().getString(R.string.msgPopulateDBError));			
		}
	}

	private void showToastMessage(String message) {
		Toast toast = Toast.makeText(AppData.getCurrentActivity(), message, Toast.LENGTH_LONG);
		toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, 40);
		toast.show();
	}
}
