package mobility.location;

import android.content.Context;
import android.location.LocationManager;

public class Util {

    private Context context;

    public Util(Context context) {
        this.context = context;
    }

    public boolean verifyGPSService() {
        LocationManager lm = (LocationManager) context.getSystemService(context.LOCATION_SERVICE);
        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            return false;
        } else {
            return true;
        }
    }

    public boolean verifyNetworkService() {
        LocationManager lm = (LocationManager) context.getSystemService(context.LOCATION_SERVICE);
        if (!lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            return false;
        } else {
            return true;
        }
    }
}