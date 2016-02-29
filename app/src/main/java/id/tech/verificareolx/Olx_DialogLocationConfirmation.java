package id.tech.verificareolx;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class Olx_DialogLocationConfirmation extends DialogFragment{
	private SharedPreferences sh;
	Context context;
	String text;
	int from;


	public void setSh(SharedPreferences sh) {
		this.sh = sh;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public void setText(String text) {
		this.text = text;
	}

	public void setFrom(int from) {
		this.from = from;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.MyLocationDialogTheme);
		builder.setMessage(text);
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				if(from == 0){
					Intent load = new Intent(getActivity(), Olx_MenuUtama_WP.class);
					getActivity().startActivity(load);
					getActivity().finish();
					
					Intent notif_service = new Intent(getActivity(), Notif_Service.class);
					getActivity().startService(notif_service);
				}else if(from == 1){
					Intent load = new Intent(getActivity(), Olx_Login_Activity.class);
					getActivity().startActivity(load);
					
					getActivity().finish();
				}else if(from == 9){
					getActivity().finish();
				}else{
					dismiss();
				}
				
				
				
					
			}
		});
		
		
		
		return builder.create();
	}

}