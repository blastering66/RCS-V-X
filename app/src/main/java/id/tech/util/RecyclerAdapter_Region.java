package id.tech.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import id.tech.verificareolx.R;

/**
 * Created by RebelCreative-A1 on 07/12/2015.
 */
public class RecyclerAdapter_Region extends RecyclerView.Adapter<RecyclerAdapter_Region.Viewholder>{
    private Activity activity_adapter;
    private Context context_adapter;
    private List<RowData_Toko> data, data_selected;
    boolean selected = false;
    onRegionSelection_Listener mCallback;

    public RecyclerAdapter_Region(Activity activity_adapter, Context context_adapter, List<RowData_Toko> data) {
        this.activity_adapter = activity_adapter;
        this.context_adapter = context_adapter;
        this.data = data;

        this.data_selected = new ArrayList<>();
        try{
            mCallback=(onRegionSelection_Listener)activity_adapter;
        }catch (ClassCastException e){
            Log.e("Error Casting Callback", e.getMessage().toString());
        }
    }

    public interface onRegionSelection_Listener{
        public void onRegionSelected(List<RowData_Toko> data, boolean checked);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public Viewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_checkbox_region, parent, false);

        Viewholder viewholder = new Viewholder(v,this.activity_adapter);

        return viewholder;
    }

    @Override
    public void onBindViewHolder(final Viewholder holder, int position) {
        final RowData_Toko item = data.get(position);

        holder.tv.setText(item.nama_toko);

        holder.wrapper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.checkBox.isChecked()){
                    holder.checkBox.setChecked(false);
                    data_selected.remove(item);
                    mCallback.onRegionSelected(data_selected, false);
//                    holder.wrapper.setBackgroundColor(Color.WHITE);
//                    holder.tv.setTextColor(Color.BLACK);
//                    selected = false;
                }else{
                    holder.checkBox.setChecked(true);
                    data_selected.add(item);
                    mCallback.onRegionSelected(data_selected, true);
//                    holder.wrapper.setBackgroundColor(Color.BLUE);
//                    holder.tv.setTextColor(Color.WHITE);
//                    selected = true;
                }

            }
        });

    }

    public class Viewholder extends RecyclerView.ViewHolder  {
        public TextView tv;
        public CheckBox checkBox;
        public View wrapper;
        public Activity activity;

        public Viewholder(View itemView, Activity activity) {
            super(itemView);
            tv = (TextView)itemView.findViewById(R.id.txt_label);
            wrapper = (View)itemView.findViewById(R.id.wrapper);
            checkBox = (CheckBox)itemView.findViewById(R.id.checkbox);
            this.activity = activity;
        }


    }
}
