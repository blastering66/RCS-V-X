package id.tech.verificareolx;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import id.tech.verificareolx.R;
import id.tech.util.Parameter_Collections;

public class DialogChooserInputPennjualan extends FragmentActivity{
	Button btn_manual, btn_scan;
	SharedPreferences sh;
	
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.layout_dialog_chooser_inputpenjualan);
		sh = getSharedPreferences(Parameter_Collections.SH_NAME,
				Context.MODE_PRIVATE);
		
		btn_manual = (Button) findViewById(R.id.btn_input_manual);
		btn_scan = (Button) findViewById(R.id.btn_scan_barcode);
		
		btn_scan.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				Intent load = new Intent(v.getContext(),
//						DialogHargaProduk.class);
//				startActivity(load);
				
				Intent load = new Intent(getApplicationContext(), InputPenjualanScan_Activity.class);				
				startActivity(load);
				
			}
		});
		
		btn_manual.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent load = new Intent(getApplicationContext(), InputPenjualanManual_Activity.class);				
				startActivity(load);
			}
		});
	}
}