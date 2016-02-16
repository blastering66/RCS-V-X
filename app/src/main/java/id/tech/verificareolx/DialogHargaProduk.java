package id.tech.verificareolx;

import id.tech.verificareolx.R;
import id.tech.util.Parameter_Collections;
import android.app.Activity;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

public class DialogHargaProduk extends FragmentActivity{
	private EditText ed, ed_tgl, ed_remarks;
	private Button btn, btn_tgl;
	private String cHargaProduk;
	private String cTglMulai;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_dialog_hargaproduk);
		
		ed = (EditText)findViewById(R.id.ed_harga);
		ed_tgl = (EditText)findViewById(R.id.ed_tgl);
		ed_remarks = (EditText)findViewById(R.id.ed_remarks);
		
		btn = (Button)findViewById(R.id.btn);
		btn_tgl = (Button)findViewById(R.id.btn_tgl_penjualan);
		
		final OnDateSetListener listenerDate_mulai = new OnDateSetListener() {

			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {
				// TODO Auto-generated method stub
				cTglMulai =String.valueOf(year) + "-"
						+ String.valueOf(monthOfYear+1) + "-"
						+ String.valueOf(dayOfMonth);
				ed_tgl.setText(cTglMulai);
				ed_tgl.setEnabled(false);
				ed_tgl.setVisibility(View.VISIBLE);
				btn_tgl.setVisibility(View.GONE);
			}
		};
		
		btn_tgl.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				DialogDatePicker fragment = new DialogDatePicker();
				fragment.setDateListener(listenerDate_mulai);
				FragmentManager fm = getSupportFragmentManager();
				fm.beginTransaction().add(fragment, "").commit();
			}
		});
		
		btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!ed.getText().toString().equals("") && !ed_tgl.getText().toString().equals("")){
					Intent load = new Intent(getApplicationContext(), ScanPenjualan_Activity.class);
					load.putExtra(Parameter_Collections.TAG_HARGA_PRODUK_KELUAR, ed.getText().toString());
					load.putExtra(Parameter_Collections.EXTRA_REMARKS, ed_remarks.getText().toString());
					load.putExtra(Parameter_Collections.EXTRA_TGL_TRANSAKSI, cTglMulai);
					startActivity(load);
				}
				
				finish();
			}
		});
	}

}
