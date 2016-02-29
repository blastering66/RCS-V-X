package id.tech.verificareolx;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import id.tech.util.Olx_CustomAdapter_HistoryNotif;
import id.tech.util.Parameter_Collections;
import id.tech.util.RowData_Notif;
import id.tech.util.Olx_ServiceHandlerJSON;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class HistoryNotification_Activity extends ActionBarActivity {
	ListView ls;
	Olx_CustomAdapter_HistoryNotif adapter;
	ArrayList<RowData_Notif> data;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_historynotif);
		ActionBar ac = getSupportActionBar();
		ac.setTitle("History Notification");
		ac.setDisplayHomeAsUpEnabled(true);

		ls = (ListView) findViewById(R.id.ls);
		ls.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent load = new Intent(getApplicationContext(),
						Notif_Detail_Activity.class);
				load.putExtra(Parameter_Collections.EXTRA_PESAN,
						data.get(position).isi_pesan);
				startActivity(load);
			}

		});
		// new Async_GetHistoryNotif().execute();

//		if (Public_Functions.isNetworkAvailable(getApplicationContext())) {
		boolean b = true;
		if (b) {
			new Async_GetHistoryNotif().execute();
		}else {
			Toast.makeText(getApplicationContext(),
					"No Internet Connection, Cek Your Network",
					Toast.LENGTH_LONG).show();
		}
	}

	private class Async_GetHistoryNotif extends AsyncTask<Void, Void, Void> {
		Olx_DialogFragmentProgress pDialog;
		String cCode, cMessage;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pDialog = new Olx_DialogFragmentProgress();
			pDialog.show(getSupportFragmentManager(), "");
			data = new ArrayList<RowData_Notif>();
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			Olx_ServiceHandlerJSON sh = new Olx_ServiceHandlerJSON();

			try {
				JSONObject jObj = sh.json_get_allnotif();
				
				cCode = jObj.getString(Parameter_Collections.TAG_JSON_CODE);
				if (cCode.equals("1")) {
					JSONArray jArray = jObj
							.getJSONArray(Parameter_Collections.TAG_DATA);
					for (int i = 0; i < jArray.length(); i++) {
						JSONObject c = jArray.getJSONObject(i);
						String id = c.getString(Parameter_Collections.TAG_ID_PESAN);
						String judul = c
								.getString(Parameter_Collections.TAG_JUDUL_PESAN);
						String pesan = c.getString(Parameter_Collections.TAG_PESAN);

						data.add(new RowData_Notif(id, judul, pesan));
					}
				}else{
					//NO Data 
					cMessage = jObj.getString(Parameter_Collections.TAG_JSON_ERROR_MESSAGE);	
				}
				

			} catch (JSONException e) {

			}

			// try {
			// JSONArray jArray = jObj
			// .getJSONArray(Parameter_Collections.TAG_DATA);
			// for (int i = 0; i < jArray.length(); i++) {
			// JSONObject c = jArray.getJSONObject(i);
			// String id = c
			// .getString(Parameter_Collections.TAG_ID_PESAN);
			// String judul = c
			// .getString(Parameter_Collections.TAG_JUDUL_PESAN);
			// String pesan = c
			// .getString(Parameter_Collections.TAG_PESAN);
			//
			// data.add(new RowData_Notif(id, judul, pesan));
			// }
			//
			// } catch (JSONException e) {
			//
			// }
			cCode = "1";
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			pDialog.dismiss();

			if (cCode.equals("1")) {
				adapter = new Olx_CustomAdapter_HistoryNotif(
						getApplicationContext(), 0, data);
				ls.setAdapter(adapter);
			}else{
				Toast.makeText(getApplicationContext(), cMessage, Toast.LENGTH_LONG).show();
			}
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

}
