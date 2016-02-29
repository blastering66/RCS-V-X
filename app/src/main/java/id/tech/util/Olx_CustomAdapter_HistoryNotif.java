package id.tech.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import id.tech.verificareolx.R;

public class Olx_CustomAdapter_HistoryNotif extends ArrayAdapter<RowData_Notif> {
	Context context;
	List<RowData_Notif> datanya;

	public Olx_CustomAdapter_HistoryNotif(Context context, int resource,
										  List<RowData_Notif> objects) {
		super(context, resource, objects);
		// TODO Auto-generated constructor stub
		this.context = context;
		this.datanya = objects;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		LayoutInflater mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = mInflater.inflate(R.layout.item_list_history, null);
		
		final RowData_Notif item = datanya.get(position);
		TextView tv_judul = (TextView)v.findViewById(R.id.tv_history_judul);
		TextView tv_pesan = (TextView)v.findViewById(R.id.tv_history_pesan);
		
		tv_judul.setText(item.judul_pesan);
		tv_pesan.setText(item.isi_pesan);
		
		return v;
	}

}
