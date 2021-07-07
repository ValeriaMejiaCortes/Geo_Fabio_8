package com.example.locationexample;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import mobility.config.Data;
import mobility.storage.DeviceStorage;
import mobility.ui.Alerts;
import service.LocationService;

public class geoService extends AppCompatActivity {

    private Button start, stop;
    private Context context;
    private DeviceStorage storage;
    private Alerts alerts;
    private static final String[] PERMS = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION
    };
    private static final int REQUEST = 2301;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geo_service);
        initObjects();
        getViews();
        listenEvents();
        //Verificamos el acceso a los permisos de escritura y ubicación
        verifyPermission();
    }

    private void initObjects() {
        context = this;
        storage = new DeviceStorage(context);
        alerts = new Alerts(context);
    }

    private void getViews() {
        start = findViewById(R.id.start);
        stop = findViewById(R.id.stop);
    }

    private void listenEvents() {
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start();
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stop();
            }
        });
    }

    private void start() {
        if (storage.get(Data.SESSION).equals(Data.FALSE) || storage.get(Data.SESSION).isEmpty()) {
            storage.save(Data.SESSION, Data.TRUE);
            startService();
            Intent myIntent = new Intent( this, MapsActivity.class);
            startActivity(myIntent);
            alerts.toast("Servicio Iniciado");
        } else {
            alerts.toast("El servicio ya se encuentra corriendo");
        }
    }

    private void stop() {
        if (storage.get(Data.SESSION).equals(Data.TRUE)) {
            storage.save(Data.SESSION, Data.FALSE);
            stopService();
            alerts.toast("Servicio Detenido");
        } else {
            alerts.toast("El servicio no se encuentra corriendo");
        }
    }

    private void startService() {
        stopService();
        Thread thread = new Thread() {
            @Override
            public void run() {
                Intent intent = new Intent(context, LocationService.class);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    startForegroundService(intent);
                } else {
                    startService(intent);
                }
            }
        };
        thread.start();
    }

    private void stopService() {
        try {
            stopService(new Intent(context, LocationService.class));
        } catch (Exception e) {
        }
    }


    @TargetApi(Build.VERSION_CODES.M)
    private void verifyPermission() {
        if (!canAccessLocation() || !canAccessWrite()) {
            requestPermissions(PERMS, REQUEST);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST:
                if (canAccessWrite() && canAccessLocation()) {
                    alerts.toast("Permisos Concedidos");
                } else {
                    alerts.toast("Por favor habilita los permisos");
                }
                break;
        }
    }

    private boolean canAccessLocation() {
        return (hasPermission(Manifest.permission.ACCESS_FINE_LOCATION));
    }

    private boolean canAccessWrite() {
        return (hasPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE));
    }

    private boolean hasPermission(String perm) {
        try {
            return (PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(this, perm));
        } catch (Exception e) {
            return true;
        }
    }
}