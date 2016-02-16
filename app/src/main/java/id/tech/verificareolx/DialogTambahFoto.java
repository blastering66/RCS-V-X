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
import android.app.ProgressDialog;
import android.content.Intent;
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
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.Toast;

public class DialogTambahFoto extends FragmentActivity {
	Button btn, btn_submit;
	String mUrl_Img_00, mUrl_Img_01, mUrl_Img_02, mUrl_Img_03;
	ImageView imgview_00, imgview_01, imgview_02, imgview_03;
	public static int CODE_UPLOAD = 111;
	HorizontalScrollView horizontalScroll;

	int serverRespondCode = 0;
	byte[] dataX;
	long totalSize = 0;
	String id_toko;
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.layout_dialog_tambahfoto);
		
		id_toko = getIntent().getStringExtra("id_toko");

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
		
		btn_submit = (Button)findViewById(R.id.btn_submit);
		btn_submit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new Async_TambahFoto().execute();
			}
		});
	}
	
	private class Async_TambahFoto extends AsyncTask<Void, Void, String> {
		ProgressDialog pdialog;
		String respondMessage;
		JSONObject jsonResult;
		DialogFragmentProgress dialogProgress;
		int serverRespondCode = 0;
		
		String url_file00, url_file01, url_file02, url_file03;
		File sourceFile00,sourceFile01, sourceFile02, sourceFile03;
		FileInputStream fileInputStream00,fileInputStream01, fileInputStream02, fileInputStream03;
		private String row_count, error_message;
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			dialogProgress = new DialogFragmentProgress();
			dialogProgress.show(getSupportFragmentManager(), "");
		}
		
		@Override
		protected String doInBackground(Void... params) {
			// TODO Auto-generated method stub
			return uploadDataForm(mUrl_Img_00, mUrl_Img_01, mUrl_Img_02, mUrl_Img_03);
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
				finish();
				Toast.makeText(getApplicationContext(), "Upload Foto Success", Toast.LENGTH_LONG).show();
			}else{
				Toast.makeText(getApplicationContext(), error_message, Toast.LENGTH_LONG).show();
			}
			
		}
		
		private String uploadDataForm(String url_gambar00, String url_gambar01,
				String url_gambar02, String url_gambar03){
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
					
				}	
				
				
				
				// param kind
				dos.writeBytes(twoHyphens + boundary + lineEnd);
				dos.writeBytes("Content-Disposition: form-data; name=\""
						+ Parameter_Collections.KIND + "\"" + lineEnd);
				dos.writeBytes(lineEnd);
				dos.writeBytes(Parameter_Collections.KIND_TAMBAHFOTO_TOKO+ lineEnd);

				
				// param kode toko
				dos.writeBytes(twoHyphens + boundary + lineEnd);
				dos.writeBytes("Content-Disposition: form-data; name=\""
						+ Parameter_Collections.TAG_ID_TOKO + "\"" + lineEnd);
				dos.writeBytes(lineEnd);
				dos.writeBytes(id_toko + lineEnd);				
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
	

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (resultCode == RESULT_OK) {

			if (requestCode == CODE_UPLOAD) {

				if (mUrl_Img_00 == null) {
//					horizontalScroll.setVisibility(View.VISIBLE);
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
}
