package id.tech.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pkmmte.view.CircularImageView;

import id.tech.verificareolx.R;

public class Olx_RecyclerAdapter_Slider_Empty extends RecyclerView.Adapter<Olx_RecyclerAdapter_Slider_Empty.ViewHolder>{
	private Context context;
	private SharedPreferences sh;
	private Activity activity;
	private String nama, target, achievement, url;
	private FragmentManager fm;
	private String total_visited_store, total_visited_store_all;

	public Olx_RecyclerAdapter_Slider_Empty() {
		// TODO Auto-generated constructor stub
	}

	public class ViewHolder extends RecyclerView.ViewHolder{
		public TextView tv_nama, tv_target, tv_achivement, tv_logout, tv_summary_visit,tv_summary_visit_total;
		public ImageView img;
		private CircularImageView circularImageView;
		private Activity activity;
		private Context context;


		public ViewHolder(View v, Activity activity) {
			super(v);
			tv_nama = (TextView) v.findViewById(R.id.tv_nama);
			tv_target = (TextView) v.findViewById(R.id.tv_target);
			tv_achivement = (TextView) v.findViewById(R.id.tv_achievement);
			tv_logout = (TextView)v.findViewById(R.id.tv_logout);

			tv_summary_visit= (TextView)v.findViewById(R.id.tv_summary_visit);
			tv_summary_visit_total = (TextView)v.findViewById(R.id.tv_summary_visit_total);
			img = (ImageView) v.findViewById(R.id.img);
			circularImageView = (CircularImageView)v.findViewById(R.id.img_profile);

			this.activity = activity;

		}


	}


	@Override
	public int getItemCount() {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public void onBindViewHolder(ViewHolder arg0, int arg1) {
		// TODO Auto-generated method stub

		arg0.tv_nama.setText("Loading...");
		arg0.tv_summary_visit.setText("");
		arg0.tv_summary_visit_total.setText("");


	}


	@Override
	public ViewHolder onCreateViewHolder(ViewGroup arg0, int arg1) {
		// TODO Auto-generated method stub
		View v = LayoutInflater.from(arg0.getContext()).inflate(
				R.layout.layout_profile_empty, arg0, false);
		ViewHolder viewholder = new ViewHolder(v, this.activity);
		return viewholder;
	}

	
}
