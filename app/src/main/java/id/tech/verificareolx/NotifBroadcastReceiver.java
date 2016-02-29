package id.tech.verificareolx;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import id.tech.util.Parameter_Collections;
import id.tech.util.Public_Functions;
import id.tech.util.RowData_Notif;
import id.tech.util.Olx_ServiceHandlerJSON;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

public class NotifBroadcastReceiver extends BroadcastReceiver {
	Context ctx;
	String pesan;
	Intent activate;
	NotificationManager notifManager;
	NotificationCompat.Builder builder;

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		ctx = context;
//		new Async_GetNotif().execute();

	}

	private class Async_GetNotif extends AsyncTask<Void, Void, Void> {
		String cCode, cMessage, cJudulPesan, cIdPesan;
		ArrayList<RowData_Notif> data;
		boolean isConnected = true;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			data = new ArrayList<RowData_Notif>();
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			if(Public_Functions.isNetworkAvailable(ctx)){
				Olx_ServiceHandlerJSON sh = new Olx_ServiceHandlerJSON();
				JSONObject jobj = sh.json_cek_notif();

				try {
					cCode = jobj.getString(Parameter_Collections.TAG_JSON_CODE);

					if (cCode.equals("1")) {
						JSONArray jArray = jobj
								.getJSONArray(Parameter_Collections.TAG_DATA);
						for (int i = 0; i < jArray.length(); i++) {
							JSONObject c = jArray.getJSONObject(i);

							cIdPesan = c
									.getString(Parameter_Collections.TAG_ID_PESAN);
							cJudulPesan = c
									.getString(Parameter_Collections.TAG_JUDUL_PESAN);
							cMessage = c.getString(Parameter_Collections.TAG_PESAN);

							data.add(new RowData_Notif(cIdPesan, cJudulPesan,
									cMessage));
						}

					} else {
						cMessage = jobj.getString(Parameter_Collections.TAG_DATA);
					}

				} catch (JSONException e) {

				}
			}else{
				isConnected = false;
			}
			
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(isConnected){
				SharedPreferences spf = ctx
						.getSharedPreferences(
								Parameter_Collections.SH_NAME,
								Context.MODE_PRIVATE);					
				String id_nya = spf.getString(Parameter_Collections.SH_ARRAY_IDPESAN, "");

				if (cCode.equals("1")) {

					for (int i = 0; i < data.size(); i++) {
						String id = data.get(i).id_pesan;
						String judul = data.get(i).judul_pesan;
						String isi = data.get(i).isi_pesan;
						

						if(id_nya.equals("")){
							spf.edit().putString(Parameter_Collections.SH_ARRAY_IDPESAN, id).commit();
							Uri sound = Uri.parse("android.resource://"
									+ ctx.getPackageName() + "/" + R.raw.notif_solem);

							builder = new NotificationCompat.Builder(ctx)
									.setSmallIcon(R.drawable.ic_launcher)
									.setContentTitle(judul).setContentText(isi)
									.setSound(sound);
							builder.setAutoCancel(true);

							Intent load = new Intent(ctx, Notif_Detail_Activity.class);
							load.putExtra(Parameter_Collections.EXTRA_PESAN, isi);
							TaskStackBuilder stackBuilder = TaskStackBuilder
									.create(ctx);
							stackBuilder.addParentStack(Olx_MenuUtama_WP.class);
							stackBuilder.addNextIntent(load);

							PendingIntent resultPendingIntent = stackBuilder
									.getPendingIntent(0,
											PendingIntent.FLAG_UPDATE_CURRENT);
							builder.setContentIntent(resultPendingIntent);

							notifManager = (NotificationManager) ctx
									.getSystemService(Context.NOTIFICATION_SERVICE);

							notifManager.notify(111, builder.build());
							Log.e("Notif kosong", "");
						}else if(!id.equals(id_nya)){
							spf.edit().putString(Parameter_Collections.SH_ARRAY_IDPESAN, id).commit();
							Log.e("id Notif sebelumnya tidak sama", "");
							Uri sound = Uri.parse("android.resource://"
									+ ctx.getPackageName() + "/" + R.raw.notif_solem);

							builder = new NotificationCompat.Builder(ctx)
									.setSmallIcon(R.drawable.ic_launcher)
									.setContentTitle(judul).setContentText(isi)
									.setSound(sound);
							builder.setAutoCancel(true);
							
							Intent load = new Intent(ctx, Notif_Detail_Activity.class);
							load.putExtra(Parameter_Collections.EXTRA_PESAN, isi);
							TaskStackBuilder stackBuilder = TaskStackBuilder
									.create(ctx);
							stackBuilder.addParentStack(Olx_MenuUtama_WP.class);
							stackBuilder.addNextIntent(load);

							PendingIntent resultPendingIntent = stackBuilder
									.getPendingIntent(0,
											PendingIntent.FLAG_UPDATE_CURRENT);
							builder.setContentIntent(resultPendingIntent);

							notifManager = (NotificationManager) ctx
									.getSystemService(Context.NOTIFICATION_SERVICE);

							notifManager.notify(111, builder.build());
						}else{
							Log.e("id Notif sebelumnya sama", "");
							spf.edit().putString(Parameter_Collections.SH_ARRAY_IDPESAN, id).commit();
						}
						
					}

				} else {
					Log.e("NOTIF", "No Data");
				}
			}else{
//				Toast.makeText(ctx,
//						"No Internet Connection, Cek Your Network",
//						Toast.LENGTH_LONG).show();
				Log.e("NOTIF", "No Connections");
			}
			
			
			
		}
	}

}
