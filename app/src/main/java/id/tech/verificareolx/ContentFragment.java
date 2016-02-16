package id.tech.verificareolx;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import id.tech.verificareolx.R;
import id.tech.util.CustomAdapter_HistoryAbsensi;
import id.tech.util.CustomAdapter_HistoryNotif;
import id.tech.util.CustomAdapter_History_Branding;
import id.tech.util.CustomAdapter_History_Issue;
import id.tech.util.Parameter_Collections;
import id.tech.util.RowData_History_Absensi;
import id.tech.util.RowData_History_Branding;
import id.tech.util.RowData_History_Issue;
import id.tech.util.RowData_Notif;
import id.tech.util.ServiceHandlerJSON;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ContentFragment extends Fragment {
	private static final String KEY_TITLE = "title";
	private static final String KEY_ID = "id";

	CharSequence titlenya;
	String namaAbsen, namaBranding, namaIssue;
	TextView title;

	static ArrayList<RowData_History_Absensi> data_Absensi;
	static ArrayList<RowData_History_Branding> data_Branding;
	static ArrayList<RowData_History_Issue> data_Issue;
	static ArrayList<RowData_Notif> data_Notif;

	ListView ls;

	CustomAdapter_HistoryAbsensi adapterAbsensi;
	CustomAdapter_History_Branding adapterBranding;
	CustomAdapter_History_Issue adapterIssue;
	CustomAdapter_HistoryNotif adapterNotif;

	public static ContentFragment newInstance(CharSequence title, int posisi,
			ArrayList<RowData_History_Absensi> data_absen,
			ArrayList<RowData_History_Branding> data_brand,
			ArrayList<RowData_History_Issue> data_issue,
			ArrayList<RowData_Notif> data_notif) {
		Bundle bundle = new Bundle();
		bundle.putCharSequence(KEY_TITLE, title);
		bundle.putInt(KEY_ID, posisi);
		ContentFragment fragment = new ContentFragment();
		fragment.setArguments(bundle);

		data_Absensi = data_absen;
		data_Branding = data_brand;
		data_Issue = data_issue;
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
			adapterAbsensi = new CustomAdapter_HistoryAbsensi(getActivity(), 0,
					0, data_Absensi);
			ls.setAdapter(adapterAbsensi);
		} else if (posisi == 1) {
			adapterBranding = new CustomAdapter_History_Branding(getActivity(),
					0, data_Branding);
			ls.setAdapter(adapterBranding);
		} else if (posisi == 2) {
			adapterIssue = new CustomAdapter_History_Issue(getActivity(), 0,
					data_Issue);
			ls.setAdapter(adapterIssue);
		} else if (posisi == 3) {
			adapterNotif = new CustomAdapter_HistoryNotif(getActivity(), 0,
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
			ls.setAdapter(adapterAbsensi);
		} else if (posisi == 1) {
			ls.setAdapter(adapterBranding);
		} else if (posisi == 2) {
			ls.setAdapter(adapterIssue);
		} else if (posisi == 3) {
			ls.setAdapter(adapterNotif);
		}
	}
}
