package id.tech.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import id.tech.verificareolx.R;

public class CustomAdapter_History_Issue extends ArrayAdapter<RowData_History_Issue> {
	private Context context;
	private List<RowData_History_Issue> objects;

	public CustomAdapter_History_Issue(Context context, int resource,
			List<RowData_History_Issue> objects) {
		super(context, resource, objects);
		// TODO Auto-generated constructor stub
		this.context = context;
		this.objects = objects;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View v = inflater.inflate(R.layout.item_history_issue, null);
		final RowData_History_Issue item = objects.get(position);

		TextView tv_NamaToko = (TextView) v
				.findViewById(R.id.item_historyissue_namatoko);
		TextView tv_NamaBrand = (TextView) v
				.findViewById(R.id.item_historyissue_namabrand);
		TextView tv_JamTgl = (TextView) v
				.findViewById(R.id.item_historyissue_jamtgl);
		TextView tv_Ket = (TextView) v
				.findViewById(R.id.item_historyissue_pesan);

		tv_NamaToko.setText(item.nama_toko);
		tv_NamaBrand.setText(item.brand);
		tv_JamTgl.setText(item.jam + ", " + item.tgl);
		tv_Ket.setText(item.pesan);
		
		return v;
	}

}
