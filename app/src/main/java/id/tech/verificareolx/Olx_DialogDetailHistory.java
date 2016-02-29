package id.tech.verificareolx;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Olx_DialogDetailHistory extends FragmentActivity{
	private EditText ed_NamaOutlet;
	private Button btn, btn_tgl;
	private String cHargaProduk;
	private String cTglMulai;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_dialog_detail_history);

		String outlet_name = getIntent().getStringExtra("outlet_name");
		String alamat_outlet = getIntent().getStringExtra("alamat_outlet");
		String outlet_username = getIntent().getStringExtra("outlet_username");
		String telepon_outlet = getIntent().getStringExtra("telepon_outlet");
		String confirmed = getIntent().getStringExtra("confirmed");

		TextView tv_outlet_name = (TextView)findViewById(R.id.tv_outlet_name);
		TextView tv_alamat_outlet = (TextView)findViewById(R.id.tv_outlet_address);
		TextView tv_outlet_username = (TextView)findViewById(R.id.tv_outlet_username);
		TextView tv_telepon_outlet = (TextView)findViewById(R.id.tv_outlet_phone);

		Button btn_confirmed = (Button)findViewById(R.id.btn_status);

		tv_outlet_name.setText(outlet_name);
		tv_alamat_outlet.setText(alamat_outlet);
		tv_outlet_username.setText(outlet_username);
		tv_telepon_outlet.setText(telepon_outlet);
		if(confirmed.equals("0")){
			btn_confirmed.setText("Pending");
			btn_confirmed.setBackgroundColor(Color.parseColor("#e59400"));
		}else if(confirmed.equals("1")){
			btn_confirmed.setText("Confirmed");
			btn_confirmed.setBackgroundColor(Color.GREEN);
		}else{
			btn_confirmed.setText("Rejected");
			btn_confirmed.setBackgroundColor(Color.RED);
		}

	}


}
