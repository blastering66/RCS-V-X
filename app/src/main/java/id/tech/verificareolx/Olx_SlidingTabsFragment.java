package id.tech.verificareolx;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import id.tech.util.RowData_History;
import id.tech.util.Parameter_Collections;
import id.tech.util.RowData_Notif;
import id.tech.util.Olx_ServiceHandlerJSON;
import common.view.SlidingTabLayout;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Olx_SlidingTabsFragment extends Fragment {
//	static ArrayList<RowData_History_Absensi> data_Absensi;
	static ArrayList<RowData_History> data_Branding;
//	static ArrayList<RowData_History_Issue> data_Issue;
	static ArrayList<RowData_Notif> data_Notif;
	Olx_ServiceHandlerJSON sh;
	SharedPreferences spf;
	String id_pegawai;

	private SlidingTabLayout mSlidingTableLayout;
	private ViewPager mViewPager;
	private List<SamplePagerItem> mTabs = new ArrayList<SamplePagerItem>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		spf = getActivity().getSharedPreferences(Parameter_Collections.SH_NAME,
				Context.MODE_PRIVATE);
		id_pegawai = spf.getString(Parameter_Collections.SH_ID_PEGAWAI, "");
		
		new Async_GetAllHistory().execute();		

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater
				.inflate(R.layout.test_fragment_sample, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		mViewPager = (ViewPager) view.findViewById(R.id.viewpager);		

		mSlidingTableLayout = (SlidingTabLayout) view
				.findViewById(R.id.sliding_tabs);
		
		
	}

	class SampleFragmentPagerAdapter extends FragmentPagerAdapter {

		public SampleFragmentPagerAdapter(FragmentManager fm) {
			// TODO Auto-generated constructor stub
			super(fm);
		}

		@Override
		public Fragment getItem(int posisi) {
			// TODO Auto-generated method stub
			return mTabs.get(posisi).createFragment(posisi);

		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mTabs.size();
		}

		@Override
		public CharSequence getPageTitle(int position) {
			// TODO Auto-generated method stub
			return mTabs.get(position).getTitle();
		}
	}

	static class SamplePagerItem {
		private final CharSequence mTitle;

		SamplePagerItem(CharSequence title) {
			mTitle = title;
		}

		Fragment createFragment(int posisi) {
			return Olx_ContentFragment.newInstance(mTitle, posisi,
					data_Branding, data_Notif);
		}

		CharSequence getTitle() {
			return mTitle;
		}

	}

	public class Async_GetAllHistory extends AsyncTask<Void, Void, Void> {
		Olx_DialogFragmentProgress pDialog;
		String cCode, cMessage;
		String total_data ="0";

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			data_Branding = new ArrayList<RowData_History>();
			data_Notif = new ArrayList<RowData_Notif>();
			
			pDialog = new Olx_DialogFragmentProgress();
			pDialog.show(getChildFragmentManager(), "");
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				sh = new Olx_ServiceHandlerJSON();
				getData_Branding();
				getData_Notif();
			} catch (Exception e) {

			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			pDialog.dismiss();

			mTabs.add(new SamplePagerItem("History Branding"));
			mTabs.add(new SamplePagerItem("History Notifikasi"));
			
			mViewPager.setAdapter(new SampleFragmentPagerAdapter(
					getChildFragmentManager()));
			mSlidingTableLayout.setViewPager(mViewPager);
		}

		private void getData_Branding() {
			JSONObject jObj = sh.json_get_history(id_pegawai);
//			JSONObject jobj = sh.json_get_history_branding(id_pegawai);
//			data_Branding.add(new RowData_History_Branding("Telefone", "12.00",
//					"27-22-2015", "Keterangan"));
			
			try{
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

						data_Branding.add(new RowData_History(nama_outlet,username_visit, email_visit, topup_visit,
								alamat_outlet,telepon_outlet,confirm));
					}
				}
			}catch(JSONException e){
				
			}

		}



		private void getData_Notif() {
			

			JSONObject jObj = sh.json_get_allnotif();
			Log.e("Log SH notif", jObj.toString());

			try {
				cCode = jObj.getString(Parameter_Collections.TAG_JSON_CODE);
				if (cCode.equals("1")) {
					JSONArray jArray = jObj
							.getJSONArray(Parameter_Collections.TAG_DATA);
					for (int i = 0; i < jArray.length(); i++) {
						JSONObject c = jArray.getJSONObject(i);
						String id = c
								.getString(Parameter_Collections.TAG_ID_PESAN);
						String judul = c
								.getString(Parameter_Collections.TAG_JUDUL_PESAN);
						String pesan = c
								.getString(Parameter_Collections.TAG_PESAN);

						data_Notif.add(new RowData_Notif(id, judul, pesan));
					}
				} else {
					// NO Data
					cMessage = jObj
							.getString(Parameter_Collections.TAG_JSON_ERROR_MESSAGE);
				}
			} catch (JSONException e) {
				cMessage = e.getMessage().toString();
			}
		}
	}

}
