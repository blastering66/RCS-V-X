package id.tech.util;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import id.tech.verificareolx.R;

/**
 * Created by RebelCreative-A1 on 15/12/2015.
 */
public class RecyclerAdapter_HistoryInputStock extends RecyclerView.Adapter<RecyclerAdapter_HistoryInputStock.ViewHolder> {
    private Activity activity_adapter;
    private Context context_adapter;
    private List<RowData_History> data;

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        RowData_History item = data.get(position);

//        holder.tv_namatoko.setText(item.nama_toko);
//        holder.tv_quantity_display.setText(item.stock_qty_display);
//        holder.tv_quantity_warehouse.setText(item.stock_qty_warehouse);
//        holder.tv_quantityname.setText(item.stock_qty_unit);
//        holder.tv_regiontoko.setText(item.region_toko);
//        holder.tv_stockname.setText(item.stock_name);

        holder.wrapper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context_adapter,"Klik", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history_inputstock,null);

        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    public RecyclerAdapter_HistoryInputStock(Activity activity_adapter, Context context_adapter, List<RowData_History> data) {
        this.activity_adapter = activity_adapter;
        this.context_adapter = context_adapter;
        this.data = data;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_namatoko,tv_regiontoko,tv_stockname,tv_quantityname,tv_quantity_display,tv_quantity_warehouse;
        public View wrapper;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_namatoko = (TextView)itemView.findViewById(R.id.tv_namatoko);
            tv_regiontoko = (TextView)itemView.findViewById(R.id.tv_regiontoko);
            tv_stockname = (TextView)itemView.findViewById(R.id.tv_stockname);
            tv_quantityname= (TextView)itemView.findViewById(R.id.tv_quantityname);
            tv_quantity_display = (TextView)itemView.findViewById(R.id.tv_quantity_display);
            tv_quantity_warehouse = (TextView)itemView.findViewById(R.id.tv_quantity_warehouse);
            wrapper= (View)itemView.findViewById(R.id.wrapper);
        }
    }
}
