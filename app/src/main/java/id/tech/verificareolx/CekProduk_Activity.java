package id.tech.verificareolx;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

import id.tech.verificareolx.R;
import id.tech.util.CustomAdapter_ProdukToko;
import id.tech.util.Parameter_Collections;
import id.tech.util.Public_Functions;
import id.tech.util.RecyclerAdapterProduk;
import id.tech.util.RecyclerItemClickListener;
import id.tech.util.RowData_Produk;
import id.tech.util.ServiceHandlerJSON;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnItemTouchListener;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.animation.OvershootInterpolator;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class CekProduk_Activity extends ActionBarActivity {
	// ListView ls;
	// CustomAdapter_ProdukToko adapter;
	SharedPreferences spf;
	private RecyclerView rv;
	private RecyclerView.Adapter adapter;
	private RecyclerView.LayoutManager layoutManager;

	TextView tv_total_stok, tv_total_penjualan;
	private int total_stok, total_penjualan;
	ArrayList<RowData_Produk> data;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cekproduk);

		spf = getSharedPreferences(Parameter_Collections.SH_NAME,
				Context.MODE_PRIVATE);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle("All Produk");

		tv_total_stok = (TextView) findViewById(R.id.txt_total_stok);
		tv_total_penjualan = (TextView) findViewById(R.id.txt_total_penjualan);

		rv = (RecyclerView) findViewById(R.id.recycler_view);
		rv.setHasFixedSize(true);
		rv.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemLongClickListenerRidho() {
			
			@Override
			public void onItemLongClickRidho(View view, int position) {
				// TODO Auto-generated method stub
								
				Log.e("imei", data.get(position).imei_produk);
				
				if(data.get(position).status_produk.equals("1")){
					Intent intent = new Intent(getApplicationContext(), DialogTransfer.class);
//					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					intent.putExtra("imei", data.get(position).imei_produk);
					intent.putExtra("id_produk_toko", data.get(position).id_produk_toko);
//					startActivity(intent);
					startActivityForResult(intent, 111);
				}else{
					Toast.makeText(getApplicationContext(), 
							"Tidak bisa di Transfer, Produk sudah Terjual", Toast.LENGTH_LONG).show();
				}
				
			}
		}));
		
		// rv.setItemAnimator(new SlideInLeftAnimator());
		// rv.getItemAnimator().setAddDuration(1000);
		// rv.getItemAnimator().setRemoveDuration(1000);
		// rv.getItemAnimator().setMoveDuration(1000);
		// rv.getItemAnimator().setChangeDuration(1000);

		// layoutManager = new LinearLayoutManager(getApplicationContext());
		layoutManager = new GridLayoutManager(getApplicationContext(), 2);
		rv.setLayoutManager(layoutManager);

		if (Public_Functions.isNetworkAvailable(getApplicationContext())) {
//			boolean b = true;
//			if (b) {
				new Async_GetAllProduk().execute();
			}else {
				Toast.makeText(getApplicationContext(),
						"No Internet Connection, Cek Your Network",
						Toast.LENGTH_LONG).show();
			}

		
	}
	
	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO Auto-generated method stub
		if(arg1 == RESULT_OK && arg0 == 111){			
				new Async_GetAllProduk().execute();			
		}
	}

	private class Async_GetAllProduk extends AsyncTask<Void, Void, Void> {
		DialogFragmentProgress pDialog;
		
		String cCode;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
//			pDialog = new DialogFragmentProgress();
//			pDialog.show(getSupportFragmentManager(), "");
			Toast.makeText(getApplicationContext(), "Loading Data", Toast.LENGTH_LONG).show();
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				Thread.sleep(1000);

				ServiceHandlerJSON sh = new ServiceHandlerJSON();

				JSONObject jObj = sh.json_cek_stok(spf.getString(
						Parameter_Collections.SH_KODE_TOKO, ""));
				
				Log.e("result cek produk", jObj.toString());

				cCode = jObj.getString(Parameter_Collections.TAG_DATA);
				if (!cCode.equals("no data")) {
					JSONArray jArray = jObj
							.getJSONArray(Parameter_Collections.TAG_DATA);

					data = new ArrayList<RowData_Produk>();

					total_stok = jArray.length();
					total_penjualan = 0;

					for (int i = 0; i < jArray.length(); i++) {
						JSONObject c = jArray.getJSONObject(i);
						String id_produk_toko = c
								.getString(Parameter_Collections.TAG_ID_PRODUK_TOKO);
						
						String nama_produk = c
								.getString(Parameter_Collections.TAG_NAMA_PRODUK);
						String harga_produk = c
								.getString(Parameter_Collections.TAG_KODE_TOKO);
						String imei_produk = c
								.getString(Parameter_Collections.TAG_IMEI);
						String imei2_produk = c
								.getString(Parameter_Collections.TAG_IMEI2);
						String status_produk = c
								.getString(Parameter_Collections.TAG_STATUS_PRODUK);

						if (!status_produk.equals("1")) {
							total_penjualan += 1;
						}

						data.add(new RowData_Produk(id_produk_toko,nama_produk, harga_produk,
								imei_produk,imei2_produk, status_produk));
					}
				}

				total_stok = total_stok - total_penjualan;
				// data.add(new RowData_Produk("Ridho", "Rp. 5000000",
				// "adasdsdad", "1"));
				// data.add(new RowData_Produk("Ridho", "Rp. 5000000",
				// "adasdsdad", "1"));
				// data.add(new RowData_Produk("Ridho", "Rp. 5000000",
				// "adasdsdad", "2"));
				// data.add(new RowData_Produk("Ridho", "Rp. 5000000",
				// "adasdsdad", "2"));
				// data.add(new RowData_Produk("Ridho", "Rp. 5000000",
				// "adasdsdad", "1"));

			} catch (Exception e) {

			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
//			pDialog.dismiss();

			if (!cCode.equals("no data")) {
				adapter = new RecyclerAdapterProduk(data,
						getApplicationContext());
				rv.setAdapter(adapter);

				tv_total_stok.setText(String.valueOf(total_stok));
				tv_total_penjualan.setText(String.valueOf(total_penjualan));
			}else{
				Toast.makeText(getApplicationContext(), "No Data", Toast.LENGTH_LONG).show();
			}
			// rv.setAdapter(new AlphaInAnimationAdapter(adapter));

			// AlphaInAnimationAdapter alphaAdapter = new
			// AlphaInAnimationAdapter(
			// adapter);
			// alphaAdapter.setDuration(1000);
			// rv.setAdapter(alphaAdapter);

			// not working
			// AlphaInAnimationAdapter alphaAdapter = new
			// AlphaInAnimationAdapter(adapter);
			// alphaAdapter.setInterpolator(new OvershootInterpolator());
			// rv.setAdapter(alphaAdapter);

			// AlphaInAnimationAdapter alphaAdapter = new
			// AlphaInAnimationAdapter(adapter);
			// rv.setAdapter(new ScaleInAnimationAdapter(alphaAdapter));
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
