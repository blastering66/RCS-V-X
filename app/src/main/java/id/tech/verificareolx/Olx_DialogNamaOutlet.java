package id.tech.verificareolx;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
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

import id.tech.util.Parameter_Collections;

public class Olx_DialogNamaOutlet extends FragmentActivity{
	private EditText ed_NamaOutlet;
//	private ImageView img_Selfie_Landscape,img_Selfie_Portrait;
	private Button btn, btn_tgl;
	private String kode_outlet, nama_outlet;
	private SharedPreferences spf;
	private String id_pegawai, mUrl_Img_00, tipe_absensi;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_dialog_nama_outlet);

		spf = getSharedPreferences(Parameter_Collections.SH_NAME, Context.MODE_PRIVATE);
		id_pegawai = spf.getString(Parameter_Collections.SH_ID_PEGAWAI, "0");
		tipe_absensi = getIntent().getStringExtra(Parameter_Collections.EXTRA_ABSENSI);

		ed_NamaOutlet = (EditText)findViewById(R.id.ed_nama_outlet);

		if(tipe_absensi.equals("2")){
			kode_outlet = spf.getString(Parameter_Collections.SH_KODE_OUTLET, "0");
			nama_outlet = spf.getString(Parameter_Collections.SH_NAMA_OUTLET, "0");

			ed_NamaOutlet.setText(nama_outlet);
			ed_NamaOutlet.setEnabled(false);
		}

		btn = (Button)findViewById(R.id.btn);

		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String cNamaToko = ed_NamaOutlet.getText().toString();
				if(cNamaToko.equals("") || cNamaToko.isEmpty()){
					Toast.makeText(getApplicationContext(), "Nama Toko tidak boleh Kosong", Toast.LENGTH_LONG).show();
				}else{
					new AsyncTask_Absensi().execute();
				}



			}
		});

