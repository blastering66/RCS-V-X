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
	String mUrl_Img_00, mUrl_Img_01, mUrl_Img_02, mUrl_Img_03, mUrl_Img_04, mUrl_Img_05, mUrl_Img_06;
	ImageView imgview_00,imgview_01,imgview_02, imgview_03,imgview_04,imgview_05, imgview_06;
	public static int CODE_UPLOAD = 111;
	HorizontalScrollView horizontalScroll;
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

		mUrl_Img_00 = null;
		mUrl_Img_01 = null;
		mUrl_Img_02 = null;
		mUrl_Img_03 = null;
		mUrl_Img_04 = null;
		mUrl_Img_05 = null;
		mUrl_Img_06 = null;

		getAllView();
	}

	private void getAllView() {		
		horizontalScroll = (HorizontalScrollView)findViewById(R.id.wrapper_horizontalview);
		imgview_00 = (ImageView)findViewById(R.id.img_00);
		imgview_01 = (ImageView)findViewById(R.id.img_01);
		imgview_02 = (ImageView)findViewById(R.id.img_02);
		imgview_03 = (ImageView)findViewById(R.id.img_03);
		imgview_04 = (ImageView)findViewById(R.id.img_04);
		imgview_05 = (ImageView)findViewById(R.id.img_05);
		imgview_06 = (ImageView)findViewById(R.id.img_06);
		
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
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (resultCode == RESULT_OK) {

			if (requestCode == CODE_UPLOAD) {

				if (mUrl_Img_00 == null) {
					horizontalScroll.setVisibility(View.VISIBLE);
					
					mUrl_Img_00 = data.getStringExtra("mUrl_Img");					
					Bitmap b = BitmapFactory.decodeFile(mUrl_Img_00);
					imgview_00.setVisibility(View.VISIBLE);
					imgview_00.setImageBitmap(b);
				} else if (mUrl_Img_01 == null) {
					mUrl_Img_01 = data.getStringExtra("mUrl_Img");
					Bitmap b = BitmapFactory.decodeFile(mUrl_Img_01);
					imgview_01.setVisibility(View.VISIBLE);
					imgview_01.setImageBitmap(b);
				} else if (mUrl_Img_02 == null) {
					mUrl_Img_02 = data.getStringExtra("mUrl_Img");
					Bitmap b = BitmapFactory.decodeFile(mUrl_Img_02);
					imgview_02.setVisibility(View.VISIBLE);
					imgview_02.setImageBitmap(b);
				} else if (mUrl_Img_03 == null) {
					mUrl_Img_03 = data.getStringExtra("mUrl_Img");
					Bitmap b = BitmapFactory.decodeFile(mUrl_Img_03);
					imgview_03.setVisibility(View.VISIBLE);
					imgview_03.setImageBitmap(b);
				}else if (mUrl_Img_04 == null) {
					mUrl_Img_04 = data.getStringExtra("mUrl_Img");
					Bitmap b = BitmapFactory.decodeFile(mUrl_Img_04);
					imgview_04.setVisibility(View.VISIBLE);
					imgview_04.setImageBitmap(b);
				} else if (mUrl_Img_05 == null) {
					mUrl_Img_05 = data.getStringExtra("mUrl_Img");
					Bitmap b = BitmapFactory.decodeFile(mUrl_Img_05);
					imgview_05.setVisibility(View.VISIBLE);
					imgview_05.setImageBitmap(b);
				} else if (mUrl_Img_06 == null) {
					mUrl_Img_06 = data.getStringExtra("mUrl_Img");
					Bitmap b = BitmapFactory.decodeFile(mUrl_Img_06);
					imgview_06.setVisibility(View.VISIBLE);
					imgview_06.setImageBitmap(b);
				}

			}

		}

	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		Public_Functions.delete_IssuePhoto();
		Toast.makeText(getApplicationContext(), "Canceled. Images deleted", Toast.LENGTH_LONG).show();
		finish();
	}
	
	private class Async_SubmitUpdateBranding extends AsyncTask<Void, Void, String>{
		ProgressDialog pdialog;
		String respondMessage;
		JSONObject jsonResult;
		Olx_DialogFragmentProgress dialogProgress;
		String cDesc ,cUsername, cPhone, cEmail, cNominal;
		int serverRespondCode = 0;

		String url_file00, url_file01, url_file02, url_file03, url_file04, url_file05, url_file06;
		File sourceFile00,sourceFile01, sourceFile02, sourceFile03,sourceFile04, sourceFile05, sourceFile06;
		FileInputStream fileInputStream00,fileInputStream01, fileInputStream02, fileInputStream03
				,fileInputStream04, fileInputStream05, fileInputStream06;
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
			return uploadDataForm(mUrl_Img_00, mUrl_Img_01, mUrl_Img_02, mUrl_Img_03
					, mUrl_Img_04, mUrl_Img_05, mUrl_Img_06);
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
		
		private String uploadDataForm(String url_gambar00, String url_gambar01,
									  String url_gambar02, String url_gambar03, String url_gambar04,
									  String url_gambar05, String url_gambar06){
			HttpURLConnection conn = null;
			DataOutputStream dos = null;
			String lineEnd = "\r\n";
			String twoHyphens = "--";
			String boundary = "*****";
			int bytesRead, bytesAvailable, bufferSize;
			byte[] buffer;
			int maxBufferSize = 1 * 1024 * 1024;

			
			
			if(url_gambar00 != null){
				url_file00 = url_gambar00;
				 sourceFile00 = new File(url_file00);				
			}
			if(url_gambar01 != null){
				 url_file01 = url_gambar01;
				 sourceFile01 = new File(url_file01);
			}
			if(url_gambar02 != null){
				 url_file02 = url_gambar02;
				 sourceFile02 = new File(url_file02);
			}if(url_gambar03 != null){
				 url_file03 = url_gambar03;
				 sourceFile03 = new File(url_file03);
			}if(url_gambar04 != null){
				url_file04 = url_gambar04;
				sourceFile04 = new File(url_file04);
			}
			if(url_gambar05 != null){
				url_file05 = url_gambar05;
				sourceFile05 = new File(url_file05);
			}if(url_gambar06 != null){
				url_file06 = url_gambar06;
				sourceFile06 = new File(url_file06);
			}
			
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
				if(url_gambar00 != null){
					conn.setRequestProperty("img0", url_file00);
				}
				if(url_gambar01 != null){
					conn.setRequestProperty("img1", url_file01);
				}
				if(url_gambar02 != null){
					conn.setRequestProperty("img2", url_file02);
				}
				if(url_gambar03 != null){
					conn.setRequestProperty("img3", url_file03);
				}
				if(url_gambar04 != null){
					conn.setRequestProperty("img4", url_file04);
				}
				if(url_gambar05 != null){
					conn.setRequestProperty("img5", url_file05);
				}
				if(url_gambar06 != null){
					conn.setRequestProperty("img6", url_file06);
				}
				
				dos = new DataOutputStream(conn.getOutputStream());
				
				if(url_gambar00 != null){
					fileInputStream00 = new FileInputStream(
							sourceFile00);
					//img 00
					dos.writeBytes(twoHyphens + boundary + lineEnd);
					dos.writeBytes("Content-Disposition: form-data; name=\"img0\";filename=\""
							+ url_file00 + "\"" + lineEnd);
					dos.writeBytes(lineEnd);
					
					bytesAvailable = fileInputStream00.available();

					bufferSize = Math.min(bytesAvailable, maxBufferSize);
					buffer = new byte[bufferSize];

					bytesRead = fileInputStream00.read(buffer, 0, bufferSize);
					while (bytesRead > 0) {
						dos.write(buffer, 0, bufferSize);
						bytesAvailable = fileInputStream00.available();
						bufferSize = Math.min(bytesAvailable, maxBufferSize);
						bytesRead = fileInputStream00.read(buffer, 0, bufferSize);
					}
					
					dos.writeBytes(lineEnd);
					dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
					
				}if(url_gambar01 != null){
					fileInputStream01 = new FileInputStream(
							sourceFile01);
					//img 01
					dos.writeBytes(twoHyphens + boundary + lineEnd);
					dos.writeBytes("Content-Disposition: form-data; name=\"img1\";filename=\""
							+ url_file01 + "\"" + lineEnd);
					dos.writeBytes(lineEnd);
					
					bytesAvailable = fileInputStream01.available();

					bufferSize = Math.min(bytesAvailable, maxBufferSize);
					buffer = new byte[bufferSize];

					bytesRead = fileInputStream01.read(buffer, 0, bufferSize);
					while (bytesRead > 0) {
						dos.write(buffer, 0, bufferSize);
						bytesAvailable = fileInputStream01.available();
						bufferSize = Math.min(bytesAvailable, maxBufferSize);
						bytesRead = fileInputStream01.read(buffer, 0, bufferSize);
					}
					
					dos.writeBytes(lineEnd);
					dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
					
				}if(url_gambar02 != null){
					fileInputStream02 = new FileInputStream(
							sourceFile02);
					//img 02
					dos.writeBytes(twoHyphens + boundary + lineEnd);
					dos.writeBytes("Content-Disposition: form-data; name=\"img2\";filename=\""
							+ url_file02 + "\"" + lineEnd);
					dos.writeBytes(lineEnd);
					
					bytesAvailable = fileInputStream02.available();

					bufferSize = Math.min(bytesAvailable, maxBufferSize);
					buffer = new byte[bufferSize];

					bytesRead = fileInputStream02.read(buffer, 0, bufferSize);
					while (bytesRead > 0) {
						dos.write(buffer, 0, bufferSize);
						bytesAvailable = fileInputStream02.available();
						bufferSize = Math.min(bytesAvailable, maxBufferSize);
						bytesRead = fileInputStream02.read(buffer, 0, bufferSize);
					}
					
					dos.writeBytes(lineEnd);
					dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
				}if(url_gambar03 != null){
					fileInputStream03 = new FileInputStream(
							sourceFile03);
					//img 03
					dos.writeBytes(twoHyphens + boundary + lineEnd);
					dos.writeBytes("Content-Disposition: form-data; name=\"img3\";filename=\""
							+ url_file03 + "\"" + lineEnd);
					dos.writeBytes(lineEnd);
					
					bytesAvailable = fileInputStream03.available();

					bufferSize = Math.min(bytesAvailable, maxBufferSize);
					buffer = new byte[bufferSize];

					bytesRead = fileInputStream03.read(buffer, 0, bufferSize);
					while (bytesRead > 0) {
						dos.write(buffer, 0, bufferSize);
						bytesAvailable = fileInputStream03.available();
						bufferSize = Math.min(bytesAvailable, maxBufferSize);
						bytesRead = fileInputStream03.read(buffer, 0, bufferSize);
					}
					
					dos.writeBytes(lineEnd);
					dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
					
				}if(url_gambar04 != null){
					fileInputStream04 = new FileInputStream(
							sourceFile04);
					//img 01
					dos.writeBytes(twoHyphens + boundary + lineEnd);
					dos.writeBytes("Content-Disposition: form-data; name=\"img4\";filename=\""
							+ url_file04 + "\"" + lineEnd);
					dos.writeBytes(lineEnd);

					bytesAvailable = fileInputStream04.available();

					bufferSize = Math.min(bytesAvailable, maxBufferSize);
					buffer = new byte[bufferSize];

					bytesRead = fileInputStream04.read(buffer, 0, bufferSize);
					while (bytesRead > 0) {
						dos.write(buffer, 0, bufferSize);
						bytesAvailable = fileInputStream04.available();
						bufferSize = Math.min(bytesAvailable, maxBufferSize);
						bytesRead = fileInputStream04.read(buffer, 0, bufferSize);
					}

					dos.writeBytes(lineEnd);
					dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

				}if(url_gambar05 != null){
					fileInputStream05 = new FileInputStream(
							sourceFile05);
					//img 02
					dos.writeBytes(twoHyphens + boundary + lineEnd);
					dos.writeBytes("Content-Disposition: form-data; name=\"img5\";filename=\""
							+ url_file05 + "\"" + lineEnd);
					dos.writeBytes(lineEnd);

					bytesAvailable = fileInputStream05.available();

					bufferSize = Math.min(bytesAvailable, maxBufferSize);
					buffer = new byte[bufferSize];

					bytesRead = fileInputStream05.read(buffer, 0, bufferSize);
					while (bytesRead > 0) {
						dos.write(buffer, 0, bufferSize);
						bytesAvailable = fileInputStream05.available();
						bufferSize = Math.min(bytesAvailable, maxBufferSize);
						bytesRead = fileInputStream05.read(buffer, 0, bufferSize);
					}

					dos.writeBytes(lineEnd);
					dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
				}if(url_gambar06 != null){
					fileInputStream06 = new FileInputStream(
							sourceFile06);
					//img 03
					dos.writeBytes(twoHyphens + boundary + lineEnd);
					dos.writeBytes("Content-Disposition: form-data; name=\"img6\";filename=\""
							+ url_file06 + "\"" + lineEnd);
					dos.writeBytes(lineEnd);

					bytesAvailable = fileInputStream06.available();

					bufferSize = Math.min(bytesAvailable, maxBufferSize);
					buffer = new byte[bufferSize];

					bytesRead = fileInputStream06.read(buffer, 0, bufferSize);
					while (bytesRead > 0) {
						dos.write(buffer, 0, bufferSize);
						bytesAvailable = fileInputStream06.available();
						bufferSize = Math.min(bytesAvailable, maxBufferSize);
						bytesRead = fileInputStream06.read(buffer, 0, bufferSize);
					}

					dos.writeBytes(lineEnd);
					dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

				}
				
				
				
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
				
				
				if(url_gambar00 != null){
					fileInputStream00.close();
				}if(url_gambar01 != null){
					fileInputStream01.close();
				}if(url_gambar02 != null){
					fileInputStream02.close();
				}if(url_gambar03 != null){
					fileInputStream03.close();
				}if(url_gambar04 != null){
					fileInputStream04.close();
				}if(url_gambar05 != null){
					fileInputStream05.close();
				}if(url_gambar06 != null){
					fileInputStream06.close();
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
