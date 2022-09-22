package com.example.allinoneapplication.admin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.allinoneapplication.R;
import com.example.allinoneapplication.constant.EndPoint;
import com.example.allinoneapplication.model.Complaint;
import com.example.allinoneapplication.model.Mechanic;
import com.example.allinoneapplication.retrofit.RetrofitClient;
import com.example.allinoneapplication.service.DeleteComplainsService;
import com.example.allinoneapplication.service.UpdateMechanicStatusService;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectedMechanicDetails extends AppCompatActivity {
    CircleImageView selected_mechanic_img;
    TextView selected_mechanic_name, selected_mechanic_email, selected_mechanic_contact, selected_mechanic_address,
            selected_mechanic_status, selected_mechanic_createdDate, selected_mechanic_cnic, selected_mechanic_vtype;
    int mID;
    Mechanic mechanic;
    Complaint complaint;
    ProgressDialog progressDialog;
    Button btn_approved_mechanic, btn_block_mechanic;
    String mName, mEmail, mContact, mAddress, mProfile, mStatus, mDateTime, mCnic, mVtype;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_mechanic_details);
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.colorAccent));
        selected_mechanic_img = findViewById(R.id.selected_mechanic_img);
        btn_approved_mechanic = findViewById(R.id.btn_approved_mechanic);
        btn_block_mechanic = findViewById(R.id.btn_block_mechanic);
        selected_mechanic_name = findViewById(R.id.selected_mechanic_name);
        selected_mechanic_email = findViewById(R.id.selected_mechanic_email);
        selected_mechanic_contact = findViewById(R.id.selected_mechanic_contact);
        selected_mechanic_address = findViewById(R.id.selected_mechanic_address);
        selected_mechanic_status = findViewById(R.id.selected_mechanic_status);
        selected_mechanic_createdDate = findViewById(R.id.selected_mechanic_createdDate);
        selected_mechanic_cnic = findViewById(R.id.selected_mechanic_cnic);
        selected_mechanic_vtype = findViewById(R.id.selected_mechanic_vtype);
        mID = getIntent().getIntExtra("MECHANIC_ID", 0);
        mName = getIntent().getStringExtra("MECHANIC_NAME");
        mEmail = getIntent().getStringExtra("MECHANIC_EMAIL");
        mContact = getIntent().getStringExtra("MECHANIC_CONTACT");
        mAddress = getIntent().getStringExtra("MECHANIC_ADDRESS");
        mProfile = getIntent().getStringExtra("MECHANIC_PROFILE");
        mStatus = getIntent().getStringExtra("MECHANIC_STATUS");
        mDateTime = getIntent().getStringExtra("MECHANIC_DATETIME");
        mCnic = getIntent().getStringExtra("MECHANIC_CNIC");
        mVtype = getIntent().getStringExtra("MECHANIC_VEHICLE_TYPE");
        Glide.with(SelectedMechanicDetails.this)
                .load(EndPoint.IMAGE_URL + mProfile).into(selected_mechanic_img);
        selected_mechanic_name.setText(mName);
        selected_mechanic_email.setText(mEmail);
        selected_mechanic_contact.setText(mContact);
        selected_mechanic_address.setText(mAddress);
        if (mStatus.equals("A")) {
            selected_mechanic_status.setText("Approved");
            btn_approved_mechanic.setVisibility(View.GONE);
            btn_block_mechanic.setVisibility(View.VISIBLE);
        } else if (mStatus.equals("P")) {
            selected_mechanic_status.setText("Pending");
            btn_approved_mechanic.setVisibility(View.VISIBLE);
            btn_block_mechanic.setVisibility(View.VISIBLE);
        } else {
            selected_mechanic_status.setText("Blocked");
            btn_approved_mechanic.setVisibility(View.VISIBLE);
            btn_block_mechanic.setVisibility(View.GONE);
        }
        selected_mechanic_createdDate.setText(mDateTime);
        selected_mechanic_cnic.setText(mCnic);
        selected_mechanic_vtype.setText(mVtype);

        btn_approved_mechanic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateStatus(mID, "A");
                DeleteComplain(mID);
            }
        });

        btn_block_mechanic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateStatus(mID, "B");
            }
        });

    }

    private void DeleteComplain(int mID) {
        complaint = new Complaint();
        DeleteComplainsService service = RetrofitClient.getClient().create(DeleteComplainsService.class);
        Call<Complaint> call = service.deleteComp(mID, "m");
        call.enqueue(new Callback<Complaint>() {
            @Override
            public void onResponse(Call<Complaint> call, Response<Complaint> response) {
                if (response.isSuccessful()) {
                    complaint = new Complaint();
                    if (!complaint.isError()) {
                        Log.e("COMPALIN", "onResponse: " + complaint.getMessage());
                    } else {
                        Log.e("COMPALIN", "onResponse: " + complaint.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<Complaint> call, Throwable t) {
                Toast.makeText(SelectedMechanicDetails.this,
                        t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void UpdateStatus(int MID, String Status) {
        mechanic = new Mechanic();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("please wait...");
        progressDialog.show();

        UpdateMechanicStatusService service = RetrofitClient.getClient().create(UpdateMechanicStatusService.class);
        Call<Mechanic> call = service.updateStatus(MID, Status);
        call.enqueue(new Callback<Mechanic>() {
            @Override
            public void onResponse(Call<Mechanic> call, Response<Mechanic> response) {
                if (response.isSuccessful()) {
                    progressDialog.dismiss();
                    mechanic = response.body();
                    if (!mechanic.isError()) {
                        Toast.makeText(getApplicationContext(),
                                mechanic.getMessage(), Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(),
                                ManageMechanicActivity.class));
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(),
                                mechanic.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onFailure(Call<Mechanic> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(),
                        t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}