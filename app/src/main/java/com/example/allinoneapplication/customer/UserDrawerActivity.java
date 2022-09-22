package com.example.allinoneapplication.customer;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bumptech.glide.Glide;
import com.example.allinoneapplication.R;
import com.example.allinoneapplication.complain.CusMechComplaintDetailActivity;
import com.example.allinoneapplication.constant.EndPoint;
import com.example.allinoneapplication.constant.TinyDB;
import com.example.allinoneapplication.model.Complaint;
import com.example.allinoneapplication.model.Customer;
import com.example.allinoneapplication.model.Favourite;
import com.example.allinoneapplication.model.Mechanic;
import com.example.allinoneapplication.model.RateFeedback;
import com.example.allinoneapplication.model.Tracking;
import com.example.allinoneapplication.model.Workshop;
import com.example.allinoneapplication.nearbyareas.NearByAreasActivity;
import com.example.allinoneapplication.retrofit.RetrofitClient;
import com.example.allinoneapplication.service.CustomerRatingService;
import com.example.allinoneapplication.service.GetCustomerComplainDetailService;
import com.example.allinoneapplication.service.GetManageMechanicDetailsServices;
import com.example.allinoneapplication.service.GetMechLocService;
import com.example.allinoneapplication.service.GetTopMechanicService;
import com.example.allinoneapplication.service.GetWorkshopDetailsService;
import com.example.allinoneapplication.service.InsertFavouriteService;
import com.example.allinoneapplication.service.UpdateCustomerstatusService;
import com.example.allinoneapplication.ui.AboutActivity;
import com.example.allinoneapplication.ui.LoginActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
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

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserDrawerActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    List<RateFeedback> rateFeedbackList = new ArrayList<>();
    int code;
    Customer customer;
    private DrawerLayout drawer;
    private NavigationView navigationview;
    String list_pos;
    int selecttype;
    Tracking tracking;
    private Toolbar toolbar;
    private ActionBarDrawerToggle toggle;
    Spinner vehicle_spinner;
    String selectedVehicle;
    String vehicle_array[] = {"Select vehicle", "Car", "Bus", "Truck", "Bike", "Van"};
    RadioButton radio_workshop, radio_mechanic;
    Button btn_customer_search;
    ProgressDialog progressDialog;
    List<Mechanic> mechanicList = new ArrayList<>();
    List<Mechanic> getmechanicList = new ArrayList<>();
    List<Workshop> workshopList = new ArrayList<>();
    List<Workshop> getWorkshopList = new ArrayList<>();
    List<Double> latitudeList = new ArrayList<>();
    List<Double> longitudeList = new ArrayList<>();
    GoogleMap googleMap;
    private static long INTERAl = 60 * 1000;
    private static long FAST_INTERVAL = 15 * 1000;
    private LocationRequest locationRequest;
    private GoogleApiClient googleApiClient;
    Dialog dialog;
    TinyDB tinyDB;
    List<Complaint> complaintList = new ArrayList<>();
    Favourite favourite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_drawer);
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.colorAccent));
        toolbar = findViewById(R.id.toolbar);
        navigationview = findViewById(R.id.navigation);
        drawer = findViewById(R.id.drawer);
        tinyDB = new TinyDB(this);
        vehicle_spinner = findViewById(R.id.vehicle_spinner);
        radio_workshop = findViewById(R.id.radio_workshop);
        radio_mechanic = findViewById(R.id.radio_mechanic);
        btn_customer_search = findViewById(R.id.btn_customer_search);
        dialog = new Dialog(this, R.style.Base_Theme_AppCompat_Light_Dialog_Alert);

        Dexter.withContext(getApplicationContext())
                .withPermissions(Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {

                    }
                }).check();

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("User DashBoard");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, vehicle_array); // spinner ko set kr rahy hai
        vehicle_spinner.setAdapter(adapter);
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.open, R.string.close);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        vehicle_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedVehicle = vehicle_spinner.getSelectedItem().toString(); // spinner pai wo item get ho kr a jy ga like bike
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        final SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.google_map);
        mapFragment.getMapAsync(this);

        navigationview.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.user_drawer_complain:
                        drawer.closeDrawer(GravityCompat.START);
                        startActivity(new Intent(getApplicationContext(), CusMechComplaintDetailActivity.class));
                        finish();
                        break;
                    case R.id.user_drawer_favourite:
                        drawer.closeDrawer(GravityCompat.START);
                        startActivity(new Intent(getApplicationContext(), CustomerFavouriteActivity.class));
                        break;
                    case R.id.near_by_area:
                        drawer.closeDrawer(GravityCompat.START);
                        startActivity(new Intent(UserDrawerActivity.this, NearByAreasActivity.class));
                        break;
                    case R.id.userbooking_history:
                        drawer.closeDrawer(GravityCompat.START);
                        startActivity(new Intent(UserDrawerActivity.this, CustomerHistoryTabLayout.class));
                        break;
                    case R.id.usereditprofile:
                        drawer.closeDrawer(GravityCompat.START);
                        startActivity(new Intent(UserDrawerActivity.this, UserProfileActivity.class));
                        break;
                    case R.id.user_about:
                        drawer.closeDrawer(GravityCompat.START);
                        startActivity(new Intent(UserDrawerActivity.this, AboutActivity.class));
                        break;
                    case R.id.user_logout:
                        tinyDB.clear();
                        Toast.makeText(UserDrawerActivity.this,
                                "Logout Successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        finishAffinity();
                        break;
                }
                return true;
            }
        });

        btn_customer_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (radio_mechanic.isChecked()) {
                    ShowChepestRateDialog();
                } else if (radio_workshop.isChecked()) {
                    GetWorkshopDetails();
                }
            }
        });

    }

    private void ShowChepestRateDialog() {
        Dialog choicedialog = new Dialog(this);
        choicedialog.setContentView(R.layout.mechanic_choice_dialog);
        choicedialog.show();
        Button btn_cheapest = choicedialog.findViewById(R.id.btn_cheapest);
        Button btn_all = choicedialog.findViewById(R.id.btn_all);
        Button btn_rating_mechanic = choicedialog.findViewById(R.id.btn_rating_mechanic);

        btn_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetAllManageMechanicdetails();
                choicedialog.dismiss();
            }
        });

        btn_rating_mechanic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetTopRatedManageMechanicdetails();
                choicedialog.dismiss();
            }
        });

        btn_cheapest.setOnClickListener(view -> {
            GetManageMechanicdetails();
            choicedialog.dismiss();
        });

    }

    private void ShowDialog(String img, String name, String contact, int MID, int _pos, int check) {
        dialog.setContentView(R.layout.mechanic_details);
        dialog.show();
        CircleImageView _mechanic_img;
        TextView mechanic_name, mechanic_contact, mechanic_fav, tv_dialog_heading_title, tv_dialog_heading_contact;
        Button send_request, view_previous_work;

        _mechanic_img = dialog.findViewById(R.id._mechanic_img);
        mechanic_name = dialog.findViewById(R.id.mechanic_name);
        mechanic_contact = dialog.findViewById(R.id.mechanic_contact);
        mechanic_fav = dialog.findViewById(R.id.mechanic_fav);
        send_request = dialog.findViewById(R.id.send_request);
        view_previous_work = dialog.findViewById(R.id.view_previous_work);
        tv_dialog_heading_title = dialog.findViewById(R.id.tv_dialog_heading_title);
        tv_dialog_heading_contact = dialog.findViewById(R.id.tv_dialog_heading_contact);

        if (check == 2) {
            view_previous_work.setVisibility(View.GONE);
            tv_dialog_heading_contact.setText("Workshop Contact");
            tv_dialog_heading_title.setText("Workshop Name");
        } else if (check == 1) {
            view_previous_work.setVisibility(View.VISIBLE);
            tv_dialog_heading_contact.setText("Mechanic Contact");
            tv_dialog_heading_title.setText("Mechanic Name");

        }

        view_previous_work.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (check == 1) {
                    startActivity(new Intent(getApplicationContext(), ViewPreviousWorkActivity.class)
                            .putExtra("MECHANIC_ID", mechanicList.get(_pos).getMechanic_id())
                    );
                }
            }
        });

        mechanic_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selecttype == 1) {
                    AddFavourite(mechanicList.get(_pos).getMechanic_id(), "M");
                } else if (selecttype == 2) {
                    AddFavourite(workshopList.get(_pos).getWmechanic_id(), "W");
                }

            }
        });

        send_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selecttype == 1) {
                    startActivity(new Intent(UserDrawerActivity.this, SelectedMechanicFurtherDetails.class)
                            .putExtra("MECHANIC_NAME", name)
                            .putExtra("MECHANIC_EMAIL", mechanicList.get(_pos).getMechanic_email())
                            .putExtra("MECHANIC_CONTACT", mechanicList.get(_pos).getMechanic_contact())
                            .putExtra("MECHANIC_CNIC", mechanicList.get(_pos).getMechanic_cnic())
                            .putExtra("MECHANIC_IMG", mechanicList.get(_pos).getMechanic_profile_img())
                            .putExtra("MECHANIC_ID", mechanicList.get(_pos).getMechanic_id())
                            .putExtra("MECHANIC_PRICE", mechanicList.get(_pos).getMechanic_price())
                    );
                } else if (selecttype == 2) {
                    startActivity(new Intent(UserDrawerActivity.this, SelectedWorkshopDetails.class)
                            .putExtra("Workshop_MECHANIC_NAME", name)
                            .putExtra("Workshop_MECHANIC_EMAIL", workshopList.get(_pos).getWmechanic_email())
                            .putExtra("Workshop_MECHANIC_CONTACT", workshopList.get(_pos).getWmechanic_contact())
                            .putExtra("Workshop_MECHANIC_CNIC", workshopList.get(_pos).getWmechanic_cnic())
                            .putExtra("Workshop_MECHANIC_PIC", workshopList.get(_pos).getWmechanic_profile_img())
                            .putExtra("WORKSHOP_ID", workshopList.get(_pos).getWmechanic_id())
                    );
                }

            }
        });
        Glide.with(UserDrawerActivity.this).load(EndPoint.IMAGE_URL + img).into(_mechanic_img);
        mechanic_name.setText(name);
        mechanic_contact.setText(contact);

    }

    private void GetManageMechanicdetails() {
        latitudeList.clear();
        longitudeList.clear();
        workshopList.clear();
        getWorkshopList.clear();
        getmechanicList.clear();
        googleMap.clear();
        if (mechanicList != null) {
            mechanicList.clear();
        }
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("please wait...");
        progressDialog.show();
        selecttype = 1;

        GetManageMechanicDetailsServices service = RetrofitClient.getClient().create(GetManageMechanicDetailsServices.class);

        Call<JsonObject> call = service.getManageMechanicdetails();
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    progressDialog.dismiss();
                    if (response.code() == 200) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.body().getAsJsonObject().toString());

                            JSONArray jsonArray = jsonObject.getJSONArray("records");

                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject data = jsonArray.getJSONObject(i);
                                getmechanicList.add(new Mechanic(
                                        data.getInt("mechanic_id"),
                                        data.getString("mechanic_name"),
                                        data.getString("mechanic_address"),
                                        data.getString("mechanic_email"),
                                        data.getDouble("mechanic_lng"),
                                        data.getDouble("mechanic_lat"),
                                        data.getString("mechanic_profile_img"),
                                        data.getString("mehanic_contact"),
                                        data.getString("mechanic_status"),
                                        data.getString("mechanic_datetime"),
                                        data.getString("mechanic_cnic"),
                                        data.getString("vehicle_type"),
                                        data.getInt("mechanic_price")
                                ));

                            }

                            for (int k = 0; k < getmechanicList.size(); k++) {
                                if (getmechanicList.get(k).getVehicle_type().contains(selectedVehicle)
                                        && getmechanicList.get(k).getMechanic_price() <= 200
                                ) {
                                    mechanicList.add(new Mechanic(
                                            getmechanicList.get(k).getMechanic_id(),
                                            getmechanicList.get(k).getMechanic_name(),
                                            getmechanicList.get(k).getMechanic_address(),
                                            getmechanicList.get(k).getMechanic_email(),
                                            getmechanicList.get(k).getMechanic_lng(),
                                            getmechanicList.get(k).getMechanic_lat(),
                                            getmechanicList.get(k).getMechanic_profile_img(),
                                            getmechanicList.get(k).getMechanic_contact(),
                                            getmechanicList.get(k).getMechanic_status(),
                                            getmechanicList.get(k).getMechanic_datetime(),
                                            getmechanicList.get(k).getMechanic_cnic(),
                                            getmechanicList.get(k).getVehicle_type(),
                                            getmechanicList.get(k).getMechanic_price()
                                    ));
                                    GetLocation(getmechanicList.get(k).getMechanic_id());
                                }
                            }
                            Log.e("INDEX", "onResponse: " + mechanicList.size());


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    } else {
                        Toast.makeText(UserDrawerActivity.this,
                                response.message(), Toast.LENGTH_SHORT).show();
                    }

                } else {
                    progressDialog.dismiss();
                    Toast.makeText(UserDrawerActivity.this,
                            response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(UserDrawerActivity.this,
                        t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void GetAllManageMechanicdetails() {
        latitudeList.clear();
        longitudeList.clear();
        workshopList.clear();
        getmechanicList.clear();
        if (mechanicList != null) {
            mechanicList.clear();
        }
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("please wait...");
        progressDialog.show();
        selecttype = 1;

        GetManageMechanicDetailsServices service = RetrofitClient.getClient().create(GetManageMechanicDetailsServices.class);

        Call<JsonObject> call = service.getManageMechanicdetails();
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    progressDialog.dismiss();
                    if (response.code() == 200) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.body().getAsJsonObject().toString());

                            JSONArray jsonArray = jsonObject.getJSONArray("records");

                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject data = jsonArray.getJSONObject(i);
                                getmechanicList.add(new Mechanic(
                                        data.getInt("mechanic_id"),
                                        data.getString("mechanic_name"),
                                        data.getString("mechanic_address"),
                                        data.getString("mechanic_email"),
                                        data.getDouble("mechanic_lng"),
                                        data.getDouble("mechanic_lat"),
                                        data.getString("mechanic_profile_img"),
                                        data.getString("mehanic_contact"),
                                        data.getString("mechanic_status"),
                                        data.getString("mechanic_datetime"),
                                        data.getString("mechanic_cnic"),
                                        data.getString("vehicle_type"),
                                        data.getInt("mechanic_price")
                                ));

                            }

                            for (int k = 0; k < getmechanicList.size(); k++) {
                                if (getmechanicList.get(k).getVehicle_type().contains(selectedVehicle)) {
                                    mechanicList.add(new Mechanic(
                                            getmechanicList.get(k).getMechanic_id(),
                                            getmechanicList.get(k).getMechanic_name(),
                                            getmechanicList.get(k).getMechanic_address(),
                                            getmechanicList.get(k).getMechanic_email(),
                                            getmechanicList.get(k).getMechanic_lng(),
                                            getmechanicList.get(k).getMechanic_lat(),
                                            getmechanicList.get(k).getMechanic_profile_img(),
                                            getmechanicList.get(k).getMechanic_contact(),
                                            getmechanicList.get(k).getMechanic_status(),
                                            getmechanicList.get(k).getMechanic_datetime(),
                                            getmechanicList.get(k).getMechanic_cnic(),
                                            getmechanicList.get(k).getVehicle_type(),
                                            getmechanicList.get(k).getMechanic_price()
                                    ));
                                    GetLocation(getmechanicList.get(k).getMechanic_id());
                                }
                            }
                            Log.e("INDEX", "onResponse: " + mechanicList.size());


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    } else {
                        Toast.makeText(UserDrawerActivity.this,
                                response.message(), Toast.LENGTH_SHORT).show();
                    }

                } else {
                    progressDialog.dismiss();
                    Toast.makeText(UserDrawerActivity.this,
                            response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(UserDrawerActivity.this,
                        t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    public void GetWorkshopDetails() {
        latitudeList.clear();
        longitudeList.clear();
        mechanicList.clear();
        getWorkshopList.clear();
        getmechanicList.clear();
        googleMap.clear();

        if (workshopList != null) {
            workshopList.clear();
        }
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("please wait...");
        progressDialog.show();
        selecttype = 2;

        GetWorkshopDetailsService service = RetrofitClient.getClient().create(GetWorkshopDetailsService.class);

        Call<JsonObject> call = service.getManageWorkshopdetails();
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    progressDialog.dismiss();
                    if (response.code() == 200) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.body().getAsJsonObject().toString());

                            JSONArray jsonArray = jsonObject.getJSONArray("records");

                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject data = jsonArray.getJSONObject(i);

                                getWorkshopList.add(new Workshop(
                                        data.getInt("wmechanic_id"),
                                        data.getString("wmechanic_name"),
                                        data.getString("wmechanic_address"),
                                        data.getString("wmechanic_email"),
                                        data.getDouble("wmechanic_lng"),
                                        data.getDouble("wmechanic_lat"),
                                        data.getString("wmechanic_profile_img"),
                                        data.getString("wmechanic_contact"),
                                        data.getString("wmechanic_status"),
                                        data.getString("wmechanic_datetime"),
                                        data.getString("wmechanic_cnic"),
                                        data.getString("vehicle_type")
                                ));
                            }
                            for (int k = 0; k < getWorkshopList.size(); k++) {
                                if (getWorkshopList.get(k).getVehicle_type().contains(selectedVehicle)) {

                                    workshopList.add(new Workshop(
                                            getWorkshopList.get(k).getWmechanic_id(),
                                            getWorkshopList.get(k).getWmechanic_name(),
                                            getWorkshopList.get(k).getWmechanic_address(),
                                            getWorkshopList.get(k).getWmechanic_email(),
                                            getWorkshopList.get(k).getWmechanic_lng(),
                                            getWorkshopList.get(k).getWmechanic_lat(),
                                            getWorkshopList.get(k).getWmechanic_profile_img(),
                                            getWorkshopList.get(k).getWmechanic_contact(),
                                            getWorkshopList.get(k).getWmechanic_status(),
                                            getWorkshopList.get(k).getWmechanic_datetime(),
                                            getWorkshopList.get(k).getWmechanic_cnic(),
                                            getWorkshopList.get(k).getVehicle_type()
                                    ));
                                    latitudeList.add(getWorkshopList.get(k).getWmechanic_lat());
                                    longitudeList.add(getWorkshopList.get(k).getWmechanic_lng());
                                }
                            }

                            for (int j = 0; j < workshopList.size(); j++) {
                                createMarker(latitudeList.get(j), longitudeList.get(j),
                                        workshopList.get(j).getWmechanic_id(),
                                        workshopList.get(j).getWmechanic_name(),
                                        workshopList.get(j).getWmechanic_contact()
                                        , workshopList.get(j).getWmechanic_id());
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    } else {
                        Toast.makeText(UserDrawerActivity.this,
                                response.message(), Toast.LENGTH_SHORT).show();
                    }

                } else {
                    progressDialog.dismiss();
                    Toast.makeText(UserDrawerActivity.this,
                            response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(UserDrawerActivity.this,
                        t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;
        if (googleMap != null) {
            googleMap.clear();
        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {

                buildGoogleApiClient();
                googleMap.setMyLocationEnabled(true);
            }

        } else {
            buildGoogleApiClient();
            googleMap.setMyLocationEnabled(true);
        }
        googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(final Marker marker) {
                if (selecttype == 1) {
                    String selected_id = marker.getSnippet();
                    list_pos = selected_id.replace("Click to see Details", "");
                    Log.e("INDEX", "onResponseMAP: " + mechanicList.size());
                    ShowMechanic(list_pos);
                } else if (selecttype == 2) {
                    String selected_id = marker.getSnippet();
                    list_pos = selected_id.replace("Click to see Details", "");
                    Log.e("INDEX", "onResponseMAP: " + workshopList.size());
                    ShowWorkshopMechanic(list_pos);
                }
            }
        });

    }

    private void ShowMechanic(String list_pos) {
        for (int i = 0; i < mechanicList.size(); i++) {
            if (mechanicList.get(i).getMechanic_id() == Integer.parseInt(list_pos)) {
                ShowDialog(mechanicList.get(i).getMechanic_profile_img(),
                        mechanicList.get(i).getMechanic_name(),
                        mechanicList.get(i).getMechanic_contact(),
                        mechanicList.get(i).getMechanic_id(),
                        i, 1);
            }

        }


    }

    private void ShowWorkshopMechanic(String list_pos) {
        for (int i = 0; i < workshopList.size(); i++) {
            if (workshopList.get(i).getWmechanic_id() == Integer.parseInt(list_pos)) {
                ShowDialog(workshopList.get(i).getWmechanic_profile_img(),
                        workshopList.get(i).getWmechanic_name(),
                        workshopList.get(i).getWmechanic_contact(),
                        workshopList.get(i).getWmechanic_id(),
                        i, 2);
            }

        }


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
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
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
    public void onLocationChanged(Location location) {
    }

    protected Marker createMarker(double latitude, double longitude,
                                  int Id, String dName, String sName, int pos) {

        return googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .anchor(0.5f, 0.5f)
                .title(dName + " (" + sName + ") ")
                .snippet(String.valueOf(pos) + "Click to see Details")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
    }

    protected Marker createMechMarker(double latitude, double longitude,
                                      int pos) {

        return googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .anchor(0.5f, 0.5f)
                .title("Mechanic Here")
                .snippet(String.valueOf(pos) + "Click to see Details")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
    }

    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);

        } else {
            super.onBackPressed();
        }
    }

    public void GetLocation(int mID) {
        tracking = new Tracking();

        GetMechLocService service = RetrofitClient.getClient().create(GetMechLocService.class);
        Call<Tracking> call = service.getLocation(mID);
        call.enqueue(new Callback<Tracking>() {
            @Override
            public void onResponse(Call<Tracking> call, Response<Tracking> response) {
                if (response.isSuccessful()) {
                    tracking = response.body();
                    if (!tracking.isError()) {
                        createMechMarker(tracking.getCl_latitude(), tracking.getCl_longitude()
                                , mID);

                    } else {
                        Toast.makeText(getApplicationContext(),
                                tracking.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Tracking> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(),
                        t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void AddFavourite(int ID, String type) {
        favourite = new Favourite();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("please wait....");
        progressDialog.show();

        InsertFavouriteService service = RetrofitClient.getClient().create(InsertFavouriteService.class);
        Call<Favourite> call = service.addintoFav(ID, type, tinyDB.getInt("CUSTOMERID"));
        call.enqueue(new Callback<Favourite>() {
            @Override
            public void onResponse(Call<Favourite> call, Response<Favourite> response) {
                if (response.isSuccessful()) {
                    progressDialog.dismiss();
                    favourite = response.body();
                    if (!favourite.isError()) {
                        Toast.makeText(UserDrawerActivity.this,
                                favourite.getMessage(), Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(UserDrawerActivity.this,
                                favourite.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                }
            }

            @Override
            public void onFailure(Call<Favourite> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(UserDrawerActivity.this,
                        t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void GetRatings(int id) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("please wait..");
        progressDialog.setCancelable(false);
        progressDialog.show();

        if (rateFeedbackList != null) {
            rateFeedbackList.clear();
        }

        CustomerRatingService service = RetrofitClient.getClient().create(CustomerRatingService.class);
        Call<JsonObject> call = service.getRatings(id);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    progressDialog.dismiss();
                    code = response.code();

                    if (code == 200) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.body().getAsJsonObject().toString());
                            JSONArray jsonArray = jsonObject.getJSONArray("records");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject data = jsonArray.getJSONObject(i);
                                rateFeedbackList.add(new RateFeedback(
                                        data.getInt("fk_booking_id"),
                                        data.getInt("payment_amount"),
                                        data.getInt("payment_additional"),
                                        data.getInt("payment_total"),
                                        data.getString("payment_datetime"),
                                        data.getInt("payment_discount"),
                                        data.getString("booking_num"),
                                        data.getInt("fk_id")));

                            }

                            if (rateFeedbackList.size() != 0) {
                                ShowBillActivity();
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
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(),
                            "Response issue, Try Again Later", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void ShowBillActivity() {
        for (int i = 0; i < rateFeedbackList.size(); i++) {
            startActivity(new Intent(getApplicationContext(), CustomerBillActivity.class)
                    .putExtra("BOOKING_NUM", rateFeedbackList.get(i).getBooking_num())
                    .putExtra("BOOKING_FEE", rateFeedbackList.get(i).getPayment_amount())
                    .putExtra("ADDITIONAL_PRICE", rateFeedbackList.get(i).getPayment_additional())
                    .putExtra("BOOKING_NUM", rateFeedbackList.get(i).getBooking_num())
                    .putExtra("TOTAL_PRICE", rateFeedbackList.get(i).getPayment_total())
                    .putExtra("DISCOUNT", rateFeedbackList.get(i).getPayment_discount())
                    .putExtra("MECHANIC_ID", rateFeedbackList.get(i).getFk_id())
                    .putExtra("BOOKING_ID", rateFeedbackList.get(i).getFk_booking_id())
            );
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        GetRatings(new TinyDB(this).getInt("CUSTOMERID"));
        int id = tinyDB.getInt("CUSTOMERID");
        GetCompalint(id, "m", "C");

    }

    private void GetTopRatedManageMechanicdetails() {
        latitudeList.clear();
        longitudeList.clear();
        workshopList.clear();
        getmechanicList.clear();
        if (mechanicList != null) {
            mechanicList.clear();
        }
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("please wait...");
        progressDialog.show();
        selecttype = 1;

        GetTopMechanicService service = RetrofitClient.getClient().create(GetTopMechanicService.class);
        Call<JsonObject> call = service.getTopMechanic();
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    progressDialog.dismiss();
                    if (response.code() == 200) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.body().getAsJsonObject().toString());

                            JSONArray jsonArray = jsonObject.getJSONArray("records");

                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject data = jsonArray.getJSONObject(i);
                                getmechanicList.add(new Mechanic(
                                        data.getInt("mechanic_id"),
                                        data.getString("mechanic_name"),
                                        data.getString("mechanic_address"),
                                        data.getString("mechanic_email"),
                                        data.getDouble("mechanic_lng"),
                                        data.getDouble("mechanic_lat"),
                                        data.getString("mechanic_profile_img"),
                                        data.getString("mehanic_contact"),
                                        data.getString("mechanic_status"),
                                        data.getString("mechanic_datetime"),
                                        data.getString("mechanic_cnic"),
                                        data.getString("vehicle_type"),
                                        data.getInt("mechanic_price")
                                ));

                            }

                            for (int k = 0; k < getmechanicList.size(); k++) {
                                if (getmechanicList.get(k).getVehicle_type().contains(selectedVehicle)) {
                                    mechanicList.add(new Mechanic(
                                            getmechanicList.get(k).getMechanic_id(),
                                            getmechanicList.get(k).getMechanic_name(),
                                            getmechanicList.get(k).getMechanic_address(),
                                            getmechanicList.get(k).getMechanic_email(),
                                            getmechanicList.get(k).getMechanic_lng(),
                                            getmechanicList.get(k).getMechanic_lat(),
                                            getmechanicList.get(k).getMechanic_profile_img(),
                                            getmechanicList.get(k).getMechanic_contact(),
                                            getmechanicList.get(k).getMechanic_status(),
                                            getmechanicList.get(k).getMechanic_datetime(),
                                            getmechanicList.get(k).getMechanic_cnic(),
                                            getmechanicList.get(k).getVehicle_type(),
                                            getmechanicList.get(k).getMechanic_price()
                                    ));
                                    GetLocation(getmechanicList.get(k).getMechanic_id());
                                }
                            }
                            Log.e("INDEX", "onResponse: " + mechanicList.size());


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    } else {
                        Toast.makeText(UserDrawerActivity.this,
                                response.message(), Toast.LENGTH_SHORT).show();
                    }

                } else {
                    progressDialog.dismiss();
                    Toast.makeText(UserDrawerActivity.this,
                            response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(UserDrawerActivity.this,
                        t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void GetCompalint(int id, String s_type, String status) {
        if (complaintList != null) {
            complaintList.clear();
        }
        GetCustomerComplainDetailService service = RetrofitClient.getClient().create(GetCustomerComplainDetailService.class);
        Call<JsonObject> call = service.getCustomerComplain(id, s_type, status);
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
                                UpdateCStatus(tinyDB.getInt("CUSTOMERID"), "B");
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

    private void UpdateCStatus(int CID, String Status) {
        customer = new Customer();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("please wait...");
        progressDialog.show();

        UpdateCustomerstatusService service = RetrofitClient.getClient().create(UpdateCustomerstatusService.class);
        Call<Customer> call = service.updateStatus(CID, Status);
        call.enqueue(new Callback<Customer>() {
            @Override
            public void onResponse(Call<Customer> call, Response<Customer> response) {
                if (response.isSuccessful()) {
                    progressDialog.dismiss();
                    customer = response.body();
                    if (!customer.isError()) {
                        Toast.makeText(getApplicationContext(),
                                customer.getMessage(), Toast.LENGTH_SHORT).show();
                        ShowDialog();
                    } else {
                        Toast.makeText(getApplicationContext(),
                                customer.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onFailure(Call<Customer> call, Throwable t) {
                progressDialog.dismiss();
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
