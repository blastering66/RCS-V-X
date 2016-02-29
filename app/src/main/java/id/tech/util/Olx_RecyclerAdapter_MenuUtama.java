package id.tech.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import id.tech.verificareolx.Olx_Activity_Gallery;
import id.tech.verificareolx.Olx_DialogAbsen;
import id.tech.verificareolx.Olx_Activity_History_TabSlider;
import id.tech.verificareolx.Olx_DataOutlet_Activity;
import id.tech.verificareolx.Olx_UpdateBranding_Activity;
import id.tech.verificareolx.R;

public class Olx_RecyclerAdapter_MenuUtama extends
        RecyclerView.Adapter<Olx_RecyclerAdapter_MenuUtama.ViewHolder> {
    private Context context;
    private SharedPreferences sh;
    private Activity activity;

    public Olx_RecyclerAdapter_MenuUtama(Context context, Activity activity) {
        // TODO Auto-generated constructor stub
        this.context = context;
        sh = this.context.getSharedPreferences(Parameter_Collections.SH_NAME,
                Context.MODE_PRIVATE);
        this.activity = activity;

    }

    private void showToast(String txt, Context ctx) {
        Toast.makeText(ctx, txt, Toast.LENGTH_SHORT).show();

    }

    public class ViewHolder extends RecyclerView.ViewHolder implements
            OnClickListener {
        public TextView tv_label;
        public ImageView img;
        public View wrapper;
        private Activity activity;

        public ViewHolder(View v, Activity activity) {
            super(v);
            tv_label = (TextView) v.findViewById(R.id.txt_label);
            img = (ImageView) v.findViewById(R.id.img);
            wrapper = (View) v.findViewById(R.id.wrapper);
            wrapper.setOnClickListener(this);

            this.activity = activity;
            // harga_produk = (TextView) v.findViewById(R.id.tv_harga_produk);
            // imei_produk = (TextView) v.findViewById(R.id.tv_imei_produk);
            // status_produk = (TextView) v.findViewById(R.id.tv_status_produk);
        }

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            // Toast.makeText(, "position = " + getAdapterPosition(),
            // Toast.LENGTH_SHORT).show();

            switch (getAdapterPosition()) {
                case 0:
                    Intent load0 = new Intent(v.getContext(), Olx_DialogAbsen.class);
                    v.getContext().startActivity(load0);

                    break;
                case 1:
                    if (!sh.getBoolean(Parameter_Collections.SH_ABSENTED, false)) {
                        showToast("Please absent first", v.getContext());
                    } else if (sh.getBoolean(Parameter_Collections.SH_OUTLET_UPDATED, false)) {
                        showToast("This Outlet alreaddy updated", v.getContext());
                    }else {
                        Intent loadHistory_Activity = new Intent(v.getContext(),
                                Olx_DataOutlet_Activity.class);
                        v.getContext
                                ().startActivity(loadHistory_Activity);
                    }
                    break;
                case 2:
                    if (!sh.getBoolean(Parameter_Collections.SH_ABSENTED, false)) {
                        showToast("Please absent first", v.getContext());
                    } else if (!sh.getBoolean(Parameter_Collections.SH_OUTLET_UPDATED, false)) {
                        showToast("Please update data outlet first", v.getContext());
                    } else if (sh.getBoolean(Parameter_Collections.SH_OUTLET_VISITED, false)) {
                        showToast("This outlet already visited", v.getContext());

                    } else {
                        Intent load = new Intent(v.getContext(),
                                Olx_UpdateBranding_Activity.class);
                        v.getContext().startActivity(load);

                    }


                    break;
                case 3:
                    if (true) {
//					Intent load = new Intent(v.getContext(),
                        Intent loadHistory_Activity = new Intent(v.getContext(),
                                Olx_Activity_Gallery.class);
                        v.getContext
                                ().startActivity(loadHistory_Activity);

                    } else {
                        showToast("Please absent first", v.getContext());
                    }

                    break;
                case 4:
                    if (true) {
//					Intent load = new Intent(v.getContext(),
                        Intent loadHistory_Activity = new Intent(v.getContext(),
                                Olx_Activity_History_TabSlider.class);
                        v.getContext
                                ().startActivity(loadHistory_Activity);

                    } else {
                        showToast("Please absent first", v.getContext());
                    }
                    break;


                default:
                    break;
            }
        }

    }

    @Override
    public int getItemCount() {
        // TODO Auto-generated method stub
        return 5;
    }

    @Override
    public void onBindViewHolder(ViewHolder arg0, int arg1) {
        // TODO Auto-generated method stub
        switch (arg1) {
            case 0:
                arg0.img.setImageResource(R.drawable.menu_wp_absen);
                arg0.wrapper.setBackgroundColor(context.getResources().getColor(
                        R.color.color_wp_darkblue));
                arg0.tv_label.setText("Input Nama Outlet / User Baru");
                break;
            case 1:
                arg0.img.setImageResource(R.drawable.menu_wp_outlet);
                arg0.wrapper.setBackgroundColor(context.getResources().getColor(
                        R.color.color_wp_darkpurple));
//			arg0.tv_label.setText("Input Penjualan");
                arg0.tv_label.setText("Input Data Outlet / User");


                break;
            case 2:
                arg0.img.setImageResource(R.drawable.menu_wp_history);
                arg0.wrapper.setBackgroundColor(context.getResources().getColor(
                        R.color.color_wp_darkgreen));
                arg0.tv_label.setText("Input Visit Activity");


                break;
            case 3:
                arg0.img.setImageResource(R.drawable.menu_wp_gallery);
                arg0.wrapper.setBackgroundColor(context.getResources().getColor(
                        R.color.color_wp_darkorange));
                arg0.tv_label.setText("Input Photo Activity");
                break;

            case 4:
                arg0.img.setImageResource(R.drawable.menu_wp_cek_stok);
                arg0.wrapper.setBackgroundColor(context.getResources().getColor(
                        R.color.color_wp_darkblue));
//			arg0.tv_label.setText("Info Toko");
                arg0.tv_label.setText("History Activity");
                break;


            default:
                break;
        }

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup arg0, int arg1) {
        // TODO Auto-generated method stub
        View v = LayoutInflater.from(arg0.getContext()).inflate(
                R.layout.item_menuutama_wp, arg0, false);
        ViewHolder viewholder = new ViewHolder(v, this.activity);
        return viewholder;
    }

}
