package com.edwise.dedicamns;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

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

	EditText userLoginEditText = (EditText) findViewById(R.id.userMNText);
	String userLogin = userLoginEditText.getText().toString();

	EditText passLoginEditText = (EditText) findViewById(R.id.passMNText);
	String passLogin = passLoginEditText.getText().toString();
	
	CheckBox rememberMeCheckBox = (CheckBox) findViewById(R.id.rememberUserCheck);
	String checked = Boolean.toString(rememberMeCheckBox.isChecked());

	Log.d(LoginActivity.class.toString(),
		"doLogin: User y pass insertado: " + userLogin + " / "
			+ passLogin + " / " + checked);
    }

}
