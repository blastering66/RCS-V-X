package id.tech.verificareolx;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by RebelCreative-A1 on 15/12/2015.
 */
public class NotifBroadcastReceiver_Vericare extends BroadcastReceiver {

    public NotifBroadcastReceiver_Vericare() {
        super();
    }

    @Override
    public IBinder peekService(Context myContext, Intent service) {
        return super.peekService(myContext, service);
    }

    @Override
    public void onReceive(Context context, Intent intent) {

    }
}
