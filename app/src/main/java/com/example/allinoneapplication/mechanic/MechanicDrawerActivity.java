package com.example.allinoneapplication.mechanic;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.allinoneapplication.R;
import com.example.allinoneapplication.adapter.UpcomingBookingAdapter;
import com.example.allinoneapplication.complain.CusMechComplaintDetailActivity;
import com.example.allinoneapplication.constant.TinyDB;
import com.example.allinoneapplication.model.Booking;
import com.example.allinoneapplication.model.Complaint;
import com.example.allinoneapplication.model.Mechanic;
import com.example.allinoneapplication.model.Tracking;
import com.example.allinoneapplication.retrofit.RetrofitClient;
import com.example.allinoneapplication.service.GetComplaintService;
import com.example.allinoneapplication.service.GetMechanicBookService;
import com.example.allinoneapplication.service.GetMechanicComplainDetailService;
import com.example.allinoneapplication.service.InsertUpdateLocService;
import com.example.allinoneapplication.service.UpdateMechanicStatusService;
import com.example.allinoneapplication.ui.AboutActivity;
import com.example.allinoneapplication.ui.LoginActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.JsonObject;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MechanicDrawerActivity extends AppCompatActivity implements LocationListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private DrawerLayout drawer;
    private NavigationView navigationview;
    private Toolbar toolbar;
    private ActionBarDrawerToggle toggle;
    TextView tv_upcome_booking;
    MenuItem services;
    ListView mechanic_home_LV;
    TinyDB tinyDB;
    UpcomingBookingAdapter adapter;
    ProgressDialog progressDialog;
    List<Booking> bookingList = new ArrayList<>();
    Tracking tracking;
    int code;
    private static long INTERAl = 2000;
    private static long FAST_INTERVAL = 1000;
    private static final int REQUEST_LOCATION_CODE = 10;
    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    long INTERVAl = 10;
    private Location lastLocation;
    private double getLat, getLng;
    List<Complaint> complaintList = new ArrayList<>();
    Mechanic mechanic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mechanic_drawer);
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.colorAccent));
        toolbar = findViewById(R.id.toolbar);
        tinyDB = new TinyDB(this);
        navigationview = findViewById(R.id.navigation);
        drawer = findViewById(R.id.drawer);
        tv_upcome_booking = findViewById(R.id.tv_upcome_booking);
        services = findViewById(R.id.services);
        mechanic_home_LV = findViewById(R.id.mechanic_home_LV);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Mechanic DashBoard");
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));
        toggle.syncState();
        GetBookings();
        navigationview.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.mechanic_complain:
                        startActivity(new Intent(getApplicationContext(), CusMechComplaintDetailActivity.class));
                        finish();
                        break;
                    case R.id.services:
                        startActivity(new Intent(MechanicDrawerActivity.this, MechanicServiceActivity.class));
                        finish();
                        break;
                    case R.id.mech_bookings:
                        startActivity(new Intent(MechanicDrawerActivity.this, MHistoryTablayoutActivity.class));
                        break;
                    case R.id.mech_editprofile:
                        startActivity(new Intent(MechanicDrawerActivity.this, MechanicProfileActivity.class));
                        break;
                    case R.id.mech_about:
                        startActivity(new Intent(MechanicDrawerActivity.this, AboutActivity.class));
                        break;
                    case R.id.mech_logout:
                        tinyDB.clear();
                        Toast.makeText(MechanicDrawerActivity.this, "Logout Successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        finish();
                        break;
                }
                return true;
            }

        });


        Dexter.withActivity(MechanicDrawerActivity.this)
                .withPermissions(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION)
                .withListener(new MultiplePermissionsListener() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (mGoogleApiClient == null) {
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
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(INTERVAl)
                .setFastestInterval(FAST_INTERVAL);

    }

    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);

        } else {
            super.onBackPressed();
        }
    }

    private void GetBookings() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("please wait...");
        progressDialog.show();

        GetMechanicBookService service = RetrofitClient.getClient().create(GetMechanicBookService.class);
        Call<JsonObject> call = service.getBookings(tinyDB.getInt("MECHANIC_ID"), "M", "P");
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    progressDialog.dismiss();

                    try {
                        JSONObject jsonObject = new JSONObject(response.body().getAsJsonObject().toString());

                        JSONArray jsonArray = jsonObject.getJSONArray("records");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject data = jsonArray.getJSONObject(i);

                            bookingList.add(new Booking(data.getInt("customer_id"),
                                    data.getString("customer_name"),
                                    data.getString("customer_contact"),
                                    data.getString("customer_profile_img"),
                                    data.getInt("booking_id"),
                                    data.getString("booking_num"),
                                    data.getString("booking_datetime"),
                                    data.getInt("booking_fee"),
                                    data.getString("booking_description"),
                                    data.getString("booking_status")
                            ));

                        }
                        if (bookingList.size() == 0) {
                            tv_upcome_booking.setVisibility(View.VISIBLE);
                        } else {
                            tv_upcome_booking.setVisibility(View.GONE);
                            adapter = new UpcomingBookingAdapter(bookingList, MechanicDrawerActivity.this);
                            mechanic_home_LV.setAdapter(adapter);

                        }

                    } catch (JSONException e) {
                        progressDialog.dismiss();
                        tv_upcome_booking.setVisibility(View.VISIBLE);
                        e.printStackTrace();
                    }
                } else {
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(MechanicDrawerActivity.this,
                        t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void UpdateLoc(double lat, double lng) {
        tracking = new Tracking();
        InsertUpdateLocService service = RetrofitClient.getClient().create(InsertUpdateLocService.class);
        Call<Tracking> call = service.updateLoc(lat, lng, tinyDB.getInt("MECHANIC_ID"));
        call.enqueue(new Callback<Tracking>() {
            @Override
            public void onResponse(Call<Tracking> call, Response<Tracking> response) {
                if (response.isSuccessful()) {
                    tracking = response.body();
                    if (!tracking.isError()) {
                        Log.e("LOCATION", "onResponse: " + tracking.getMessage());
                    } else {
                        Toast.makeText(getApplicationContext(),
                                tracking.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Tracking> call, Throwable t) {
                Toast.makeText(getApplicationContext(),
                        t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    private void showGPSDisabledAlertToUser() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("GPS is disabled in your device. Would you like to enable it?")
                .setCancelable(false)
                .setPositiveButton("Goto Settings Page To Enable GPS",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent callGPSSettingIntent = new Intent(
                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(callGPSSettingIntent);
                            }
                        });
        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    @Override
    public void onConnected(Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAl);
        mLocationRequest.setFastestInterval(FAST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (mGoogleApiClient.isConnected()) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
            } else {
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, 1000);
                /*
                 * Thrown if Google Play services canceled the original
                 * PendingIntent
                 */
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else {
            /*
             * If no resolution is available, display a dialog to the
             * user with the error.
             */
            Log.e("Error", "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

    /**
     * If locationChanges change lat and long
     *
     * @param location
     */
    @Override
    public void onLocationChanged(Location location) {

        lastLocation = location;

        getLat = lastLocation.getLatitude();
        getLng = lastLocation.getLongitude();

        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();


        UpdateLoc(getLat, getLng);
        Log.d("LOCATIONU", "onLocationChanged: " + getLat + " " + getLng);

    }

    @Override
    protected void onResume() {
        super.onResume();
        int id = tinyDB.getInt("MECHANIC_ID");
        GetCompalint(id, "c", "C");
    }

    private void GetCompalint(int id, String s_type, String status) {
        if (complaintList != null) {
            complaintList.clear();
        }

        GetMechanicComplainDetailService service = RetrofitClient.getClient().create(GetMechanicComplainDetailService.class);
        Call<JsonObject> call = service.getMechanicComplain(id, s_type, status);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    code = response.code();

                    if (code == 200) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.body().getAsJsonObject().toString());
                            JSONArray jsonArray = jsonObject.getJSONArray("records");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject data = jsonArray.getJSONObject(i);
                                complaintList.add(new Complaint(
                                        data.getInt("comp_id"),
                                        data.getString("comp_sub")));
                            }
                            if (complaintList.size() >= 3) {
                                UpdateStatus(tinyDB.getInt("MECHANIC_ID"), "B");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else if (code == 404) {
                        Toast.makeText(getApplicationContext(), "Server connectivity error!", Toast.LENGTH_SHORT).show();
                    } else if (code == 500) {
                        Toast.makeText(getApplicationContext(), "Internal Server error!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Response issue, Try Again Later", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void UpdateStatus(int MID, String Status) {
        mechanic = new Mechanic();

        UpdateMechanicStatusService service = RetrofitClient.getClient().create(UpdateMechanicStatusService.class);
        Call<Mechanic> call = service.updateStatus(MID, Status);
        call.enqueue(new Callback<Mechanic>() {
            @Override
            public void onResponse(Call<Mechanic> call, Response<Mechanic> response) {
                if (response.isSuccessful()) {
                    mechanic = response.body();
                    if (!mechanic.isError()) {
                        Toast.makeText(getApplicationContext(),
                                mechanic.getMessage(), Toast.LENGTH_SHORT).show();
                        ShowDialog();
                    } else {
                        Toast.makeText(getApplicationContext(),
                                mechanic.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onFailure(Call<Mechanic> call, Throwable t) {
                Toast.makeText(getApplicationContext(),
                        t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void ShowDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.pending_request_dialog);
        dialog.show();

        TextView tv_pending_req = dialog.findViewById(R.id.tv_pending_req);

        tv_pending_req.setText("Your Account Status has been Blocked Due to Exceed of Complain Limits");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
                startActivity(new Intent(getApplicationContext(),
                        LoginActivity.class));
                finish();
            }
        }, 5500);

    }

}
