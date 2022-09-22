package com.example.allinoneapplication.customer;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.allinoneapplication.R;
import com.example.allinoneapplication.adapter.GetServiceAdapter;
import com.example.allinoneapplication.constant.EndPoint;
import com.example.allinoneapplication.constant.TinyDB;
import com.example.allinoneapplication.model.Booking;
import com.example.allinoneapplication.model.Service;
import com.example.allinoneapplication.retrofit.RetrofitClient;
import com.example.allinoneapplication.service.BookingDetailsService;
import com.example.allinoneapplication.service.GetServiceRecordAPI;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectedMechanicFurtherDetails extends AppCompatActivity {

    CircleImageView mechanic_selected_img;
    TextView mechanic_selected_name, mechanic_selected_email, mechanic_selected_contact,
            mechanic_selected_cnic, mechanic_selected_price;
    EditText optional;
    Button btn_send_mechanic_request, btn_view_mechanic_skills;
    int getMID;
    String mName, mEmail, mContact, mCnic, mProfile;
    Booking booking;
    TinyDB tinyDB;
    ProgressDialog progressDialog;
    Dialog dialog;
    int getBookingfee;
    ListView show_skills_LV;
    List<Service> serviceList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_mechanic_further_details);
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.colorAccent));
        mechanic_selected_img = findViewById(R.id.mechanic_selected_img);
        mechanic_selected_name = findViewById(R.id.mechanic_selected_name);
        mechanic_selected_email = findViewById(R.id.mechanic_selected_email);
        mechanic_selected_contact = findViewById(R.id.mechanic_selected_contact);
        mechanic_selected_cnic = findViewById(R.id.mechanic_selected_cnic);
        mechanic_selected_price = findViewById(R.id.mechanic_selected_price);
        btn_view_mechanic_skills = findViewById(R.id.btn_view_mechanic_skills);
        dialog = new Dialog(this, android.R.style.Theme_Material_Light_NoActionBar_Fullscreen);
        optional = findViewById(R.id.optional);
        tinyDB = new TinyDB(this);
        mName = getIntent().getStringExtra("MECHANIC_NAME");
        mEmail = getIntent().getStringExtra("MECHANIC_EMAIL");
        mContact = getIntent().getStringExtra("MECHANIC_CONTACT");
        mCnic = getIntent().getStringExtra("MECHANIC_CNIC");
        mProfile = getIntent().getStringExtra("MECHANIC_IMG");
        getMID = getIntent().getIntExtra("MECHANIC_ID", 0);
        if (getIntent().getIntExtra("MECHANIC_PRICE", 0) == 0) {
            mechanic_selected_price.setText("Rs. 150");
            getBookingfee = 150;
        } else {
            mechanic_selected_price.setText("Rs. " + getIntent().getIntExtra("MECHANIC_PRICE", 0));
            getBookingfee = getIntent().getIntExtra("MECHANIC_PRICE", 0);
        }
        btn_send_mechanic_request = findViewById(R.id.btn_send_mechanic_request);
        Glide.with(SelectedMechanicFurtherDetails.this)
                .load(EndPoint.IMAGE_URL + mProfile).into(mechanic_selected_img);
        mechanic_selected_name.setText(mName);
        mechanic_selected_email.setText(mEmail);
        mechanic_selected_contact.setText(mContact);
        mechanic_selected_cnic.setText(mCnic);
        btn_send_mechanic_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BookingDetail();
            }
        });

        btn_view_mechanic_skills.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowDialog();
            }
        });

    }

    public void BookingDetail() {

        booking = new Booking();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Booking Details");
        progressDialog.setMessage("please wait...");
        progressDialog.show();

        BookingDetailsService service = RetrofitClient.getClient().create(BookingDetailsService.class);
        Call<Booking> call = service.booking_details(getMID, tinyDB.getInt("CUSTOMERID"),
                "M", getBookingfee, optional.getText().toString());
        call.enqueue(new Callback<Booking>() {
            @Override
            public void onResponse(Call<Booking> call, Response<Booking> response) {
                if (response.isSuccessful()) {
                    progressDialog.dismiss();
                    booking = response.body();
                    if (!booking.isError()) {
                        Toast.makeText(SelectedMechanicFurtherDetails.this,
                                booking.getMessage(), Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), UserDrawerActivity.class));
                        finish();
                    } else {
                        Toast.makeText(SelectedMechanicFurtherDetails.this,
                                booking.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(SelectedMechanicFurtherDetails.this,
                            response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Booking> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(SelectedMechanicFurtherDetails.this,
                        t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getMechanicServices() {
        serviceList.clear();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("please wait...");
        progressDialog.show();

        GetServiceRecordAPI service = RetrofitClient.getClient().create(GetServiceRecordAPI.class);

        Call<JsonObject> call = service.getServices(getMID, "M");
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

                                serviceList.add(new Service(
                                        data.getInt("service_id"),
                                        data.getString("service_name"),
                                        data.getInt("service_price"),
                                        data.getInt("service_discount"),
                                        data.getString("service_status")
                                ));
                            }

                            GetServiceAdapter adapter = new GetServiceAdapter(serviceList,
                                    SelectedMechanicFurtherDetails.this, 2);
                            show_skills_LV.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    } else {
                        Toast.makeText(SelectedMechanicFurtherDetails.this,
                                response.message(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(SelectedMechanicFurtherDetails.this,
                            response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(SelectedMechanicFurtherDetails.this,
                        t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void ShowDialog() {
        dialog.setContentView(R.layout.skills_dialog);
        ImageView img_close_drawer;
        show_skills_LV = dialog.findViewById(R.id.show_skills_LV);
        img_close_drawer = dialog.findViewById(R.id.img_close_drawer);
        dialog.show();
        getMechanicServices();
        img_close_drawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

    }

}
