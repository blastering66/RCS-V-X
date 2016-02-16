package id.tech.verificareolx;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import id.tech.verificareolx.R;
import id.tech.util.Parameter_Collections;
import id.tech.util.ServiceHandlerJSON;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

public class DialogFormInputProduk extends FragmentActivity {
	Button btn, btn_submit;
	TextView tv_label, tv_keterangan_error;
	EditText ed_Sn, ed_Imei1, ed_Imei2, ed_Esn;
	// EditText ed_NamaProduk, ed_WarnaProduk,ed_Keterangan;
	TableLayout table_Form;
	boolean ada_di_db;
	SharedPreferences spf;
	View wrapper_bottom;
	AutoCompleteTextView auto_namaProduk, auto_warnaProduk,
			auto_keteranganProduk;
	ArrayAdapter<String> adapter_nama, adapter_warna, adapter_ket;
	Spinner spinner_TipeProduk;
	String cNamaProduk;
	ArrayList<String> data_tipe;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.layout_dialog_input_produk);

		spf = getSharedPreferences(Parameter_Collections.SH_NAME,
				Context.MODE_PRIVATE);
		ada_di_db = getIntent().getBooleanExtra("ada_di_db", false);

		getALlView();

	}

	private class Async_SubmitProduk extends AsyncTask<Void, Void, Void> {
		private String cSn, cImei1, cImei2, cEsn, cWarnaProduk,
				cKeterangan;
		private String cKodeToko, cIdPegawai, cLongitude, cLatitude, nama_produk;
		private String row_count, json_code, error_message;
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

			// cNamaProduk = auto_namaProduk.getText().toString();
			nama_produk = spinner_TipeProduk.getSelectedItem().toString();

			cWarnaProduk = auto_warnaProduk.getText().toString();
			cKeterangan = auto_keteranganProduk.getText().toString();

			if(spf.getBoolean(Parameter_Collections.SH_PINDAH_TOKO, false)){
				cKodeToko = spf.getString(Parameter_Collections.SH_KODE_TOKO_SEMENTARA, "");
			}else{
				cKodeToko = spf.getString(Parameter_Collections.SH_KODE_TOKO, "");
			}
			
			cIdPegawai = spf.getString(Parameter_Collections.SH_ID_PEGAWAI, "");
			// cLongitude = "106.8151608";
			// cLatitude = "-6.3025544";
			cLongitude = spf.getString(Parameter_Collections.TAG_LONGITUDE_NOW,
					"106.8151608");
			cLatitude = spf.getString(Parameter_Collections.TAG_LATITUDE_NOW,
					"-6.3025544");

			
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			ServiceHandlerJSON sh = new ServiceHandlerJSON();
			JSONObject jObj = sh.json_input_produk(cSn, cImei1, cImei2, cEsn,
					nama_produk, cWarnaProduk, cKeterangan, cKodeToko,
					cIdPegawai, cLongitude, cLatitude);

			try {

				json_code = jObj.getString(Parameter_Collections.TAG_JSON_CODE);

				if (json_code.equals("1")) {
					row_count = jObj
							.getString(Parameter_Collections.TAG_ROWCOUNT);
				} else {
					error_message = jObj
							.getString(Parameter_Collections.TAG_JSON_ERROR_MESSAGE);
				}

			} catch (JSONException e) {

			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			dialogProgress.dismiss();
			if (json_code.equals("1")) {
				if (row_count.equals("1")) {
					DialogLocationConfirmation dialog = new DialogLocationConfirmation();
					dialog.setContext(getApplicationContext());
					dialog.setText("Input Success");
					dialog.setFrom(9);
					dialog.setCancelable(false);
					dialog.show(getSupportFragmentManager(), "");

					// Toast.makeText(getApplicationContext(), "Input Success",
					// Toast.LENGTH_LONG).show();
					// finish();
				} else {
					DialogLocationConfirmation dialog = new DialogLocationConfirmation();
					dialog.setContext(getApplicationContext());
					dialog.setText("Somethine wrong please contact administator");
					dialog.setFrom(9);
					dialog.setCancelable(false);
					dialog.show(getSupportFragmentManager(), "");

					// Toast.makeText(getApplicationContext(),
					// "Somethine wrong please contact administator",
					// Toast.LENGTH_LONG).show();
					// finish();
				}
			} else {
				DialogLocationConfirmation dialog = new DialogLocationConfirmation();
				dialog.setContext(getApplicationContext());
				dialog.setText(error_message);
				dialog.setFrom(9);
				dialog.setCancelable(false);
				dialog.show(getSupportFragmentManager(), "");
			}
		}
	}

	private void getALlView() {
		ed_Sn = (EditText) findViewById(R.id.ed_form_sn);
		ed_Imei1 = (EditText) findViewById(R.id.ed_form_imei);
		ed_Imei2 = (EditText) findViewById(R.id.ed_form_imei2);
		ed_Esn = (EditText) findViewById(R.id.ed_form_esn);

		// load imeinya langsung
		ed_Imei1.setText(getIntent().getStringExtra("imei"));

		// ed_NamaProduk = (EditText) findViewById(R.id.ed_form_namaproduk);
		// ed_WarnaProduk = (EditText) findViewById(R.id.ed_form_warnaproduk);
		// ed_Keterangan = (EditText) findViewById(R.id.ed_form_ket);
		btn_submit = (Button) findViewById(R.id.btn_submit);

		wrapper_bottom = (View) findViewById(R.id.wrapper_bottom);

		// auto_namaProduk =
		// (AutoCompleteTextView)findViewById(R.id.autocomplete_nama_produk);
		spinner_TipeProduk = (Spinner) findViewById(R.id.autocomplete_nama_produk);

		auto_warnaProduk = (AutoCompleteTextView) findViewById(R.id.autocomplete_warna_produk);
		auto_keteranganProduk = (AutoCompleteTextView) findViewById(R.id.autocomplete_ket_produk);

		adapter_warna = new ArrayAdapter<String>(getApplicationContext(),
				R.layout.spinner_item, getResources().getStringArray(
						R.array.warna_produk));
		auto_warnaProduk.setAdapter(adapter_warna);

		adapter_ket = new ArrayAdapter<String>(getApplicationContext(),
				R.layout.spinner_item, getResources().getStringArray(
						R.array.ket_produk));
		auto_keteranganProduk.setAdapter(adapter_ket);

		new Async_Get_TipeProduk().execute();

	}

	private class Async_Get_TipeProduk extends AsyncTask<Void, Void, Void> {

		DialogFragmentProgress pDialog;
		String cCode;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			data_tipe = new ArrayList<String>();

			// pDialog = new DialogFragmentProgress();
			// pDialog.show(getSupportFragmentManager(), "");
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			ServiceHandlerJSON sh = new ServiceHandlerJSON();
			JSONObject jObj = sh.json_load_produktipe();

			try {
				JSONArray jArray = jObj
						.getJSONArray(Parameter_Collections.TAG_DATA);
				cCode = jObj.getString(Parameter_Collections.TAG_JSON_CODE);
				if (cCode.equals("1")) {
					for (int i = 0; i < jArray.length(); i++) {
						JSONObject c = jArray.getJSONObject(i);
						data_tipe
								.add(c.getString(Parameter_Collections.TAG_NAMA_PRODUK));
						Log.e("NAMA PRODUK",
								c.getString(Parameter_Collections.TAG_NAMA_PRODUK));
					}
				}

			} catch (JSONException e) {

			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			// pDialog.dismiss();
			if (cCode.equals("1")) {
				adapter_nama = new ArrayAdapter<String>(
						getApplicationContext(), R.layout.spinner_item,
						data_tipe);
				spinner_TipeProduk.setAdapter(adapter_nama);

			} else {
				Toast.makeText(getApplicationContext(),
						"Tidak bisa Load Nama Produk, Cek Internet anda",
						Toast.LENGTH_LONG).show();
			}

			tv_label = (TextView) findViewById(R.id.txt_label);
			tv_keterangan_error = (TextView) findViewById(R.id.keterangan_error);

			if (ada_di_db) {
				tv_label.setText("Produk tidak ada di database !");
				tv_keterangan_error.setVisibility(View.VISIBLE);
			} else {
				tv_label.setText("Produk tidak ada di dalam stok toko !");
				tv_keterangan_error.setVisibility(View.GONE);
			}

			spinner_TipeProduk
			.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> parent,
						View view, int position, long id) {
					// TODO Auto-generated method stub
					cNamaProduk = data_tipe.get(position);
					Log.e("Nama Tipe Produk", cNamaProduk);
				}

				@Override
				public void onNothingSelected(AdapterView<?> parent) {
					// TODO Auto-generated method stub

				}
			});
			
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
					if (!ed_Imei1.getText().toString().equals("")
							&& !auto_warnaProduk.getText().toString()
									.equals("")
							&& !auto_keteranganProduk.getText().toString()
									.equals("")) {
						new Async_SubmitProduk().execute();
					} else {
						Toast.makeText(getApplicationContext(),
								"Please fill required field",
								Toast.LENGTH_SHORT).show();
					}
				}
			});
		}
	}
}
