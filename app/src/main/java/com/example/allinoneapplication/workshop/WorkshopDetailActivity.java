package com.example.allinoneapplication.workshop;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.allinoneapplication.R;
import com.example.allinoneapplication.admin.ManageMechanicActivity;
import com.example.allinoneapplication.admin.ManageWorkshopActivity;
import com.example.allinoneapplication.constant.EndPoint;
import com.example.allinoneapplication.model.Admin;
import com.example.allinoneapplication.model.Mechanic;
import com.example.allinoneapplication.model.Workshop;
import com.example.allinoneapplication.retrofit.RetrofitClient;
import com.example.allinoneapplication.service.UpdateMechanicStatusService;
import com.example.allinoneapplication.service.UpdateWorkshopMechanicstatusService;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WorkshopDetailActivity extends AppCompatActivity {

    int wID;
    String wName, wEmail, wAddress, wProfile, wContact, wStatus, wDateTime, wCnic, wVtype;
    CircleImageView selected_workshop_img;
    TextView selected_workshop_name, selected_workshop_email, selected_workshop_contact, selected_workshop_address,
            selected_workshop_status, selected_workshop_createdDate, selected_workshop_cnic, selected_workshop_vtype;
    Button btn_approved_wmechanic, btn_block_wmechanic;
    Workshop workshop;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workshop_detail);
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.colorAccent));
        selected_workshop_img = findViewById(R.id.selected_workshop_img);
        selected_workshop_name = findViewById(R.id.selected_workshop_name);
        selected_workshop_email = findViewById(R.id.selected_workshop_email);
        selected_workshop_contact = findViewById(R.id.selected_workshop_contact);
        selected_workshop_address = findViewById(R.id.selected_workshop_address);
        selected_workshop_status = findViewById(R.id.selected_workshop_status);
        selected_workshop_createdDate = findViewById(R.id.selected_workshop_createdDate);
        selected_workshop_cnic = findViewById(R.id.selected_workshop_cnic);
        selected_workshop_vtype = findViewById(R.id.selected_workshop_vtype);
        btn_approved_wmechanic = findViewById(R.id.btn_approved_wmechanic);
        btn_block_wmechanic = findViewById(R.id.btn_blocked_wmechanic);
        wID = getIntent().getIntExtra("WORKSHOP_ID", 0);
        wName = getIntent().getStringExtra("WORKSHOP_NAME");
        wEmail = getIntent().getStringExtra("WORKSHOP_EMAIL");
        wAddress = getIntent().getStringExtra("WORKSHOP_ADDRESS");
        wProfile = getIntent().getStringExtra("WORKSHOP_PROFILE");
        wContact = getIntent().getStringExtra("WORKSHOP_CONTACT");
        wStatus = getIntent().getStringExtra("WORKSHOP_STATUS");
        wDateTime = getIntent().getStringExtra("WORKSHOP_DATETIME");
        wCnic = getIntent().getStringExtra("WORKSHOP_CNIC");
        wVtype = getIntent().getStringExtra("WORKSHOP_VTYPE");
        selected_workshop_name.setText(wName);
        selected_workshop_email.setText(wEmail);
        selected_workshop_contact.setText(wContact);
        selected_workshop_address.setText(wAddress);
        Glide.with(WorkshopDetailActivity.this)
                .load(EndPoint.IMAGE_URL + wProfile).into(selected_workshop_img);
        if (wStatus.equals("A")) {
            selected_workshop_status.setText("Approved");
            btn_approved_wmechanic.setVisibility(View.GONE);
            btn_block_wmechanic.setVisibility(View.VISIBLE);
        } else if (wStatus.equals("B")) {
            selected_workshop_status.setText("Blocked");
            btn_approved_wmechanic.setVisibility(View.VISIBLE);
            btn_block_wmechanic.setVisibility(View.GONE);
        } else if (wStatus.equals("P")) {
            selected_workshop_status.setText("Pending");
            btn_approved_wmechanic.setVisibility(View.VISIBLE);
            btn_block_wmechanic.setVisibility(View.VISIBLE);
        }
        selected_workshop_createdDate.setText(wDateTime);
        selected_workshop_cnic.setText(wCnic);
        selected_workshop_vtype.setText(wVtype);
        btn_approved_wmechanic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateWMStatus(wID,"A");
            }
        });

        btn_block_wmechanic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateWMStatus(wID,"B");
            }
        });

    }
    private void UpdateWMStatus(int WID, String Status){
        workshop=new Workshop();
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("please wait...");
        progressDialog.show();

        UpdateWorkshopMechanicstatusService service= RetrofitClient.getClient().create(UpdateWorkshopMechanicstatusService.class);
        Call<Workshop> call=service.updateStatus(WID,Status);
        call.enqueue(new Callback<Workshop>() {
            @Override
            public void onResponse(Call<Workshop> call, Response<Workshop> response) {
                if(response.isSuccessful()){
                    progressDialog.dismiss();
                    workshop=response.body();
                    if(!workshop.isError()){
                        Toast.makeText(getApplicationContext(),
                                workshop.getMessage(), Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(),
                                ManageWorkshopActivity.class));
                        finish();
                    }else{
                        Toast.makeText(getApplicationContext(),
                                workshop.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onFailure(Call<Workshop> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(),
                        t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}