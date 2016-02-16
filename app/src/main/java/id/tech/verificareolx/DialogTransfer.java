package id.tech.verificareolx;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import id.tech.verificareolx.R;
import id.tech.util.Parameter_Collections;
import id.tech.util.RowData_Toko;
import id.tech.util.ServiceHandlerJSON;
import android.content.Context;
import android.content.Intent;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class DialogTransfer extends FragmentActivity {
	Button btn;
	EditText ed_Imei;
	Spinner spinner_Toko;
	String imei, id_pegawai, id_produk_toko, id_toko, kode_toko;
	SharedPreferences spf;
	ArrayList<RowData_Toko> data_toko;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.layout_dialog_transfer);

		imei = getIntent().getExtras().getString("imei");
		id_produk_toko = getIntent().getExtras().getString("id_produk_toko");

		spf = getSharedPreferences(Parameter_Collections.SH_NAME,
				Context.MODE_PRIVATE);

		kode_toko = spf.getString(Parameter_Collections.SH_KODE_TOKO, "");
		id_pegawai = spf.getString(Parameter_Collections.SH_ID_PEGAWAI, "");

		ed_Imei = (EditText) findViewById(R.id.ed_imei_produk);
		btn = (Button) findViewById(R.id.btn);
		spinner_Toko = (Spinner) findViewById(R.id.spinner_toko);

		ed_Imei.setHint(imei);
		ed_Imei.setEnabled(false);

		// ArrayAdapter<String> adapter= new
		// ArrayAdapter<String>(getApplicationContext(),
		// R.layout.spinner_item,
		// getResources().getStringArray(R.array.country_arrays));
		// adapter.setDropDownViewResource(R.layout.spinner_item);
		// spinner_Toko.setAdapter(adapter);
		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (id_toko.equals("") || id_toko.isEmpty() || id_toko == null) {
					Toast.makeText(getApplicationContext(), "id toko kosong",
							Toast.LENGTH_LONG).show();
				} else {
					new Async_TransferStok().execute();
				}
			}
		});

		new Async_GetAllToko().execute();
	}

	private class Async_TransferStok extends AsyncTask<Void, Void, Void> {
		DialogFragmentProgress pDialog;
		String cCode, cMessage;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pDialog = new DialogFragmentProgress();
			pDialog.show(getSupportFragmentManager(), "");
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			ServiceHandlerJSON sh = new ServiceHandlerJSON();
			// kirim datanya
			JSONObject jObj = sh.json_transfer_stok(id_produk_toko, imei,
					id_pegawai, kode_toko, id_toko,
					spf.getString(Parameter_Collections.TAG_LONGITUDE_NOW, ""),
					spf.getString(Parameter_Collections.TAG_LATITUDE_NOW, ""));
			
			try{
				cCode = jObj.getString(Parameter_Collections.TAG_JSON_CODE);
				
				if(cCode.equals("0")){
					cMessage = jObj.getString(Parameter_Collections.TAG_JSON_ERROR_MESSAGE);
				}
			}catch(JSONException e){
				cCode = "0";
				cMessage = "Something wrong";
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			pDialog.dismiss();
			
			if(cCode.equals("1")){
				Toast.makeText(getApplicationContext(), "Transfer sukses", Toast.LENGTH_LONG).show();
				Intent intent = getIntent();
				setResult(RESULT_OK, intent);				
				finish();
				
			}else{
				Toast.makeText(getApplicationContext(), cMessage, Toast.LENGTH_LONG).show();
				Intent intent = getIntent();
				setResult(RESULT_CANCELED, intent);				
				finish();
			}
		}
	}

	private class Async_GetAllToko extends AsyncTask<Void, Void, Void> {
		DialogFragmentProgress pDialog;
		String cCode, cMessage;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pDialog = new DialogFragmentProgress();
			pDialog.show(getSupportFragmentManager(), "");

			data_toko = new ArrayList<RowData_Toko>();
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			ServiceHandlerJSON sh = new ServiceHandlerJSON();

			JSONObject jObj = sh.json_get_alltoko();
			try {
				JSONArray jArray = jObj
						.getJSONArray(Parameter_Collections.TAG_DATA);
				for (int i = 0; i < jArray.length(); i++) {
					JSONObject c = jArray.getJSONObject(i);
					String id_tokonya = c
							.getString(Parameter_Collections.TAG_ID_TOKO);
					String kode_tokonya = c
							.getString(Parameter_Collections.TAG_KODE_TOKO);
					String nama_tokonya = c
							.getString(Parameter_Collections.TAG_NAMA_TOKO);

					if (!kode_toko.equals(kode_tokonya)) {
						data_toko.add(new RowData_Toko(id_tokonya,
								kode_tokonya, nama_tokonya, ""));
					}

					cCode = "1";
				}
			} catch (JSONException e) {
				cCode = "1";
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			pDialog.dismiss();

			if (cCode.equals("1")) {
				List<String> nama_toko = new ArrayList<String>();
				for (int i = 0; i < data_toko.size(); i++) {
					nama_toko.add(data_toko.get(i).nama_toko);
				}

				ArrayAdapter<String> adapter = new ArrayAdapter<String>(
						getApplicationContext(), R.layout.spinner_item,
						nama_toko);
				adapter.setDropDownViewResource(R.layout.spinner_item);
				spinner_Toko.setAdapter(adapter);

				spinner_Toko
						.setOnItemSelectedListener(new OnItemSelectedListener() {

							@Override
							public void onItemSelected(AdapterView<?> parent,
									View view, int position, long id) {
								// TODO Auto-generated method stub
								id_toko = data_toko.get(position).kode_toko;
							}

							@Override
							public void onNothingSelected(AdapterView<?> parent) {
								// TODO Auto-generated method stub

							}
						});
			}

		}
	}
}
