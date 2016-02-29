package id.tech.verificareolx;

import java.io.IOException;
import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.IBinder;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import org.json.JSONObject;

import id.tech.util.Parameter_Collections;
import id.tech.util.Olx_ServiceHandlerJSON;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

public class Notif_Service extends Service{
	Intent myIntent;
	PendingIntent alarmIntent;
	AlarmManager alarams;
	SharedPreferences sp;
	String cContactId;
	Intent activate;

	private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
	private static final String TAG = "MainActivity";
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		sp =  getSharedPreferences(Parameter_Collections.SH_NAME, Context.MODE_PRIVATE);

//		new AsyncTas_RegistedGCM().execute();

	}

	private boolean checkPlayServices() {
		GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
		int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
		if (resultCode != ConnectionResult.SUCCESS) {
			if (apiAvailability.isUserResolvableError(resultCode)) {
				Log.e(TAG, "Error");
			} else {
				Log.i(TAG, "This device is not supported.");

			}
			return false;
		}
		return true;
	}

	private class AsyncTas_RegistedGCM extends AsyncTask<Void,Void,Void>{

		@Override
		protected Void doInBackground(Void... params) {
			try {
				InstanceID instanceID = InstanceID.getInstance(getApplicationContext());
				String tokenID = instanceID.getToken(getString(R.string.GCM_SENDER_ID), GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);

				TelephonyManager mngr = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
				String imei = mngr.getDeviceId();


				Olx_ServiceHandlerJSON sh = new Olx_ServiceHandlerJSON();

				JSONObject jObj = sh.json_register_GCM_to_Server(tokenID,imei);

				sp.edit().putString(Parameter_Collections.SH_TOKEN_GCM, tokenID).commit();

				Log.e("RESULT GCM = ", jObj.toString());

			}catch (IOException e){

			}




			return null;
		}
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		myIntent = intent;
		
		Calendar c = Calendar.getInstance();
//		activate = new Intent(Notif_Service.this, NotifBroadcastReceiver.class);
//		activate = new Intent(Notif_Service.this, Verificare_RegistrationIntentService.class);
//		activate.putExtra("code", "1");
		
		alarmIntent = PendingIntent.getBroadcast(getApplicationContext(), 112, activate,PendingIntent.FLAG_CANCEL_CURRENT);
		
		alarams = (AlarmManager)getSystemService(ALARM_SERVICE);
		alarams.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(),
				 1000 * 60 * 60, alarmIntent);
		Log.e("Service Started", "");

		if (checkPlayServices()) {
			// Start IntentService to register this application with GCM.
			Log.e("Service gcm", "Start Verificare GCM service");

			String registered_gcm = sp.getString(Parameter_Collections.SH_GCM_REGISTERED, "0");
			if(!registered_gcm.equals("1")){
				Intent intent_gcm = new Intent(this, Verificare_RegistrationIntentService.class);
				startService(intent_gcm);
			}
		}
		
		return Service.START_STICKY;
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

}
