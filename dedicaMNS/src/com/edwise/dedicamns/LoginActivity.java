package com.edwise.dedicamns;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;

import com.edwise.dedicamns.login.ConnectionAsyncTask;

public class LoginActivity extends Activity {

    private ProgressDialog pDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.login);

	Log.d(LoginActivity.class.toString(), "onCreate: Comenzando...");

	// TODO obtener datos anteriores (preferences?)
	
	// TODO obtener properties para tema mock
//	Resources resources = this.getResources();
//	AssetManager assetManager = resources.getAssets();
//
//	// Read from the /assets directory
//	try {
//	    InputStream inputStream = assetManager.open("app.properties");
//	    Properties properties = new Properties();
//	    properties.load(inputStream);
//	    System.out.println("The properties are now loaded");
//	    System.out.println("properties: " + properties);
//	} catch (IOException e) {
//	    System.err.println("Failed to open microlog property file");
//	    e.printStackTrace();
//	}
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	getMenuInflater().inflate(R.menu.login, menu);
	return true;
    }

    public void doLogin(View view) {
	Log.d(LoginActivity.class.toString(), "doLogin: Click en conectar...");
	HashMap<String, String> accessData = new HashMap<String, String>();

	EditText userLoginEditText = (EditText) findViewById(R.id.userMNText);
	String userLogin = userLoginEditText.getText().toString();
	accessData.put("user", userLogin); // TODO constantes o enum

	EditText passLoginEditText = (EditText) findViewById(R.id.passMNText);
	String passLogin = passLoginEditText.getText().toString();
	accessData.put("pass", passLogin); // TODO constantes o enum

	CheckBox rememberMeCheckBox = (CheckBox) findViewById(R.id.rememberUserCheck);
	String checked = Boolean.toString(rememberMeCheckBox.isChecked());
	accessData.put("check", checked); // TODO constantes o enum

	Log.d(LoginActivity.class.toString(),
		"doLogin: User y pass insertado: " + userLogin + " / "
			+ passLogin + " / " + checked);

	
	// Ocultamos teclado
	InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
	imm.hideSoftInputFromWindow(passLoginEditText.getWindowToken(), 0);

	// TODO guardar datos si el check está puesto, si no, borrarlos si
	// existen (en el task?)
	
	pDialog = ProgressDialog.show(LoginActivity.this, "Vamoooh!",
		"Cargando, please, epeate...", true);

	callConnectionAsyncTask(accessData);
    }

    private void callConnectionAsyncTask(Map<String, String> accessData) {
	Log.d(LoginActivity.class.toString(),
		"callConnectionAsyncTask: Llamada al asyncTask...");

	// TODO ver como hacer este new en un factory, para meter un mock...
	AsyncTask<Map<String, String>, Integer, Integer> connectionAsyncTask = new ConnectionAsyncTask(
		this, this.pDialog);
	connectionAsyncTask.execute(accessData);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	super.onActivityResult(requestCode, resultCode, data);
	
	Log.d(LoginActivity.class.toString(), "onActivityResult: en el onActivity...");
    }

    
}
