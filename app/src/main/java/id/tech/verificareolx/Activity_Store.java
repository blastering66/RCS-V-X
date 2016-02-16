package id.tech.verificareolx;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import id.tech.util.Parameter_Collections;
import id.tech.util.RecyclerAdapter_Region;
import id.tech.util.RecyclerAdapter_StoreList_Review;
import id.tech.util.RecyclerAdapter_StoreList_Search;
import id.tech.util.RowData_Toko;
import id.tech.util.ServiceHandlerJSON;

/**
 * Created by RebelCreative-A1 on 30/11/2015.
 */
public class Activity_Store extends AppCompatActivity implements RecyclerAdapter_StoreList_Search.onStoreSelected_Listener,
        RecyclerAdapter_Region.onRegionSelection_Listener {
    EditText ed_Search;
    private RecyclerView rv, rv_pilihan;
    private RecyclerView.Adapter adapter, adapter_pilihan;
    private RecyclerView.LayoutManager layoutManager, layoutManager_pilihan;
    private Button btn;
    private TextView tv;
    List<RowData_Toko> data_toko, data_toko_selected, data_toko_dumb;
    Activity activity;
    private SharedPreferences sp;
    private String id_pegawai;
    List<RowData_Toko> data_regions;

    boolean sudah_mengisi=false;

    @Override
    public void getRowData_ListVisitStore(List<RowData_Toko> data) {
        data_toko_selected = data;
    }

    @Override
    public void onStoreSelected(int size) {
        if (size == 25) {
            btn.setVisibility(View.VISIBLE);

            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(sudah_mengisi){
                        Toast.makeText(getApplicationContext(), "Visit List Today Already Created", Toast.LENGTH_LONG).show();
                    }else{
                        new AsyncTask_Submit_VisitList().execute();
                    }



                }
            });

            tv.setVisibility(View.GONE);
        } else {
            tv.setText("Total Store = " + String.valueOf(size) + " / 25");
        }

    }

    @Override
    public void onRegionSelected(List<RowData_Toko> data, boolean checked) {

        for (int i = 0; i < data.size(); i++) {
            Log.e("Kode Region Selected", data.get(i).region_toko);
            for (int j = 0; j < data_toko.size(); j++) {
                Log.e(data.get(i).region_toko + "-", data_toko.get(j).region_toko);
                if (!data.get(i).region_toko.equals(data_toko.get(j).region_toko)) {
                    data_toko.remove(j);
//                        data_toko_dumb.add(data_toko.get(j));
                    new AsyncTask_RefreshRV().execute();
                }
            }
        }


    }

    private class AsyncTask_RefreshRV extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            adapter.notifyDataSetChanged();
//            rv_pilihan.setAdapter(adapter_pilihan);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_search);

        activity = this;

        sp = getSharedPreferences(Parameter_Collections.SH_NAME,
                Context.MODE_PRIVATE);

        id_pegawai = sp.getString(Parameter_Collections.SH_ID_PEGAWAI, "");
        data_toko_dumb = new ArrayList<>();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Store Search");

        ed_Search = (EditText) findViewById(R.id.ed_search);
        ed_Search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    new AsyncTask_GetAllStore_By_Location().execute();
                    return true;
                }
                return false;
            }
        });
        btn = (Button) findViewById(R.id.btn);
        tv = (TextView) findViewById(R.id.tv_total);

        rv = (RecyclerView) findViewById(R.id.rv);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        rv.setLayoutManager(layoutManager);

        rv_pilihan = (RecyclerView) findViewById(R.id.rv_checkbox);
