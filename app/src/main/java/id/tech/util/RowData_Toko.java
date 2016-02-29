package id.tech.util;

public class RowData_Toko {
	public String id_toko,kode_toko, nama_toko, region_toko;
	public boolean visited;
	
	public RowData_Toko(String id_toko, String kode_toko, String nama_toko, String region_toko) {
		// TODO Auto-generated constructor stub
		this.id_toko = id_toko;
		this.kode_toko = kode_toko;
		this.nama_toko = nama_toko;
		this.region_toko = region_toko;
	}

	public RowData_Toko(String id_toko, String kode_toko, String nama_toko, String region_toko, boolean visited) {
		// TODO Auto-generated constructor stub
		this.id_toko = id_toko;
		this.kode_toko = kode_toko;
		this.nama_toko = nama_toko;
		this.region_toko = region_toko;
		this.visited = visited;
	}

	public RowData_Toko(String nama_toko, boolean visited) {
		// TODO Auto-generated constructor stub

		this.nama_toko = nama_toko;
		this.visited = visited;
	}
}
