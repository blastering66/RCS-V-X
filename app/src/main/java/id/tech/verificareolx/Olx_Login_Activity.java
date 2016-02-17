package id.tech.verificareolx;

import org.json.JSONException;
import org.json.JSONObject;

import id.tech.util.Parameter_Collections;
import id.tech.util.Public_Functions;
import id.tech.util.Olx_ServiceHandlerJSON;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class Olx_Login_Activity extends ActionBarActivity {
	Button btn_login;
	EditText ed_Username, ed_Password;
	SharedPreferences sh;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		getSupportActionBar().hide();

		sh = getSharedPreferences(Parameter_Collections.SH_NAME,
				Context.MODE_PRIVATE);

		ed_Username = (EditText) findViewById(R.id.ed_username);
		ed_Password = (EditText) findViewById(R.id.ed_password);

		btn_login = (Button) findViewById(R.id.btn_login);
		btn_login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new Async_Login().execute();
			}
		});
	}

	private class Async_Login extends AsyncTask<Void, Void, String> {
		String cUsername, cPassword, cCode, cMessage, cIdPegawai, cJabatan;
		Olx_DialogFragmentProgress dialogProgress;
		boolean isConnected = true;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			dialogProgress = new Olx_DialogFragmentProgress();
			dialogProgress.show(getSupportFragmentManager(), "");

			cUsername = ed_Username.getText().toString();
			cPassword = ed_Password.getText().toString();
		}

		@Override
		protected String doInBackground(Void... params) {
			// TODO Auto-generated method stub

			if (Public_Functions.isNetworkAvailable(getApplicationContext())) {
				// boolean b = true;
				// if (b) {
				Olx_ServiceHandlerJSON sh = new Olx_ServiceHandlerJSON();
				JSONObject jObj = sh.json_login(cUsername, cPassword);
				Log.e("Result = 	", jObj.toString());

				try {
					cCode = jObj.getString(Parameter_Collections.TAG_JSON_CODE);

					if (cCode.equals("1")) {
						cMessage = jObj
								.getString(Parameter_Collections.TAG_JSON_MESSAGE);
						JSONObject c = jObj
								.getJSONObject(Parameter_Collections.TAG_DATA);
						cIdPegawai = c
								.getString(Parameter_Collections.TAG_ID_PEGAWAI);
						cJabatan = c
								.getString(Parameter_Collections.TAG_LOGIN_JABATAN);
					} else {
						cMessage = jObj
								.getString(Parameter_Collections.TAG_JSON_ERROR_MESSAGE);
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else {
				isConnected = false;
				cMessage = "No Internet Connections";
			}

			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			dialogProgress.dismiss();

			if (isConnected) {
				if (cCode.equals("1")) {
					sh.edit().putBoolean(Parameter_Collections.SH_LOGGED, true)
							.commit();
					sh.edit()
							.putString(Parameter_Collections.SH_LOG_USERNAME,
									cUsername).commit();
					sh.edit()
							.putString(Parameter_Collections.SH_ID_PEGAWAI,
									cIdPegawai).commit();

					if (cJabatan.equals("Promotor")) {
						sh.edit()
								.putString(
										Parameter_Collections.SH_JABATAN_PEGAWAI,
										cJabatan).commit();
					} else {
						sh.edit()
								.putString(
										Parameter_Collections.SH_JABATAN_PEGAWAI,
										cJabatan).commit();
					}

					Intent load = new Intent(getApplicationContext(),
							Olx_MenuUtama_WP.class);
					startActivity(load);
					finish();

				} else {
					Olx_DialogLocationConfirmation dialog = new Olx_DialogLocationConfirmation();
					dialog.setContext(getApplicationContext());
					dialog.setText(cMessage);
					dialog.setFrom(2);
					dialog.setCancelable(false);
					dialog.show(getSupportFragmentManager(), "");

					// Toast.makeText(getApplicationContext(),
					// cMessage,
					// Toast.LENGTH_LONG).show();
				}
			}
		}

	}
}
