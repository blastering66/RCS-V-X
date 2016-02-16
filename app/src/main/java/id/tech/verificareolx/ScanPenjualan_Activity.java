package id.tech.verificareolx;


import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.Random;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.google.zxing.ResultPoint;
import com.jwetherell.quick_response_code.DecoderActivity;
import com.jwetherell.quick_response_code.DecoderActivityHandler;
import com.jwetherell.quick_response_code.IDecoderActivity;
import com.jwetherell.quick_response_code.ViewfinderView;
import com.jwetherell.quick_response_code.camera.CameraManager;
import com.jwetherell.quick_response_code.result.ResultHandler;
import com.jwetherell.quick_response_code.result.ResultHandlerFactory;
import id.tech.verificareolx.R;
import id.tech.util.GPSTracker;
import id.tech.util.Parameter_Collections;
import id.tech.util.Public_Functions;

public class ScanPenjualan_Activity extends FragmentActivity implements IDecoderActivity, SurfaceHolder.Callback{
	private static final String TAG = DecoderActivity.class.getSimpleName();

	protected DecoderActivityHandler handler = null;
	protected ViewfinderView viewfinderView = null;
	protected CameraManager cameraManager = null;
	protected boolean hasSurface = false;
	protected Collection<BarcodeFormat> decodeFormats = null;
	protected String characterSet = null;
	String code, cUrlImage, cUrlImage00,cUrlImage01,cUrlImage02,cUrlImage03;
	SharedPreferences spf;
	private String kode_toko, id_pegawai;
	int serverRespondCode = 0;
	byte[] dataX;
	long totalSize = 0;
	String cHargaProduk, cRemarks, cTglTransaksi;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_scan);

		spf = getSharedPreferences(Parameter_Collections.SH_NAME, Context.MODE_PRIVATE);

		//update dialog pindah toko
		if(spf.getBoolean(Parameter_Collections.SH_PINDAH_TOKO, false)){
			kode_toko = spf.getString(Parameter_Collections.SH_KODE_TOKO_SEMENTARA, "");
		}else{
			kode_toko = spf.getString(Parameter_Collections.SH_KODE_TOKO, "");
		}

		id_pegawai = spf.getString(Parameter_Collections.SH_ID_PEGAWAI, "");

		cHargaProduk = getIntent().getStringExtra(Parameter_Collections.TAG_HARGA_PRODUK_KELUAR);
		cRemarks = getIntent().getStringExtra(Parameter_Collections.EXTRA_REMARKS);
		cTglTransaksi = getIntent().getStringExtra(Parameter_Collections.EXTRA_TGL_TRANSAKSI);

		cUrlImage00 = getIntent().getStringExtra(Parameter_Collections.EXTRA_IMG_00);
		cUrlImage01 = getIntent().getStringExtra(Parameter_Collections.EXTRA_IMG_01);
		cUrlImage02 = getIntent().getStringExtra(Parameter_Collections.EXTRA_IMG_02);
		cUrlImage03 = getIntent().getStringExtra(Parameter_Collections.EXTRA_IMG_03);

		Toast.makeText(getApplicationContext(), cHargaProduk, Toast.LENGTH_SHORT).show();
		Toast.makeText(getApplicationContext(), cRemarks, Toast.LENGTH_SHORT).show();
		Toast.makeText(getApplicationContext(), cTglTransaksi, Toast.LENGTH_SHORT).show();

		Window window = getWindow();
		window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		handler = null;
		hasSurface = false;
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (cameraManager == null)
			cameraManager = new CameraManager(getApplication());

		if (viewfinderView == null) {
			viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
			viewfinderView.setCameraManager(cameraManager);
		}

		showScanner();

		SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
		SurfaceHolder surfaceHolder = surfaceView.getHolder();
		if (hasSurface) {
			// The activity was paused but not stopped, so the surface still
			// exists. Therefore
			// surfaceCreated() won't be called, so init the camera here.
			initCamera(surfaceHolder);
		} else {
			// Install the callback and wait for surfaceCreated() to init the
			// camera.
			surfaceHolder.addCallback(this);
			surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if (handler != null) {
			handler.quitSynchronously();
			handler = null;
		}

		cameraManager.closeDriver();

		if (!hasSurface) {
			SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
			SurfaceHolder surfaceHolder = surfaceView.getHolder();
			surfaceHolder.removeCallback(this);
		}

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		if (holder == null)
			Log.e(TAG,
					"*** WARNING *** surfaceCreated() gave us a null surface!");
		if (!hasSurface) {
			hasSurface = true;
			initCamera(holder);
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
							   int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		hasSurface = false;
	}

	@Override
	public ViewfinderView getViewfinder() {
		// TODO Auto-generated method stub
		return viewfinderView;
	}

	@Override
	public Handler getHandler() {
		// TODO Auto-generated method stub
		return handler;
	}

	@Override
	public CameraManager getCameraManager() {
		// TODO Auto-generated method stub
		return cameraManager;
	}

	@Override
	public void handleDecode(Result rawResult, Bitmap barcode) {
		// TODO Auto-generated method stub
		drawResultPoints(barcode, rawResult);
		ResultHandler resultHandler = ResultHandlerFactory.makeResultHandler(
				this, rawResult);
		code = resultHandler.getDisplayContents().toString();
		Log.d("code", code);


//		Intent load = new Intent(getApplicationContext(), Hasil.class);
//		Bitmap b = barcode;
//		ByteArrayOutputStream bs = new ByteArrayOutputStream();
//		b.compress(Bitmap.CompressFormat.PNG, 50, bs);
//		load.putExtra("byte", bs.toByteArray());
//		startActivity(load);

		Toast.makeText(getApplicationContext(), code, Toast.LENGTH_LONG).show();

		cUrlImage =  SaveImage(barcode, rawResult.getText(), rawResult.getTimestamp());

//		Intent intent_result = new Intent();
//		intent_result.putExtra(ParameterCollections.SH_BARCODE_ARRAY, code);
//		setResult(RESULT_OK, intent_result);

//		Intent result = new Intent();
//		result.putExtra("xxx", "xxx");
//		setResult(RESULT_OK, result);
		Parameter_Collections.ARRAY = rawResult.getText();


		if (Public_Functions.isNetworkAvailable(getApplicationContext())) {
			new Async_SubmitPenjual().execute();
		}else {
			Toast.makeText(getApplicationContext(),
					"No Internet Connection, Cek Your Network",
					Toast.LENGTH_LONG).show();
		}

	}

	private class Async_SubmitPenjual extends AsyncTask<Void, Void, String>{
		DialogFragmentProgress dialogProgress;
		String respondMessage;
		JSONObject jsonResult;
		String row_count, error_message;

		String url_file,url_file00, url_file01, url_file02, url_file03;
		File sourceFile ,sourceFile00,sourceFile01, sourceFile02, sourceFile03;
		FileInputStream fileInputStream, fileInputStream00,fileInputStream01, fileInputStream02, fileInputStream03;


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
			return uploadDataForm(cUrlImage, cUrlImage00, cUrlImage01,cUrlImage02,cUrlImage03);

		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			dialogProgress.dismiss();
			JSONObject jObj = jsonResult;
			try{
				row_count = jObj.getString(Parameter_Collections.TAG_ROWCOUNT);

			}catch(JSONException e){
				row_count = "0";
				try{
					error_message = jObj.getString(Parameter_Collections.TAG_JSON_ERROR_MESSAGE);
				}catch(JSONException e2){

				}
			}

			if(row_count.equals("1")){


				DialogLocationConfirmation dialog = new DialogLocationConfirmation();
				dialog.setContext(getApplicationContext());
				dialog.setText("Input Penjualan Success");
				dialog.setFrom(9);
				dialog.setCancelable(false);
				dialog.show(getSupportFragmentManager(), "");

//				Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
//				Toast.makeText(getApplicationContext(), "Input Penjualan Success", Toast.LENGTH_LONG).show();
//				finish();
			}else if(error_message.equals("produk tidak ada di database")){
				Intent load = new Intent(getApplicationContext(), DialogFormPenjualanProduk.class);
				load.putExtra("ada_di_db", true);
				startActivity(load);
				finish();

			}else if(error_message.equals("produk tidak ada di dalam stok toko")){
				dialogProgress.dismiss();

				DialogLocationConfirmation dialog = new DialogLocationConfirmation();
				dialog.setContext(getApplicationContext());
				dialog.setText("Input Produk ini dahulu");
				dialog.setFrom(9);
				dialog.setCancelable(false);
				dialog.show(getSupportFragmentManager(), "");

//				Toast.makeText(getApplicationContext(), "Input Produk ini dahulu", Toast.LENGTH_LONG).show();
//				finish();
			}else if(error_message.equals("produk sudah terjual")){
				dialogProgress.dismiss();

				DialogLocationConfirmation dialog = new DialogLocationConfirmation();
				dialog.setContext(getApplicationContext());
				dialog.setText("Produk sudah terjual");
				dialog.setFrom(9);
				dialog.setCancelable(false);
				dialog.show(getSupportFragmentManager(), "");

//				Toast.makeText(getApplicationContext(), "Produk sudah terjual", Toast.LENGTH_LONG).show();
//				finish();
			}



		}
		private String uploadDataForm(String url_gambar,String url_gambar00, String url_gambar01,
									  String url_gambar02, String url_gambar03){
			HttpURLConnection conn = null;
			DataOutputStream dos = null;
			String lineEnd = "\r\n";
			String twoHyphens = "--";
			String boundary = "*****";
			int bytesRead, bytesAvailable, bufferSize;
			byte[] buffer;
			int maxBufferSize = 1 * 1024 * 1024;

			url_file = url_gambar;
			sourceFile = new File(url_file);

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
				dos.writeBytes(Parameter_Collections.KIND_PENJUALAN_PRODUKTOKO+ lineEnd);

				Log.e("Content Form : ",
						"Content-Disposition: form-data; name=\""
								+ Parameter_Collections.KIND_PENJUALAN_PRODUKTOKO + "\"" + lineEnd);

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
						+ Parameter_Collections.TAG_PENJUALAN_HARGA_PRODUK + "\"" + lineEnd);
				dos.writeBytes(lineEnd);
				dos.writeBytes(cHargaProduk + lineEnd);

				Log.e("Content Form : ",
						"Content-Disposition: form-data; name=\""
								+ Parameter_Collections.TAG_PENJUALAN_HARGA_PRODUK + "\""
								+ lineEnd);

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
						+ Parameter_Collections.TAG_TGL_PENJUALAN_PRODUK+ "\"" + lineEnd);
				dos.writeBytes(lineEnd);
				dos.writeBytes(cTglTransaksi + lineEnd);

				Log.e("Content Form : ",
						"Content-Disposition: form-data; name=\""
								+ Parameter_Collections.TAG_REMARKS + "\""
								+ lineEnd);


				// param latitude
				dos.writeBytes(twoHyphens + boundary + lineEnd);
				dos.writeBytes("Content-Disposition: form-data; name=\""
						+ Parameter_Collections.TAG_LAT_PRODUK_KELUAR + "\"" + lineEnd);
				dos.writeBytes(lineEnd);
				dos.writeBytes(spf.getString(Parameter_Collections.TAG_LATITUDE_NOW, "") + lineEnd);

				// param longitude
				dos.writeBytes(twoHyphens + boundary + lineEnd);
				dos.writeBytes("Content-Disposition: form-data; name=\""
						+ Parameter_Collections.TAG_LONG_PRODUK_KELUAR + "\"" + lineEnd);
				dos.writeBytes(lineEnd);
				dos.writeBytes(spf.getString(Parameter_Collections.TAG_LONGITUDE_NOW, "") + lineEnd);

				// param imei produk
				dos.writeBytes(twoHyphens + boundary + lineEnd);
				dos.writeBytes("Content-Disposition: form-data; name=\""
						+ Parameter_Collections.TAG_IMEI + "\"" + lineEnd);
				dos.writeBytes(lineEnd);
				dos.writeBytes(code + lineEnd);
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

				fileInputStream.close();

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

	private String SaveImage(Bitmap finalBitmap, String kode_barcode, long time_stamp) {

		if(Environment.isExternalStorageEmulated()){
			String root = Environment.getExternalStorageDirectory().toString();
			File myDir = new File(root +Parameter_Collections.URL_FOLDER_IMG_PENJUALAN);
			myDir.mkdirs();
			Random generator = new Random();
			int n = 10000;
			n = generator.nextInt(n);

			String namafile = "";

			String fname = namafile + "_" + kode_barcode + "_" +String.valueOf(time_stamp) +".jpg";
			File file = new File (myDir, fname);
			if (file.exists ()) file.delete ();
			try {
				FileOutputStream out = new FileOutputStream(file);
				finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
				out.flush();
				out.close();

			} catch (Exception e) {
				e.printStackTrace();
			}
			return file.getAbsolutePath();
		}else{

			String root = Environment.getExternalStorageDirectory().toString();
			File myDir = new File(root + Parameter_Collections.URL_FOLDER_IMG_PENJUALAN);
			myDir.mkdirs();
			Random generator = new Random();
			int n = 10000;
			n = generator.nextInt(n);

			String namafile = "";

			String fname = namafile + "_" + kode_barcode + "_" +String.valueOf(time_stamp) +".jpg";
			File file = new File (myDir, fname);
			if (file.exists ()) file.delete ();
			try {
				FileOutputStream out = new FileOutputStream(file);
				finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
				out.flush();
				out.close();

			} catch (Exception e) {
				e.printStackTrace();
			}
			return file.getAbsolutePath();
		}

	}

	protected void drawResultPoints(Bitmap barcode, Result rawResult) {
		ResultPoint[] points = rawResult.getResultPoints();
		if (points != null && points.length > 0) {
			Canvas canvas = new Canvas(barcode);
			Paint paint = new Paint();
			paint.setColor(getResources().getColor(R.color.result_image_border));
			paint.setStrokeWidth(3.0f);
			paint.setStyle(Paint.Style.STROKE);
			Rect border = new Rect(2, 2, barcode.getWidth() - 2,
					barcode.getHeight() - 2);
			canvas.drawRect(border, paint);

			paint.setColor(getResources().getColor(R.color.result_points));
			if (points.length == 2) {
				paint.setStrokeWidth(4.0f);
				drawLine(canvas, paint, points[0], points[1]);
			} else if (points.length == 4
					&& (rawResult.getBarcodeFormat() == BarcodeFormat.UPC_A || rawResult
					.getBarcodeFormat() == BarcodeFormat.EAN_13)) {
				// Hacky special case -- draw two lines, for the barcode and
				// metadata
				drawLine(canvas, paint, points[0], points[1]);
				drawLine(canvas, paint, points[2], points[3]);
			} else {
				paint.setStrokeWidth(10.0f);
				for (ResultPoint point : points) {
					canvas.drawPoint(point.getX(), point.getY(), paint);
				}
			}
		}
	}

	protected static void drawLine(Canvas canvas, Paint paint, ResultPoint a,
								   ResultPoint b) {
		canvas.drawLine(a.getX(), a.getY(), b.getX(), b.getY(), paint);
	}

	protected void showScanner() {
		viewfinderView.setVisibility(View.VISIBLE);
	}

	protected void initCamera(SurfaceHolder surfaceHolder) {
		try {
			cameraManager.openDriver(surfaceHolder);
			// Creating the handler starts the preview, which can also throw a
			// RuntimeException.
			if (handler == null)
				handler = new DecoderActivityHandler(this, decodeFormats,
						characterSet, cameraManager);
		} catch (IOException ioe) {
			Log.w(TAG, ioe);
		} catch (RuntimeException e) {
			// Barcode Scanner has seen crashes in the wild of this variety:
			// java.?lang.?RuntimeException: Fail to connect to camera service
			Log.w(TAG, "Unexpected error initializing camera", e);
		}
	}

	@Override
	public void finish() {
		if(code!=null){
			Intent data = new Intent();
			// Return some hard-coded values
			data.putExtra("code", code);
			setResult(RESULT_OK, data);
		}
		super.finish();
	}

}
