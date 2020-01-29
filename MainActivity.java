package com.example.gpsproject;

        import androidx.annotation.NonNull;
        import androidx.appcompat.app.AppCompatActivity;
        import androidx.core.app.ActivityCompat;

        import android.Manifest;
        import android.annotation.SuppressLint;
        import android.content.Context;
        import android.content.pm.PackageManager;
        import android.location.Address;
        import android.location.Geocoder;
        import android.location.Location;
        import android.location.LocationListener;
        import android.location.LocationManager;
        import android.os.Bundle;
        import android.os.SystemClock;
        import android.util.Log;
        import android.view.View;
        import android.widget.Button;
        import android.widget.TextView;

        import java.io.IOException;
        import java.util.ArrayList;
        import java.util.List;
        import java.util.Locale;

public class MainActivity extends AppCompatActivity implements LocationListener {
    TextView distance;
    TextView latitude;
    TextView longitude;
    TextView address;
    Button reset;

    ArrayList<Location> locationList = new ArrayList<>();
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    double lat;
    double lon;
    List<Address> list;
    Geocoder geocoder;
    LocationManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        distance = findViewById(R.id.distance);
        latitude = findViewById(R.id.lat);
        longitude = findViewById(R.id.lon);
        address = findViewById(R.id.address);
        geocoder = new Geocoder(this, Locale.US);
        reset = findViewById(R.id.button);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                distance.setText("");
                locationList.clear();
            }
        });

        manager = (LocationManager)getSystemService(LOCATION_SERVICE);

        if((ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) &&
                (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
        }
        else{
            manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 1, this);
            manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 3000, 1, this);
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 10, this);
                    manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 3000, 10, this);
                }
                break;
        }

    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d("TAG", "AGAEGFASf");
        lat = location.getLatitude();
        lon = location.getLongitude();
        longitude.setText(lon + "");
        latitude.setText(lat + "");

        try {
            list = geocoder.getFromLocation(lat, lon, 1);
        } catch (IOException e) {
            Log.d("TAG", "IOException");
            //  Log.d("TAG", list.toString());
            e.printStackTrace();
        }
        address.setText(list.get(0).getAddressLine(0));


        locationList.add(location);
        if(locationList.size() > 1) {
            Location one = locationList.get(locationList.size()-1);
            Location two = locationList.get(0);
            float distanceNum = one.distanceTo(two);

            if((SystemClock.uptimeMillis() < 1000)) {
                distanceNum = 0;
            }
            String dist = distanceNum + "";
            distance.setText(dist);
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
