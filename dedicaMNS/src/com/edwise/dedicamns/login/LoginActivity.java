package com.edwise.dedicamns.login;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.edwise.dedicamns.R;

public class LoginActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.login);

	Log.d(LoginActivity.class.toString(), "onCreate: Comenzando...");

	// TODO obtener datos anteriores (preferences?)
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
	
	// TODO guardar datos si el check está puesto, si no, borrarlos si existen
	
	// Habrá que ponerlo como atributo de la clase, y que el asynctask acceda a ella
	ProgressDialog dialog = ProgressDialog.show(LoginActivity.this,
		"Vamoooh!", "Cargando, please, epeate...", true);
	
	// TODO ver como hacer este new en un factory, para meter un mock...
	new ConnectionAsyncTask().execute(accessData);
    }

}
