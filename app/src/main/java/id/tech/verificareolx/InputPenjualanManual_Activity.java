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

import android.app.ProgressDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.Toast;
import id.tech.verificareolx.R;
import id.tech.util.CustomAdapter_Img;
import id.tech.util.GPSTracker;
import id.tech.util.Parameter_Collections;
import id.tech.util.Public_Functions;

public class InputPenjualanManual_Activity extends ActionBarActivity {
	Button btn;
	String mUrl_Img_00, mUrl_Img_01, mUrl_Img_02, mUrl_Img_03;
	// String mUrl_Img_00;
	CustomAdapter_Img adapter;
	ImageView imgview_00, imgview_01, imgview_02, imgview_03;
	// ImageView imgview_00;
	public static int CODE_UPLOAD = 111;
	HorizontalScrollView horizontalScroll;
	EditText ed_imei;
	SharedPreferences spf;
	private String id_pegawai, kode_toko;
	int serverRespondCode = 0;
	private EditText ed_Harga, ed_tgl, ed_remarks;
	private Button btn_tgl;
	private String cHargaProduk, cRemarks, cTglTransaksi, cImei;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.activity_input_manual_penjualan);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle("Input Penjualan Manual");

		spf = getSharedPreferences(Parameter_Collections.SH_NAME,
				Context.MODE_PRIVATE);
		// update dialog pindah toko
		if (spf.getBoolean(Parameter_Collections.SH_PINDAH_TOKO, false)) {
			kode_toko = spf.getString(
					Parameter_Collections.SH_KODE_TOKO_SEMENTARA, "");
		} else {
			kode_toko = spf.getString(Parameter_Collections.SH_KODE_TOKO, "");
		}

		id_pegawai = spf.getString(Parameter_Collections.SH_ID_PEGAWAI, "");

		mUrl_Img_00 = null;
		mUrl_Img_01 = null;
		mUrl_Img_02 = null;
		mUrl_Img_03 = null;
		getAllView();
	}

	private void getAllView() {
		horizontalScroll = (HorizontalScrollView) findViewById(R.id.wrapper_horizontalview);
		imgview_00 = (ImageView) findViewById(R.id.img_00);
		imgview_01 = (ImageView) findViewById(R.id.img_01);
		imgview_02 = (ImageView) findViewById(R.id.img_02);
		imgview_03 = (ImageView) findViewById(R.id.img_03);

		ed_Harga = (EditText) findViewById(R.id.ed_harga);
		ed_tgl = (EditText) findViewById(R.id.ed_tgl);
		ed_remarks = (EditText) findViewById(R.id.ed_remarks);
		ed_imei = (EditText) findViewById(R.id.ed_imei);

		final OnDateSetListener listenerDate_mulai = new OnDateSetListener() {

			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {
				// TODO Auto-generated method stub
				cTglTransaksi = String.valueOf(year) + "-"
						+ String.valueOf(monthOfYear + 1) + "-"
						+ String.valueOf(dayOfMonth);
				ed_tgl.setText(cTglTransaksi);
				ed_tgl.setEnabled(false);
				ed_tgl.setVisibility(View.VISIBLE);
				btn_tgl.setVisibility(View.GONE);
			}
		};

		btn_tgl = (Button) findViewById(R.id.btn_tgl_penjualan);
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

		btn = (Button) findViewById(R.id.btn);

		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent upload = new Intent(getApplicationContext(),
						UploadImageDialog.class);
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
			Toast.makeText(getApplicationContext(), "Canceled. Images deleted",
					Toast.LENGTH_LONG).show();
			finish();
			break;

		case R.id.action_send_updatebranding:

			if (Public_Functions.isNetworkAvailable(getApplicationContext())) {
				// boolean b = true;
				// if (b) {
				new Async_SubmitPenjual().execute();
			} else {
				Toast.makeText(getApplicationContext(),
						"No Internet Connection, Cek Your Network",
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
				}

			}

		}

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		Public_Functions.delete_IssuePhoto();
		Toast.makeText(getApplicationContext(), "Canceled. Images deleted",
				Toast.LENGTH_LONG).show();
		finish();
	}

	private class Async_SubmitPenjual extends AsyncTask<Void, Void, String> {
		DialogFragmentProgress dialogProgress;
		String respondMessage;
		JSONObject jsonResult;
		String row_count, error_message;

		String url_file00, url_file01, url_file02, url_file03;
		File sourceFile00, sourceFile01, sourceFile02, sourceFile03;
		FileInputStream fileInputStream00, fileInputStream01,
				fileInputStream02, fileInputStream03;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			dialogProgress = new DialogFragmentProgress();
			dialogProgress.show(getSupportFragmentManager(), "");

			cHargaProduk = ed_Harga.getText().toString();
			cTglTransaksi = ed_tgl.getText().toString();
			cRemarks = ed_remarks.getText().toString();
			cImei = ed_imei.getText().toString();

		}

		@Override
		protected String doInBackground(Void... params) {
			// TODO Auto-generated method stub
			return uploadDataForm(mUrl_Img_00, mUrl_Img_01, mUrl_Img_02,
					mUrl_Img_03);

		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			JSONObject jObj = jsonResult;
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
				dialogProgress.dismiss();

				DialogLocationConfirmation dialog = new DialogLocationConfirmation();
				dialog.setContext(getApplicationContext());
				dialog.setText( "Input Penjualan Success");
				dialog.setFrom(9);
				dialog.setCancelable(false);
				dialog.show(getSupportFragmentManager(), "");

				// Toast.makeText(getApplicationContext(), result,
				// Toast.LENGTH_LONG).show();
				// Toast.makeText(getApplicationContext(),
				// "Input Penjualan Success", Toast.LENGTH_LONG).show();
				// finish();
			} else if (error_message.equals("produk tidak ada di database")) {
				Intent load = new Intent(getApplicationContext(),
						DialogFormPenjualanProduk.class);
				load.putExtra("ada_di_db", true);
				startActivity(load);
				finish();

			} else if (error_message
					.equals("produk tidak ada di dalam stok toko")) {
				dialogProgress.dismiss();

				DialogLocationConfirmation dialog = new DialogLocationConfirmation();
				dialog.setContext(getApplicationContext());
				dialog.setText( "Input Produk ini dahulu");
				dialog.setFrom(9);
				dialog.setCancelable(false);
				dialog.show(getSupportFragmentManager(), "");

				// Toast.makeText(getApplicationContext(),
				// "Input Produk ini dahulu", Toast.LENGTH_LONG).show();
				// finish();
			} else if (error_message.equals("produk sudah terjual")) {
				dialogProgress.dismiss();

				DialogLocationConfirmation dialog = new DialogLocationConfirmation();
				dialog.setContext(getApplicationContext());
				dialog.setText( "Produk sudah terjual");
				dialog.setFrom(9);
				dialog.setCancelable(false);
				dialog.show(getSupportFragmentManager(), "");

				// Toast.makeText(getApplicationContext(),
				// "Produk sudah terjual", Toast.LENGTH_LONG).show();
				// finish();
			}

		}

		private String uploadDataForm(String url_gambar00, String url_gambar01,
				String url_gambar02, String url_gambar03) {
			HttpURLConnection conn = null;
			DataOutputStream dos = null;
			String lineEnd = "\r\n";
			String twoHyphens = "--";
			String boundary = "*****";
			int bytesRead, bytesAvailable, bufferSize;
			byte[] buffer;
			int maxBufferSize = 1 * 1024 * 1024;

			if (url_gambar00 != null) {
				url_file00 = url_gambar00;
				sourceFile00 = new File(url_file00);
			}
			if (url_gambar01 != null) {
				url_file01 = url_gambar01;
				sourceFile01 = new File(url_file01);
			}
			if (url_gambar02 != null) {
				url_file02 = url_gambar02;
				sourceFile02 = new File(url_file02);
			}
			if (url_gambar03 != null) {
				url_file03 = url_gambar03;
				sourceFile03 = new File(url_file03);
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
				if (url_gambar00 != null) {
					conn.setRequestProperty("img0", url_file00);
				}
				if (url_gambar01 != null) {
					conn.setRequestProperty("img1", url_file01);
				}
				if (url_gambar02 != null) {
					conn.setRequestProperty("img2", url_file02);
				}
				if (url_gambar03 != null) {
					conn.setRequestProperty("img3", url_file03);
				}

				dos = new DataOutputStream(conn.getOutputStream());
				if (url_gambar00 != null) {
					fileInputStream00 = new FileInputStream(sourceFile00);
					// img 00
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
						bytesRead = fileInputStream00.read(buffer, 0,
								bufferSize);
					}

					dos.writeBytes(lineEnd);
					dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

				}
				if (url_gambar01 != null) {
					fileInputStream01 = new FileInputStream(sourceFile01);
					// img 01
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
						bytesRead = fileInputStream01.read(buffer, 0,
								bufferSize);
					}

					dos.writeBytes(lineEnd);
					dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

				}
				if (url_gambar02 != null) {
					fileInputStream02 = new FileInputStream(sourceFile02);
					// img 02
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
						bytesRead = fileInputStream02.read(buffer, 0,
								bufferSize);
					}

					dos.writeBytes(lineEnd);
					dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
				}
				if (url_gambar03 != null) {
					fileInputStream03 = new FileInputStream(sourceFile03);
					// img 03
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
						bytesRead = fileInputStream03.read(buffer, 0,
								bufferSize);
					}

					dos.writeBytes(lineEnd);
					dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

				}

				// param kind
				dos.writeBytes(twoHyphens + boundary + lineEnd);
				dos.writeBytes("Content-Disposition: form-data; name=\""
						+ Parameter_Collections.KIND + "\"" + lineEnd);
				dos.writeBytes(lineEnd);
				dos.writeBytes(Parameter_Collections.KIND_PENJUALAN_PRODUKTOKO
						+ lineEnd);

				Log.e("Content Form : ",
						"Content-Disposition: form-data; name=\""
								+ Parameter_Collections.KIND_PENJUALAN_PRODUKTOKO
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

				// param harga produk keluar
				dos.writeBytes(twoHyphens + boundary + lineEnd);
				dos.writeBytes("Content-Disposition: form-data; name=\""
						+ Parameter_Collections.TAG_PENJUALAN_HARGA_PRODUK
						+ "\"" + lineEnd);
				dos.writeBytes(lineEnd);
				dos.writeBytes(cHargaProduk + lineEnd);

				Log.e("Content Form : ",
						"Content-Disposition: form-data; name=\""
								+ Parameter_Collections.TAG_PENJUALAN_HARGA_PRODUK
								+ "\"" + lineEnd);

				// param REMARKS
				dos.writeBytes(twoHyphens + boundary + lineEnd);
				dos.writeBytes("Content-Disposition: form-data; name=\""
						+ Parameter_Collections.TAG_REMARKS + "\"" + lineEnd);
				dos.writeBytes(lineEnd);
				dos.writeBytes(cRemarks + lineEnd);

				Log.e("Content Form : ",
						"Content-Disposition: form-data; name=\""
								+ Parameter_Collections.TAG_REMARKS + "\""
								+ lineEnd);

				// param TGL PENUALAN
				dos.writeBytes(twoHyphens + boundary + lineEnd);
				dos.writeBytes("Content-Disposition: form-data; name=\""
						+ Parameter_Collections.TAG_TGL_PENJUALAN_PRODUK + "\""
						+ lineEnd);
				dos.writeBytes(lineEnd);
				dos.writeBytes(cTglTransaksi + lineEnd);

				Log.e("Content Form : ",
						"Content-Disposition: form-data; name=\""
								+ Parameter_Collections.TAG_REMARKS + "\""
								+ lineEnd);

				// param latitude
				dos.writeBytes(twoHyphens + boundary + lineEnd);
				dos.writeBytes("Content-Disposition: form-data; name=\""
						+ Parameter_Collections.TAG_LAT_PRODUK_KELUAR + "\""
						+ lineEnd);
				dos.writeBytes(lineEnd);
				dos.writeBytes(spf.getString(
						Parameter_Collections.TAG_LATITUDE_NOW, "") + lineEnd);

				// param longitude
				dos.writeBytes(twoHyphens + boundary + lineEnd);
				dos.writeBytes("Content-Disposition: form-data; name=\""
						+ Parameter_Collections.TAG_LONG_PRODUK_KELUAR + "\""
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

				Log.e("Content Form : ",
						"Content-Disposition: form-data; name=\""
								+ Parameter_Collections.TAG_IMEI + "\""
								+ lineEnd);

				serverRespondCode = conn.getResponseCode();
				respondMessage = conn.getResponseMessage();

				Log.e("RESPOND", respondMessage);

				if (serverRespondCode == 200) {
					Log.e("CODE ", "Success Upload");
				} else {
					Log.e("CODE ", "Success failed");
				}

				if (url_gambar00 != null) {
					fileInputStream00.close();
				}
				if (url_gambar01 != null) {
					fileInputStream01.close();
				}
				if (url_gambar02 != null) {
					fileInputStream02.close();
				}
				if (url_gambar03 != null) {
					fileInputStream03.close();
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

			} catch (MalformedURLException ex) {
				ex.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return respondMessage;
		}
	}

}
