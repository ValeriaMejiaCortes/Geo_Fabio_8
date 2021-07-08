package com.example.locationexample;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.locationexample.databinding.ActivityMapsBinding;

import org.json.JSONException;

import mobility.config.Data;
import mobility.storage.DeviceStorage;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private Button btnSatelite;
    private DeviceStorage storage;
    private Double latit, longi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        storage = new DeviceStorage(this);
        this.latit = Double.parseDouble(storage.get(Data.LATITUDE));
        this.longi = Double.parseDouble(storage.get(Data.LONGITUDE));

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng myLocation = new LatLng(this.latit, this.longi);
        mMap.addMarker(new MarkerOptions().position(myLocation).title("My location"));
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(myLocation, 15);
        mMap.moveCamera(update);


        for (int i = 0; i < Data.mecanicos.length(); i++) {
            try {
                LatLng newLocation = new LatLng(
                        Data.mecanicos.getJSONObject(i).getDouble("latitudCliente"),
                        Data.mecanicos.getJSONObject(i).getDouble("longitudCliente")
                );
                mMap.addMarker(new MarkerOptions().position(newLocation).title(
                        Data.mecanicos.getJSONObject(i).getString("nombreCliente")
                )).setIcon(BitmapDescriptorFactory.fromResource(R.drawable.realimentacion));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}