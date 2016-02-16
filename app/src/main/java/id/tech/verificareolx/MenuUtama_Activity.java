package id.tech.verificareolx;

import org.json.JSONException;
import org.json.JSONObject;

import id.tech.verificareolx.R;
import id.tech.util.GPSTracker;
import id.tech.util.Parameter_Collections;
import id.tech.util.ServiceHandlerJSON;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

public class MenuUtama_Activity extends ActionBarActivity {
	Button radio_masuk, radio_pulang;
	Button btn_logout, btn_absen, btn_issue, btn_penjualan, btn_inputproduk, btn_updatebranding, btn_cek_produk;
	SharedPreferences sh;

	private void showToast(String txt, Context ctx){
		Toast.makeText(ctx, txt, Toast.LENGTH_LONG).show();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menuutama);
		getAllView();

		sh = getSharedPreferences(Parameter_Collections.SH_NAME,
				Context.MODE_PRIVATE);
		
		btn_cek_produk.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(sh.getBoolean(Parameter_Collections.SH_ABSENTED, false)){
					Intent load = new Intent(getApplicationContext(),
							CekProduk_Activity.class);
					startActivity(load);
				}else{
					showToast("Please absent first", getApplicationContext());
				}
				
				
			}
		});

		btn_penjualan.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(sh.getBoolean(Parameter_Collections.SH_ABSENTED, false)){
					Intent load = new Intent(getApplicationContext(),
							DialogHargaProduk.class);
					startActivity(load);
				}else{
					showToast("Please absent first", getApplicationContext());
				}
				
			}
		});

		btn_inputproduk.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(sh.getBoolean(Parameter_Collections.SH_ABSENTED, false)){
					Intent load = new Intent(getApplicationContext(),
							ScanInputProduk_Activity.class);
					startActivity(load);
				}else{
					showToast("Please absent first", getApplicationContext());
				}
				
			}
		});

		btn_issue.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(sh.getBoolean(Parameter_Collections.SH_ABSENTED, false)){
					Intent load = new Intent(getApplicationContext(),
							Issue_Activity.class);
					startActivity(load);
				}else{
					showToast("Please absent first", getApplicationContext());
				}
				
			}
		});

		btn_absen.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// btn_absen.setBackgroundColor(getResources().getColor(R.color.primaryColor));

				// btn_absen.setVisibility(View.INVISIBLE);
				Animation anim = AnimationUtils
						.loadAnimation(getApplicationContext(),
								android.R.anim.slide_out_right);
				anim.setAnimationListener(new AnimationListener() {

					@Override
					public void onAnimationStart(Animation animation) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onAnimationRepeat(Animation animation) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onAnimationEnd(Animation animation) {
						// TODO Auto-generated method stub
						btn_absen.setVisibility(View.INVISIBLE);
					}
				});
				btn_absen.setAnimation(anim);
				btn_absen.startAnimation(anim);

				radio_masuk.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if(!sh.getBoolean(Parameter_Collections.SH_ABSENTED, false)){
							GPSTracker gps = new GPSTracker(getApplicationContext());
							if (gps.canGetLocation()) {
								double latitude = gps.getLatitude();
								double longitude = gps.getLongitude();
								Intent load = new Intent(getApplicationContext(),
										ScanAbsen_Activity.class);
								load.putExtra(
										Parameter_Collections.SH_ID_PEGAWAI,
										sh.getString(
												Parameter_Collections.SH_ID_PEGAWAI,
												""));
								load.putExtra(Parameter_Collections.EXTRA_ABSENSI,
										"1");
								load.putExtra(
										Parameter_Collections.TAG_LATITUDE_ABSENSI,
										latitude);
								load.putExtra(
										Parameter_Collections.TAG_LONGITUDE_ABSENSI,
										longitude);
								startActivity(load);

							} else {
								Toast.makeText(
										getApplicationContext(),
										"Can not get your location, Please check your GPS or Network",
										Toast.LENGTH_LONG).show();
							}
						}else{
							Toast.makeText(getApplicationContext(), "Please Logout First", Toast.LENGTH_LONG).show();
						}
					}
				});

				radio_pulang.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						GPSTracker gps = new GPSTracker(getApplicationContext());
						if (gps.canGetLocation()) {
							double latitude = gps.getLatitude();
							double longitude = gps.getLongitude();
							Intent load = new Intent(getApplicationContext(),
									ScanAbsen_Activity.class);
							load.putExtra(
									Parameter_Collections.SH_ID_PEGAWAI,
									sh.getString(
											Parameter_Collections.SH_ID_PEGAWAI,
											""));
							load.putExtra(Parameter_Collections.EXTRA_ABSENSI,
									"2");

							load.putExtra(
									Parameter_Collections.TAG_LATITUDE_ABSENSI,
									latitude);
							load.putExtra(
									Parameter_Collections.TAG_LONGITUDE_ABSENSI,
									longitude);
							startActivity(load);

						} else {
							Toast.makeText(
									getApplicationContext(),
									"Can not get your location, Please check your GPS or Network",
									Toast.LENGTH_LONG).show();
						}
					}
				});

			}
		});
		
		btn_updatebranding.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(sh.getBoolean(Parameter_Collections.SH_ABSENTED, false)){
					Intent load = new Intent(getApplicationContext(),
							UpdateBranding_Activity.class);
					startActivity(load);
				}else{
					showToast("Please absent first", getApplicationContext());
				}
			}
		});
		
		btn_logout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				sh.edit().clear().commit();
				finish();
				Toast.makeText(getApplicationContext(), "Logged Out", Toast.LENGTH_LONG).show();
			}
		});
	}

	private void getAllView() {
		ActionBar ac = getSupportActionBar();
		ac.setTitle("Menu Utama");

		btn_absen = (Button) findViewById(R.id.btn_absen);
		radio_masuk = (Button) findViewById(R.id.radio_masuk);
		radio_pulang = (Button) findViewById(R.id.radio_pulang);
		btn_issue = (Button) findViewById(R.id.btn_issue);
		btn_penjualan = (Button) findViewById(R.id.btn_penjualan);
		btn_inputproduk = (Button) findViewById(R.id.btn_inputproduk);
		btn_updatebranding = (Button) findViewById(R.id.btn_update_branding);
		btn_cek_produk = (Button) findViewById(R.id.btn_cek_produk);
		btn_logout= (Button) findViewById(R.id.btn_logout);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		getAllView();
		btn_absen.setVisibility(View.VISIBLE);
	}
	
	
}
