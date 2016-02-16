package id.tech.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import id.tech.verificareolx.R;

public class CustomAdapter_History_Branding extends
		ArrayAdapter<RowData_History_Branding> {
	private Context context;
	private List<RowData_History_Branding> objects;

	public CustomAdapter_History_Branding(Context context, int resource,
			List<RowData_History_Branding> objects) {
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

		View v = inflater.inflate(R.layout.item_history_branding, null);
		final RowData_History_Branding item = objects.get(position);

		TextView tv_NamaToko = (TextView) v
				.findViewById(R.id.item_historybranding_namatoko);
		TextView tv_JamTgl = (TextView) v
				.findViewById(R.id.item_historybranding_jamtgl);
		TextView tv_Ket = (TextView) v
				.findViewById(R.id.item_historybranding_ket);

		tv_NamaToko.setText(item.nama_toko);
		tv_JamTgl.setText(item.jam + ", " + item.tgl);
		tv_Ket.setText(item.ket);
		return v;
	}

}
