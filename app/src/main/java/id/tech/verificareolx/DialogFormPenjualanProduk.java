package id.tech.verificareolx;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;
import id.tech.verificareolx.R;
import id.tech.util.Parameter_Collections;
import id.tech.util.ServiceHandlerJSON;

public class DialogFormPenjualanProduk extends FragmentActivity {
	Button btn, btn_submit;
	TextView tv_label, tv_keterangan_error;
	EditText ed_Sn, ed_Imei1, ed_Imei2, ed_Esn, ed_NamaProduk, ed_WarnaProduk,
			ed_Keterangan;
	TableLayout table_Form;
	boolean ada_di_db;
	SharedPreferences spf;
	View wrapper_bottom;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.layout_dialog_penjualan_produk);

		spf = getSharedPreferences(Parameter_Collections.SH_NAME,
				Context.MODE_PRIVATE);

		getALlView();

		ada_di_db = getIntent().getBooleanExtra("ada_di_db", false);

		tv_label = (TextView) findViewById(R.id.txt_label);
		tv_keterangan_error = (TextView) findViewById(R.id.keterangan_error);

		if (ada_di_db) {
			tv_label.setText("Produk tidak ada di database !");
			tv_keterangan_error.setVisibility(View.VISIBLE);
		} else {
			tv_label.setText("Produk tidak ada di dalam stok toko !");
			tv_keterangan_error.setVisibility(View.GONE);
		}

		btn = (Button) findViewById(R.id.btn);
		table_Form = (TableLayout) findViewById(R.id.form);

		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				table_Form.setVisibility(View.VISIBLE);
				tv_label.setVisibility(View.GONE);
				btn.setVisibility(View.GONE);
				wrapper_bottom.setVisibility(View.VISIBLE);
			}
		});

		btn_submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!ed_Sn.getText().toString().equals("")
						&& !ed_Imei1.getText().toString().equals("")
						&& !ed_NamaProduk.getText().toString().equals("")
						&& !ed_WarnaProduk.getText().toString().equals("")
						&& !ed_Keterangan.getText().toString().equals("")) {
					new Async_SubmitPenjualanProduk().execute();
				} else {
					Toast.makeText(getApplicationContext(),
							"Please fill required field", Toast.LENGTH_SHORT)
							.show();
				}
			}
		});
	}

	private class Async_SubmitPenjualanProduk extends AsyncTask<Void, Void, Void> {
		private String cSn, cImei1, cImei2, cEsn, cNamaProduk, cWarnaProduk,
				cKeterangan;
		private String cKodeToko, cIdPegawai, cLongitude, cLatitude;
		private String row_count;
		DialogFragmentProgress dialogProgress;
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			dialogProgress = new DialogFragmentProgress();
			dialogProgress.show(getSupportFragmentManager(), "");

			cSn = ed_Sn.getText().toString();
			cImei1 = ed_Imei1.getText().toString();
			cImei2 = ed_Imei2.getText().toString();
			cEsn = ed_Esn.getText().toString();
			cNamaProduk = ed_NamaProduk.getText().toString();
			cWarnaProduk = ed_WarnaProduk.getText().toString();
			cKeterangan = ed_Keterangan.getText().toString();

			cKodeToko = spf.getString(Parameter_Collections.SH_KODE_TOKO, "");
			cIdPegawai = spf.getString(Parameter_Collections.SH_ID_PEGAWAI, "");
//			cLongitude = "106.8151608";
//			cLatitude = "-6.3025544";
			cLongitude = spf.getString(Parameter_Collections.TAG_LONGITUDE_NOW, "106.8151608");
			cLatitude = spf.getString(Parameter_Collections.TAG_LATITUDE_NOW, "-6.3025544");
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			ServiceHandlerJSON sh = new ServiceHandlerJSON();
			JSONObject jObj = sh.json_input_produk(cSn, cImei1, cImei2, cEsn,
					cNamaProduk, cWarnaProduk, cKeterangan, cKodeToko,
					cIdPegawai, cLongitude, cLatitude);

			try {
				row_count = jObj.getString(Parameter_Collections.TAG_ROWCOUNT);

			} catch (JSONException e) {

			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			dialogProgress.dismiss();
			if (row_count.equals("1")) {				
				DialogLocationConfirmation dialog = new DialogLocationConfirmation();
				dialog.setContext(getApplicationContext());
				dialog.setText("Input Success");
				dialog.setFrom(9);
				dialog.setCancelable(false);
				dialog.show(getSupportFragmentManager(), "");
				
//				Toast.makeText(getApplicationContext(), "Input Success",
//						Toast.LENGTH_LONG).show();
//				finish();
			} else {
				DialogLocationConfirmation dialog = new DialogLocationConfirmation();
				dialog.setContext(getApplicationContext());
				dialog.setText("Somethine wrong please contact administator");
				dialog.setFrom(9);
				dialog.setCancelable(false);
				dialog.show(getSupportFragmentManager(), "");
				
//				Toast.makeText(getApplicationContext(),
//						"Somethine wrong please contact administator",
//						Toast.LENGTH_LONG).show();
//				finish();
			}

		}
	}

	private void getALlView() {
		ed_Sn = (EditText) findViewById(R.id.ed_form_sn);
		ed_Imei1 = (EditText) findViewById(R.id.ed_form_imei);
		ed_Imei2 = (EditText) findViewById(R.id.ed_form_imei2);
		ed_Esn = (EditText) findViewById(R.id.ed_form_esn);
		ed_NamaProduk = (EditText) findViewById(R.id.ed_form_namaproduk);
		ed_WarnaProduk = (EditText) findViewById(R.id.ed_form_warnaproduk);
		ed_Keterangan = (EditText) findViewById(R.id.ed_form_ket);
		btn_submit = (Button) findViewById(R.id.btn_submit);
		
		wrapper_bottom = (View) findViewById(R.id.wrapper_bottom);
	}

}
