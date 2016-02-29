package id.tech.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import id.tech.verificareolx.Olx_DialogDetailHistory;
import id.tech.verificareolx.R;

/**
 * Created by RebelCreative-A1 on 15/12/2015.
 */
public class Olx_RecyclerAdapter_History extends RecyclerView.Adapter<Olx_RecyclerAdapter_History.ViewHolder> {
    private Activity activity_adapter;
    private Context context_adapter;
    private List<RowData_History> data;

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final RowData_History item = data.get(position);

        holder.tv_namaoutlet.setText(item.outlet_name);
        holder.tv_email_outlet.setText(item.outlet_email);
        holder.tv_usernameoutlet.setText(item.outlet_username);
        holder.tv_topup.setText(item.outlet_topup);

        holder.wrapper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context_adapter, Olx_DialogDetailHistory.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("outlet_name", item.outlet_name);
                intent.putExtra("alamat_outlet",item.alamat_outlet);
                intent.putExtra("outlet_username",item.outlet_username);
                intent.putExtra("telepon_outlet",item.telepon_outlet);
                intent.putExtra("confirmed",item.confirm);
                context_adapter.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history_visitactivity,null);

        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    public Olx_RecyclerAdapter_History(Activity activity_adapter, Context context_adapter, List<RowData_History> data) {
        this.activity_adapter = activity_adapter;
        this.context_adapter = context_adapter;
        this.data = data;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_namaoutlet,tv_usernameoutlet,tv_email_outlet,tv_topup;
        public View wrapper;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_namaoutlet = (TextView)itemView.findViewById(R.id.tv_namaoutlet);
            tv_usernameoutlet = (TextView)itemView.findViewById(R.id.tv_usernameoutlet);
            tv_email_outlet = (TextView)itemView.findViewById(R.id.tv_email_outlet);
            tv_topup = (TextView)itemView.findViewById(R.id.tv_topup);

            wrapper= (View)itemView.findViewById(R.id.wrapper);
        }
    }
}
