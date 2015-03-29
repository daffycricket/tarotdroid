package org.nla.tarotdroid.lib.ui;

import org.nla.tarotdroid.lib.app.AppContext;
import org.nla.tarotdroid.lib.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/**
 * Handles signup process.
 */
public class MyAccountActivity extends Activity {

	private ProgressDialog progressDialog;

	private TextView txtEmail;

	public void onClickOnBtnConnectWithFacebook(View view) {
	}

	public void onClickOnBtnConnectWithTwitter(View view) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_account);

		// no account, finish and start signin process
		if (AppContext.getApplication().getTarotDroidUser() == null) {
			Intent intent = new Intent(this, SigninActivity.class);
			startActivity(intent);
			finish();
		}

		txtEmail = (TextView) findViewById(R.id.txtEmail);
		txtEmail.setText(AppContext.getApplication().getTarotDroidUser().getEmail());

	}
}
