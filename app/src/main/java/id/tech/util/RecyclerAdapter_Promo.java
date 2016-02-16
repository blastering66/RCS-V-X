package id.tech.util;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import id.tech.verificareolx.R;

/**
 * Created by RebelCreative-A1 on 10/12/2015.
 */
public class RecyclerAdapter_Promo extends RecyclerView.Adapter<RecyclerAdapter_Promo.ViewHolder> {
    private Activity activity_adapter;
    private Context context_adapter;
    private List<RowData_Promo> data;

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        RowData_Promo item = data.get(position);

        holder.tv_nama_stock.setText(item.stock_name);
        holder.tv_nama_promo.setText(item.promo_name);
        holder.tv_start_promo.setText(item.promo_start_date);
        holder.tv_end_promo.setText(item.promo_end_date);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_promo,null);

        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    public RecyclerAdapter_Promo(Activity activity_adapter, Context context_adapter, List<RowData_Promo> data) {
        this.activity_adapter = activity_adapter;
        this.context_adapter = context_adapter;
        this.data = data;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_nama_promo,tv_nama_stock,tv_start_promo,tv_end_promo;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_nama_promo = (TextView)itemView.findViewById(R.id.tv_nama_promo);
            tv_nama_stock = (TextView)itemView.findViewById(R.id.tv_nama_stock);
            tv_start_promo = (TextView)itemView.findViewById(R.id.tv_start_promo);
            tv_end_promo= (TextView)itemView.findViewById(R.id.tv_end_promo);
        }
    }
}
