package id.tech.verificareolx;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import id.tech.verificareolx.R;
import id.tech.util.Parameter_Collections;
import id.tech.util.RowData_InfoToko;
import id.tech.util.ServiceHandlerJSON;

import com.squareup.picasso.Picasso;
import com.viewpagerindicator.CirclePageIndicator;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ImageView.ScaleType;
import android.widget.Toast;
import common.activities.SampleActivityBase;

public class InfoToko_Activity extends SampleActivityBase {
	SharedPreferences spf;
	ViewPager viewpager;
	CirclePageIndicator indicator;
	Button btn_tambahfoto, btn_edit, btn_save, btn_infostok;
	EditText ed_NamaToko, ed_ALamatToko, ed_TlpToko;
	EditText ed_AreaToko, ed_KotaToko, ed_DescToko;
	
	private String kode_toko, id_pegawai, id_toko;

	ArrayList<RowData_InfoToko> datanya;

//	String[] img_gallery;
	ArrayList<String> url_img_gallery;
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		new Async_GetInfoToko().execute();
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
		return true;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_infotoko);

		ActionBar ac = getSupportActionBar();
		ac.setTitle("Info Toko");
		// ac.setDisplayShowHomeEnabled(true);
		ac.setDisplayHomeAsUpEnabled(true);

		spf = getSharedPreferences(Parameter_Collections.SH_NAME,
				Context.MODE_PRIVATE);
		kode_toko = spf.getString(Parameter_Collections.SH_KODE_TOKO, "");
		id_pegawai = spf.getString(Parameter_Collections.SH_ID_PEGAWAI, "");

		viewpager = (ViewPager) findViewById(R.id.viewPager);
		indicator = (CirclePageIndicator) findViewById(R.id.indicator);
		
		ed_NamaToko = (EditText) findViewById(R.id.tv_namatoko);
		ed_ALamatToko = (EditText) findViewById(R.id.tv_alamattoko);
		ed_TlpToko = (EditText) findViewById(R.id.tv_tlptoko);
		ed_AreaToko = (EditText) findViewById(R.id.tv_areatoko);
		ed_KotaToko= (EditText) findViewById(R.id.tv_kotatoko);
		ed_DescToko= (EditText) findViewById(R.id.tv_desctoko);
		
		btn_tambahfoto = (Button) findViewById(R.id.btn_tambahfoto);
		btn_edit = (Button) findViewById(R.id.btn_edit_data);
		btn_save = (Button) findViewById(R.id.btn_save_data);
		btn_infostok= (Button) findViewById(R.id.btn_infostok);
		
		btn_infostok.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent load = new Intent(getApplicationContext(), CekProduk_Activity.class);
				startActivity(load);
			}
		});
		
		btn_tambahfoto.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent load = new Intent(getApplicationContext(), DialogTambahFoto.class);
				load.putExtra("id_toko", id_toko);
				startActivity(load);
			}
		});
		
		btn_save.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new Async_UpdateInfoToko().execute();
			}
		});
		
		btn_edit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				ed_NamaToko.setEnabled(true);
				ed_ALamatToko.setEnabled(true);
				ed_TlpToko.setEnabled(true);
				ed_AreaToko.setEnabled(true);
				ed_KotaToko.setEnabled(true);
				ed_DescToko.setEnabled(true);
				
				btn_tambahfoto.setVisibility(View.VISIBLE);
				btn_save.setVisibility(View.VISIBLE);
				btn_edit.setVisibility(View.GONE);
			}
		});

