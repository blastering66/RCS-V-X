package id.tech.verificareolx;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import id.tech.util.GPSTracker;
import id.tech.util.Parameter_Collections;
import id.tech.util.Public_Functions;

public class Olx_UpdateBranding_Activity extends ActionBarActivity{
	Button btn;
	public static int CODE_UPLOAD = 111;
	EditText ed_namatoko, ed_ket, ed_store_phone_regis, ed_store_email_regis, ed_store_username_regis, ed_nominal_topup;
	CheckBox checkbox_topup;
	SharedPreferences spf;
	private String id_pegawai , nama_outlet, kode_outlet;
	
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.activity_updatebranding_olx);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//		getSupportActionBar().setTitle("Visit Activity");
		getSupportActionBar().setTitle(Html.fromHtml("<font color='#ffffff'>Visit Activity </font>"));
		
		spf = getSharedPreferences(Parameter_Collections.SH_NAME, Context.MODE_PRIVATE);
		nama_outlet = spf.getString(Parameter_Collections.SH_NAMA_OUTLET, "");
		kode_outlet = spf.getString(Parameter_Collections.SH_KODE_OUTLET, "");
		id_pegawai = spf.getString(Parameter_Collections.SH_ID_PEGAWAI, "");


		getAllView();
	}

	private void getAllView() {		

		ed_namatoko = (EditText)findViewById(R.id.ed_namatoko);
		ed_namatoko.setText(nama_outlet);
		ed_store_username_regis = (EditText)findViewById(R.id.ed_store_username_regis);
		ed_store_phone_regis = (EditText)findViewById(R.id.ed_store_phone_regis);
		ed_store_email_regis = (EditText)findViewById(R.id.ed_store_email_regis);
		checkbox_topup = (CheckBox)findViewById(R.id.checkbox_topup);
		ed_nominal_topup = (EditText)findViewById(R.id.ed_nominal_topup);
//		MaskedEditTextListener maskedEditTextListener = new MaskedEditTextListener("###.###.###", ed_nominal_topup);
//		ed_nominal_topup.addTextChangedListener(new ThreeDigitNominalWatcher());

		int id_holo_style = Resources.getSystem().getIdentifier("btn_check_holo_light", "drawable", "android");
		checkbox_topup.setButtonDrawable(id_holo_style);
		checkbox_topup.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(checkbox_topup.isChecked()){
					ed_nominal_topup.setVisibility(View.VISIBLE);
				}else{
					ed_nominal_topup.setVisibility(View.GONE);
				}
			}
		});

		ed_ket= (EditText)findViewById(R.id.ed_ket);
		btn = (Button) findViewById(R.id.btn);

		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent upload = new Intent(getApplicationContext(),
						Olx_UploadImageDialog.class);
				startActivityForResult(upload, CODE_UPLOAD);
				// adapter = new CustomAdapter_Img(getApplicationContext(), 0,
				// 0, data);

			}
		});
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.updatebrand, menu);
		return super.onCreateOptionsMenu(menu);
	}
	

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case android.R.id.home:
			Public_Functions.delete_IssuePhoto();
			Toast.makeText(getApplicationContext(), "Canceled. Images deleted", Toast.LENGTH_LONG).show();
			finish();
			break;
			
		case R.id.action_send_updatebranding:
			
			GPSTracker gps = new GPSTracker(getApplicationContext());
			if (gps.canGetLocation()) {
				double latitude = gps.getLatitude();
				double longitude = gps.getLongitude();			
				
				spf.edit().putString(Parameter_Collections.TAG_LONGITUDE_NOW, String.valueOf(longitude)).commit();
				spf.edit().putString(Parameter_Collections.TAG_LATITUDE_NOW, String.valueOf(latitude)).commit();
				

				if (Public_Functions.isNetworkAvailable(getApplicationContext())) {
//				boolean b = true;
//				if (b) {
					new Async_SubmitUpdateBranding().execute();
				}else {
					Toast.makeText(getApplicationContext(),
							"No Internet Connection, Cek Your Network",
							Toast.LENGTH_LONG).show();
				}
				

			} else {
				
				if(Public_Functions.isNetworkAvailable(getApplicationContext())){
					new Async_SubmitUpdateBranding().execute();
				}else {
					Toast.makeText(getApplicationContext(),
							"No Internet Connection, Cek Your Network",
							Toast.LENGTH_LONG).show();
				}
				
				Toast.makeText(
						getApplicationContext(),
						"Can not get your location now, Sent your last locations",
						Toast.LENGTH_LONG).show();
			}
			
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}


	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		finish();
	}
	
	private class Async_SubmitUpdateBranding extends AsyncTask<Void, Void, String>{
		ProgressDialog pdialog;
		String respondMessage;
		JSONObject jsonResult;
		Olx_DialogFragmentProgress dialogProgress;
		String cDesc ,cUsername, cPhone, cEmail, cNominal;
		int serverRespondCode = 0;

		private String row_count;
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			dialogProgress = new Olx_DialogFragmentProgress();
			dialogProgress.show(getSupportFragmentManager(), "");
			

			cUsername = ed_store_username_regis.getText().toString();
			cPhone = ed_store_phone_regis.getText().toString();
			cEmail = ed_store_email_regis.getText().toString();
			cDesc = ed_ket.getText().toString();
			cNominal = ed_nominal_topup.getText().toString();
		}
		
		@Override
		protected String doInBackground(Void... params) {
			// TODO Auto-generated method stub
			return uploadDataForm();
		}
		
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			
			JSONObject jObj = jsonResult;
			try{				
				row_count = jObj.getString(Parameter_Collections.TAG_ROWCOUNT);
				
			}catch(JSONException e){
				row_count = "0";
				
			}
			
			if(row_count.equals("1")){
				dialogProgress.dismiss();

				spf.edit().putBoolean(Parameter_Collections.SH_OUTLET_VISITED, true).commit();
				
				Olx_DialogLocationConfirmation dialog = new Olx_DialogLocationConfirmation();
				dialog.setContext(getApplicationContext());
				dialog.setText("Input Visit Activity Success");
				dialog.setFrom(9);
				dialog.setCancelable(false);
				dialog.show(getSupportFragmentManager(), "");
//				Toast.makeText(getApplicationContext(), "Update Branding Success", Toast.LENGTH_LONG).show();			
//				finish();
			}else{
				dialogProgress.dismiss();
				
				Olx_DialogLocationConfirmation dialog = new Olx_DialogLocationConfirmation();
				dialog.setContext(getApplicationContext());
				dialog.setText(result);
				dialog.setFrom(9);
				dialog.setCancelable(false);
				dialog.show(getSupportFragmentManager(), "");
//				Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();			
//				finish();
			}
			
			
		}
		
		private String uploadDataForm(){
			HttpURLConnection conn = null;
			DataOutputStream dos = null;
			String lineEnd = "\r\n";
			String twoHyphens = "--";
			String boundary = "*****";
			int bytesRead, bytesAvailable, bufferSize;
			byte[] buffer;
			int maxBufferSize = 1 * 1024 * 1024;


			try {				
				URL url = new URL(Parameter_Collections.URL_INSERT);

				conn = (HttpURLConnection) url.openConnection();
				conn.setDoInput(true);
				conn.setDoOutput(true);
				conn.setUseCaches(false);
				conn.setRequestMethod("POST");

				conn.setRequestProperty("ENCTYPE", "multipart/form-data");
				conn.setRequestProperty("Content-Type",
						"multipart/form-data;boundary=" + boundary);

				dos = new DataOutputStream(conn.getOutputStream());
				
				// param kind
				dos.writeBytes(twoHyphens + boundary + lineEnd);
				dos.writeBytes("Content-Disposition: form-data; name=\""
						+ Parameter_Collections.KIND + "\"" + lineEnd);
				dos.writeBytes(lineEnd);
				dos.writeBytes(Parameter_Collections.KIND_VISIT+ lineEnd);

				// param mobile
				dos.writeBytes(twoHyphens + boundary + lineEnd);
				dos.writeBytes("Content-Disposition: form-data; name=\""
						+ Parameter_Collections.KIND_MOBILE + "\"" + lineEnd);
				dos.writeBytes(lineEnd);
				dos.writeBytes("true" + lineEnd);


				// param kode toko
				dos.writeBytes(twoHyphens + boundary + lineEnd);
				dos.writeBytes("Content-Disposition: form-data; name=\""
						+ Parameter_Collections.TAG_KODE_OUTLET + "\"" + lineEnd);
				dos.writeBytes(lineEnd);
				dos.writeBytes(kode_outlet + lineEnd);

				// param id pegawai
				dos.writeBytes(twoHyphens + boundary + lineEnd);
				dos.writeBytes("Content-Disposition: form-data; name=\""
						+ Parameter_Collections.TAG_ID_PEGAWAI + "\"" + lineEnd);
				dos.writeBytes(lineEnd);
				dos.writeBytes(id_pegawai + lineEnd);
				
				// param latitude
				dos.writeBytes(twoHyphens + boundary + lineEnd);
				dos.writeBytes("Content-Disposition: form-data; name=\""
						+ Parameter_Collections.TAG_LAT_VISIT + "\"" + lineEnd);
				dos.writeBytes(lineEnd);
				dos.writeBytes(spf.getString(Parameter_Collections.TAG_LATITUDE_NOW, "") + lineEnd);
				
				// param longitude
				dos.writeBytes(twoHyphens + boundary + lineEnd);
				dos.writeBytes("Content-Disposition: form-data; name=\""
						+ Parameter_Collections.TAG_LONG_VISIT + "\"" + lineEnd);
				dos.writeBytes(lineEnd);
				dos.writeBytes(spf.getString(Parameter_Collections.TAG_LONGITUDE_NOW, "")+ lineEnd);

				// param keterangan
				dos.writeBytes(twoHyphens + boundary + lineEnd);
				dos.writeBytes("Content-Disposition: form-data; name=\""
						+ Parameter_Collections.TAG_USERNAME_OUTLET_VISIT + "\"" + lineEnd);
				dos.writeBytes(lineEnd);
				dos.writeBytes(cUsername+ lineEnd);

				// param keterangan
				dos.writeBytes(twoHyphens + boundary + lineEnd);
				dos.writeBytes("Content-Disposition: form-data; name=\""
						+ Parameter_Collections.TAG_EMAIL_OUTLET_VISIT + "\"" + lineEnd);
				dos.writeBytes(lineEnd);
				dos.writeBytes(cEmail+ lineEnd);

				// param keterangan
				dos.writeBytes(twoHyphens + boundary + lineEnd);
				dos.writeBytes("Content-Disposition: form-data; name=\""
						+ Parameter_Collections.TAG_PHONE_OUTLET_VISIT + "\"" + lineEnd);
				dos.writeBytes(lineEnd);
				dos.writeBytes(cPhone+ lineEnd);

				// param keterangan
				dos.writeBytes(twoHyphens + boundary + lineEnd);
				dos.writeBytes("Content-Disposition: form-data; name=\""
						+ Parameter_Collections.TAG_TOPUP_OUTLET_VISIT + "\"" + lineEnd);
				dos.writeBytes(lineEnd);
				dos.writeBytes(cNominal+ lineEnd);

				// param desc program
				dos.writeBytes(twoHyphens + boundary + lineEnd);
				dos.writeBytes("Content-Disposition: form-data; name=\""
						+ Parameter_Collections.TAG_DESC_OUTLET_VISIT + "\"" + lineEnd);
				dos.writeBytes(lineEnd);
				dos.writeBytes(cDesc + lineEnd);
				dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
							
				
				serverRespondCode = conn.getResponseCode();
				respondMessage = conn.getResponseMessage();

				Log.e("RESPOND", respondMessage);

				if (serverRespondCode == 200) {
					Log.e("CODE ", "Success Upload");
				} else {
					Log.e("CODE ", "Success failed");
				}
				
				dos.flush();

				InputStream is = conn.getInputStream();
				int ch;

				StringBuffer buff = new StringBuffer();
				while ((ch = is.read()) != -1) {
					buff.append((char) ch);
				}
				Log.e("publish", buff.toString());

				jsonResult = new JSONObject(buff.toString());
				dos.close();
				
			}catch (MalformedURLException ex) {
				ex.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return respondMessage;
		}
	}
	
}
