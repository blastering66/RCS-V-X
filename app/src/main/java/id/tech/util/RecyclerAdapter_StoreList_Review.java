package id.tech.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import id.tech.verificareolx.R;

/**
 * Created by RebelCreative-A1 on 30/11/2015.
 */
public class RecyclerAdapter_StoreList_Review extends  RecyclerView.Adapter<RecyclerAdapter_StoreList_Review.Viewholder> {
    private Activity activity_adapter;
    private Context context_adapter;
    private List<RowData_Toko> data, data_selected;


    public RecyclerAdapter_StoreList_Review(Activity activity_adapter, Context context_adapter,
                                            List<RowData_Toko> data) {
        this.activity_adapter = activity_adapter;
        this.context_adapter = context_adapter;
        this.data = data;

        data_selected = new ArrayList<>();


    }

   @Override
    public Viewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_store_review, parent, false);

        Viewholder viewholder = new Viewholder(v,this.activity_adapter);

        return viewholder;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public void onBindViewHolder(final Viewholder holder, int position) {
        final RowData_Toko item = data.get(position);
        holder.tv.setText(item.nama_toko);
        if(item.visited){
            holder.wrapper.setBackgroundColor(Color.parseColor("#990000"));
        }else{
            holder.wrapper.setBackgroundColor(Color.parseColor("#00ff00"));
        }


    }

    public List<RowData_Toko> getStoreSelected(){
        return data_selected;
    }

    public class Viewholder extends RecyclerView.ViewHolder  {
        public TextView tv;
        public View wrapper;
        public Activity activity;

        public Viewholder(View itemView, Activity activity) {
            super(itemView);
            tv = (TextView)itemView.findViewById(R.id.txt_label);
            wrapper = (View)itemView.findViewById(R.id.wrapper);
            this.activity = activity;
        }


    }
}
