package id.tech.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import id.tech.verificareolx.R;

public class Olx_CustomAdapter_History_Branding extends
		ArrayAdapter<RowData_History> {
	private Context context;
	private List<RowData_History> objects;
	public TextView tv_namaoutlet,tv_usernameoutlet,tv_email_outlet,tv_topup;
	public View wrapper;

	public Olx_CustomAdapter_History_Branding(Context context, int resource,
											  List<RowData_History> objects) {
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

		View itemView = inflater.inflate(R.layout.item_history_visitactivity, null);
		final RowData_History item = objects.get(position);

		tv_namaoutlet = (TextView)itemView.findViewById(R.id.tv_namaoutlet);
		tv_usernameoutlet = (TextView)itemView.findViewById(R.id.tv_usernameoutlet);
		tv_email_outlet = (TextView)itemView.findViewById(R.id.tv_email_outlet);
		tv_topup = (TextView)itemView.findViewById(R.id.tv_topup);

		wrapper= (View)itemView.findViewById(R.id.wrapper);

		tv_namaoutlet.setText(item.outlet_name);
		tv_usernameoutlet.setText(item.outlet_username);
		tv_email_outlet.setText(item.outlet_email);
		tv_topup.setText(item.outlet_topup);
		return itemView;
	}

}
