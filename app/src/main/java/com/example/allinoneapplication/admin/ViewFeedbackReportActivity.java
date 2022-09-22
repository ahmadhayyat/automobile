package com.example.allinoneapplication.admin;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.allinoneapplication.R;
import com.example.allinoneapplication.model.RateFeedback;
import com.example.allinoneapplication.retrofit.RetrofitClient;
import com.example.allinoneapplication.service.GetFeedbackService;
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

public class ViewFeedbackReportActivity extends AppCompatActivity {

    List<RateFeedback> feedbackList = new ArrayList<>();
    PieChart feedbackpie_chart;
    int positive_feedback = 0;
    int negative_feedback = 0;
    ProgressDialog progressDialog;
    TextView tv_positive_feedback, tv_feedback_neg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_feedback_report);
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.colorAccent));
        feedbackpie_chart = findViewById(R.id.feedbackpie_chart);
        tv_feedback_neg = findViewById(R.id.tv_feedback_neg);
        tv_positive_feedback = findViewById(R.id.tv_positive_feedback);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("please wait...");
        progressDialog.show();
        getRateFeedback();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                addToPiechart();
            }
        }, 2000);

    }

    public void getRateFeedback() {

        GetFeedbackService service = RetrofitClient.getClient().create(GetFeedbackService.class);

        Call<JsonObject> call = service.getFeedback();
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

                                feedbackList.add(new RateFeedback(
                                        data.getString("rf_title"),
                                        data.getDouble("rf_star")
                                ));

                                if (data.getDouble("rf_star") > 2.5) {
                                    positive_feedback++;
                                } else {
                                    negative_feedback++;
                                }
                            }

                        } catch (JSONException e) {
                            Toast.makeText(ViewFeedbackReportActivity.this,
                                    response.message(), Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(ViewFeedbackReportActivity.this,
                                response.message(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ViewFeedbackReportActivity.this,
                            response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(ViewFeedbackReportActivity.this,
                        t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        positive_feedback = 0;
        negative_feedback = 0;
    }


    private void addToPiechart() {
        progressDialog.dismiss();
        feedbackpie_chart.addPieSlice(new PieModel("Positive", positive_feedback, Color.parseColor("#00FF0A")));
        feedbackpie_chart.addPieSlice(new PieModel("Negative", negative_feedback, Color.parseColor("#FF1100")));
        feedbackpie_chart.startAnimation();
        tv_positive_feedback.setText("Positive Feedback (" + positive_feedback + ")");
        tv_feedback_neg.setText("Negative Feedback (" + negative_feedback + ")");
    }
}