package id.tech.util;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import id.tech.verificareolx.R;

public class RecyclerAdapterProduk extends
		RecyclerView.Adapter<RecyclerAdapterProduk.ViewHolder> implements OnClickListener {
	private List<RowData_Produk> data;
	private Context context;

	public class ViewHolder extends RecyclerView.ViewHolder implements OnLongClickListener {
		public TextView nama_produk, harga_produk, imei_produk,imei2_produk, status_produk;

		public ViewHolder(View v) {
			super(v);
			nama_produk = (TextView) v.findViewById(R.id.tv_nama_produk);
			harga_produk = (TextView) v.findViewById(R.id.tv_harga_produk);
			imei_produk = (TextView) v.findViewById(R.id.tv_imei_produk);
			imei2_produk = (TextView) v.findViewById(R.id.tv_imei2_produk);
			status_produk = (TextView) v.findViewById(R.id.tv_status_produk);
//			v.setOnLongClickListener(this);
		}

		@Override
		public boolean onLongClick(View v) {
			// TODO Auto-generated method stub		
//			Toast.makeText(context, "11111", Toast.LENGTH_SHORT).show();
			return true;
		}
	}

	public RecyclerAdapterProduk(List<RowData_Produk> data, Context context) {
		this.data = data;
		this.context = context;
	}

	@Override
	public int getItemCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		// TODO Auto-generated method stub
		holder.nama_produk.setText("Tipe : " + data.get(position).nama_produk);
		holder.harga_produk.setText(data.get(position).harga_produk);
		holder.imei_produk.setText("IMEI 1 : "+ data.get(position).imei_produk);
		if(data.get(position).imei2_produk.equals("")){
			holder.imei2_produk.setText("IMEI 2 : - - -");
		}else{
			holder.imei2_produk.setText("IMEI 2 : "+ data.get(position).imei2_produk);
		}
		
		
		if(data.get(position).status_produk.equals("1")){
			holder.status_produk.setText("Tersedia");
			holder.status_produk.setTextColor(context.getResources().getColor(R.color.color_red));
		}else{
			holder.status_produk.setText("Terjual");
			holder.status_produk.setTextColor(context.getResources().getColor(R.color.color_green));
		}
		holder.onLongClick(holder.itemView);
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int position) {
		// TODO Auto-generated method stub
		View v = LayoutInflater.from(parent.getContext()).inflate(
				R.layout.item_list_produk, parent, false);
		ViewHolder viewholder = new ViewHolder(v);
		
		return viewholder;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
//		int itemPosition = mRecyclerView.getChildPosition(view);
//	    String item = mList.get(itemPosition);
//	    Toast.makeText(this, "test", Toast.LENGTH_LONG).show();
	}

	

}
