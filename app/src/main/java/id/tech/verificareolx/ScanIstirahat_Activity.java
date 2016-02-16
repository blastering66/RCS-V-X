package id.tech.verificareolx;


import java.io.IOException;
import java.util.Collection;

import org.json.JSONException;
import org.json.JSONObject;

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
import id.tech.util.Parameter_Collections;
import id.tech.util.Public_Functions;
import id.tech.util.ServiceHandlerJSON;

public class ScanIstirahat_Activity extends FragmentActivity implements
		IDecoderActivity, SurfaceHolder.Callback {
	private static final String TAG = DecoderActivity.class.getSimpleName();

	protected DecoderActivityHandler handler = null;
	protected ViewfinderView viewfinderView = null;
	protected CameraManager cameraManager = null;
	protected boolean hasSurface = false;
	protected Collection<BarcodeFormat> decodeFormats = null;
	protected String characterSet = null;
	String code, cUrlImage;
	int serverRespondCode = 0;
	byte[] dataX;
	long totalSize = 0;
	private String extra_kode_absensi, extra_id_pegawai;
	// private double extra_longitude, extra_latitude;
	SharedPreferences sh_pf;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_scan);

		sh_pf = getSharedPreferences(Parameter_Collections.SH_NAME,
				Context.MODE_PRIVATE);

		extra_id_pegawai = getIntent().getStringExtra(
				Parameter_Collections.SH_ID_PEGAWAI);
		extra_kode_absensi = getIntent().getStringExtra(
				Parameter_Collections.EXTRA_ABSENSI);
		Log.e("kode absen", extra_kode_absensi);

		// extra_latitude = getIntent().getDoubleExtra(
		// Parameter_Collections.TAG_LATITUDE_ABSENSI, 0);
		// extra_longitude = getIntent().getDoubleExtra(
		// Parameter_Collections.TAG_LONGITUDE_ABSENSI, 0);

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

		// Intent load = new Intent(getApplicationContext(), Hasil.class);
		// Bitmap b = barcode;
		// ByteArrayOutputStream bs = new ByteArrayOutputStream();
		// b.compress(Bitmap.CompressFormat.PNG, 50, bs);
		// load.putExtra("byte", bs.toByteArray());
		// startActivity(load);

		Toast.makeText(getApplicationContext(), "Kode Toko = " + code,
				Toast.LENGTH_LONG).show();

		// cUrlImage = SaveImage(barcode, rawResult.getText(),
		// rawResult.getTimestamp());

		// Intent intent_result = new Intent();
		// intent_result.putExtra(ParameterCollections.SH_BARCODE_ARRAY, code);
		// setResult(RESULT_OK, intent_result);

		// Intent result = new Intent();
		// result.putExtra("xxx", "xxx");
		// setResult(RESULT_OK, result);
		Parameter_Collections.ARRAY = rawResult.getText();

		if (Public_Functions.isNetworkAvailable(getApplicationContext())) {
			// boolean b = true;
			// if (b) {
			new Async_SubmitAbsenIstirahat().execute();
		} else {
			Toast.makeText(getApplicationContext(),
					"No Internet Connection. Cek Your Network, and Try Again",
					Toast.LENGTH_LONG).show();
		}

	}

	private class Async_SubmitAbsenIstirahat extends AsyncTask<Void, Void, Void> {
		ProgressDialog pdialog;
		DialogFragmentProgress pDialog;
		String respondMessage;
		JSONObject jsonResult;
		String c;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pDialog = new DialogFragmentProgress();
			pDialog.show(getSupportFragmentManager(), "");

			// pdialog = new ProgressDialog(getApplicationContext());
			// pdialog.setMessage("Submitting");
			// pdialog.show();
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			ServiceHandlerJSON sh = new ServiceHandlerJSON();

			String latitude = sh_pf.getString(
					Parameter_Collections.TAG_LATITUDE_NOW, "0.0");
			String longitude = sh_pf.getString(
					Parameter_Collections.TAG_LONGITUDE_NOW, "0.0");

			Log.e("LOCATION NOW",
					String.valueOf(latitude) + "," + String.valueOf(longitude));

			JSONObject jObj = sh.json_absen_istirahat(code, extra_id_pegawai,
					String.valueOf(longitude), String.valueOf(latitude),
					extra_kode_absensi);
			try {
				c = jObj.getString(Parameter_Collections.TAG_ROWCOUNT);
				sh_pf.edit()
						.putString(Parameter_Collections.SH_KODE_TOKO, code)
						.commit();
//				if (extra_kode_absensi.equals("1")) {
//					sh_pf.edit()
//							.putBoolean(Parameter_Collections.SH_ABSENTED, true)
//							.commit();
//				} else {
//					sh_pf.edit()
//							.putBoolean(Parameter_Collections.SH_ABSENTED,
//									false).commit();
//				}

			} catch (JSONException e) {

			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (c.equals("1")) {
				pDialog.dismiss();
				DialogLocationConfirmation dialog = new DialogLocationConfirmation();
				dialog.setContext(getApplicationContext());
				dialog.setText("Absent Istirahat Success");
				dialog.setFrom(9);
				dialog.setCancelable(false);
				dialog.show(getSupportFragmentManager(), "");

			} else {
				Toast.makeText(getApplicationContext(),
						"Something wrong happened", Toast.LENGTH_LONG).show();
			}

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
		if (code != null) {
			Intent data = new Intent();
			// Return some hard-coded values
			data.putExtra("code", code);
			setResult(RESULT_OK, data);
		}
		super.finish();
	}

}
