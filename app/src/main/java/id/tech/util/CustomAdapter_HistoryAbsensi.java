package id.tech.util;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import id.tech.verificareolx.R;

public class CustomAdapter_HistoryAbsensi extends ArrayAdapter<RowData_History_Absensi> {
	private Context context;
	private List<RowData_History_Absensi> objects;

	public CustomAdapter_HistoryAbsensi(Context context, int resource,
			int textViewResourceId, List<RowData_History_Absensi> objects) {
		super(context, resource, textViewResourceId, objects);
		// TODO Auto-generated constructor stub
		this.context = context;
		this.objects = objects;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		View v = inflater.inflate(R.layout.item_history_absensi	,null);
		final RowData_History_Absensi item = objects.get(position);
		
		TextView tv_Nama = (TextView)v.findViewById(R.id.item_historyabsen_nama);
		TextView tv_Jam = (TextView)v.findViewById(R.id.item_historyabsen_jam);
		TextView tv_Tgl = (TextView)v.findViewById(R.id.item_historyabsen_tgl);
		TextView tv_Ket = (TextView)v.findViewById(R.id.item_historyabsen_status);
		
		tv_Nama.setText(item.nama_pegawai);
		tv_Jam.setText(item.jam);
		tv_Tgl.setText(item.tgl);
		
		if(item.ket_absen.equals("1")){
			tv_Ket.setText("Datang");
			tv_Ket.setTextColor(Color.GREEN);
		}else{
			tv_Ket.setText("Pulang");
			tv_Ket.setTextColor(Color.RED);
		}
		
		
		return v;
	}

}

