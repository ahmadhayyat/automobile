package com.example.allinoneapplication.complain;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.allinoneapplication.R;
import com.example.allinoneapplication.constant.TinyDB;
import com.example.allinoneapplication.model.Complaint;
import com.example.allinoneapplication.retrofit.RetrofitClient;
import com.example.allinoneapplication.service.RegisterComplainService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ComplainActivity extends AppCompatActivity {

    int id, getid, loginPref;
    String against_name;
    Complaint complaint;
    ProgressDialog progressDialog;
    TinyDB tinyDB;
    Button btn_submit_complaint;
    TextView tv_cmpagainst_name;
    EditText edt_comp_subject, edt_comp_message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complain);
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.colorAccent));
        tv_cmpagainst_name = findViewById(R.id.tv_cmpagainst_name);
        edt_comp_subject = findViewById(R.id.edt_comp_subject);
        edt_comp_message = findViewById(R.id.edt_comp_message);
        btn_submit_complaint = findViewById(R.id.btn_submit_complaint);
        tinyDB = new TinyDB(this);
        loginPref = tinyDB.getInt("LOGIN_PREF");
        if (loginPref == 1) {
            getid = tinyDB.getInt("CUSTOMERID");
            id = getIntent().getIntExtra("MECHANIC_ID", 0);
            against_name = getIntent().getStringExtra("MECHANIC_NAME");
        } else if (loginPref == 2) {
            getid = tinyDB.getInt("MECHANIC_ID");
            id = getIntent().getIntExtra("CUSTOMER_ID", 0);
            against_name = getIntent().getStringExtra("CUSTOMER_NAME");
        }
        tv_cmpagainst_name.setText(against_name);

        btn_submit_complaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (loginPref == 1) {
                    if (edt_comp_subject.getText().toString().isEmpty()) {
                        edt_comp_subject.setError("enter complaint title");
                    } else if (edt_comp_message.getText().toString().isEmpty()) {
                        edt_comp_message.setError("enter complaint message");
                    } else {
                        RegisterComplaint(getid, id, "c");
                        finish();
                    }
                } else if (loginPref == 2) {
                    if (edt_comp_subject.getText().toString().isEmpty()) {
                        edt_comp_subject.setError("enter complaint title");
                    } else if (edt_comp_message.getText().toString().isEmpty()) {
                        edt_comp_message.setError("enter complaint message");
                    } else {
                        RegisterComplaint(id, getid, "m");
                        finish();
                    }
                }
            }
        });

    }

    private void RegisterComplaint(int cID, int mID, String _for) {
        complaint = new Complaint();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("please wait...");
        progressDialog.show();

        RegisterComplainService service = RetrofitClient.getClient().create(RegisterComplainService.class);
        Call<Complaint> call = service.registerComp(
                edt_comp_subject.getText().toString(),
                edt_comp_message.getText().toString(),
                cID,
                mID,
                _for
        );
        call.enqueue(new Callback<Complaint>() {
            @Override
            public void onResponse(Call<Complaint> call, Response<Complaint> response) {
                if (response.isSuccessful()) {
                    progressDialog.dismiss();
                    complaint = response.body();
                    if (!complaint.isError()) {
                        Toast.makeText(getApplicationContext(),
                                complaint.getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(),
                                complaint.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Complaint> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(),
                        t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}