package id.tech.verificareolx;

import id.tech.verificareolx.R;
import id.tech.util.Parameter_Collections;
import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

public class Notif_Detail_Activity extends Activity{
	TextView tv_pesan;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_dialog_notif);
		
		tv_pesan = (TextView)findViewById(R.id.txt);
		
		String cPesan = getIntent().getStringExtra(Parameter_Collections.EXTRA_PESAN);
		tv_pesan.setText(cPesan);
	}

}
