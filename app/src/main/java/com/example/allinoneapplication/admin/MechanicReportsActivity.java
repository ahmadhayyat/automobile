package com.example.allinoneapplication.admin;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.allinoneapplication.R;
import com.example.allinoneapplication.retrofit.RetrofitClient;
import com.example.allinoneapplication.service.GetManageMechanicDetailsServices;
import com.example.allinoneapplication.service.GetTopMechanicService;
import com.google.gson.JsonObject;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MechanicReportsActivity extends AppCompatActivity {

    PieChart pie_chart;
    int topRated = 0;
    int cheapest = 0;
    ProgressDialog progressDialog;
    TextView tv_top_rated, tv_cheapest_mechanic;

    List<Integer> mechanicList = new ArrayList<>();
    List<Integer> getmechanicList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mechanic_reports);
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.colorAccent));
        pie_chart = findViewById(R.id.pie_chart);
        tv_top_rated = findViewById(R.id.tv_top_rated);
        tv_cheapest_mechanic = findViewById(R.id.tv_cheapest_mechanic);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("please wait....");
        progressDialog.show();
        GetManageMechanicdetails();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                addToPiechart();
            }
        }, 2000);

    }

    private void addToPiechart() {
        progressDialog.dismiss();
        pie_chart.addPieSlice(new PieModel("Top Rated", topRated, Color.parseColor("#FFFFEB3B")));
        pie_chart.addPieSlice(new PieModel("Cheapest", cheapest, Color.parseColor("#FFF44336")));
        pie_chart.startAnimation();
        tv_top_rated.setText("Top Rated Mechanics (" + topRated + ")");
        tv_cheapest_mechanic.setText("Cheapest Mechanics (" + cheapest + ")");
    }

    private void GetManageMechanicdetails() {
        getmechanicList.clear();
        if (mechanicList != null) {
            mechanicList.clear();
        }

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
                                if (data.getInt("mechanic_price") <= 200) {
                                    getmechanicList.add(data.getInt("mechanic_id"));
                                }
                            }
                            cheapest = getmechanicList.size();
                            getmechanicList.clear();
                            GetTopRatedManageMechanicdetails();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            GetTopRatedManageMechanicdetails();
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

    private void GetTopRatedManageMechanicdetails() {
        getmechanicList.clear();
        if (mechanicList != null) {
            mechanicList.clear();
        }

        GetTopMechanicService service = RetrofitClient.getClient().create(GetTopMechanicService.class);
        Call<JsonObject> call = service.getTopMechanic();
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
                                getmechanicList.add(
                                        data.getInt("mechanic_id"));

                            }

                            Log.e("INDEX", "onResponse: " + mechanicList.size());
                            topRated = getmechanicList.size();


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

    @Override
    protected void onPause() {
        super.onPause();
        topRated = 0;
        cheapest = 0;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        topRated = 0;
        cheapest = 0;

    }
}