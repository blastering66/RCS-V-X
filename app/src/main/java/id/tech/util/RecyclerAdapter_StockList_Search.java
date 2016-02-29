package id.tech.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import id.tech.verificareolx.R;

/**
 * Created by RebelCreative-A1 on 30/11/2015.
 */
public class RecyclerAdapter_StockList_Search extends  RecyclerView.Adapter<RecyclerAdapter_StockList_Search.Viewholder> {
    private Activity activity_adapter;
    private Context context_adapter;
    private List<RowData_Stock> data;

    String id_pegawai, id_toko, id_stock,stock_qty_warehouse,stock_qty_display,note,
            latitude_stock_store,longitude_stock_store;
    SharedPreferences sp;

    public RecyclerAdapter_StockList_Search(Activity activity_adapter, Context context_adapter,
                                            List<RowData_Stock> data) {
        this.activity_adapter = activity_adapter;
        this.context_adapter = context_adapter;
        this.data = data;
    }

   @Override
    public Viewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_stock, parent, false);

        Viewholder viewholder = new Viewholder(v,this.activity_adapter);

        return viewholder;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public void onBindViewHolder(final Viewholder holder, int position) {
        final RowData_Stock item = data.get(position);
//        id_stock = item.id_stock_toko;
        holder.txt_id_stock.setText(item.id_stock_toko);
        holder.tv.setText(item.nama_stock);

        holder.wrapper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.wrapper_form.setVisibility(View.VISIBLE);
            }
        });

        holder.ed_Stock_Minimum.setText(item.stock_minimum);



        holder.ed_Stock_Display.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String minimum = holder.ed_Stock_Minimum.getText().toString();
                String stock_warehouse = holder.ed_Stock_Warehouse.getText().toString();
                if (stock_warehouse.equals("")) {
                    stock_warehouse = "0";
                }
                int int_min = Integer.parseInt(minimum);
                int int_warehouse = Integer.parseInt(stock_warehouse);
                int int_display = Integer.parseInt(s.toString());

                int calculated = int_min - (int_display + int_warehouse);

                if (calculated < 0) {
                    holder.ed_Stock_Needed.setText("0");
                    //btn Request disable
                } else {
                    //btn Request ENABLE
                    holder.ed_Stock_Needed.setText(String.valueOf(calculated));
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                stock_qty_display = s.toString();

            }
        });

        holder.ed_Stock_Warehouse.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String minimum = holder.ed_Stock_Minimum.getText().toString();
                String stock_displayed = holder.ed_Stock_Display.getText().toString();
                if (stock_displayed.equals("")) {
                    stock_displayed = "0";
                }
                int int_min = Integer.parseInt(minimum);
                int int_warehouse = Integer.parseInt(s.toString());
                int int_display = Integer.parseInt(stock_displayed);

                int calculated = int_min - (int_display + int_warehouse);

                if (calculated < 0) {
                    holder.ed_Stock_Needed.setText("0");
                    //btn Request disable
                } else {
                    //btn Request ENABLE
                    holder.ed_Stock_Needed.setText(String.valueOf(calculated));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                stock_qty_warehouse=s.toString();
            }
        });

        holder.btn_Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.wrapper_form.setVisibility(View.GONE);
                note = holder.ed_Stock_Remarks.getText().toString();
                id_stock = holder.txt_id_stock.getText().toString();
                new AsyncTask_Submit_Stock().execute();
            }
        });
        holder.btn_Request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.wrapper_form.setVisibility(View.GONE);
            }
        });
    }

    private class AsyncTask_Submit_Stock extends AsyncTask<Void,Void,Void>{
        String row_count;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            sp = context_adapter.getSharedPreferences(Parameter_Collections.SH_NAME, Context.MODE_PRIVATE);

            id_pegawai = sp.getString(Parameter_Collections.SH_ID_PEGAWAI, "");
            id_toko= sp.getString(Parameter_Collections.SH_ID_TOKO, "");

            latitude_stock_store = sp.getString(Parameter_Collections.TAG_LATITUDE_NOW, "");
            longitude_stock_store= sp.getString(Parameter_Collections.TAG_LONGITUDE_NOW, "");
        }

        @Override
        protected Void doInBackground(Void... params) {
            Olx_ServiceHandlerJSON sh = new Olx_ServiceHandlerJSON();

            JSONObject jObj = sh.json_insert_stock(id_pegawai,id_toko,id_stock,stock_qty_warehouse,
                    stock_qty_display,note,latitude_stock_store,longitude_stock_store);


            try{
                row_count = jObj.getString(Parameter_Collections.TAG_ROWCOUNT);

            }catch(JSONException e){
                row_count = "0";

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(row_count.equals("1")){
                Toast.makeText(context_adapter,"Input Stock to Store Success", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(context_adapter,"Input Stock to Store Failed", Toast.LENGTH_LONG).show();
            }

        }
    }

    public class Viewholder extends RecyclerView.ViewHolder  {
        public TextView tv,txt_id_stock;
        public EditText ed_Stock_Display, ed_Stock_Warehouse, ed_Stock_Minimum, ed_Stock_Needed,
        ed_Stock_Remarks;
        public Button btn_Submit, btn_Request;
        public View wrapper, wrapper_form;
        public Activity activity;

        public Viewholder(View itemView, Activity activity) {
            super(itemView);
            tv = (TextView)itemView.findViewById(R.id.txt_label);
            txt_id_stock = (TextView)itemView.findViewById(R.id.txt_id_stock);
            wrapper = (View)itemView.findViewById(R.id.wrapper);
            wrapper_form = (View)itemView.findViewById(R.id.wrapper_form);

            ed_Stock_Display = (EditText)itemView.findViewById(R.id.ed_stock_display);
            ed_Stock_Warehouse = (EditText)itemView.findViewById(R.id.ed_stock_warehouse);
            ed_Stock_Minimum = (EditText)itemView.findViewById(R.id.ed_stock_minimum);
            ed_Stock_Needed = (EditText)itemView.findViewById(R.id.ed_stock_needed);
            ed_Stock_Remarks= (EditText)itemView.findViewById(R.id.ed_stock_remarks);

            btn_Submit = (Button)itemView.findViewById(R.id.btn_submit);
            btn_Request = (Button)itemView.findViewById(R.id.btn_request);

            this.activity = activity;
        }


    }
}
