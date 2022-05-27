package com.example.mapcarta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    Button showMapButton;
    TextView latText, lonText, timeText;

    LocationManager locationManager;
    Location location;

    private boolean granted = false;
    private final int LOCATION_PERMISSION = 10001;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        showMapButton = findViewById(R.id.toMapButton);
        lonText = findViewById(R.id.lon);
        latText = findViewById(R.id.lat);
        timeText = findViewById(R.id.timeText);

        //TODO подключить менеджер местоположения
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (granted || checkPermission()){
            //TODO проверка разрешения и попытка запроса
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000 * 10, 20,listener);
        }

        showMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                intent.putExtra("latitude",location.getLatitude());
                intent.putExtra("longitude",location.getLongitude());
                startActivity(intent);

            }
        });

    }

    //TODO описать LocationListener
    LocationListener listener = new LocationListener() {
        @Override
        public void onLocationChanged(@NonNull Location location) {
            if (location == null){
                return;
            }
            else {
                showLocation(location);
            }
        }


        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            //TODO сделать окно о запросе всё-таки подрубить разрешение
        }

        @Override
        public void onProviderEnabled(@NonNull String provider) {

        }

        @Override
        public void onProviderDisabled(@NonNull String provider) {

        }
    };


    @Override
    protected void onResume() {
        super.onResume();
        //TODO реализовать получение координат с запросом разрешения

        if (granted || checkPermission()){
            //TODO получили координаты
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000 * 10, 20,listener);
            if (locationManager != null){
                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (location != null){
                    showLocation(location);
                }

            }
        }


    }

    //TODO переопределить функцию обратного вызова для обработки ответа пользователя
    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(listener);
    }

    private  boolean checkPermission(){

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            //TODO requestPermissions - создает окно проверки , если отклонили доступ

            //TODO  requestPermissions после вызывает onRequestPermissionsResult из 112 строки
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION);
            return false;

        }else{
            return true;
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //TODO получает результат запроса на разрешение из  checkPermission

        if (requestCode == LOCATION_PERMISSION){
            granted = true;
            if (grantResults.length > 0){
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED){
                        granted = false;
                    }
                }
            }else{
                granted = false;
            }
        }
    }

    private void showLocation(Location location){
        String koord  = String.valueOf(location.getLatitude());
        latText.setText(koord);
        koord  = String.valueOf(location.getLongitude());
        lonText.setText(koord);
        koord = new Date(location.getTime()).toString();
        timeText.setText(koord);

    }
}
