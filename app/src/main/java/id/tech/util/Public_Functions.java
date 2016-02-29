package id.tech.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;

public class Public_Functions {
	public Context ctx;

	//test
	
	public Public_Functions(Context ctx) {
		// TODO Auto-generated constructor stub
		this.ctx = ctx;
	}
	
	public static boolean isNetworkAvailable(Context ctx) {
		ConnectivityManager cm = (ConnectivityManager) ctx
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		try {

			android.net.NetworkInfo wifi = cm
					.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			android.net.NetworkInfo mobile = cm
					.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

			if (wifi.isConnected() || mobile.isConnected()) {
				Log.d("Koneksi Data ADA", "");
				return true;
			} else if (wifi.isConnected() && mobile.isConnected()) {
				Log.d("wifi dan mobile ADA", "");
				return true;
			} else {
				Log.d(" Data TIDAK ADA", "!!!");
				return false;
			}
		} catch (NullPointerException e) {
			Log.d("Error = ", e.getMessage().toString());
			return false;
		} catch (Exception e) {
			// TODO: handle exception
			Log.d("Exception = ", e.getMessage().toString());
			return false;
		}
	}
	
	
	public static void delete_IssuePhoto(){
		String root = Environment.getExternalStorageDirectory().toString();
		File myDir = new File(root + "/HTC/images/issue");
		if(myDir.isDirectory()){
			String[] children = myDir.list();
	        for (int i = 0; i < children.length; i++) {
	            File filenya = new File(myDir, children[i]);
	            
	            if (filenya.exists()) {
	                String deleteCmd = "rm -r " + filenya.getAbsolutePath();
	                Runtime runtime = Runtime.getRuntime();
	                try {
	                    runtime.exec(deleteCmd);
	                } catch (IOException e) { }
	            }
	            
	        }
		}
	}
	
	public static void DeleteRecursive(File fileOrDir) {
		String root = Environment.getExternalStorageDirectory().toString();
		File myDir = new File(root + "/HTC/images/issue");
	    if (myDir.isDirectory()){
	    	for (File child : myDir.listFiles()){
	    		Log.e("delete =", child.getAbsolutePath());
	            DeleteRecursive(child);
	    		myDir.delete();
	    	}
	    }	    
	}
	
//	public static boolean isNetworkAvailable(Context ctx) {
//		ConnectivityManager cm = (ConnectivityManager) ctx
//				.getSystemService(Context.CONNECTIVITY_SERVICE);
//		try {
//
//			android.net.NetworkInfo wifi = cm
//					.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
//			android.net.NetworkInfo mobile = cm
//					.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
//
//			if (wifi.isConnected() || mobile.isConnected()) {
//				Log.d("Koneksi Data ADA", "");
//				return true;
//			} else if (wifi.isConnected() && mobile.isConnected()) {
//				Log.d("Koneksi Data wifi dan mobile ADA", "");
//				return true;
//			} else {
//				Log.d("Koneksi Data TIDAK ADA", "!!!");
//				return false;
//			}
//		} catch (NullPointerException e) {
//			Log.d("Connection Null Pointer= ", e.getMessage().toString());
//			return false;
//		} catch (Exception e) {
//			// TODO: handle exception
//			Log.d("Exception = ", e.getMessage().toString());
//			return false;
//		}
//	}

}
