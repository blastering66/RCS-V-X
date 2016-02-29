package id.tech.verificareolx;

import id.tech.util.Parameter_Collections;
import id.tech.util.RecyclerAdapter_HistoryInputStock;
import id.tech.util.RowData_History;
import id.tech.util.RowData_Promo;
import id.tech.util.Olx_ServiceHandlerJSON;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class History_Activity extends AppCompatActivity{
	RecyclerView rv;
	RecyclerView.LayoutManager layoutManager;
	RecyclerView.Adapter adapter;
	private List<RowData_Promo> data;
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
		ac.setTitle("History");

		activity = this;

		rv = (RecyclerView)findViewById(R.id.rv);

		new AsyncTask_GetHistory().execute();

	}

	private class AsyncTask_GetHistory extends AsyncTask<Void,Void,Void> {
		private List<RowData_History> data;
		String total_data;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			data = new ArrayList<>();
		}

		@Override
		protected Void doInBackground(Void... params) {
			Olx_ServiceHandlerJSON sh = new Olx_ServiceHandlerJSON();
//			JSONObject jObj = sh.json_get_history_inputstock(id_pegawai);
			JSONObject jObj = new JSONObject();
			try{
				total_data = jObj.getString(Parameter_Collections.TAG_TOTAL_VISIT_MAX_TOKO);
				if(Integer.parseInt(total_data) > 0){
					JSONArray jArray = jObj.getJSONArray(Parameter_Collections.TAG_DATA);
					for(int i=0; i < jArray.length();i++){
						JSONObject c = jArray.getJSONObject(i);

						String stock_qty_warehouse = c.getString(Parameter_Collections.TAG_WAREHOUSE_STOCK);
						String stock_qty_display = c.getString(Parameter_Collections.TAG_DISPLAY_STOCK);
						String stock_qty_unit = c.getString(Parameter_Collections.TAG_UNIT_STOCK);
						String nama_toko = c.getString(Parameter_Collections.TAG_NAMA_TOKO);
						String region_toko = c.getString(Parameter_Collections.TAG_REGION_TOKO);
						String stock_name = c.getString(Parameter_Collections.TAG_NAME_STOCK);

//						data.add(new RowData_History(stock_qty_warehouse, stock_qty_display,stock_qty_unit,
//								nama_toko,region_toko,stock_name));
					}
				}
			}catch (JSONException e){

			}


			return null;
		}

		@Override
		protected void onPostExecute(Void aVoid) {
			super.onPostExecute(aVoid);

			if(!total_data.equals("0")){
				layoutManager = new LinearLayoutManager(getApplicationContext(),1,false);
				adapter = new RecyclerAdapter_HistoryInputStock(activity,getApplicationContext(),data);
				rv.setLayoutManager(layoutManager);
				rv.setAdapter(adapter);
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
