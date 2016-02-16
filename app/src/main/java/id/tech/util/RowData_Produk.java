package id.tech.util;

public class RowData_Produk {
	public String id_produk_toko, nama_produk, harga_produk,imei_produk,imei2_produk, status_produk;
	
	public RowData_Produk(String nama_produk,String harga_produk,
			String imei_produk,String status_produk) {
		// TODO Auto-generated constructor stub
		this.nama_produk = nama_produk;
		this.harga_produk = harga_produk;
		this.imei_produk = imei_produk;
		this.status_produk = status_produk;
	}
	
	public RowData_Produk(String id_produk_toko, String nama_produk,String harga_produk,
			String imei_produk,String imei2_produk,String status_produk) {
		// TODO Auto-generated constructor stub
		this.id_produk_toko = id_produk_toko;
		this.nama_produk = nama_produk;
		this.harga_produk = harga_produk;
		this.imei_produk = imei_produk;
		this.imei2_produk = imei2_produk;
		this.status_produk = status_produk;
	}

}
