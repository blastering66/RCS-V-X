package id.tech.verificareolx;

import id.tech.util.Parameter_Collections;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class Olx_DialogAbsen extends FragmentActivity {
	Button radio_masuk, radio_pulang;
	Button radio_istirahat_mulai, radio_istirahat_selesai;
	SharedPreferences sh;
	private int CODE_FOTO_ABSEN_MASUK= 889;
	private int CODE_FOTO_ABSEN_PULANG= 888;
	private String mUrl_Img;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.layout_dialog_absen);
		sh = getSharedPreferences(Parameter_Collections.SH_NAME,
				Context.MODE_PRIVATE);

		radio_masuk = (Button) findViewById(R.id.radio_masuk);
		radio_pulang = (Button) findViewById(R.id.radio_pulang);
		
		radio_istirahat_mulai = (Button) findViewById(R.id.radio_out_istirahat);
		radio_istirahat_selesai = (Button) findViewById(R.id.radio_in_istirahat);

		radio_pulang.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (sh.getBoolean(Parameter_Collections.SH_ABSENTED, false)) {

					//OLX
					Intent load = new Intent(getApplicationContext(),
							Olx_DialogNamaOutlet.class);
					load.putExtra(Parameter_Collections.SH_ID_PEGAWAI, sh
							.getString(Parameter_Collections.SH_ID_PEGAWAI,
									""));
					load.putExtra(Parameter_Collections.EXTRA_ABSENSI, "2");
					startActivity(load);
					finish();

//					Intent intent = new Intent();
//					intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
//					try {
//
//						intent.putExtra("return-data", true);
//						intent.putExtra(MediaStore.EXTRA_OUTPUT, "1");
//						startActivityForResult(Intent.createChooser(intent,
//								"Complete action using"), CODE_FOTO_ABSEN_PULANG);
//
//					} catch (ActivityNotFoundException e) {
//					}
				} else {
					Toast.makeText(getApplicationContext(),
							"Please Login First", Toast.LENGTH_LONG).show();
				}

			}
		});

		radio_masuk.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!sh.getBoolean(Parameter_Collections.SH_ABSENTED, false)) {

					//OLX
					Intent load = new Intent(getApplicationContext(),
							Olx_DialogNamaOutlet.class);
					load.putExtra(Parameter_Collections.SH_ID_PEGAWAI, sh
							.getString(Parameter_Collections.SH_ID_PEGAWAI,
									""));
					load.putExtra(Parameter_Collections.EXTRA_ABSENSI, "1");
					startActivity(load);
					finish();

//					Intent intent = new Intent();
//					intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
//					try {
//
//						intent.putExtra("return-data", true);
//						intent.putExtra(MediaStore.EXTRA_OUTPUT, "1");
//						startActivityForResult(Intent.createChooser(intent,
//								"Complete action using"), CODE_FOTO_ABSEN_MASUK);
//					} catch (ActivityNotFoundException e) {
//					}
//				} else {
//					Toast.makeText(getApplicationContext(),
//							"Please Logout First", Toast.LENGTH_LONG).show();
				}
			}
		});
		

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == RESULT_OK){
			if(requestCode == CODE_FOTO_ABSEN_MASUK){
				Bundle extras2 = data.getExtras();
				if (extras2 != null) {

					Bitmap photo = extras2.getParcelable("data");
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					byte[] ba = baos.toByteArray();
					mUrl_Img = SaveImage(photo);

					Toast.makeText(getApplicationContext(),
							"Saved to : " + mUrl_Img, Toast.LENGTH_LONG).show();

					Parameter_Collections.URL_FOTO_ABSEN = mUrl_Img;


					//OLX
					Intent load = new Intent(getApplicationContext(),
							Olx_DialogNamaOutlet.class);
					load.putExtra(Parameter_Collections.SH_ID_PEGAWAI, sh
							.getString(Parameter_Collections.SH_ID_PEGAWAI,
									""));
					load.putExtra(Parameter_Collections.EXTRA_ABSENSI, "1");
					startActivity(load);
					finish();
				}

			}else if(requestCode == CODE_FOTO_ABSEN_PULANG){
				Bundle extras2 = data.getExtras();
				if (extras2 != null) {

					Bitmap photo = extras2.getParcelable("data");
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					byte[] ba = baos.toByteArray();
					mUrl_Img = SaveImage(photo);

					Toast.makeText(getApplicationContext(),
							"Saved to : " + mUrl_Img, Toast.LENGTH_LONG).show();

					Parameter_Collections.URL_FOTO_ABSEN = mUrl_Img;

					//OLX
					Intent load = new Intent(getApplicationContext(),
							Olx_DialogNamaOutlet.class);
					load.putExtra(Parameter_Collections.SH_ID_PEGAWAI, sh
							.getString(Parameter_Collections.SH_ID_PEGAWAI,
									""));
					load.putExtra(Parameter_Collections.EXTRA_ABSENSI, "2");
					startActivity(load);


					finish();
				}
			}

		}
	}

	private String SaveImage(Bitmap finalBitmap) {

		if (Environment.isExternalStorageEmulated()) {
			String root = Environment.getExternalStorageDirectory().toString();
			File myDir = new File(root + Parameter_Collections.URL_FOLDER_IMG_ABSENSI);
			myDir.mkdirs();
			Random generator = new Random();
			int n = 10000;
			n = generator.nextInt(n);

			Calendar c = Calendar.getInstance();
			Date date = c.getTime();
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd_HHmmss");

			 String namafile = "absen_pic";
//			String namafile = format.format(date);
//			String fname = kode_toko + "_" + namafile + ".jpg";
			String fname = namafile + ".jpg";
			File file = new File(myDir, fname);
			if (file.exists())
				file.delete();
			try {
				FileOutputStream out = new FileOutputStream(file);
//				finalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
				finalBitmap.compress(Bitmap.CompressFormat.PNG, 90, out);;
				out.flush();
				out.close();

			} catch (Exception e) {
				e.printStackTrace();
			}

			return file.getAbsolutePath();
		} else {

			String root = Environment.getExternalStorageDirectory().toString();
			File myDir = new File(root + Parameter_Collections.URL_FOLDER_IMG_ABSENSI);
			myDir.mkdirs();
			Random generator = new Random();
			int n = 10000;
			n = generator.nextInt(n);

			Calendar c = Calendar.getInstance();
			Date date = c.getTime();
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd_HHmmss");

			String namafile = "absen_pic";
//			String namafile = format.format(date);
//			String fname = kode_toko + "_" + namafile + ".jpg";
			String fname = namafile + ".jpg";
			File file = new File(myDir, fname);
			if (file.exists())
				file.delete();
			try {
				FileOutputStream out = new FileOutputStream(file);
//				finalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
				finalBitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
				out.flush();
				out.close();

			} catch (Exception e) {
				e.printStackTrace();
			}

			return file.getAbsolutePath();
		}

	}
}
