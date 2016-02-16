package id.tech.util;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import java.util.List;

public class CustomAdapter_Img extends ArrayAdapter<String> {
	private Context context;
	private List<String> datanya;

	public CustomAdapter_Img(Context context, int resource,
			int textViewResourceId, List<String> objects) {
		super(context, resource, textViewResourceId, objects);
		// TODO Auto-generated constructor stubthis
		this.context = context;
		this.datanya = objects;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ImageView img = new ImageView(context);

		AbsListView.LayoutParams params = new AbsListView.LayoutParams(300, 170);
		img.setScaleType(ScaleType.CENTER_INSIDE);
		img.setLayoutParams(params);
		
		return img;
	}

}
