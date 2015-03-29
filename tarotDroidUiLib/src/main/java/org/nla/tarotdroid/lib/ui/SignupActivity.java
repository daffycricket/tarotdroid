package org.nla.tarotdroid.lib.ui;

import org.nla.tarotdroid.lib.ui.tasks.IAsyncCallback;
import org.nla.tarotdroid.lib.ui.tasks.SignupTask;
import org.nla.tarotdroid.lib.R;
import org.nla.tarotdroid.lib.app.AppContext;
import org.nla.tarotdroid.lib.helpers.ValidationHelper;
import org.nla.tarotdroid.lib.model.TarotDroidUser;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Handles signup process.
 */
public class SignupActivity extends Activity {

	private CheckBox chkAcceptCG;

	private EditText edtEmail;

	private EditText edtPassword;

	private EditText edtPasswordConfirm;

	/**
	 * Signup callback.
	 */
	private final IAsyncCallback<TarotDroidUser> onPostSignupCallback = new IAsyncCallback<TarotDroidUser>() {

		/**
		 * @param result
		 * @param exception
		 */
		@Override
		public void execute(TarotDroidUser result, Exception exception) {
			onPostSignup(result, exception);
		}
	};

	private ProgressDialog progressDialog;

	private TarotDroidUser signupTarotDroidUser;

	public void onClickOnBtnLater(View view) {
		finish();
	}

	public void onClickOnBtnSignup(View view) {
		boolean isFormvalid = ValidationHelper.validateCheckBoxChecked(chkAcceptCG, "You must accept the terms of use");
		isFormvalid &= ValidationHelper.validateEditTextIsEmail(edtEmail, "You must type in a proper email");
		isFormvalid &= ValidationHelper.validateEditTextNotEmpty(edtPassword, "Password must not be empty");
		isFormvalid &= ValidationHelper.validateEditTextNotEmpty(edtPasswordConfirm, "Password confirmation must not be empty");
		isFormvalid &= ValidationHelper.validateEditTextsAreEqual(edtPassword, edtPasswordConfirm, "Password and confirmation must be the same");

		if (isFormvalid) {
			SignupTask signupTask = new SignupTask(edtEmail.getText().toString(), edtPassword.getText().toString());
			signupTask.setCallback(onPostSignupCallback);
			signupTask.execute();

			progressDialog = ProgressDialog.show(this, "Signing up...", "Signing up...", false);
			progressDialog.setCancelable(false);
		}
	}

	public void onClickOnBtnSignupWithFacebook(View view) {

	}

	public void onClickOnBtnSignupWithTwitter(View view) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_signup);

		edtEmail = (EditText) findViewById(R.id.edtEmail);
		edtPassword = (EditText) findViewById(R.id.edtPassword);
		edtPasswordConfirm = (EditText) findViewById(R.id.edtPasswordConfirm);
		chkAcceptCG = (CheckBox) findViewById(R.id.chkAcceptCG);
	}

	/**
	 * @param result
	 * @param exception
	 */
	private void onPostSignup(TarotDroidUser result, Exception exception) {
		progressDialog.dismiss();
		if (exception != null) {
			Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
		} else {
			Toast.makeText(getApplicationContext(), "Connected as " + result.toString(), Toast.LENGTH_LONG).show();
			AppContext.getApplication().setTarotDroidUser(signupTarotDroidUser);
			Intent intent = new Intent(this, MyAccountActivity.class);
			startActivity(intent);
			finish();
		}
	}
}
