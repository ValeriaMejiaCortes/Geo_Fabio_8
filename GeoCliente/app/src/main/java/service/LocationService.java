package service;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.locationexample.MainActivity;
import com.example.locationexample.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import mobility.config.Data;
import mobility.config.Parameters;
import mobility.network.Requests;
import mobility.storage.DeviceStorage;
import mobility.ui.Alerts;

public class LocationService extends android.app.Service {

    private Context context;
    private Timer timer;
    private Handler handler;
    private LocationListener locListener;
    private LocationManager locManager;
    private Requests requests;
    private JSONObject params;
    private DeviceStorage storage;
    private Alerts alerts;
    private int TIME_REQUEST = 50;//Segundos para enviar posición

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initObjects();
        notification();
        startLocation();
        taskSend();
    }

    private void initObjects() {
        context = this;
        requests = new Requests(context);
        storage = new DeviceStorage(context);
        handler = new Handler();
    }


    public void taskSend() {
        timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        try {
                            new Alerts(context).toast("Intentamos enviar posición al servidor");
                            sendData();
                        } catch (JSONException e) {
                        }
                    }
                });
            }
        };
        timer.schedule(task, TIME_REQUEST * 1000, TIME_REQUEST * 1000);
    }

    private void sendData() throws JSONException {
        //Para este ejemplo se está usando la Volley para los consumos HTTP

        params = new JSONObject();
        //Parametros a enviar en el request
        params.put("latitude", storage.get(Data.LATITUDE));
        params.put("longitude", storage.get(Data.LONGITUDE));

        final String URL = Parameters.LOCATION_SERVER +
                "EditarLatitudyLongitud?newLatitud=" + storage.get(Data.LATITUDE) +
                "&newLongitud=" + storage.get(Data.LONGITUDE);

        Response.Listener<JSONObject> success = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
            }
        };

        Response.ErrorListener error = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        };

        requests.post(URL,
                params,
                requests.getDefaultHeaders(),
                success,
                error
        );
    }


    public void notification() {
        //Al tener un servicio en segundo plano, debemos informarle al usuario usando una notificación de tipo FOREGROUND
        Intent notificationIntent = new Intent(context, MainActivity.class);
        showNotification(context, notificationIntent);
    }


    public void showNotification(Context context, Intent intent) {
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            String CHANNEL_ID = getString(R.string.app_name);

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, getString(R.string.app_name),
                    NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription(getString(R.string.app_name));
            manager.createNotificationChannel(channel);

            builder = new NotificationCompat.Builder(context, CHANNEL_ID);
        } else {
            builder = new NotificationCompat.Builder(context);
        }

        Notification notification = builder.setContentIntent(pendingIntent)
                .setContentTitle(getString(R.string.app_name))
                .setTicker(getString(R.string.app_name))
                .setContentText(getString(R.string.app_name))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent)
                .setOngoing(true).build();
        startForeground(823, notification);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        //Validación para detener el servicio en segundo plano
        if (storage.get(Data.SESSION).equals(Data.FALSE)) {
            stop();
        }
    }


    private void stop() {
        //Detenemos el servicio y dejamos de escuchar actualizaciones de la ubicación

        try {
            if (timer != null) {
                timer.cancel();
                timer.purge();
                timer = null;
            }
            locManager.removeUpdates(locListener);
            stopSelf();
            stopForeground(true);
        } catch (Exception e) {
        }
    }


    private void startLocation() {
        //Iniciamos los objetos para obtener la ubicación del usuario
        locManager = (LocationManager) this
                .getSystemService(Context.LOCATION_SERVICE);
        Location loc;
        //Verificamos permisos
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        //Obtenemos la última posición conocida del dispositivo
        loc = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        showLocation(loc);
        locListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                showLocation(location);
            }

            public void onProviderDisabled(String provider) {
                Toast.makeText(context, "Por favor enciende el GPS", Toast.LENGTH_LONG).show();
            }

            public void onProviderEnabled(String provider) {

            }

            public void onStatusChanged(String provider, int status,
                                        Bundle extras) {
            }
        };

        //Escuchamos los cambios de posición cada 15 segundos
        locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 15000, 0,
                locListener);
    }

    private void showLocation(Location loc) {
        if (loc != null) {
            try {
                storage.save(Data.LATITUDE, String.valueOf(loc.getLatitude()));
                storage.save(Data.LONGITUDE, String.valueOf(loc.getLongitude()));
                new Alerts(context).toast("Mi Posición " + storage.get(Data.LATITUDE) + ", " + storage.get(Data.LONGITUDE));
            } catch (Exception e) {
            }
        }
    }
}