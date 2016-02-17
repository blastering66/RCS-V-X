package id.tech.verificareolx;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import id.tech.util.Olx_RecyclerAdapter_History;
import id.tech.util.Parameter_Collections;
import id.tech.util.RowData_History;
import id.tech.util.Olx_ServiceHandlerJSON;

public class Olx_History_Activity extends AppCompatActivity{
	RecyclerView rv;
	RecyclerView.LayoutManager layoutManager;
	RecyclerView.Adapter adapter;
	Activity activity;
	SharedPreferences spf;
	private String id_pegawai;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_history_inputstock);
		spf = getSharedPreferences(Parameter_Collections.SH_NAME, Context.MODE_PRIVATE);
		id_pegawai = spf.getString(Parameter_Collections.SH_ID_PEGAWAI, "");

		ActionBar ac = getSupportActionBar();
		ac.setDisplayHomeAsUpEnabled(true);
		ac.setTitle("History Visit");

		activity = this;

		rv = (RecyclerView)findViewById(R.id.rv);

		new AsyncTask_GetHistory().execute();

	}

	private class AsyncTask_GetHistory extends AsyncTask<Void,Void,Void> {
		private List<RowData_History> data;
		String total_data ="0";

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			data = new ArrayList<>();
		}

		@Override
		protected Void doInBackground(Void... params) {
			Olx_ServiceHandlerJSON sh = new Olx_ServiceHandlerJSON();
			JSONObject jObj = sh.json_get_history(id_pegawai);
//			JSONObject jObj = sh.json_get_history("2");
			try {
				total_data = jObj.getString(Parameter_Collections.TAG_TOTAL_VISIT_MAX_TOKO);
				if(Integer.parseInt(total_data) > 0){
					JSONArray jArray = jObj.getJSONArray(Parameter_Collections.TAG_DATA);
					for(int i=0; i < jArray.length();i++){
						JSONObject c = jArray.getJSONObject(i);

						String nama_outlet = c.getString(Parameter_Collections.TAG_NAMA_OUTLET);
						String username_visit = c.getString(Parameter_Collections.TAG_USERNAME_OUTLET_VISIT);
						String email_visit = c.getString(Parameter_Collections.TAG_EMAIL_OUTLET_VISIT);
						String topup_visit = c.getString(Parameter_Collections.TAG_TOPUP_OUTLET_VISIT);

						String alamat_outlet = c.getString(Parameter_Collections.TAG_ALAMAT_OUTLET);
						String telepon_outlet = c.getString(Parameter_Collections.TAG_TELEPON_OUTLET);
						String confirm = c.getString(Parameter_Collections.TAG_CONFIRMED_TOKO);

						data.add(new RowData_History(nama_outlet,username_visit, email_visit, topup_visit,
								alamat_outlet,telepon_outlet,confirm));
					}
				}

			}catch (JSONException e){
				total_data = "0";
			}


			return null;
		}

		@Override
		protected void onPostExecute(Void aVoid) {
			super.onPostExecute(aVoid);

			if(!total_data.equals("0")){
				layoutManager = new LinearLayoutManager(getApplicationContext(),1,false);
				adapter = new Olx_RecyclerAdapter_History(activity,getApplicationContext(),data);
				rv.setLayoutManager(layoutManager);
				rv.setAdapter(adapter);
			}else{
				Toast.makeText(getApplicationContext(), "No Data...", Toast.LENGTH_LONG).show();
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
		
		}
		return super.onOptionsItemSelected(item);
		
	}

//public class History_Activity extends ActionBarActivity {
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		// TODO Auto-generated method stub
//		super.onCreate(savedInstanceState);
//
//		ActionBar actionBar = getSupportActionBar();
//		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
//		actionBar.setDisplayShowTitleEnabled(true);
//
//		Tab tab1 = actionBar.newTab().setText("Tab 1")
//				.setIcon(R.drawable.ic_launcher);
//		// .setTabListener(new SupportFra)
//	}
//
//	private class TabListener implements ActionBar.TabListener {
//		private TabContentFragment mFragment;
//		
//		public TabListener(TabContentFragment fragment) {
//			// TODO Auto-generated constructor stub
//			mFragment = fragment;
//		}
//
//		@Override
//		public void onTabReselected(Tab arg0, FragmentTransaction arg1) {
//			// TODO Auto-generated method stub
//
//		}
//
//		@Override
//		public void onTabSelected(Tab arg0, FragmentTransaction arg1) {
//			// TODO Auto-generated method stub
//			arg1.add(R.id.f, arg1)
//
//		}
//
//		@Override
//		public void onTabUnselected(Tab arg0, FragmentTransaction arg1) {
//			// TODO Auto-generated method stub
//
//		}
//
//	}
//
//	private class TabContentFragment extends Fragment {
//		private String mTxt;
//
//		public TabContentFragment(String mTxt) {
//			// TODO Auto-generated constructor stub
//			this.mTxt = mTxt;
//		}
//
//		public String getText() {
//			return mTxt;
//		}
//		
//		@Override
//		public View onCreateView(LayoutInflater inflater, ViewGroup container,
//				Bundle savedInstanceState) {
//			// TODO Auto-generated method stub
//			View v = inflater.inflate(R.layout.item_test, null);
//			
//			TextView t = (TextView)v.findViewById(R.id.tv_judul);
//			t.setText(mTxt);
//			return v;
//		}
//	}
}
