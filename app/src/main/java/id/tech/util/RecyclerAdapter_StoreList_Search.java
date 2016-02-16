package id.tech.util;

import android.app.Activity;
import android.content.Context;
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
public class RecyclerAdapter_StoreList_Search extends  RecyclerView.Adapter<RecyclerAdapter_StoreList_Search.Viewholder> {
    private Activity activity_adapter;
    private Context context_adapter;
    private List<RowData_Toko> data, data_selected;

    onStoreSelected_Listener mCallback;

    public interface onStoreSelected_Listener {
        public void onStoreSelected(int size);
        public void getRowData_ListVisitStore(List<RowData_Toko> data);
    }

    public RecyclerAdapter_StoreList_Search(Activity activity_adapter, Context context_adapter,
                                            List<RowData_Toko> data) {
        this.activity_adapter = activity_adapter;
        this.context_adapter = context_adapter;
        this.data = data;

        data_selected = new ArrayList<>();

        try{
            mCallback = (onStoreSelected_Listener)activity_adapter;
        }catch (ClassCastException e){

        }
    }

    public void hideView(int position){
        notifyItemRemoved(position);
    }

   @Override
    public Viewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_store, parent, false);

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
        holder.tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(data_selected.size() >= 25){
                    Toast.makeText(context_adapter, "Store List Quota Full", Toast.LENGTH_LONG).show();
                    mCallback.getRowData_ListVisitStore(getStoreSelected());
                } else {
                    data_selected.add(new RowData_Toko(item.id_toko, item.kode_toko, item.nama_toko, item.region_toko));
                    mCallback.onStoreSelected(data_selected.size());

                    holder.tv.setVisibility(View.GONE);
                }

            }
        });
    }

    public List<RowData_Toko> getStoreSelected(){
        return data_selected;
    }

    public class Viewholder extends RecyclerView.ViewHolder  {
        public TextView tv;
        public Activity activity;

        public Viewholder(View itemView, Activity activity) {
            super(itemView);
            tv = (TextView)itemView.findViewById(R.id.txt_label);
            this.activity = activity;
        }


    }
}
