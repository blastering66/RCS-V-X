package id.tech.verificareolx;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class Olx_DialogFragmentProgress extends DialogFragment{
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		ProgressDialog dialog = new ProgressDialog(getActivity());
		dialog.setMessage("Processing...");
		dialog.setCancelable(false);
		return dialog;
	}

}
