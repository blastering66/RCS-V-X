package id.tech.verificareolx;

import android.app.Activity;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import id.tech.util.Parameter_Collections;
import id.tech.util.RecyclerAdapter_Promo;
import id.tech.util.RowData_Promo;
import id.tech.util.ServiceHandlerJSON;

/**
 * Created by RebelCreative-A1 on 10/12/2015.
 */
public class Activity_Promo extends AppCompatActivity {
    RecyclerView rv;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;
    private List<RowData_Promo> data;
    Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promo);

        getSupportActionBar().setTitle("All Promo");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        activity = this;

        rv = (RecyclerView)findViewById(R.id.rv);

        new AsyncTask_GetPromo().execute();
    }

    private class AsyncTask_GetPromo extends AsyncTask<Void,Void,Void>{
        private List<RowData_Promo> data;
        String total_data;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            data = new ArrayList<>();
        }

        @Override
        protected Void doInBackground(Void... params) {
            ServiceHandlerJSON sh = new ServiceHandlerJSON();
            JSONObject jObj = sh.json_get_promo();
            try{
                total_data = jObj.getString(Parameter_Collections.TAG_TOTAL_VISIT_MAX_TOKO);
                if(Integer.parseInt(total_data) > 0){
                    JSONArray jArray = jObj.getJSONArray(Parameter_Collections.TAG_DATA);
                    for(int i=0; i < jArray.length();i++){
                        JSONObject c = jArray.getJSONObject(i);

                        String id_promo = c.getString(Parameter_Collections.TAG_ID_PROMO);
                        String stock_name = c.getString(Parameter_Collections.TAG_NAME_STOCK);
                        String promo_name = c.getString(Parameter_Collections.TAG_NAME_PROMO);
                        String promo_start_date = c.getString(Parameter_Collections.TAG_START_PROMO);
                        String promo_end_date = c.getString(Parameter_Collections.TAG_END_PROMO);

                        data.add(new RowData_Promo(id_promo,stock_name,promo_name,promo_start_date,promo_end_date));
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
                adapter = new RecyclerAdapter_Promo(activity,getApplicationContext(), data);

                rv.setLayoutManager(layoutManager);
                rv.setAdapter(adapter);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
