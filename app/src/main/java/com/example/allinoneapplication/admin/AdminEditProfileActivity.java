package com.example.allinoneapplication.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.allinoneapplication.R;
import com.example.allinoneapplication.constant.TinyDB;
import com.example.allinoneapplication.mechanic.MechanicProfileActivity;
import com.example.allinoneapplication.model.Admin;
import com.example.allinoneapplication.model.Mechanic;
import com.example.allinoneapplication.retrofit.RetrofitClient;
import com.example.allinoneapplication.service.UpdateAdminProfileService;
import com.example.allinoneapplication.service.UpdateMechanicProfileService;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminEditProfileActivity extends AppCompatActivity {
    Button  btn_asubmit;
    TinyDB tinyDB;
    EditText edt_aemail, edt_apassword;
    ProgressDialog progressDialog;
   Admin admin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_edit_profile);
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.colorAccent));
        tinyDB = new TinyDB(this);
        btn_asubmit = findViewById(R.id.btn_asubmit);
        edt_aemail = findViewById(R.id.edt_aemail);
        edt_apassword = findViewById(R.id.edt_apassword);
        edt_aemail.setText(tinyDB.getString("ADMIN_EMAIL"));
        edt_apassword.setText(tinyDB.getString("ADMIN_PASSWORD"));
        btn_asubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Updateprofile();
            }
        });
    }
    private void Updateprofile() {
        admin = new Admin();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("please wait...");
        progressDialog.show();

        UpdateAdminProfileService service = RetrofitClient.getClient().create(UpdateAdminProfileService.class);
        Call<Admin> call = service.update_admin_profile(tinyDB.getInt("ADMIN_ID"),
                edt_aemail.getText().toString(),
                edt_apassword.getText().toString());
        call.enqueue(new Callback<Admin>() {
            @Override
            public void onResponse(Call<Admin> call, Response<Admin> response) {
                if (response.isSuccessful()) {
                    progressDialog.dismiss();
                    admin = response.body();
                    if (!admin.isError()) {
                        Toast.makeText(AdminEditProfileActivity.this,
                                admin.getMessage(), Toast.LENGTH_SHORT).show();
                        tinyDB.putString("ADMIN_EMAIL", edt_aemail.getText().toString());
                        tinyDB.putString("ADMIN_PASSWORD", edt_apassword.getText().toString());
                    } else {
                        Toast.makeText(AdminEditProfileActivity.this,
                                admin.getMessage(), Toast.LENGTH_SHORT).show();

                    }

                }
            }

            @Override
            public void onFailure(Call<Admin> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(AdminEditProfileActivity.this,
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