//		new Async_GetInfoToko().execute();
	}
	
	private class Async_UpdateInfoToko extends AsyncTask<Void, Void, Void>{
		private String cAlamat, cDesc, cArea, cTlp, cKota; 
		private String cCode, cMessage;
		DialogFragmentProgress pDialog;
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pDialog = new DialogFragmentProgress();
			pDialog.show(getSupportFragmentManager(), "");
			
			cAlamat = ed_ALamatToko.getText().toString();
			cDesc = ed_DescToko.getText().toString();
			cArea = ed_AreaToko.getText().toString();
			cTlp = ed_TlpToko.getText().toString();
			cKota = ed_KotaToko.getText().toString();
			
		}
		
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try{
				ServiceHandlerJSON sh = new ServiceHandlerJSON();
				JSONObject jobj = sh.json_update_tokoinfo(id_toko, cAlamat, cDesc, cArea, cTlp, cKota);
				
				cCode = jobj.getString(Parameter_Collections.TAG_JSON_CODE);
				
				if(!cCode.equals("1")){
					cMessage = "Something Wrong";
				}
//			}catch(JSONException e){
				
			}catch(Exception e){
				
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			pDialog.dismiss();
			if(cCode.equals("1")){
				Toast.makeText(getApplicationContext(), "Edit Data Success", Toast.LENGTH_LONG).show();
				finish();
			}else{
				Toast.makeText(getApplicationContext(), "Edit Data Failed", Toast.LENGTH_LONG).show();
			}
		}
		
	}

	private class Async_GetInfoToko extends AsyncTask<Void, Void, Void> {
		DialogFragmentProgress pDialog;
		String cCode, cMessage;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pDialog = new DialogFragmentProgress();
			pDialog.show(getSupportFragmentManager(), "");

			datanya = new ArrayList<RowData_InfoToko>();
			url_img_gallery = new ArrayList<String>();
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try{
				ServiceHandlerJSON sh = new ServiceHandlerJSON();
				JSONObject jobj = sh.json_get_tokoinfo(kode_toko);
				
				cCode = jobj.getString(Parameter_Collections.TAG_JSON_CODE);
				
				if(cCode.equals("1")){
					JSONObject c = jobj
							.getJSONObject(Parameter_Collections.TAG_DATA);
					
					String id_Toko = c.getString(Parameter_Collections.TAG_ID_TOKO);
					
					id_toko = id_Toko;
					
					String nama_Toko = c.getString(Parameter_Collections.TAG_NAMA_TOKO);
					String alamat_Toko = c.getString(Parameter_Collections.TAG_ALAMAT_TOKO);
					String desc_Toko = c.getString(Parameter_Collections.TAG_DESC_TOKO);
					String area_Toko = c.getString(Parameter_Collections.TAG_AREA_TOKO);
					String tlp_Toko = c.getString(Parameter_Collections.TAG_TLP_TOKO);
					String kota_Toko = c.getString(Parameter_Collections.TAG_KOTA_TOKO);
					
					datanya.add(new RowData_InfoToko(id_Toko, nama_Toko, alamat_Toko, tlp_Toko, 
							kode_toko, area_Toko, desc_Toko, kota_Toko));
					
					JSONArray jArray2 = c
							.getJSONArray(Parameter_Collections.TAG_ARRAY_IMAGES);

					if(jArray2.length() > 0){
						for(int i=0; i<jArray2.length(); i++){
							JSONObject z = jArray2.getJSONObject(i);
							String url = z.getString(Parameter_Collections.TAG_NAMA_IMAGE);
							url_img_gallery.add(Parameter_Collections.URL_GAMBAR + url);
						}
					}
					
					
				}
			}catch(Exception e){
				
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			pDialog.dismiss();

			if(cCode.equals("1")){
				ed_NamaToko.setText(datanya.get(0).nama_toko);
				ed_ALamatToko.setText(datanya.get(0).alamat_toko);
				ed_TlpToko.setText(datanya.get(0).tlp);
				ed_AreaToko.setText(datanya.get(0).area);
				ed_KotaToko.setText(datanya.get(0).kota);
				ed_DescToko.setText(datanya.get(0).desc);
				
				viewpager.setAdapter(new MyVPAdapter(getSupportFragmentManager()));
				indicator.setViewPager(viewpager);

				OnPageChangeListener pageChangeListener = new ViewPager.SimpleOnPageChangeListener();
				indicator.setOnPageChangeListener(pageChangeListener);
			}
			
		}
	}

	private class MyVPAdapter extends FragmentStatePagerAdapter {

		public MyVPAdapter(FragmentManager fm) {
			super(fm);
			// TODO Auto-generated constructor stub
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return url_img_gallery.size();
		}

		@Override
		public Fragment getItem(int arg0) {
			// TODO Auto-generated method stub
			return VPGallery.create(arg0, url_img_gallery);
		}

	}

	private static class VPGallery extends Fragment {
		static String ARG_PAGE = "page";
		static String ARG_ARRAY = "array";

		public static VPGallery create(int position, ArrayList<String> array_img) {
			VPGallery fragment = new VPGallery();
			Bundle bundle = new Bundle();
			bundle.putInt(ARG_PAGE, position);
//			bundle.putStringArray(ARG_ARRAY, array_img);
			bundle.putStringArrayList(ARG_ARRAY, array_img);
			fragment.setArguments(bundle);
			return fragment;
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			final int posisi = getArguments().getInt(ARG_PAGE);
//			final String[] url_gambar = getArguments()
//					.getStringArray(ARG_ARRAY);
			final ArrayList<String> url = getArguments().getStringArrayList(ARG_ARRAY);

			ImageView img = new ImageView(getActivity());
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			img.setScaleType(ScaleType.CENTER_CROP);
			img.setLayoutParams(params);

			
			
			Picasso.with(getActivity()).load(url.get(posisi)).into(img);
			
			return img;
		}
	}
}
