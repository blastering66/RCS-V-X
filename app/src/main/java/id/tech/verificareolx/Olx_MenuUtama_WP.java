package id.tech.verificareolx;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import id.tech.util.Olx_RecyclerAdapter_Slider_Empty;
import id.tech.util.Parameter_Collections;
import id.tech.util.RecyclerAdapter_Slider;
import id.tech.util.Olx_ServiceHandlerJSON;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.Toast;

public class Olx_MenuUtama_WP extends ActionBarActivity {
	RecyclerView rv, rv_slider;
	RecyclerView.Adapter adapter, adapter_slider;
	RecyclerView.LayoutManager layoutManager, layoutManager_slider;
	RecyclerView.ItemDecoration decoration;
	ImageView img_logout;
	private SharedPreferences sp;

	ActionBarDrawerToggle mDrawerToggle;
	DrawerLayout Drawer;
	
	Bundle currentState;
	Activity activity;
	private String id_pegawai;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.activity_menuutama_wp);
		setContentView(R.layout.main);

		activity = this;

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);

		ActionBar ac = getSupportActionBar();
		ac.hide();

		sp = getSharedPreferences(Parameter_Collections.SH_NAME,
				Context.MODE_PRIVATE);
		id_pegawai = sp.getString(Parameter_Collections.SH_ID_PEGAWAI, "");

		rv_slider = (RecyclerView) findViewById(R.id.slider_content);
		Drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

		layoutManager_slider = new LinearLayoutManager(this);
		rv_slider.setLayoutManager(layoutManager_slider);

		if (savedInstanceState == null) {
			currentState = savedInstanceState;
			FragmentManager fm = getSupportFragmentManager();
			Fragment fragment = new Olx_Fragment_MenuUtama();
			fm.beginTransaction().replace(R.id.frame_container, fragment)
					.commit();

//			new AsyncTask_LoadProfile().execute();
			adapter_slider = new Olx_RecyclerAdapter_Slider_Empty();
			rv_slider.setAdapter(adapter_slider);

			new AsyncTask_LoadProfile_Target().execute();

		}

		Drawer.openDrawer(Gravity.START);
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (currentState == null) {
//			new AsyncTask_LoadProfile().execute();
			adapter_slider.notifyDataSetChanged();
			rv_slider.setAdapter(adapter_slider);
		}else{
		}

