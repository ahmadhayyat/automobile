package com.example.allinoneapplication.workshop;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.allinoneapplication.R;
import com.example.allinoneapplication.constant.EndPoint;
import com.example.allinoneapplication.customer.SelectedMechanicFurtherDetails;
import com.example.allinoneapplication.model.Mechanic;
import com.example.allinoneapplication.retrofit.RetrofitClient;
import com.example.allinoneapplication.service.DeleteWorkshopmechanicService;
import com.example.allinoneapplication.ui.LoginActivity;
import com.example.allinoneapplication.ui.SignUpActivity;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectedWorkshopMechanicFurtherDetails extends AppCompatActivity {
    CircleImageView swmechanic_selected_img;
    TextView swmechanic_selected_name, swmechanic_selected_email, swmechanic_selected_contact,
            swmechanic_selected_cnic;
    Button  btn_delete;
    int getMID;
    String mName, mEmail, mContact, mCnic, mProfile;
    ProgressDialog progressDialog;
    Mechanic mechanic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_workshop_mechanic_further_details);
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.colorAccent));
        btn_delete=findViewById(R.id.btn_delete);
        swmechanic_selected_img = findViewById(R.id.swmechanic_selected_img);
        swmechanic_selected_name = findViewById(R.id.swmechanic_selected_name);
        swmechanic_selected_email = findViewById(R.id.swmechanic_selected_email);
        swmechanic_selected_contact = findViewById(R.id.swmechanic_selected_contact);
        swmechanic_selected_cnic = findViewById(R.id.swmechanic_selected_cnic);
        mName = getIntent().getStringExtra("MECHANIC_NAME");
        mEmail = getIntent().getStringExtra("MECHANIC_EMAIL");
        mContact = getIntent().getStringExtra("MECHANIC_CONTACT");
        mCnic = getIntent().getStringExtra("MECHANIC_CNIC");
        mProfile = getIntent().getStringExtra("MECHANIC_IMG");
        getMID = getIntent().getIntExtra("MECHANIC_ID", 0);
        Glide.with(SelectedWorkshopMechanicFurtherDetails.this)
                .load(EndPoint.IMAGE_URL + mProfile).into(swmechanic_selected_img);
        swmechanic_selected_name.setText(mName);
        swmechanic_selected_email.setText(mEmail);
        swmechanic_selected_contact.setText(mContact);
        swmechanic_selected_cnic.setText(mCnic);
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Deleteprofile();
            }
        });
    }
    private void Deleteprofile() {
        mechanic = new Mechanic();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("please wait...");
        progressDialog.show();

        DeleteWorkshopmechanicService service = RetrofitClient.getClient().create(DeleteWorkshopmechanicService.class);
        Call<Mechanic> call = service.deleteMechanic(getIntent().getIntExtra("MECHANIC_ID",0));
        call.enqueue(new Callback<Mechanic>() {
            @Override
            public void onResponse(Call<Mechanic> call, Response<Mechanic> response) {
                if (response.isSuccessful()) {
                    progressDialog.dismiss();
                    mechanic = response.body();
                    if (!mechanic.isError()) {
                        Toast.makeText(SelectedWorkshopMechanicFurtherDetails.this,
                                mechanic.getMessage(), Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(SelectedWorkshopMechanicFurtherDetails.this,
                                mechanic.getMessage(), Toast.LENGTH_SHORT).show();

                    }

                }
            }

            @Override
            public void onFailure(Call<Mechanic> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(SelectedWorkshopMechanicFurtherDetails.this,
                        t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}