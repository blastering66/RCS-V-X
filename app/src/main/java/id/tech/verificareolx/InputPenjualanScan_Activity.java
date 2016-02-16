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
import id.tech.util.Parameter_Collections;
import id.tech.util.Public_Functions;

public class InputPenjualanScan_Activity extends ActionBarActivity {
	Button btn;
	String mUrl_Img_00, mUrl_Img_01, mUrl_Img_02, mUrl_Img_03;
	// String mUrl_Img_00;
	CustomAdapter_Img adapter;
	ImageView imgview_00, imgview_01, imgview_02, imgview_03;
	// ImageView imgview_00;
	public static int CODE_UPLOAD = 111;
	HorizontalScrollView horizontalScroll;

	SharedPreferences spf;
	private String id_pegawai, kode_toko;
	int serverRespondCode = 0;
	private EditText ed_Harga, ed_tgl, ed_remarks;
	private Button btn_tgl;
	private String cHargaProduk, cRemarks, cTglTransaksi;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.activity_input_scan_penjualan);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle("Input Penjualan Scanbarcode");

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

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			dialogProgress = new DialogFragmentProgress();
			dialogProgress.show(getSupportFragmentManager(), "");

			cHargaProduk = ed_Harga.getText().toString();
			cTglTransaksi = ed_tgl.getText().toString();
			cRemarks = ed_remarks.getText().toString();

		}

		@Override
		protected String doInBackground(Void... params) {
			// TODO Auto-generated method stub
			// return uploadDataForm(mUrl_Img_00);
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			dialogProgress.dismiss();

			Intent load = new Intent(getApplicationContext(),
					ScanPenjualan_Activity.class);
			load.putExtra(Parameter_Collections.TAG_HARGA_PRODUK_KELUAR,
					cHargaProduk);
			load.putExtra(Parameter_Collections.EXTRA_REMARKS, cRemarks);
			load.putExtra(Parameter_Collections.EXTRA_TGL_TRANSAKSI,
					cTglTransaksi);
			load.putExtra(Parameter_Collections.EXTRA_IMG_00, mUrl_Img_00);
			load.putExtra(Parameter_Collections.EXTRA_IMG_01, mUrl_Img_01);
			load.putExtra(Parameter_Collections.EXTRA_IMG_02, mUrl_Img_02);
			load.putExtra(Parameter_Collections.EXTRA_IMG_03, mUrl_Img_03);

			startActivity(load);
			finish();
		}
	}

}