//        layoutManager_pilihan = new LinearLayoutManager(getApplicationContext());
        layoutManager_pilihan = new GridLayoutManager(getApplicationContext(), 2);
        rv_pilihan.setLayoutManager(layoutManager_pilihan);


        new AsyncTask_Get_StoreListVisit().execute();
        new AsyncTask_GetAllStore().execute();
        new AsyncTask_GetRegionsSystem().execute();

    }

    private class AsyncTask_Submit_VisitList extends AsyncTask<Void, Void, Void> {
        public String cCode = "0";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
//            data_toko_selected
            ServiceHandlerJSON sh = new ServiceHandlerJSON();
            JSONObject jObj = sh.json_insert_store_list_visit(id_pegawai, data_toko_selected);

            try {
                cCode = jObj.getString(Parameter_Collections.TAG_JSON_CODE);
            } catch (JSONException e) {

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if (cCode.equals("1")) {
                Intent intent = new Intent(getApplicationContext(), Activity_Store_Review.class);
                startActivity(intent);
                finish();
            } else {

            }

        }
    }

    private class AsyncTask_GetRegionsSystem extends AsyncTask<Void, Void, Void> {
        String total_data;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            data_regions = new ArrayList<>();
        }

        @Override
        protected Void doInBackground(Void... params) {
            ServiceHandlerJSON sh = new ServiceHandlerJSON();

            try {
                JSONObject jObj = sh.json_get_region_system();
                total_data = jObj.getString(Parameter_Collections.TAG_TOTAL_VISIT_MAX_TOKO);
                if (Integer.parseInt(total_data) > 0) {JSONArray jArray = jObj.getJSONArray(Parameter_Collections.TAG_DATA);
                    for(int i=0; i < jArray.length();i++) {
                        JSONObject c = jArray.getJSONObject(i);
                        String region = c.getString(Parameter_Collections.TAG_REGION_TOKO);
                        data_regions.add(new RowData_Toko("", "", region, region));
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

            if(Integer.parseInt(total_data) > 0){
                adapter_pilihan = new RecyclerAdapter_Region(activity, getApplicationContext(), data_regions);
                rv_pilihan.setAdapter(adapter_pilihan);
            }else{
                Toast.makeText(getApplicationContext(), "Can not load Region from system", Toast.LENGTH_LONG).show();
            }

        }
    }

    private class AsyncTask_GetAllStore extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            data_toko = new ArrayList<>();
        }

        @Override
        protected Void doInBackground(Void... params) {
            ServiceHandlerJSON sh = new ServiceHandlerJSON();

            JSONObject jObj = sh.json_get_all_store();

            Log.e("JSON RESULT = ", jObj.toString());

            try {
                JSONArray jsonArray = jObj.getJSONArray(Parameter_Collections.TAG_DATA);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject c = jsonArray.getJSONObject(i);

                    String id = c.getString(Parameter_Collections.TAG_ID_TOKO);
                    String kode_toko = c.getString(Parameter_Collections.TAG_KODE_TOKO);
                    String nama_toko = c.getString(Parameter_Collections.TAG_NAMA_TOKO);
                    String region_toko = c.getString(Parameter_Collections.TAG_REGION_TOKO);

                    data_toko.add(new RowData_Toko(id, kode_toko, nama_toko, region_toko, false));

                }
            } catch (JSONException e) {

            }
            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if (data_toko.size() > 0) {
                Toast.makeText(getApplicationContext(), "Get Store Berhasil ", Toast.LENGTH_LONG).show();
                adapter = new RecyclerAdapter_StoreList_Search(activity, getApplicationContext(), data_toko);
                rv.setAdapter(adapter);
            } else {
                Toast.makeText(getApplicationContext(), "Error Get Store", Toast.LENGTH_LONG).show();
            }
        }
    }

    private class AsyncTask_GetAllStore_By_Location extends AsyncTask<Void, Void, Void> {
        String cSearch;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            data_toko = new ArrayList<>();
            cSearch = ed_Search.getText().toString();
        }

        @Override
        protected Void doInBackground(Void... params) {
            ServiceHandlerJSON sh = new ServiceHandlerJSON();

            JSONObject jObj = sh.json_get_all_store();

            Log.e("JSON RESULT = ", jObj.toString());

            try {
                JSONArray jsonArray = jObj.getJSONArray(Parameter_Collections.TAG_DATA);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject c = jsonArray.getJSONObject(i);

                    String id = c.getString(Parameter_Collections.TAG_ID_TOKO);
                    String kode_toko = c.getString(Parameter_Collections.TAG_KODE_TOKO);
                    String nama_toko = c.getString(Parameter_Collections.TAG_NAMA_TOKO);
                    String region_toko = c.getString(Parameter_Collections.TAG_REGION_TOKO);
                    if (cSearch.equals(region_toko)) {
                        data_toko.add(new RowData_Toko(id, kode_toko, nama_toko, region_toko, false));
                    }
                }
            } catch (JSONException e) {

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (data_toko.size() > 0) {
                Toast.makeText(getApplicationContext(), "Store Berhasil ", Toast.LENGTH_LONG).show();
                adapter = new RecyclerAdapter_StoreList_Search(activity, getApplicationContext(), data_toko);
                rv.setAdapter(adapter);
            } else {
                Toast.makeText(getApplicationContext(), "Error Get Store", Toast.LENGTH_LONG).show();
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

    private class AsyncTask_Get_StoreListVisit extends AsyncTask<Void, Void, Void> {
        List<RowData_Toko> data;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            data = new ArrayList<>();
        }

        @Override
        protected Void doInBackground(Void... params) {
            ServiceHandlerJSON sh = new ServiceHandlerJSON();
            JSONObject jObj = sh.json_get_visit_store(id_pegawai);

            Log.e("JSON RESULT = ", jObj.toString());

            try {
                JSONArray jsonArray = jObj.getJSONArray(Parameter_Collections.TAG_DATA);
                for (int i = 0; i < jsonArray.length(); i++) {
                    sudah_mengisi = true;
                }
            } catch (JSONException e) {

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            adapter = new RecyclerAdapter_StoreList_Review(activity, getApplicationContext(), data);
            rv.setAdapter(adapter);
        }
    }
}
