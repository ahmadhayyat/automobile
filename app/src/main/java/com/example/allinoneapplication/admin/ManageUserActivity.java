package com.example.allinoneapplication.admin;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.allinoneapplication.R;
import com.example.allinoneapplication.adapter.ManageUserDetailsAdapter;
import com.example.allinoneapplication.model.Customer;
import com.example.allinoneapplication.retrofit.RetrofitClient;
import com.example.allinoneapplication.service.GetManageUserdetailService;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManageUserActivity extends AppCompatActivity {
    Spinner check_cstatus_spinner;
    String status[] = {"Approved", "Block"};
    ListView manage_user_LV;
    ProgressDialog progressDialog;
    List<Customer> customerList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_user);
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.colorAccent));
        manage_user_LV = findViewById(R.id.manage_user_LV);
        check_cstatus_spinner = findViewById(R.id.check_cstatus_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, status);
        check_cstatus_spinner.setAdapter(adapter);
        check_cstatus_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    customerList.clear();
                    GetManageUserdetails("A");
                } else if (i == 1) {
                    customerList.clear();
                    GetManageUserdetails("B");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        GetManageUserdetails("A");
    }

    private void GetManageUserdetails(String status) {
        customerList.clear();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("please wait...");
        progressDialog.show();

        GetManageUserdetailService service = RetrofitClient.getClient().create(GetManageUserdetailService.class);

        Call<JsonObject> call = service.getManageUserdetails();
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
                                if (data.getString("customer_status").equals(status)) {
                                    customerList.add(new Customer(
                                            data.getInt("customer_id"),
                                            data.getString("customer_name"),
                                            data.getString("customer_email"),
                                            data.getString("customer_contact"),
                                            data.getDouble("customer_lat"),
                                            data.getDouble("customer_lng"),
                                            data.getString("customer_address"),
                                            data.getString("customer_profile_img"),
                                            data.getString("customer_status"),
                                            data.getString("created_datetime")
                                    ));
                                }
                            }
                            ManageUserDetailsAdapter adapter = new ManageUserDetailsAdapter(customerList,
                                    ManageUserActivity.this);
                            manage_user_LV.setAdapter(adapter);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    } else {
                        Toast.makeText(ManageUserActivity.this,
                                response.message(), Toast.LENGTH_SHORT).show();
                    }

                } else {
                    progressDialog.dismiss();
                    Toast.makeText(ManageUserActivity.this,
                            response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(ManageUserActivity.this,
                        t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }
}