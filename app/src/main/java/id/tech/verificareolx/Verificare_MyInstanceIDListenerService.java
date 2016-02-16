package id.tech.verificareolx;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.android.gms.iid.InstanceID;
import com.google.android.gms.iid.InstanceIDListenerService;
/**
 * Created by RebelCreative-A1 on 16/12/2015.
 */
public class Verificare_MyInstanceIDListenerService extends InstanceIDListenerService {

    private static final String TAG = "MyInstanceIDLS";

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. This call is initiated by the
     * InstanceID provider.
     */
    // [START refresh_token]
    @Override
    public void onTokenRefresh() {
        // Fetch updated Instance ID token and notify our app's server of any changes (if applicable).
        Intent intent = new Intent(this, Verificare_RegistrationIntentService.class);
        startService(intent);
    }
}
