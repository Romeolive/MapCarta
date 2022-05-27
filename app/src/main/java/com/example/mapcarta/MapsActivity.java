package com.example.mapcarta;

import androidx.fragment.app.FragmentActivity;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.mapcarta.databinding.ActivityMapsBinding;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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

        //TODO получение коордианты из intent
        double lat = getIntent().getDoubleExtra("latitude",34.45);
        double lon = getIntent().getDoubleExtra("longitude",123.45);

        // Add a marker in Sydney and move the camera
        LatLng MyPlace = new LatLng(lat, lon);
        //TODO можно добавить маркер
        mMap.addMarker(new MarkerOptions().position(MyPlace).title("Ты здесь!"));
        //TODO камера для приближения по маркеру
        mMap.moveCamera(CameraUpdateFactory.newLatLng(MyPlace));
        //TODO работа с камерой для приближения сразу до нужной точки с тоностью до улиц
        CameraPosition cameraPosition = new CameraPosition(MyPlace, 0,0,20);
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mMap.addMarker(new MarkerOptions().position(latLng)).setTitle("Отметка");
            }
        });

        //TODO класс для преобразования почтового адреса в географические координаты
        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        List<Address> adresses = null;
        try {
            adresses = geocoder.getFromLocation(MyPlace.latitude, MyPlace.longitude,2);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String s = "";
        if (adresses == null || adresses.size() == 0){
            s = "Адрес не определен";
        }
        else{
            for (int i = 0; i < adresses.get(0).getMaxAddressLineIndex(); i++) {
                s += adresses.get(0).getAddressLine(i);
            }
        }
        mMap.addMarker(new MarkerOptions().position(MyPlace).title(s));
    }
}