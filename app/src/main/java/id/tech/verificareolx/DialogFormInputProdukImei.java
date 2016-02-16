package id.tech.verificareolx;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import id.tech.verificareolx.R;
import id.tech.util.Parameter_Collections;
import id.tech.util.ServiceHandlerJSON;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class DialogFormInputProdukImei extends FragmentActivity {
	Button btn, btn_submit;
	EditText ed_Imei;
	ImageView img;
	SharedPreferences spf;
	public static int CODE_UPLOAD = 111;
	String mUrl_Img_00;

	private String kode_toko, id_pegawai;
	int serverRespondCode = 0;
	byte[] dataX;
	long totalSize = 0;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.layout_dialog_input_produk_imei);

		spf = getSharedPreferences(Parameter_Collections.SH_NAME,
				Context.MODE_PRIVATE);

		if(spf.getBoolean(Parameter_Collections.SH_PINDAH_TOKO, false)){
			kode_toko = spf.getString(Parameter_Collections.SH_KODE_TOKO_SEMENTARA, "");
		}else{
			kode_toko = spf.getString(Parameter_Collections.SH_KODE_TOKO, "");
		}		
		
		id_pegawai = spf.getString(Parameter_Collections.SH_ID_PEGAWAI, "");

		ed_Imei = (EditText) findViewById(R.id.ed_form_imei);
		img = (ImageView) findViewById(R.id.img_00);

		btn = (Button) findViewById(R.id.btn);
		btn_submit = (Button) findViewById(R.id.btn_submit);

		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent upload = new Intent(getApplicationContext(),
						UploadImageDialog.class);
				startActivityForResult(upload, CODE_UPLOAD);
			}
		});

		btn_submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				new Async_InputProduk().execute();
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (resultCode == RESULT_OK) {

			if (requestCode == CODE_UPLOAD) {

				if (mUrl_Img_00 == null) {

					mUrl_Img_00 = data.getStringExtra("mUrl_Img");
					Bitmap b = BitmapFactory.decodeFile(mUrl_Img_00);
					img.setVisibility(View.VISIBLE);
					img.setImageBitmap(b);
				}
			}
		}

	}

	private class Async_InputProduk extends AsyncTask<Void, Void, String> {
		DialogFragmentProgress pDialog;
		String cCode, cMessage;
		String cImei;

		String respondMessage;
		JSONObject jsonResult;
		String row_count, error_message;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pDialog = new DialogFragmentProgress();
			pDialog.show(getSupportFragmentManager(), "");

			cImei = ed_Imei.getText().toString();
		}

		@Override
		protected String doInBackground(Void... params) {
			// TODO Auto-generated method stub
			
			return uploadDataForm(mUrl_Img_00);
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			pDialog.dismiss();

			JSONObject jObj = jsonResult;

			// String resultnya = jObj.toString();

			try {
				row_count = jObj.getString(Parameter_Collections.TAG_ROWCOUNT);

			} catch (JSONException e) {
				row_count = "0";
				try {
					error_message = jObj
							.getString(Parameter_Collections.TAG_JSON_ERROR_MESSAGE);
				} catch (JSONException e2) {

				}
			}

			if (row_count.equals("1")) {
				
				DialogLocationConfirmation dialog = new DialogLocationConfirmation();
				dialog.setContext(getApplicationContext());
				dialog.setText("Input Stok Sukses");
				dialog.setFrom(9);
				dialog.setCancelable(false);
				dialog.show(getSupportFragmentManager(), "");

				// Toast.makeText(getApplicationContext(), result,
				// Toast.LENGTH_LONG).show();
				// Toast.makeText(getApplicationContext(), "Input Stok Sukses",
				// Toast.LENGTH_LONG).show();
				// finish();
			} else if (error_message.equals("produk tidak ada di database")) {

				Intent load = new Intent(getApplicationContext(),
						DialogFormInputProduk.class);
				load.putExtra("ada_di_db", true);
				// load.putExtra("ada_di_db", false);
				startActivity(load);
				finish();

			} else if (error_message
					.equals("produk tidak ada di dalam stok toko")) {
				Intent load = new Intent(getApplicationContext(),
						DialogFormInputProduk.class);
				load.putExtra("ada_di_db", false);
				startActivity(load);
				finish();
			} else if (error_message.equals("produk sudah ada di data stok")) {
				
				DialogLocationConfirmation dialog = new DialogLocationConfirmation();
				dialog.setContext(getApplicationContext());
				dialog.setText("Produk sudah ada di Stok");
				dialog.setFrom(9);
				dialog.setCancelable(false);
				dialog.show(getSupportFragmentManager(), "");

				// Toast.makeText(getApplicationContext(), "",
				// Toast.LENGTH_LONG).show();
				// finish();
			}
		}

		private String uploadDataForm(String url_gambar) {
			HttpURLConnection conn = null;
			DataOutputStream dos = null;
			String lineEnd = "\r\n";
			String twoHyphens = "--";
			String boundary = "*****";
			int bytesRead, bytesAvailable, bufferSize;
			byte[] buffer;
			int maxBufferSize = 1 * 1024 * 1024;

			String url_file = url_gambar;
			File sourceFile = new File(url_file);

			try {
				FileInputStream fileInputStream = new FileInputStream(
						sourceFile);
				URL url = new URL(Parameter_Collections.URL_INSERT);

				Log.e("url gambar", url_file);

				conn = (HttpURLConnection) url.openConnection();
				conn.setDoInput(true);
				conn.setDoOutput(true);
				conn.setUseCaches(false);
				conn.setRequestMethod("POST");

				conn.setRequestProperty("ENCTYPE", "multipart/form-data");
				conn.setRequestProperty("Content-Type",
						"multipart/form-data;boundary=" + boundary);
				conn.setRequestProperty("img", url_file);

				dos = new DataOutputStream(conn.getOutputStream());
				dos.writeBytes(twoHyphens + boundary + lineEnd);
				dos.writeBytes("Content-Disposition: form-data; name=\"img\";filename=\""
						+ url_file + "\"" + lineEnd);
				dos.writeBytes(lineEnd);

				bytesAvailable = fileInputStream.available();

				bufferSize = Math.min(bytesAvailable, maxBufferSize);
				buffer = new byte[bufferSize];

				bytesRead = fileInputStream.read(buffer, 0, bufferSize);
				while (bytesRead > 0) {
					dos.write(buffer, 0, bufferSize);
					bytesAvailable = fileInputStream.available();
					bufferSize = Math.min(bytesAvailable, maxBufferSize);
					bytesRead = fileInputStream.read(buffer, 0, bufferSize);
				}

				dos.writeBytes(lineEnd);
				dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

				// param kind
				dos.writeBytes(twoHyphens + boundary + lineEnd);
				dos.writeBytes("Content-Disposition: form-data; name=\""
						+ Parameter_Collections.KIND + "\"" + lineEnd);
				dos.writeBytes(lineEnd);
				dos.writeBytes(Parameter_Collections.KIND_INSERT_PRODUKTOKO
						+ lineEnd);

				Log.e("Content Form : ",
						"Content-Disposition: form-data; name=\""
								+ Parameter_Collections.KIND_INSERT_PRODUKTOKO
								+ "\"" + lineEnd);

				// param kode toko
				dos.writeBytes(twoHyphens + boundary + lineEnd);
				dos.writeBytes("Content-Disposition: form-data; name=\""
						+ Parameter_Collections.TAG_KODE_TOKO + "\"" + lineEnd);
				dos.writeBytes(lineEnd);
				dos.writeBytes(kode_toko + lineEnd);

				Log.e("Content Form : ",
						"Content-Disposition: form-data; name=\""
								+ Parameter_Collections.TAG_KODE_TOKO + "\""
								+ lineEnd);

				// param id pegawai
				dos.writeBytes(twoHyphens + boundary + lineEnd);
				dos.writeBytes("Content-Disposition: form-data; name=\""
						+ Parameter_Collections.TAG_ID_PEGAWAI + "\"" + lineEnd);
				dos.writeBytes(lineEnd);
				dos.writeBytes(id_pegawai + lineEnd);

				Log.e("Content Form : ",
						"Content-Disposition: form-data; name=\""
								+ Parameter_Collections.TAG_ID_PEGAWAI + "\""
								+ lineEnd);

				// param latitude
				dos.writeBytes(twoHyphens + boundary + lineEnd);
				dos.writeBytes("Content-Disposition: form-data; name=\""
						+ Parameter_Collections.TAG_LAT_PRODUK + "\"" + lineEnd);
				dos.writeBytes(lineEnd);
				dos.writeBytes(spf.getString(
						Parameter_Collections.TAG_LATITUDE_NOW, "") + lineEnd);

				// param longitude
				dos.writeBytes(twoHyphens + boundary + lineEnd);
				dos.writeBytes("Content-Disposition: form-data; name=\""
						+ Parameter_Collections.TAG_LONG_PRODUK + "\""
						+ lineEnd);
				dos.writeBytes(lineEnd);
				dos.writeBytes(spf.getString(
						Parameter_Collections.TAG_LONGITUDE_NOW, "") + lineEnd);

				// param imei produk
				dos.writeBytes(twoHyphens + boundary + lineEnd);
				dos.writeBytes("Content-Disposition: form-data; name=\""
						+ Parameter_Collections.TAG_IMEI + "\"" + lineEnd);
				dos.writeBytes(lineEnd);
				dos.writeBytes(cImei + lineEnd);
				dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

				Log.e("IMEI", cImei);

				serverRespondCode = conn.getResponseCode();
				respondMessage = conn.getResponseMessage();

				Log.e("RESPOND", respondMessage);

				if (serverRespondCode == 200) {
					Log.e("CODE ", "Success Upload");
				} else {
					Log.e("CODE ", "Success failed");
				}

				fileInputStream.close();
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

			} catch (MalformedURLException ex) {
				ex.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return respondMessage;
		}
	}

}
