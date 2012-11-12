package com.edwise.dedicamns;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.edwise.dedicamns.asynctasks.ConnectionAsyncTask;
import com.edwise.dedicamns.menu.MenuUtils;

public class LoginActivity extends Activity {

    private ProgressDialog pDialog;

    private EditText userLoginEditText;
    private EditText passLoginEditText;
    private CheckBox rememberMeCheckBox;

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.login);
	Log.d(LoginActivity.class.toString(), "onCreate: Comenzando...");

	initFields();
	if (!checkIfLogout()) {
	    chargeSavedData();
	}
    }

    private void initFields() {
	userLoginEditText = (EditText) findViewById(R.id.userMNText);
	passLoginEditText = (EditText) findViewById(R.id.passMNText);
	rememberMeCheckBox = (CheckBox) findViewById(R.id.rememberUserCheck);
    }

    private void chargeSavedData() {
	SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
	String user = sharedPref.getString("user", null);
	String pass = sharedPref.getString("pass", null);
	Boolean remember = sharedPref.getBoolean("remember", false);

	if (user != null) {
	    userLoginEditText.setText(user);
	}
	if (pass != null) {
	    passLoginEditText.setText(pass);
	}
	rememberMeCheckBox.setChecked(remember);
    }

    private boolean checkIfLogout() {
	return getIntent().getBooleanExtra("isLogout", false);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	getMenuInflater().inflate(R.menu.login_menu, menu);
	return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
	boolean returned = false;
	switch (item.getItemId()) {
	case R.id.menu_about_us:
	    // TODO llamada a acerca de, en clase generica para todos.
	    returned = true;
	default:
	    returned = super.onOptionsItemSelected(item);
	}

	return returned;
    }

    public void doLogin(View view) {
	Log.d(LoginActivity.class.toString(), "doLogin: Click en conectar...");

	if (checkFieldsFilled()) {
	    accesWebWithLoginData();
	} else {
	    Toast toast = Toast.makeText(this, "Introduce tu usuario y contraseña de Medianet",
		    Toast.LENGTH_LONG);
	    toast.setGravity(Gravity.CENTER, 0, 100);
	    toast.show();

	}
    }

    private void accesWebWithLoginData() {
	HashMap<String, String> accessData = new HashMap<String, String>();
	String userLogin = userLoginEditText.getText().toString();
	accessData.put("user", userLogin); // TODO constantes o enum

	String passLogin = passLoginEditText.getText().toString();
	accessData.put("pass", passLogin); // TODO constantes o enum

	String checked = Boolean.toString(rememberMeCheckBox.isChecked());
	accessData.put("check", checked); // TODO constantes o enum

	Log.d(LoginActivity.class.toString(), "doLogin: User y pass insertado: " + userLogin + " / "
		+ passLogin + " / " + checked);

	hideKeyboard();

	pDialog = ProgressDialog.show(LoginActivity.this, "Conectando...",
		"Conexión en progreso, por favor, espera", true);

	callConnectionAsyncTask(accessData);
    }

    private void hideKeyboard() {
	InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
	imm.hideSoftInputFromWindow(passLoginEditText.getWindowToken(), 0);
    }

    @SuppressWarnings("unchecked")
    private void callConnectionAsyncTask(Map<String, String> accessData) {
	Log.d(LoginActivity.class.toString(), "callConnectionAsyncTask: Llamada al asyncTask...");

	// TODO ver como hacer este new en un factory, para meter un mock...
	AsyncTask<Map<String, String>, Integer, Integer> connectionAsyncTask = new ConnectionAsyncTask(this,
		this.pDialog);
	connectionAsyncTask.execute(accessData);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	super.onActivityResult(requestCode, resultCode, data);
	Log.d(LoginActivity.class.toString(), "onActivityResult: en el onActivityResult...");
    }

    @Override
    protected void onDestroy() {
	super.onStop();
	Log.d(LoginActivity.class.toString(), "onDestroy: en el onDestroy...");
	// Borrar preferences si el check está desactivado
	removeLoginDataIfNeeded();
    }

    @Override
    public void onBackPressed() {
	moveTaskToBack(true);
	super.onBackPressed();
    }

    private void removeLoginDataIfNeeded() {
	if (!rememberMeCheckBox.isChecked()) {
	    SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
	    sharedPref.edit().clear().commit();
	    Log.d(LoginActivity.class.toString(), "removeLoginDataIfNeeded: Borrado el sharedPreferences!");
	}
    }

    private boolean checkFieldsFilled() {
	boolean filled = true;
	String userLogin = userLoginEditText.getText().toString();
	String passLogin = passLoginEditText.getText().toString();

	if (StringUtils.isBlank(userLogin) || StringUtils.isBlank(passLogin)) {
	    filled = false;
	}

	return filled;
    }
}
