package id.tech.verificareolx;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import id.tech.util.Parameter_Collections;
import id.tech.util.RowData_JenisOutlet;
import id.tech.util.Olx_ServiceHandlerJSON;

/**
 * Created by macbook on 2/3/16.
 */
public class Olx_DataOutlet_Activity extends AppCompatActivity{
    private TextView tv_kodeToko;
    private EditText ed_outlet_lokasi, ed_outlet_address,ed_outlet_email, ed_outlet_phone, ed_outlet_region;
    private Button btn;
    private Spinner spinner_jenis_outlet;
    private SharedPreferences spf;
    private String nama_outlet, lati, longi;
    private List<RowData_JenisOutlet> array_jenis_outlet;
    private String selected_id_jenis_outlet;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dataoutlet_olx);
        ActionBar ac = getSupportActionBar();
        ac.setTitle("Data Outlet");
        ac.setDisplayHomeAsUpEnabled(true);

        spf = getSharedPreferences(Parameter_Collections.SH_NAME, Context.MODE_PRIVATE);
//        kode_outlet = spf.getString(Parameter_Collections.SH_KODE_OUTLET, "0");
        nama_outlet = spf.getString(Parameter_Collections.SH_NAMA_OUTLET, "0");
        lati = spf.getString(Parameter_Collections.TAG_LATITUDE_NOW, "0");
        longi = spf.getString(Parameter_Collections.TAG_LONGITUDE_NOW, "0");
        getALlView();
    }

    private void getALlView(){
        ed_outlet_lokasi = (EditText)findViewById(R.id.ed_outlet_lokasi);
        ed_outlet_lokasi.setText(nama_outlet);
        ed_outlet_region = (EditText)findViewById(R.id.ed_outlet_region);
        ed_outlet_email= (EditText)findViewById(R.id.ed_store_email);
        ed_outlet_address = (EditText)findViewById(R.id.ed_outlet_address);
        ed_outlet_phone = (EditText)findViewById(R.id.ed_outlet_phone);
        btn = (Button)findViewById(R.id.btn);

        progressBar = (ProgressBar)findViewById(R.id.pg);
        spinner_jenis_outlet = (Spinner)findViewById(R.id.spinner_jenis_outlet);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AsyncTask_UpdateDataOutlet().execute();
            }
        });

        new AsyncTask_GetJenis_Outlet().execute();
    }

    private class AsyncTask_GetJenis_Outlet extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... params) {
            Olx_ServiceHandlerJSON sh = new Olx_ServiceHandlerJSON();
            JSONObject jobj = sh.json_get_jenis_outlet();

            try{
                String total_data = jobj.getString(Parameter_Collections.TAG_TOTAL_DATA);

                if(!total_data.equals("0")){
                    JSONArray jsonArray = jobj.getJSONArray(Parameter_Collections.TAG_DATA);

                    array_jenis_outlet = new ArrayList<RowData_JenisOutlet>();
                    for(int i=0; i < jsonArray.length();i++){
                        JSONObject c = jsonArray.getJSONObject(i);

                        String id_jenis_outlet = c.getString(Parameter_Collections.TAG_ID_JENIS_OUTLET);
                        String nama_jenis_outlet = c.getString(Parameter_Collections.TAG_NAMA_JENIS_OUTLET);

                        array_jenis_outlet.add(new RowData_JenisOutlet(id_jenis_outlet, nama_jenis_outlet));

                    }
                }
            }catch (JSONException e){

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            progressBar.setVisibility(View.GONE);

            String[] values = new String[array_jenis_outlet.size()];

            for(int i=0; i<array_jenis_outlet.size(); i++){
                values[i] = array_jenis_outlet.get(i).nama_jenis_outlet;
            }
            ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item,
                    values);
            spinnerAdapter.setDropDownViewResource(R.layout.spinner_item );
            spinner_jenis_outlet.setAdapter(spinnerAdapter);

            spinner_jenis_outlet.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    selected_id_jenis_outlet = array_jenis_outlet.get(position).id_jenis_outlet;
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    selected_id_jenis_outlet = array_jenis_outlet.get(0).id_jenis_outlet;
                }
            });
        }
    }

    private class AsyncTask_UpdateDataOutlet extends AsyncTask<Void, Void,Void>{
        Olx_DialogFragmentProgress dialogProgress;
        String outlet_Phone, outlet_Address,outlet_Email, outlet_Lokasi, outlet_Region;
        String row_count, error_message, kode_outlet;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialogProgress = new Olx_DialogFragmentProgress();
            dialogProgress.show(getSupportFragmentManager(), "");

            outlet_Phone = ed_outlet_phone.getText().toString();
            outlet_Address = ed_outlet_address.getText().toString();
            outlet_Lokasi = ed_outlet_lokasi.getText().toString();
            outlet_Email = ed_outlet_email.getText().toString();
            outlet_Region = ed_outlet_region.getText().toString();
        }

        @Override
        protected Void doInBackground(Void... params) {
            Olx_ServiceHandlerJSON sh = new Olx_ServiceHandlerJSON();

            JSONObject jObj = sh.json_update_dataoutlet(outlet_Address, outlet_Phone, selected_id_jenis_outlet
                    ,outlet_Region,lati,longi, outlet_Lokasi,outlet_Email);

            try{
                row_count = jObj.getString(Parameter_Collections.TAG_ROWCOUNT);
                kode_outlet = jObj.getString(Parameter_Collections.TAG_KODE_OUTLET);
            }catch(JSONException e){
                row_count = "0";
                error_message = e.getMessage().toString();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            dialogProgress.dismiss();
            if(row_count.equals("1")){

                spf.edit().putString(Parameter_Collections.SH_KODE_OUTLET, kode_outlet).commit();
                spf.edit().putBoolean(Parameter_Collections.SH_OUTLET_UPDATED, true).commit();

                Olx_DialogLocationConfirmation dialog = new Olx_DialogLocationConfirmation();
                dialog.setContext(getApplicationContext());
                dialog.setText("Input Data Outlet Success");
                dialog.setFrom(9);
                dialog.setCancelable(false);
                dialog.show(getSupportFragmentManager(), "");
            }else{
                Olx_DialogLocationConfirmation dialog = new Olx_DialogLocationConfirmation();
                dialog.setContext(getApplicationContext());
                if(error_message.equals("No value for row_count")){
                    dialog.setText("Toko sudah terdaftar");
                }else {
                    dialog.setText(error_message);
                }

                dialog.setFrom(9);
                dialog.setCancelable(false);
                dialog.show(getSupportFragmentManager(), "");
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }
}
