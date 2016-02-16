package id.tech.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import id.tech.verificareolx.R;

public class CustomAdapter_ProdukToko extends ArrayAdapter<RowData_Produk> {
	LayoutInflater mInflater;
	Context context;
	List<RowData_Produk> datanya;

	public CustomAdapter_ProdukToko(Context context, int resource,
			List<RowData_Produk> objects) {
		super(context, resource, objects);
		// TODO Auto-generated constructor stub
		this.context = context;
		this.datanya = objects;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.item_list_produk, null);
		}
		
		final RowData_Produk item_data = datanya.get(position);
		
		TextView tv_NamaProduk = (TextView)convertView.findViewById(R.id.tv_nama_produk);
		TextView tv_HargaProduk = (TextView)convertView.findViewById(R.id.tv_harga_produk);
		TextView tv_IMEIProduk = (TextView)convertView.findViewById(R.id.tv_imei_produk);
		TextView tv_statusProduk = (TextView)convertView.findViewById(R.id.tv_status_produk);
		
		tv_NamaProduk.setText(item_data.nama_produk);
		tv_HargaProduk.setText(item_data.harga_produk);
		tv_IMEIProduk.setText(item_data.imei_produk);
		tv_statusProduk.setText(item_data.status_produk);
		
		return convertView;
	}

}
