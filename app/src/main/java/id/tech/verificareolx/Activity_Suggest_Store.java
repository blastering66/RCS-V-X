package id.tech.verificareolx;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import id.tech.util.GPSTracker;
import id.tech.util.Parameter_Collections;
import id.tech.util.Public_Functions;
import id.tech.util.ServiceHandlerJSON;

/**
 * Created by RebelCreative-A1 on 04/12/2015.
 */
public class Activity_Suggest_Store extends AppCompatActivity {
    SharedPreferences spf;
    private String id_pegawai;
    private EditText ed_namatoko,ed_alamattoko,ed_tlptoko,ed_emailtoko,ed_regiontoko;
    private Button btn;
    private String latitude, longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggest_store);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Suggest Store");

        spf = getSharedPreferences(Parameter_Collections.SH_NAME, Context.MODE_PRIVATE);
//		kode_toko = spf.getString(Parameter_Collections.SH_KODE_TOKO, "");
        id_pegawai = spf.getString(Parameter_Collections.SH_ID_PEGAWAI, "");

        initView();
    }

    private void initView(){
        ed_namatoko = (EditText)findViewById(R.id.ed_namatoko);
        ed_alamattoko = (EditText)findViewById(R.id.ed_alamattoko);
        ed_tlptoko = (EditText)findViewById(R.id.ed_tlptoko);
        ed_emailtoko = (EditText)findViewById(R.id.ed_emailtoko);
        ed_regiontoko = (EditText)findViewById(R.id.ed_regiontoko);

        latitude = spf.getString(Parameter_Collections.TAG_LATITUDE_NOW, "0.0");
        longitude = spf.getString(Parameter_Collections.TAG_LONGITUDE_NOW, "0.0");


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.updatebrand, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private class AsyncTask_InputSuggestion extends AsyncTask<Void,Void,Void> {
        private String cEd_namatoko,cEd_alamattoko,cEd_tlptoko,cEd_emailtoko,cEd_regiontoko;
        private String cCode;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            cEd_namatoko = ed_namatoko.getText().toString();
            cEd_alamattoko = ed_alamattoko.getText().toString();
            cEd_tlptoko = ed_tlptoko.getText().toString();
            cEd_emailtoko = ed_emailtoko.getText().toString();
            cEd_regiontoko = ed_regiontoko.getText().toString();
        }

        @Override
        protected Void doInBackground(Void... params) {
            ServiceHandlerJSON sh = new ServiceHandlerJSON();

            JSONObject jObj = sh.json_suggest_store(id_pegawai,cEd_namatoko,cEd_alamattoko,cEd_tlptoko,
                    cEd_emailtoko,cEd_regiontoko,latitude,longitude);

            try{
                cCode = jObj.getString(Parameter_Collections.TAG_JSON_CODE);

            }catch (JSONException e){
                cCode = "0";
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if(cCode.equals("1")){
                Toast.makeText(getApplicationContext(),"Suggest Store Berhasil", Toast.LENGTH_LONG).show();
                finish();
            }else{
                Toast.makeText(getApplicationContext(),"Suggest Store Gagal", Toast.LENGTH_LONG).show();
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
            case R.id.action_send_updatebranding:
                new AsyncTask_InputSuggestion().execute();
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
