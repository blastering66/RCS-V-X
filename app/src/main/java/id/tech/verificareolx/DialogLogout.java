package id.tech.verificareolx;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.Toast;
import id.tech.util.Parameter_Collections;

public class DialogLogout extends DialogFragment{
	private SharedPreferences sh;
	Context context;

	public void setSh(SharedPreferences sh) {
		this.sh = sh;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setMessage("Yakin Keluar ?");
		builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				sh.edit().clear().commit();
				Toast.makeText(getActivity(), "Logged Out", Toast.LENGTH_LONG).show();
//				Intent load = new Intent(getActivity(), Olx_Login_Activity.class);
//				getActivity().startActivity(load);
				getActivity().finish();	
			}
		});
		builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
			}
		});
		
		return builder.create();
	}

}
