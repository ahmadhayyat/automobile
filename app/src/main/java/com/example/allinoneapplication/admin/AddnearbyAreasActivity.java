package com.example.allinoneapplication.admin;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.allinoneapplication.R;
import com.example.allinoneapplication.adapter.NearbyAdapter;
import com.example.allinoneapplication.model.NearBy;
import com.example.allinoneapplication.retrofit.RetrofitClient;
import com.example.allinoneapplication.service.GetNearbyAreaService;
import com.example.allinoneapplication.service.NearByService;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddnearbyAreasActivity extends AppCompatActivity {

    Button dialog_nearby_area;
    NearBy nearBy;
    ProgressDialog progressDialog;
    List<NearBy> nearByList = new ArrayList<>();
    NearbyAdapter adapter;
    ListView nearbyarea_LV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addnearby_areas);
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.colorAccent));
        dialog_nearby_area = findViewById(R.id.dialog_nearby_area);
        nearbyarea_LV = findViewById(R.id.nearbyarea_LV);
        GetNearbyArea();
        dialog_nearby_area.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseNearby();
            }
        });
    }

    private void chooseNearby() {
        AlertDialog.Builder builder = new AlertDialog.Builder(AddnearbyAreasActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.nearbydialog, null);
        builder.setCancelable(false);
        builder.setView(dialogView);

        EditText text = dialogView.findViewById(R.id.ed_text);
        Button button = dialogView.findViewById(R.id.btn_add_nearby);
        final AlertDialog alertDialognearby = builder.create();
        alertDialognearby.show();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!text.getText().toString().isEmpty()) {
                    AddNearbyPlace(text.getText().toString());
                    alertDialognearby.dismiss();
                }
            }
        });
    }

    public void AddNearbyPlace(String title) {
        nearBy = new NearBy();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("please wait...");
        progressDialog.show();

        NearByService service = RetrofitClient.getClient().create(NearByService.class);
        Call<NearBy> call = service.nearBy(title);
        call.enqueue(new Callback<NearBy>() {
            @Override
            public void onResponse(Call<NearBy> call, Response<NearBy> response) {
                if (response.isSuccessful()) {
                    progressDialog.dismiss();
                    nearBy = response.body();
                    if (!nearBy.isError()) {
                        Toast.makeText(AddnearbyAreasActivity.this, nearBy.getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(AddnearbyAreasActivity.this, nearBy.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            }

            @Override
            public void onFailure(Call<NearBy> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(AddnearbyAreasActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void GetNearbyArea() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("please wait...");
        progressDialog.show();

        GetNearbyAreaService service = RetrofitClient.getClient().create(GetNearbyAreaService.class);

        Call<JsonObject> call = service.getNearbyArea();
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

                                nearByList.add(new NearBy(
                                        data.getInt("na_id"),
                                        data.getString("na_title"),
                                        data.getString("na_status")));

                            }
                            adapter = new NearbyAdapter(nearByList, AddnearbyAreasActivity.this);
                            nearbyarea_LV.setAdapter(adapter);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    } else {
                        Toast.makeText(AddnearbyAreasActivity.this,
                                response.message(), Toast.LENGTH_SHORT).show();
                    }

                } else {
                    progressDialog.dismiss();
                    Toast.makeText(AddnearbyAreasActivity.this,
                            response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(AddnearbyAreasActivity.this,
                        t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }


}