//		new AsyncTask_GetJenis_Outlet().execute();
	}



	private class AsyncTask_Absensi extends AsyncTask<Void,Void,String>{
		ProgressDialog pdialog;
		String respondMessage;
		JSONObject jsonResult;
		Olx_DialogFragmentProgress dialogProgress;
		String cNamaToko;
		int serverRespondCode = 0;

		String url_file00;
		File sourceFile00;
		FileInputStream fileInputStream00;
		private String row_count;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialogProgress = new Olx_DialogFragmentProgress();
			dialogProgress.show(getSupportFragmentManager(), "");

			cNamaToko = ed_NamaOutlet.getText().toString();
			mUrl_Img_00 = Parameter_Collections.URL_FOTO_ABSEN;
		}

		@Override
		protected String doInBackground(Void... params) {
			return uploadDataForm(mUrl_Img_00);
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			JSONObject jObj = jsonResult;
			try{
				row_count = jObj.getString(Parameter_Collections.TAG_ROWCOUNT);
//				kode_outlet = jObj.getString(Parameter_Collections.TAG_KODE_OUTLET);
				kode_outlet = "";
				nama_outlet = cNamaToko;

			}catch(JSONException e){
				row_count = "0";
				kode_outlet="0";
			}

			if(row_count.equals("1")){
				dialogProgress.dismiss();

				if(tipe_absensi.equals("1")){
					spf.edit().putString(Parameter_Collections.SH_KODE_OUTLET, kode_outlet).commit();
					// EDITAN OLX
					spf.edit()
							.putBoolean(Parameter_Collections.SH_ABSENTED, true)
							.commit();
					spf.edit().putString(Parameter_Collections.SH_NAMA_OUTLET, nama_outlet).commit();
					spf.edit().putString(Parameter_Collections.SH_KODE_OUTLET, kode_outlet).commit();
				}else{
					spf.edit().putBoolean(Parameter_Collections.SH_ABSENTED, false).commit();
					spf.edit().putBoolean(Parameter_Collections.SH_OUTLET_UPDATED, false).commit();
					spf.edit().putBoolean(Parameter_Collections.SH_OUTLET_VISITED, false).commit();
					spf.edit().putString(Parameter_Collections.SH_KODE_OUTLET, "0").commit();
					spf.edit().putString(Parameter_Collections.SH_NAMA_OUTLET, "0").commit();
				}


				Olx_DialogLocationConfirmation dialog = new Olx_DialogLocationConfirmation();
				dialog.setContext(getApplicationContext());
				dialog.setText("Absensi Success");
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

		private String uploadDataForm(String url_gambar00){
			HttpURLConnection conn = null;
			DataOutputStream dos = null;
			String lineEnd = "\r\n";
			String twoHyphens = "--";
			String boundary = "*****";
			int bytesRead, bytesAvailable, bufferSize;
			byte[] buffer;
			int maxBufferSize = 1 * 1024 * 1024;



//			if(url_gambar00 != null){
//				url_file00 = url_gambar00;
//				sourceFile00 = new File(url_file00);
//			}


			try {
				URL url = new URL(Parameter_Collections.URL_INSERT);

				conn = (HttpURLConnection) url.openConnection();
				conn.setConnectTimeout(60000);

				conn.setDoInput(true);
				conn.setDoOutput(true);
				conn.setUseCaches(false);
				conn.setRequestMethod("POST");

				conn.setRequestProperty("ENCTYPE", "multipart/form-data");
				conn.setRequestProperty("Content-Type",
						"multipart/form-data;boundary=" + boundary);
//				if(url_gambar00 != null){
//					conn.setRequestProperty("img0", url_file00);
//				}


				dos = new DataOutputStream(conn.getOutputStream());

//				if(url_gambar00 != null){
//					fileInputStream00 = new FileInputStream(
//							sourceFile00);
//					//img 00
//					dos.writeBytes(twoHyphens + boundary + lineEnd);
//					dos.writeBytes("Content-Disposition: form-data; name=\"img0\";filename=\""
//							+ url_file00 + "\"" + lineEnd);
//					dos.writeBytes(lineEnd);
//
//					bytesAvailable = fileInputStream00.available();
//
//					bufferSize = Math.min(bytesAvailable, maxBufferSize);
//					buffer = new byte[bufferSize];
//
//					bytesRead = fileInputStream00.read(buffer, 0, bufferSize);
//					while (bytesRead > 0) {
//						dos.write(buffer, 0, bufferSize);
//						bytesAvailable = fileInputStream00.available();
//						bufferSize = Math.min(bytesAvailable, maxBufferSize);
//						bytesRead = fileInputStream00.read(buffer, 0, bufferSize);
//					}
//
//					dos.writeBytes(lineEnd);
//					dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
//
//				}

				// param kind
				dos.writeBytes(twoHyphens + boundary + lineEnd);
				dos.writeBytes("Content-Disposition: form-data; name=\""
						+ Parameter_Collections.KIND + "\"" + lineEnd);
				dos.writeBytes(lineEnd);
				dos.writeBytes(Parameter_Collections.KIND_ABSEN + lineEnd);

				// param mobile
				dos.writeBytes(twoHyphens + boundary + lineEnd);
				dos.writeBytes("Content-Disposition: form-data; name=\""
						+ Parameter_Collections.KIND_MOBILE + "\"" + lineEnd);
				dos.writeBytes(lineEnd);
				dos.writeBytes("true" + lineEnd);

				Log.e("Kind Mobile True", cNamaToko);

				// param kode toko
				dos.writeBytes(twoHyphens + boundary + lineEnd);
				dos.writeBytes("Content-Disposition: form-data; name=\""
						+ Parameter_Collections.TAG_NAMA_OUTLET + "\"" + lineEnd);
				dos.writeBytes(lineEnd);
				dos.writeBytes(cNamaToko + lineEnd);

				Log.e("Nama Outlet", cNamaToko);


				// param id jenis outlet
				dos.writeBytes(twoHyphens + boundary + lineEnd);
				dos.writeBytes("Content-Disposition: form-data; name=\""
						+ Parameter_Collections.TAG_ID_JENIS_OUTLET + "\"" + lineEnd);
				dos.writeBytes(lineEnd);

//				if(tipe_absensi.equals("2")){
//					dos.writeBytes("" + lineEnd);
//				}else{
//					dos.writeBytes(selected_id_jenis_outlet + lineEnd);
//				}
				dos.writeBytes("" + lineEnd);


				// param id pegawai
				dos.writeBytes(twoHyphens + boundary + lineEnd);
				dos.writeBytes("Content-Disposition: form-data; name=\""
						+ Parameter_Collections.TAG_ID_PEGAWAI + "\"" + lineEnd);
				dos.writeBytes(lineEnd);
				dos.writeBytes(id_pegawai + lineEnd);

				// param id pegawai
				dos.writeBytes(twoHyphens + boundary + lineEnd);
				dos.writeBytes("Content-Disposition: form-data; name=\""
						+ Parameter_Collections.TAG_TIPE_ABSENSI + "\"" + lineEnd);
				dos.writeBytes(lineEnd);
				dos.writeBytes(tipe_absensi + lineEnd);

				// param latitude
				dos.writeBytes(twoHyphens + boundary + lineEnd);
				dos.writeBytes("Content-Disposition: form-data; name=\""
						+ Parameter_Collections.TAG_LATITUDE_ABSENSI + "\"" + lineEnd);
				dos.writeBytes(lineEnd);
				dos.writeBytes(spf.getString(Parameter_Collections.TAG_LATITUDE_NOW, "") + lineEnd);

				// param longitude
				dos.writeBytes(twoHyphens + boundary + lineEnd);
				dos.writeBytes("Content-Disposition: form-data; name=\""
						+ Parameter_Collections.TAG_LONGITUDE_ABSENSI + "\"" + lineEnd);
				dos.writeBytes(lineEnd);
				dos.writeBytes(spf.getString(Parameter_Collections.TAG_LONGITUDE_NOW, "")+ lineEnd);
				dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);


				serverRespondCode = conn.getResponseCode();
				respondMessage = conn.getResponseMessage();

				Log.e("RESPOND", respondMessage);

				if (serverRespondCode == 200) {
					Log.e("CODE ", "Success Upload");
				} else {
					Log.e("CODE ", "Success failed");
				}


//				if(url_gambar00 != null){
//					fileInputStream00.close();
//				}


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
