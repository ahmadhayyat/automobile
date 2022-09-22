package com.example.allinoneapplication.tracking;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.allinoneapplication.R;
import com.example.allinoneapplication.model.Tracking;
import com.example.allinoneapplication.retrofit.RetrofitClient;
import com.example.allinoneapplication.service.GetCurrentLocationService;
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
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;
import java.util.Timer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrackingActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private GoogleApiClient googleApiClient;
    private GoogleMap mMap;
    Tracking tracking;
    double getLiveLat, getliveLng;
    MarkerOptions markerOptions, currentmarkeroption;
    private LatLng latLng;
    private Marker currentLocationMarker, driverMarker;
    int id;
    private Handler mHandler;
    int v = 0;
    private int mInterval = 2000; // 2 seconds by default, can be changed later

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking);
        id = getIntent().getIntExtra("TRACK_USER_ID", 0);

        Dexter.withContext(TrackingActivity.this)
                .withPermissions(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION)
                .withListener(new MultiplePermissionsListener() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (googleApiClient == null) {
                            buildGoogleApiClient();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                })
                .onSameThread()
                .check();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.driverlatmap);
        mapFragment.getMapAsync(this);

        mHandler = new Handler();
        startRepeatingTask();

    }

    protected synchronized void buildGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();
    }

    //----------------------GOOGLE CALLBACKS--------------------------//

    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {

                try {
                    // Customise the styling of the base map using a JSON object defined
                    // in a raw resource file.
                    boolean success = mMap.setMapStyle(
                            MapStyleOptions.loadRawResourceStyle(
                                    this, R.raw.style_json));
                    if (!success) {
                        Log.e("", "Style parsing failed.");
                    }
                } catch (Resources.NotFoundException e) {
                    Log.e("error", "Can't find style. Error: ", e);
                }

                buildGoogleApiClient();

                mMap.setMyLocationEnabled(true);
            }

        } else {

            try {
                // Customise the styling of the base map using a JSON object defined
                // in a raw resource file.
                boolean success = mMap.setMapStyle(
                        MapStyleOptions.loadRawResourceStyle(
                                this, R.raw.style_json));

                if (!success) {
                    Log.e("", "Style parsing failed.");
                }
            } catch (Resources.NotFoundException e) {
                Log.e("error", "Can't find style. Error: ", e);
            }
            buildGoogleApiClient();

            mMap.setMyLocationEnabled(true);
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        currentmarkeroption = new MarkerOptions();
        currentmarkeroption.position(new LatLng(location.getLatitude(), location.getLongitude()));
        currentmarkeroption.title("My location");
        currentmarkeroption.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        currentLocationMarker = mMap.addMarker(currentmarkeroption);

    }

    private void trackDriverLocation() {
        tracking = new Tracking();
        GetCurrentLocationService service = RetrofitClient.getClient().create(GetCurrentLocationService.class);
        Call<Tracking> call = service.getcurrentLocation(id);
        call.enqueue(new Callback<Tracking>() {
            @Override
            public void onResponse(Call<Tracking> call, Response<Tracking> response) {
                if (response.isSuccessful()) {
                    tracking = response.body();
                    if (!tracking.isError()) {
                        onMapReady(mMap);
                        getLiveLat = tracking.getCl_latitude();
                        getliveLng = tracking.getCl_longitude();

                        System.out.println("Location: Lat: " + getLiveLat + " " + "Lng: " + getliveLng);
                        loadMap(getLiveLat, getliveLng);

                    }


                } else {
                    Toast.makeText(getApplicationContext(),
                            response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Tracking> call, Throwable t) {
                Toast.makeText(getApplicationContext(),
                        t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    Runnable mStatusChecker = new Runnable() {
        @Override
        public void run() {
            try {
                v++;
                trackDriverLocation();
                Log.d("GET_TIMER", "run: " + v);//this function can change value of mInterval.
            } finally {
                mHandler.postDelayed(mStatusChecker, mInterval);
            }
        }
    };

    private void refreshMapPosition(LatLng pos, float angle) {
        CameraPosition.Builder positionBuilder = new CameraPosition.Builder();
        positionBuilder.target(pos);
        positionBuilder.zoom(16f);
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(positionBuilder.build()));
    }

    private void loadMap(double lat, double lng) {
        if (markerOptions != null) {
            driverMarker.remove();
        }
        latLng = new LatLng(lat, lng);
        Log.d("CheckLoc", "onCreate: " + lat + "/" + lng);
        markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title(" Tracking");

        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE));
        driverMarker = mMap.addMarker(markerOptions);

        refreshMapPosition(latLng, 90);

    }

    void startRepeatingTask() {
        mStatusChecker.run();
    }


}