package id.tech.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import id.tech.verificareolx.R;

/**
 * Created by RebelCreative-A1 on 18/11/2015.
 */
public class CustomerAdapter_History_TradeIn extends ArrayAdapter<RowData_History_TradeIn> {
    Context context;
    List<RowData_History_TradeIn> objects;

    public CustomerAdapter_History_TradeIn(Context context, int resource, List<RowData_History_TradeIn> objects) {
        super(context, resource, objects);
        this.context = context;
        this.objects = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v= inflater.inflate(R.layout.indosat_item_history_tradein, null);

        final RowData_History_TradeIn item = objects.get(position);
        TextView tv_nama = (TextView)v.findViewById(R.id.tv_nama);
        TextView tv_old = (TextView)v.findViewById(R.id.tv_perangkat_old);
        TextView tv_new = (TextView)v.findViewById(R.id.tv_perangkat_new);
        TextView tv_plan = (TextView)v.findViewById(R.id.tv_plan);

        tv_nama.setText(item.nama);
        tv_old.setText(item.old_phone);
        tv_new.setText(item.new_phone);
        tv_plan.setText(item.plan);

        return v;
    }
}
