package id.tech.verificareolx;

import java.util.ArrayList;

import id.tech.util.Olx_CustomAdapter_HistoryNotif;
import id.tech.util.RowData_History;
import id.tech.util.Olx_CustomAdapter_History_Branding;
import id.tech.util.RowData_Notif;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

public class Olx_ContentFragment extends Fragment {
	private static final String KEY_TITLE = "title";
	private static final String KEY_ID = "id";

	CharSequence titlenya;
	String namaAbsen, namaBranding, namaIssue;
	TextView title;

	static ArrayList<RowData_History> data_Branding;
	static ArrayList<RowData_Notif> data_Notif;

	ListView ls;

	Olx_CustomAdapter_History_Branding adapterBranding;
	Olx_CustomAdapter_HistoryNotif adapterNotif;

	public static Olx_ContentFragment newInstance(CharSequence title, int posisi,
			ArrayList<RowData_History> data_brand,
			ArrayList<RowData_Notif> data_notif) {
		Bundle bundle = new Bundle();
		bundle.putCharSequence(KEY_TITLE, title);
		bundle.putInt(KEY_ID, posisi);
		Olx_ContentFragment fragment = new Olx_ContentFragment();
		fragment.setArguments(bundle);

		data_Branding = data_brand;
		data_Notif = data_notif;

		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		int posisi = getArguments().getInt(KEY_ID);

		View v = inflater.inflate(R.layout.pager_listview, container, false);
		ls = (ListView) v.findViewById(R.id.listview);

		if (posisi == 0) {
			adapterBranding = new Olx_CustomAdapter_History_Branding(getActivity(),
					0, data_Branding);
			ls.setAdapter(adapterBranding);
		}else if (posisi == 1) {
			adapterNotif = new Olx_CustomAdapter_HistoryNotif(getActivity(), 0,
					data_Notif);
			ls.setAdapter(adapterNotif);
		}

		return v;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		Bundle args = getArguments();
		// if (args != null) {
		// title = (TextView) view.findViewById(R.id.item_title);
		// if (args.getCharSequence(KEY_TITLE).equals("History 1")) {
		// title.setText(namaAbsen);
		// } else if (args.getCharSequence(KEY_TITLE).equals("History 2")) {
		// title.setText(namaBranding);
		// } else if (args.getCharSequence(KEY_TITLE).equals("History 3")) {
		// title.setText(namaIssue);
		// }
		// }
		int posisi = getArguments().getInt(KEY_ID);

		// View v = inflater.inflate(R.layout.pager_listview, container, false);
		ls = (ListView) view.findViewById(R.id.listview);

		if (posisi == 0) {
			ls.setAdapter(adapterBranding);
		} else if (posisi == 1) {
			ls.setAdapter(adapterNotif);
		}
	}
}
