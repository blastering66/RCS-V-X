package id.tech.verificareolx;

import id.tech.util.Parameter_Collections;
import id.tech.util.Olx_RecyclerAdapter_MenuUtama;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class Olx_Fragment_MenuUtama extends Fragment{
	RecyclerView rv;
	RecyclerView.Adapter adapter;
	RecyclerView.LayoutManager layoutManager;
	RecyclerView.ItemDecoration decoration;
	ImageView img_logout;
	private SharedPreferences sh;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.activity_menuutama_wp, null);

		sh = getActivity().getSharedPreferences(Parameter_Collections.SH_NAME, Context.MODE_PRIVATE);

		rv = (RecyclerView)v.findViewById(R.id.recycler_view);
		layoutManager = new GridLayoutManager(getActivity(), 1);
		rv.setLayoutManager(layoutManager);		
		adapter = new Olx_RecyclerAdapter_MenuUtama(getActivity(), getActivity());
		rv.setAdapter(adapter);
		
		return v;
	}

}
