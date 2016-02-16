package id.tech.verificareolx;

import id.tech.verificareolx.R;
import id.tech.util.Parameter_Collections;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class DialogPindahToko extends FragmentActivity{
	Button btn_ya, btn_tidak;
	SharedPreferences sh;
	boolean extra_dari_inputstok;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.layout_dialog_pindahtoko);
		sh = getSharedPreferences(Parameter_Collections.SH_NAME,
				Context.MODE_PRIVATE);

		extra_dari_inputstok = getIntent().getBooleanExtra(Parameter_Collections.EXTRA_PINDAHTOKO_INPUTSTOK, false);
		
		btn_ya = (Button) findViewById(R.id.btn_ya);
		btn_tidak = (Button) findViewById(R.id.btn_tidak);

		btn_tidak.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(extra_dari_inputstok){
					Intent load = new Intent(v.getContext(), DialogChooserInputStok.class);
					sh.edit().putBoolean(Parameter_Collections.SH_PINDAH_TOKO, false).commit();
					startActivity(load);
					finish();
				}else{
					Intent load = new Intent(v.getContext(), DialogChooserInputPennjualan.class);
					sh.edit().putBoolean(Parameter_Collections.SH_PINDAH_TOKO, false).commit();
					startActivity(load);
					finish();
				}
				
			}
		});

		btn_ya.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent load = new Intent(getApplicationContext(),
						ScanPindahToko_Activity.class);					
				sh.edit().putBoolean(Parameter_Collections.SH_PINDAH_TOKO, true).commit();
				load.putExtra(Parameter_Collections.EXTRA_PINDAHTOKO_INPUTSTOK, extra_dari_inputstok);
				startActivity(load);
				finish();				
				
			}
		});
	}

}
