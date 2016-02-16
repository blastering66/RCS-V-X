package id.tech.verificareolx;

import android.app.Activity;
import android.content.Intent;
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
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import id.tech.util.Parameter_Collections;
import id.tech.util.RecyclerAdapter_StockList_Search;
import id.tech.util.RecyclerAdapter_StoreList_Search;
import id.tech.util.RowData_Stock;
import id.tech.util.RowData_Toko;
import id.tech.util.ServiceHandlerJSON;

/**
 * Created by RebelCreative-A1 on 30/11/2015.
 */
public class Activity_Stock extends AppCompatActivity {
    private EditText ed_Search;
    private RecyclerView rv;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private Button btn;
    private TextView tv;
    private List<RowData_Stock> data;
    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_search);
        activity = this;

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Stock Search");

        ed_Search = (EditText)findViewById(R.id.ed_search);
        btn= (Button)findViewById(R.id.btn);
        tv= (TextView)findViewById(R.id.tv_total);

        rv = (RecyclerView)findViewById(R.id.rv);


        new AsyncTask_GetProduct().execute();
    }

    private class AsyncTask_GetProduct extends AsyncTask<Void,Void,Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            data = new ArrayList<>();
        }

        @Override
        protected Void doInBackground(Void... params) {
//            data.add(new RowData_Toko("","","Product 1", false));
//            data.add(new RowData_Toko("","","Product 2", false));
//            data.add(new RowData_Toko("","","Product 3", false));
//            data.add(new RowData_Toko("","","Product 4", false));
//            data.add(new RowData_Toko("","","Product 5", false));
            ServiceHandlerJSON sh = new ServiceHandlerJSON();

            JSONObject jObj = sh.json_get_all_stock();

            Log.e("JSON RESULT = ", jObj.toString());

            try{
                JSONArray jsonArray = jObj.getJSONArray(Parameter_Collections.TAG_DATA);
                for(int i=0; i<jsonArray.length();i++){
                    JSONObject c = jsonArray.getJSONObject(i);

                    String id = c.getString(Parameter_Collections.TAG_ID_STOCK);
                    String nama_stock = c.getString(Parameter_Collections.TAG_NAME_STOCK);
                    String min_stock = c.getString(Parameter_Collections.TAG_MIN_STOCK);
                    String unit_stock = c.getString(Parameter_Collections.TAG_UNIT_STOCK);

                    data.add(new RowData_Stock(id,nama_stock,min_stock,unit_stock));

                }
            }catch (JSONException e){

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if(data.size() >0){
                layoutManager = new LinearLayoutManager(getApplicationContext());
                rv.setLayoutManager(layoutManager);
                adapter = new RecyclerAdapter_StockList_Search(activity, getApplicationContext(), data);
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


            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
