package id.tech.verificareolx;

import java.util.Calendar;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DatePickerDialog.OnDateSetListener;

public class DialogDatePicker extends DialogFragment {
	OnDateSetListener listener;

	public DialogDatePicker(){

	}

	public void setDateListener(OnDateSetListener listener){
		this.listener = listener;
	}
	

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		final Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);

		// Create a new instance of DatePickerDialog and return it
		return new DatePickerDialog(getActivity(), listener, year, month, day);
	}

}
