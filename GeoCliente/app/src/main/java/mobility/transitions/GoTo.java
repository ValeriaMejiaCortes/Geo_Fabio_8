package mobility.transitions;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;


public class GoTo {
    private Context context;

    public GoTo(Context context) {
        this.context = context;
    }

    public void goToGoogleMaps(String endLat, String endLng) {
        try {
            String url = "http://maps.google.com/maps?&daddr=" + endLat + "," + endLng + "&mode=driving";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
            context.startActivity(intent);
        } catch (Exception e) {
        }
    }

    public void goToGoogleMapsWalking(String endLat, String endLng, String originLat, String originLng) {
        try {
            Uri uri = Uri.parse("http://maps.google.com/maps?&saddr=" + originLat + "," + originLng + "&daddr=" + endLat + "," + endLng + "&mode=w");
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, uri);
            mapIntent.setPackage("com.google.android.apps.maps");
            context.startActivity(mapIntent);
        } catch (Exception e) {
        }
    }

    public void goToWaze(String lat, String lng) {
        try {
            String url = "waze://?ll=" + lat + "," + lng + "&navigate=yes&z=10";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            context.startActivity(intent);
        } catch (Exception e) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.waze"));
            context.startActivity(intent);
        }
    }
}