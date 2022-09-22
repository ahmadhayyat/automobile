package com.example.allinoneapplication.nearbyareas;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.allinoneapplication.R;
import com.example.allinoneapplication.adapter.PlacesListAdapter;
import com.example.allinoneapplication.nearbyareas.mapmodel.PlacesResults;
import com.example.allinoneapplication.nearbyareas.mapmodel.Result;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NearByAreasActivity extends AppCompatActivity implements
        OnMapReadyCallback,//map control krta hai
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    Spinner nearby_spinner;
    String selectedCat;
    Button btn_nearby;
    String nearby_array[] = {"Select nearby areas", "Tyre shop", "Rest areas", "Petrol pumps"};
    private static final int PROXIMITY_RADIUS = 15000;
    double latitude, longitude;
    private static final int REQUEST_LOCATION_CODE = 10;
    private GoogleApiClient googleApiClient;
    private GoogleMap mMap;
    MarkerOptions place1;
    ProgressDialog progressDialog;
    private static long INTERAl = 60 * 1000;
    private static long FAST_INTERVAL = 15 * 1000;
    private LocationRequest locationRequest;
    private Location lastLocation;
    private Marker currentLocationMarker;
    private LatLng latLng;
    Marker Mmarker;
    double current_lat, current_lng;
    private LocationManager locationManager;
    List<Result> results = new ArrayList<>();
    ListView nearby_listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_near_by_areas);
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.colorAccent));
        nearby_spinner = findViewById(R.id.nearby_spinner);
        btn_nearby = findViewById(R.id.btn_nearby);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, nearby_array);
        nearby_spinner.setAdapter(adapter);
        nearby_listview = findViewById(R.id.nearby_listview);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            checkSelfPermission();
        }


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nearbymap);
        mapFragment.getMapAsync(this);

        btn_nearby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog = new ProgressDialog(NearByAreasActivity.this);
                progressDialog.setTitle("Searching");
                progressDialog.setMessage("Please wait...");
                progressDialog.show();

                if (selectedCat.equals("Select nearby areas")) {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(),
                            "Choose Nearby Area", Toast.LENGTH_SHORT).show();
                } else {
                    if (selectedCat.equals("Tyre shop")) {
                        initView(current_lat, current_lng, "tyreshop");
                    } else if (selectedCat.equals("Rest areas")) {
                        initView(current_lat, current_lng, "restarea");
                    } else if (selectedCat.equals("Petrol pumps")) {
                        initView(current_lat, current_lng, "petrolpump");
                    }
                }
            }
        });

        nearby_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedCat = nearby_spinner.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    protected synchronized void buildGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(INTERAl);
        locationRequest.setFastestInterval(FAST_INTERVAL);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
        } else {
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
        }

    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (mMap != null) {
            mMap.clear();
        }
        mMap = googleMap;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {

            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {

                buildGoogleApiClient();
                googleMap.setMyLocationEnabled(true);

            } else {
                buildGoogleApiClient();
                googleMap.setMyLocationEnabled(true);
            }


            mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker marker) {
                    //TailorDetailLayout.setVisibility(View.VISIBLE);
                    /*Driver_Record(getD_id);
                    latLng = marker.getPosition();
                    place2 = new MarkerOptions().position(latLng).title(getDriver_name);
                    new FetchURL(DriverMapActivity.this).execute(getUrl(place2.getPosition(), place1.getPosition(), "driving"), "driving");
                    new GetDistanceDuration().execute();*/
                }
            });
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        lastLocation = location;
        current_lat = lastLocation.getLatitude();
        current_lng = lastLocation.getLongitude();
        place1 = new MarkerOptions().position(new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude())).title("My Location");
        latLng = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("My Location");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        currentLocationMarker = mMap.addMarker(markerOptions);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (googleApiClient != null) {
            googleApiClient.disconnect();
        }
    }

    private void LoadMap() {

        for (int pos = 0; pos < results.size(); pos++) {
            latitude = results.get(pos).getGeometry().getLocation().getLat();
            longitude = results.get(pos).getGeometry().getLocation().getLng();
            MarkerOptions markerOptions = new MarkerOptions();
            LatLng latLng = new LatLng(latitude, longitude);
            markerOptions.position(latLng);
            markerOptions.title(results.get(pos).getName());
            markerOptions.snippet(results.get(pos).getVicinity());
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            Mmarker = mMap.addMarker(markerOptions);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(12));

        }
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                LatLng position = marker.getPosition();
                String lat = String.valueOf(position.latitude);
                String lng = String.valueOf(position.longitude);
                String name = marker.getTitle();
                Log.d("ULATLNG", "onInfoWindowClick: " + lat + " " + lng + " " + name);
            }
        });

    }

    private void initView(double lat, double lng, String selectedVal) {
        if(results!=null){
            results.clear();
        }
        String keyword = selectedVal;
        String key = getText(R.string.google_maps_key).toString();
        String currentLocation = lat + "," + lng;
        int radius = PROXIMITY_RADIUS;
        String type = selectedVal;
        GoogleMapAPI googleMapAPI = APIClient.getClient().create(GoogleMapAPI.class);
        googleMapAPI.getNearBy(currentLocation, radius, type, keyword, key).enqueue(new Callback<PlacesResults>() {
            @Override
            public void onResponse(Call<PlacesResults> call, Response<PlacesResults> response) {
                if (response.isSuccessful()) {
                    progressDialog.dismiss();
                    results = response.body().getResults();
                    LoadMap();
                    PlacesListAdapter placesListAdapter = new PlacesListAdapter(NearByAreasActivity.this, results);
                    nearby_listview.setAdapter(placesListAdapter);
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<PlacesResults> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        if (ContextCompat.checkSelfPermission(NearByAreasActivity.this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(NearByAreasActivity.this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
//                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2 * 1000, 1);
        } else {
            if (ContextCompat.checkSelfPermission(NearByAreasActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(NearByAreasActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 12);
            }
            if (ContextCompat.checkSelfPermission(NearByAreasActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(NearByAreasActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 13);
            }
        }

    }

    private boolean checkSelfPermission() {

        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_CODE);
            } else {

                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_CODE);
            }

            return false;

        } else
            return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {

            case REQUEST_LOCATION_CODE:

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (googleApiClient == null) {

                            buildGoogleApiClient();
                            mMap.setMyLocationEnabled(true);
                        } else {
                            Toast.makeText(this, "Permission denied!", Toast.LENGTH_SHORT).show();
                        }

                        return;
                    }
                }
                break;
        }
    }

}