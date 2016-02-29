package id.tech.verificareolx;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootCompletedReceiver extends BroadcastReceiver{
	Context context;

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		this.context =context;
		
		try{
			Thread.sleep(60000);
		}catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Intent notif_service = new Intent(context, Notif_Service.class);
		notif_service.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		notif_service.addFlags(Intent.FLAG_RECEIVER_NO_ABORT);
		//add FLAG_RECEIVER_FOREGROUND to force the intent in foreground
		notif_service.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
		context.startService(notif_service);
		
		Log.e("SERVICE >>>>", "SERVICE START");
	}
	
	public boolean isServiceCekRegistrasiRunning() {
		ActivityManager manager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		for (RunningServiceInfo service : manager
				.getRunningServices(Integer.MAX_VALUE)) {
			if (Notif_Service.class.getName().equals(
					service.service.getClassName())) {
				return true;
			}
		}
		return false;
	}

}
