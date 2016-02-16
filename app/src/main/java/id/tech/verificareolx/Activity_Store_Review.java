package id.tech.verificareolx;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
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
import id.tech.util.RecyclerAdapter_StoreList_Review;
import id.tech.util.RecyclerAdapter_StoreList_Search;
import id.tech.util.RowData_Toko;
import id.tech.util.ServiceHandlerJSON;

/**
 * Created by RebelCreative-A1 on 30/11/2015.
 */
public class Activity_Store_Review extends AppCompatActivity {
    private Activity activity;
    private RecyclerView rv;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private SharedPreferences sp;
    private String id_pegawai;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_list);
        activity = this;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Visit Store List");

        sp = getSharedPreferences(Parameter_Collections.SH_NAME,
                Context.MODE_PRIVATE);

        id_pegawai = sp.getString(Parameter_Collections.SH_ID_PEGAWAI, "");

        rv = (RecyclerView) findViewById(R.id.rv);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        rv.setLayoutManager(layoutManager);


        new AsyncTask_Get_StoreListVisit().execute();
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
                    JSONObject c = jsonArray.getJSONObject(i);

                    String nama_toko = c.getString(Parameter_Collections.TAG_NAMA_TOKO);
                    String mark = c.getString(Parameter_Collections.TAG_MARK);

                    if(mark.equals("0")){
                        data.add(new RowData_Toko(nama_toko, false));
                    }else{
                        data.add(new RowData_Toko(nama_toko, true));
                    }


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
