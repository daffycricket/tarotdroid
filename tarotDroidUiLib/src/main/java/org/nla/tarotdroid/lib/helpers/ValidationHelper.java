package org.nla.tarotdroid.lib.helpers;

import android.util.Patterns;
import android.widget.CheckBox;
import android.widget.EditText;

/**
 * Utility class to validate input
 */
public class ValidationHelper {

	/** String is empty until its length is less than 3. */
	private static final int NOT_EMPTY_MIN_LENGTH = 3;

	/**
	 * Check edt is checked.
	 * 
	 * @param edt
	 *            CheckBox to check.
	 * @param errorMessage
	 *            Error message to display
	 * @return TRUE if the input is valid.
	 */
	public static boolean validateCheckBoxChecked(CheckBox edt, String errorMessage) {

		// Check conditions
		boolean valid = edt.isChecked();

		// Display error message
		if (!valid) {
			edt.setError(errorMessage);
		} else {
			edt.setError(null);
		}

		// return result
		return valid;
	}

	/**
	 * Check edt value equals the parameter.
	 * 
	 * @param edt
	 *            EditText to check.
	 * @param value
	 *            The value to compare with
	 * @param errorMessage
	 *            Error message to display
	 * @return TRUE if the input is valid.
	 */
	public static boolean validateEditTextEquals(EditText edt, String value, String errorMessage) {

		// Check conditions
		boolean valid;
		if (value == null || value.length() == 0) {
			valid = edt.getText().toString().length() == 0;
		} else {
			valid = value.equals(edt.getText().toString());
		}

		// Display error message
		if (!valid) {
			edt.setError(errorMessage);
		}

		// return result
		return valid;
	}

	/**
	 * Check edt value equals the parameter.
	 * 
	 * @param edt
	 *            EditText to check.
	 * @param errorMessage
	 *            Error message to display
	 * @return TRUE if the input is valid.
	 */
	public static boolean validateEditTextIsEmail(EditText edt, String errorMessage) {

		// Check conditions
		boolean valid = Patterns.EMAIL_ADDRESS.matcher(edt.getText()).matches();
		;

		// Display error message
		if (!valid) {
			edt.setError(errorMessage);
		}

		// return result
		return valid;
	}

	/**
	 * Check edt is not empty.
	 * 
	 * @param edt
	 *            EditText to check.
	 * @param errorMessage
	 *            Error message to display
	 * @return TRUE if the input is valid.
	 */
	public static boolean validateEditTextNotEmpty(EditText edt, String errorMessage) {

		// Check conditions
		boolean valid = edt.getText().toString().length() > NOT_EMPTY_MIN_LENGTH;

		// Display error message
		if (!valid) {
			edt.setError(errorMessage);
		} else {
			edt.setError(null);
		}

		// return result
		return valid;
	}

	/**
	 * Check both edit texts have the same values.
	 * 
	 * @param edt1
	 *            EditText to check.
	 * @param edt2
	 *            EditText to check.
	 * 
	 * @param errorMessage
	 *            Error message to display
	 * @return TRUE if the input is valid.
	 */
	public static boolean validateEditTextsAreEqual(EditText edt1, EditText edt2, String errorMessage) {

		// Check conditions
		boolean valid = edt1.getText().toString().equals(edt2.getText().toString());

		// Display error message
		if (!valid) {
			edt1.setError(errorMessage);
			edt1.setError(errorMessage);
		}

		// return result
		return valid;
	}
}
