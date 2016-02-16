package id.tech.verificareolx;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import id.tech.verificareolx.R;
import id.tech.util.Parameter_Collections;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class Olx_UploadImageDialog extends Activity {
	private static final int PICK_FROM_GALLERY = 2;
	private static final int PICK_FROM_CAMERA = 1;

	Button btn_camera, btn_gallery, btn;
	ImageView img_Nota;
	View wrapper_img;
	String cNotaBuktiPembayaran, mUrl_Img;
	SharedPreferences spf;
	private String kode_toko, id_pegawai;
	Bitmap new_photo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_upload_foto);
		
		
		spf = getSharedPreferences(Parameter_Collections.SH_NAME, Context.MODE_PRIVATE);
		kode_toko = spf.getString(Parameter_Collections.SH_KODE_TOKO, "");
		id_pegawai = spf.getString(Parameter_Collections.SH_ID_PEGAWAI, "");
		
		btn = (Button) findViewById(R.id.btn);
		btn_camera = (Button) findViewById(R.id.btn_from_camera);
		btn_gallery = (Button) findViewById(R.id.btn_from_gallery);

		wrapper_img = (View) findViewById(R.id.wrapper_img);

		img_Nota = (ImageView) findViewById(R.id.img);

		btn_camera.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent intent = new Intent();
				intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);

				try {

					intent.putExtra("return-data", true);
					intent.putExtra(MediaStore.EXTRA_OUTPUT, "1");
					startActivityForResult(Intent.createChooser(intent,
							"Complete action using"), PICK_FROM_CAMERA);

				} catch (ActivityNotFoundException e) {
					// Do nothing for now
				}
			}
		});

		btn_gallery.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				// call android default gallery
				intent.setType("image/*");
				intent.setAction(Intent.ACTION_GET_CONTENT);
				// ******** code for crop image
				intent.putExtra("crop", "true");
				intent.putExtra("scale", "true");
				intent.putExtra("aspectX", 3);
				intent.putExtra("aspectY", 4);
				intent.putExtra("outputX", 900);
				intent.putExtra("outputY", 1200);

				try {

					intent.putExtra("return-data", true);
					startActivityForResult(Intent.createChooser(intent,
							"Complete action using"), PICK_FROM_GALLERY);

				} catch (ActivityNotFoundException e) {
					// Do nothing for now
				}

			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (resultCode == RESULT_OK) {

			if (requestCode == PICK_FROM_GALLERY) {
				Bundle extras2 = data.getExtras();
				if (extras2 != null) {
					Bitmap photo = extras2.getParcelable("data");
					img_Nota.setImageBitmap(photo);
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
//					photo.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//					photo.compress(Bitmap.CompressFormat.PNG, 90, baos);
					byte[] ba = baos.toByteArray();
					cNotaBuktiPembayaran = Base64.encodeToString(ba,
							Base64.DEFAULT);

//					int w = photo.getWidth();
//					 mUrl_Img = SaveImage(photo);
//					int h = photo.getHeight();
//
//					float bitmapRatio = (float)w/(float)h;
//					if(bitmapRatio >0){
//						w = w / 2;
//						h = (int)(w/bitmapRatio);
//					}else{
//						h = h/2;
//						w = (int)(h * bitmapRatio);
//					}
//					new_photo = Bitmap.createScaledBitmap(photo,w, h, true);
					mUrl_Img = SaveImage(photo);

					Toast.makeText(getApplicationContext(),
							"Saved to : " + mUrl_Img, Toast.LENGTH_LONG).show();

					wrapper_img.setVisibility(View.VISIBLE);
					img_Nota.setVisibility(View.VISIBLE);

					Intent intent_result = new Intent();
					intent_result.putExtra("mUrl_Img", mUrl_Img);
					this.setResult(RESULT_OK, intent_result);
					finish();

				}
			}
			if (requestCode == PICK_FROM_CAMERA) {
				Bundle extras2 = data.getExtras();
				if (extras2 != null) {
					Bitmap photo = extras2.getParcelable("data");
					img_Nota.setImageBitmap(photo);
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
//					photo.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//					photo.compress(Bitmap.CompressFormat.PNG, 90, baos);
					byte[] ba = baos.toByteArray();
					cNotaBuktiPembayaran = Base64.encodeToString(ba,
							Base64.DEFAULT);

//					int w = photo.getWidth();
//					int h = photo.getHeight();
//
//					float bitmapRatio = (float)w/(float)h;
//					if(bitmapRatio >0){
//						w = w / 2;
//						h = (int)(w/bitmapRatio);
//					}else{
//						h = h/2;
//						w = (int)(h * bitmapRatio);
//					}
//					new_photo = Bitmap.createScaledBitmap(photo, w, h, true);
//					mUrl_Img = SaveImage(new_photo);
					 mUrl_Img = SaveImage(photo);

					Toast.makeText(getApplicationContext(),
							"Saved to : " + mUrl_Img, Toast.LENGTH_LONG).show();

					wrapper_img.setVisibility(View.VISIBLE);
					img_Nota.setVisibility(View.VISIBLE);

					Intent intent_result = new Intent();
					intent_result.putExtra("mUrl_Img", mUrl_Img);
					this.setResult(RESULT_OK, intent_result);
					finish();

				}

			}

		}
	}

	private String SaveImage(Bitmap finalBitmap) {

		if (Environment.isExternalStorageEmulated()) {
			String root = Environment.getExternalStorageDirectory().toString();
			File myDir = new File(root + Parameter_Collections.URL_FOLDER_IMG_ISSUE); 
			myDir.mkdirs();
			Random generator = new Random();
			int n = 10000;
			n = generator.nextInt(n);

			Calendar c = Calendar.getInstance();
			Date date = c.getTime();
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd_HHmmss");

			// String namafile = "temp";
			String namafile = format.format(date);

			String fname = kode_toko + "_" + namafile + ".jpg";
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
			File myDir = new File(root + Parameter_Collections.URL_FOLDER_IMG_ISSUE);
			myDir.mkdirs();
			Random generator = new Random();
			int n = 10000;
			n = generator.nextInt(n);

			Calendar c = Calendar.getInstance();
			Date date = c.getTime();
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd_HHmmss");

			// String namafile = "temp";
			String namafile = format.format(date);

			String fname = kode_toko + "_" + namafile + ".jpg";
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
