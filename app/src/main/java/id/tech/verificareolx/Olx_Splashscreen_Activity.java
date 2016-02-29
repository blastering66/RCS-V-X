package id.tech.verificareolx;

import id.tech.util.GPSTracker;
import id.tech.util.Parameter_Collections;

import com.squareup.picasso.Picasso;

import android.support.v4.app.FragmentActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

public class Olx_Splashscreen_Activity extends FragmentActivity {
	SharedPreferences sh;
	ImageView img_Logo, img_GetLoc, img_Refresh, img_Loc, img_Ok;
	String now_latitude;
	String now_longitude;
	TextView tv_GetLoc, tv_Retry, tv_Ok, tv_Info;
	TextView tv_presented;
	View wrapper;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_splashscreen);

		sh = getSharedPreferences(Parameter_Collections.SH_NAME,
				Context.MODE_PRIVATE);

		String registered_gcm = sh.getString(Parameter_Collections.SH_GCM_REGISTERED, "0");
		if(!registered_gcm.equals("1")){
			Intent intent_gcm = new Intent(this, Verificare_RegistrationIntentService.class);
			startService(intent_gcm);
		}


		img_Logo = (ImageView) findViewById(R.id.img);
		img_GetLoc = (ImageView) findViewById(R.id.btn_get_loc);
		img_Refresh = (ImageView) findViewById(R.id.btn_get_refresh);
		img_Loc = (ImageView) findViewById(R.id.img_loc);
		img_Ok = (ImageView) findViewById(R.id.btn_ok);
		
		tv_GetLoc = (TextView)findViewById(R.id.tv_getloc);
		tv_Retry = (TextView)findViewById(R.id.tv_refresh);
		tv_Ok = (TextView)findViewById(R.id.tv_ok);
		tv_Info = (TextView)findViewById(R.id.tv_info);
		
		tv_presented = (TextView)findViewById(R.id.tv_presented);
		wrapper = (View)findViewById(R.id.wrapper);
		
		img_GetLoc.setVisibility(View.INVISIBLE);
		img_Loc.setVisibility(View.INVISIBLE);		
		

		Animation fadeIn = new AlphaAnimation(0, 1);
		fadeIn.setInterpolator(new DecelerateInterpolator()); // add this
		fadeIn.setDuration(4000);

		Animation fadeOut = new AlphaAnimation(1, 0);
		fadeOut.setInterpolator(new AccelerateInterpolator()); // and this
		fadeOut.setStartOffset(2000);
		fadeOut.setDuration(2000);

		AnimationSet animation = new AnimationSet(false); // change to false
		animation.addAnimation(fadeIn);
		// animation.addAnimation(fadeOut);
		img_Logo.setAnimation(animation);
		wrapper.setAnimation(animation);
		// img.startAnimation(animation);
