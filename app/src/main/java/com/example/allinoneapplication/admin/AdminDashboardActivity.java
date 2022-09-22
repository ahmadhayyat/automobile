package com.example.allinoneapplication.admin;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.allinoneapplication.R;
import com.example.allinoneapplication.constant.TinyDB;
import com.example.allinoneapplication.model.Mechanic;
import com.example.allinoneapplication.model.Workshop;
import com.example.allinoneapplication.retrofit.RetrofitClient;
import com.example.allinoneapplication.service.GetManageMechanicDetailsServices;
import com.example.allinoneapplication.service.GetWorkshopDetailsService;
import com.example.allinoneapplication.ui.LoginActivity;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminDashboardActivity extends AppCompatActivity {

    CardView manage_complain_CV, manage_workshop_CV,
            manage_user_CV, manage_mechanic_CV, manage_profile_CV, manage_report_CV, logout_CV;
    TinyDB tinyDB;
    List<Mechanic> mechanicList = new ArrayList<>();
    List<Workshop> workshopList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.colorAccent));
        tinyDB = new TinyDB(this);
        manage_complain_CV = findViewById(R.id.manage_complain_CV);
        manage_user_CV = findViewById(R.id.manage_user_CV);
        manage_workshop_CV = findViewById(R.id.manage_workshop_CV);
        manage_profile_CV = findViewById(R.id.manage_profile_CV);
        logout_CV = findViewById(R.id.logout_CV);
        manage_report_CV = findViewById(R.id.manage_report_CV);
        manage_mechanic_CV = findViewById(R.id.manage_mechanic_CV);
        manage_profile_CV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminDashboardActivity.this, AdminEditProfileActivity.class));
            }
        });
        manage_mechanic_CV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminDashboardActivity.this, ManageMechanicActivity.class));
            }
        });
        manage_user_CV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminDashboardActivity.this, ManageUserActivity.class));
            }
        });
        manage_complain_CV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminDashboardActivity.this, AdminAllComplainActivity.class));
                finish();
            }
        });
        manage_workshop_CV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminDashboardActivity.this, ManageWorkshopActivity.class));

            }
        });

        logout_CV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tinyDB.clear();
                Toast.makeText(getApplicationContext(),
                        "Logout Successfully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
            }
        });

        GetManageMechanicdetails("P");


        manage_report_CV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ReportsActivity.class));
            }
        });

    }

    private void GetManageMechanicdetails(String status) {
        mechanicList.clear();

        GetManageMechanicDetailsServices service = RetrofitClient.getClient().create(GetManageMechanicDetailsServices.class);

        Call<JsonObject> call = service.getManageMechanicdetails();
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    if (response.code() == 200) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.body().getAsJsonObject().toString());

                            JSONArray jsonArray = jsonObject.getJSONArray("records");

                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject data = jsonArray.getJSONObject(i);
                                if (data.getString("mechanic_status").equals(status)) {
                                    mechanicList.add(new Mechanic(
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
                            }
                            ViewDialog();


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    } else {
                        Toast.makeText(getApplicationContext(),
                                response.message(), Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(getApplicationContext(),
                            response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(getApplicationContext(),
                        t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void GetManageWorkshopdetails(String status) {
        workshopList.clear();

        GetWorkshopDetailsService service = RetrofitClient.getClient().create(GetWorkshopDetailsService.class);

        Call<JsonObject> call = service.getManageWorkshopdetails();
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    if (response.code() == 200) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.body().getAsJsonObject().toString());

                            JSONArray jsonArray = jsonObject.getJSONArray("records");

                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject data = jsonArray.getJSONObject(i);
                                if (data.getString("wmechanic_status").equals(status)) {
                                    workshopList.add(new Workshop(
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
                            }

                            ViewDialogWorkshop();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    } else {
                        Toast.makeText(getApplicationContext(),
                                response.message(), Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(getApplicationContext(),
                            response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(getApplicationContext(),
                        t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void ViewDialog() {
        for (int i = 0; i < mechanicList.size(); i++) {
            ShowDialog(mechanicList.get(i).getMechanic_name());
        }
    }

    private void ShowDialog(String name) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.pending_request_dialog);
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                dialog.dismiss();
                dialogInterface.dismiss();
                GetManageWorkshopdetails("P");
            }
        });
        dialog.show();
        TextView tv_pending_req = dialog.findViewById(R.id.tv_pending_req);

        tv_pending_req.setText("New Booking Request From Mechanic Arrived named " + name);

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                dialog.dismiss();
//            }
//        }, 5500);

    }

    private void ViewDialogWorkshop() {
        for (int i = 0; i < workshopList.size(); i++) {
            ShowDialogWorkshop(workshopList.get(i).getWmechanic_name());
        }
    }

    private void ShowDialogWorkshop(String name) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.pending_request_dialog);
        dialog.show();
        TextView tv_pending_req = dialog.findViewById(R.id.tv_pending_req);

        tv_pending_req.setText("New Booking Request From Workshop Arrived named " + name);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
            }
        }, 5500);

    }
}