//		Drawer.openDrawer(Gravity.START);
	}

	private class AsyncTask_LoadProfile_Target extends AsyncTask<Void,Void,Void>{
		String result, cCode, cNama_Pegawai, cUrl_ImgProfilePic, cTotalVisited, cTotalVisited_All;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			Toast.makeText(getApplicationContext(), "Loading Data", Toast.LENGTH_SHORT).show();
		}

		@Override
		protected Void doInBackground(Void... params) {

			Olx_ServiceHandlerJSON sh = new Olx_ServiceHandlerJSON();
			JSONObject jObj = sh.json_get_pic_profile_target(id_pegawai);

			try{
				cCode = jObj.getString(Parameter_Collections.TAG_JSON_CODE);

				if (cCode.equals("1")) {
					JSONObject jObj_Data = jObj
							.getJSONObject(Parameter_Collections.TAG_DATA);
					cNama_Pegawai = jObj_Data
							.getString(Parameter_Collections.TAG_NAMA_PEGAWAI);
					String img_no_data = jObj_Data.getString(Parameter_Collections.TAG_ARRAY_IMAGES);

					if(img_no_data.equals("no data")){
						cUrl_ImgProfilePic = "";

					}else{
						JSONArray jArray_Data = jObj_Data
								.getJSONArray(Parameter_Collections.TAG_ARRAY_IMAGES);
						for (int i = 0; i < jArray_Data.length(); i++) {
							JSONObject c = jArray_Data.getJSONObject(i);

							cUrl_ImgProfilePic = Parameter_Collections.URL_GAMBAR_THUMB
									+ c.getString(Parameter_Collections.TAG_NAMA_IMAGE);
						}
					}
					String total_visited = jObj_Data.getString(Parameter_Collections.TAG_TOTAL_VISIT_TOKO);
					String total_visited_daily = jObj_Data.getString(Parameter_Collections.TAG_TOTAL_VISIT_DAILY);
					String jumlah_target = jObj_Data.getString(Parameter_Collections.TAG_TARGET);
					String jumlah_target_daily = jObj_Data.getString(Parameter_Collections.TAG_TARGET_DAILY);

					Log.e("Total Daily = ", total_visited_daily + " / " +jumlah_target_daily);
					Log.e("Total Target = ", total_visited + " / " +jumlah_target);

					cTotalVisited = total_visited_daily + " / " +jumlah_target_daily;
					cTotalVisited_All = total_visited + " / " +jumlah_target;

					adapter_slider = new RecyclerAdapter_Slider(cNama_Pegawai, cUrl_ImgProfilePic, getApplicationContext(),
							activity, getSupportFragmentManager(),cTotalVisited, cTotalVisited_All,sp);

				}
			}catch (JSONException e){
				cCode = "0";

			}catch (Exception e){

			}

			return null;
		}

		@Override
		protected void onPostExecute(Void aVoid) {
			super.onPostExecute(aVoid);

			if(cCode.equals("1")) {
				rv_slider.setAdapter(adapter_slider);
			}else{
				Toast.makeText(getApplicationContext(), "Hubungi Admin untuk isi Target", Toast.LENGTH_LONG).show();
				finish();

			}
		}
	}
	

	private class AsyncTask_LoadProfile extends AsyncTask<Void, Void, Void> {
		Olx_DialogFragmentProgress pDialog;
		String result, cCode, cNama_Pegawai, cUrl_ImgProfilePic, cTotalVisited;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
//			pDialog = new Olx_DialogFragmentProgress();
//			pDialog.show(getSupportFragmentManager(), "");
			Toast.makeText(getApplicationContext(), "Loading Data", Toast.LENGTH_SHORT).show();
			
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {

				Olx_ServiceHandlerJSON sh = new Olx_ServiceHandlerJSON();
				JSONObject jObj = sh.json_get_pic_profile(sp.getString(
						Parameter_Collections.SH_ID_PEGAWAI, ""));
				JSONObject jObj_Info = sh.json_get_target_info(sp.getString(
						Parameter_Collections.SH_ID_PEGAWAI, ""));

				cCode = jObj.getString(Parameter_Collections.TAG_JSON_CODE);

				if (cCode.equals("1")) {
					JSONObject jObj_Data = jObj
							.getJSONObject(Parameter_Collections.TAG_DATA);
					cNama_Pegawai = jObj_Data
							.getString(Parameter_Collections.TAG_NAMA_PEGAWAI);

					String img_no_data = jObj_Data.getString(Parameter_Collections.TAG_ARRAY_IMAGES);
					
					if(img_no_data.equals("no data")){
						cUrl_ImgProfilePic = "";
						
					}else{
						JSONArray jArray_Data = jObj_Data
								.getJSONArray(Parameter_Collections.TAG_ARRAY_IMAGES);
						for (int i = 0; i < jArray_Data.length(); i++) {
							JSONObject c = jArray_Data.getJSONObject(i);

							cUrl_ImgProfilePic = Parameter_Collections.URL_GAMBAR_THUMB
									+ c.getString(Parameter_Collections.TAG_NAMA_IMAGE);
						}
					}

				} else {

				}

				JSONObject jObj_TotalStoreVisited = sh.json_get_visit_store(id_pegawai);

				Log.e("JSON RESULT = ", jObj.toString());

				try {
					String total = jObj_TotalStoreVisited.getString(Parameter_Collections.TAG_TOTAL_VISIT_TOKO);
					String max = jObj_TotalStoreVisited.getString(Parameter_Collections.TAG_TOTAL_VISIT_MAX_TOKO);

					cTotalVisited = total + " / " + max;
				} catch (JSONException e) {

					cTotalVisited= "0 / 25";
				}

//				adapter_slider = new RecyclerAdapter_Slider(cNama_Pegawai, cUrl_ImgProfilePic, getApplicationContext(),
//						activity, getSupportFragmentManager(),cTotalVisited,sp);

			} catch (Exception e) {

			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
//			pDialog.dismiss();

//			FragmentManager fm = getSupportFragmentManager();
//			Fragment fragment = new Olx_Fragment_MenuUtama();
//			fm.beginTransaction().replace(R.id.frame_container, fragment)
//					.commit();
			rv_slider.setAdapter(adapter_slider);
		}
	}

}