//		new LoadViewState().execute();

		img_GetLoc.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (getLocationNow(getApplicationContext())) {

					String url = "http://maps.google.com/maps/api/staticmap?center="
							+ now_latitude
							+ ","
							+ now_longitude
							+ "&zoom=15&size=300x200&markers=color:blue|size:mid|"
							+ now_latitude
							+ ","
							+ now_longitude
							+ "&sensor=false";

					img_GetLoc.setVisibility(View.GONE);
					img_Loc.setVisibility(View.VISIBLE);
					img_Refresh.setVisibility(View.VISIBLE);
					img_Ok.setVisibility(View.VISIBLE);
					
					tv_GetLoc.setVisibility(View.GONE);
					tv_Info.setVisibility(View.VISIBLE);
					tv_Retry.setVisibility(View.VISIBLE);
					tv_Ok.setVisibility(View.VISIBLE);
					
					Picasso.with(getApplicationContext()).load(url)
							.into(img_Loc);
				}
			}
		});

		img_Refresh.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// TODO Auto-generated method stub
				if (getLocationNow(getApplicationContext())) {

					String url = "http://maps.google.com/maps/api/staticmap?center="
							+ now_latitude
							+ ","
							+ now_longitude
							+ "&zoom=15&size=300x200&markers=color:blue|size:mid|"
							+ now_latitude
							+ ","
							+ now_longitude
							+ "&sensor=false";

					img_GetLoc.setVisibility(View.GONE);
					tv_GetLoc.setVisibility(View.VISIBLE);
					
					img_Refresh.setVisibility(View.VISIBLE);
					img_Ok.setVisibility(View.VISIBLE);
					Picasso.with(getApplicationContext()).load(url)
							.into(img_Loc);
				}
			}
		});

		img_Ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new LoadNext().execute();
			}
		});

		new LoadNext().execute();
	}

	private boolean getLocationNow(Context context) {
		GPSTracker gps = new GPSTracker(context);
		if (gps.canGetLocation()) {
			double latitude = gps.getLatitude();
			double longitude = gps.getLongitude();

			now_latitude = String.valueOf(latitude);
			now_longitude = String.valueOf(longitude);

			Log.e("Longitude", String.valueOf(longitude));
			Log.e("Latitude", String.valueOf(latitude));

			sh.edit()
					.putString(Parameter_Collections.TAG_LONGITUDE_NOW,
							String.valueOf(longitude)).commit();
			sh.edit()
					.putString(Parameter_Collections.TAG_LATITUDE_NOW,
							String.valueOf(latitude)).commit();

			return true;

		} else {
			return false;
		}
	}

	private class LoadNext extends AsyncTask<Void, Void, Void> {
		private boolean locationGot = false;

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				Thread.sleep(4000);

				if (!now_latitude.equals("0.0") && !now_longitude.equals("0.0")) {
					locationGot = true;
				} else if (!now_latitude.isEmpty() && !now_longitude.isEmpty()) {
					locationGot = true;
				} else if (now_latitude != null && now_longitude != null) {
					locationGot = true;
				} else {
					locationGot = false;
				}
			} catch (Exception e) {

			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			boolean logged = sh.getBoolean(Parameter_Collections.SH_LOGGED,
					false);
				if (logged) {
					// Olx_DialogLocationConfirmation dialog = new
					// Olx_DialogLocationConfirmation(
					// getApplicationContext(),
					// "Loading Location Completed", 0);
					// dialog.setCancelable(false);
					// dialog.show(getSupportFragmentManager(), "");

					Intent load = new Intent(getApplicationContext(),
							Olx_MenuUtama_WP.class);

//					Intent load = new Intent(getApplicationContext(),
//							Activity_Scrolling.class);
					startActivity(load);
					finish();

				} else {

					Intent load = new Intent(getApplicationContext(),
							Olx_Login_Activity.class);
					startActivity(load);
					finish();
				}

		}

	}

	private class LoadViewState extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				Thread.sleep(4000);

			} catch (Exception e) {

			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			boolean logged = sh.getBoolean(Parameter_Collections.SH_LOGGED, false);
			if(logged){
				tv_Ok.setVisibility(View.VISIBLE);
				tv_Retry.setVisibility(View.VISIBLE);
				tv_Info.setVisibility(View.VISIBLE);
				
				tv_GetLoc.setVisibility(View.GONE);
				img_Logo.setVisibility(View.GONE);
				//tambahan
				tv_presented.setVisibility(View.GONE);
				wrapper.setVisibility(View.GONE);
				
				img_Refresh.setVisibility(View.VISIBLE);				
				img_Loc.setVisibility(View.VISIBLE);
				
				if (getLocationNow(getApplicationContext())) {

					String url = "http://maps.google.com/maps/api/staticmap?center="
							+ now_latitude
							+ ","
							+ now_longitude
							+ "&zoom=15&size=300x200&markers=color:blue|size:mid|"
							+ now_latitude
							+ ","
							+ now_longitude
							+ "&sensor=false";

					img_GetLoc.setVisibility(View.GONE);
					tv_GetLoc.setVisibility(View.GONE);
					img_Refresh.setVisibility(View.VISIBLE);
					img_Ok.setVisibility(View.VISIBLE);
					Picasso.with(getApplicationContext()).load(url)
							.into(img_Loc);
				}
			}else{
				img_Logo.setVisibility(View.GONE);
				//tambahan
				tv_presented.setVisibility(View.GONE);
				wrapper.setVisibility(View.GONE);
				
				img_GetLoc.setVisibility(View.VISIBLE);
				tv_GetLoc.setVisibility(View.VISIBLE);
				
//				img_Ok.setVisibility(View.VISIBLE);
//				img_Loc.setVisibility(View.VISIBLE);
			}
			
			

		}
	}
}
