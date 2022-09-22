package com.example.allinoneapplication.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.allinoneapplication.R;
import com.example.allinoneapplication.admin.AdminDashboardActivity;
import com.example.allinoneapplication.constant.TinyDB;
import com.example.allinoneapplication.customer.UserDrawerActivity;
import com.example.allinoneapplication.mechanic.MechanicDrawerActivity;
import com.example.allinoneapplication.model.Admin;
import com.example.allinoneapplication.model.Customer;
import com.example.allinoneapplication.model.Mechanic;
import com.example.allinoneapplication.model.Workshop;
import com.example.allinoneapplication.retrofit.RetrofitClient;
import com.example.allinoneapplication.service.AdminLoginService;
import com.example.allinoneapplication.service.CustomerLoginService;
import com.example.allinoneapplication.service.MechanicLoginService;
import com.example.allinoneapplication.service.WorkshopLoginService;
import com.example.allinoneapplication.workshop.WorkshopActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    EditText edt_login_email, edt_login_password;
    Button btn_login;
    TextView tv_signup;
    RadioButton radio_user, radio_mechanic, radio_admin, radio_workshop;
    Customer customer;
    Mechanic mechanic;
    TinyDB tinyDB;
    Workshop workshop;
    Admin admin;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.colorAccent));
        tinyDB = new TinyDB(this);
        edt_login_email = findViewById(R.id.edt_login_email);
        edt_login_password = findViewById(R.id.edt_login_password);
        tv_signup = findViewById(R.id.tv_signup);
        btn_login = findViewById(R.id.btn_login);
        radio_user = findViewById(R.id.radio_user);
        radio_mechanic = findViewById(R.id.radio_mechanic);
        radio_admin = findViewById(R.id.radio_admin);
        radio_workshop = findViewById(R.id.radio_workshop);
        radio_user.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    radio_admin.setChecked(false);
                    radio_workshop.setChecked(false);
                }
            }
        });
        radio_admin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    radio_mechanic.setChecked(false);
                    radio_user.setChecked(false);

                }
            }
        });
        radio_mechanic.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    radio_admin.setChecked(false);
                    radio_workshop.setChecked(false);

                }
            }
        });
        radio_workshop.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    radio_user.setChecked(false);
                    radio_mechanic.setChecked(false);

                }
            }
        });
        tv_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Validation()) {
                    if (radio_mechanic.isChecked() || radio_mechanic.isSelected()) {
                        MechanicLogin();
                    } else if (radio_admin.isChecked() || radio_admin.isSelected()) {
                        AdminLogin();
                    } else if (radio_user.isChecked() || radio_user.isSelected()) {
                        CustomerLogin();
                    } else if (radio_workshop.isChecked() || radio_workshop.isSelected()) {
                        WorkshopLogin();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public boolean Validation() {
        if (edt_login_email.getText().toString().isEmpty()) {
            edt_login_email.setError("Email field is empty");
            return false;
        } else if (edt_login_password.getText().toString().isEmpty()) {
            edt_login_password.setError("Password is not Entered");
            return false;
        }

        return true;
    }

    public void CustomerLogin() {
        customer = new Customer();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Login");
        progressDialog.setMessage("please wait...");
        progressDialog.show();


        CustomerLoginService service = RetrofitClient.getClient().create(CustomerLoginService.class);
        Call<Customer> call = service.customer_login(edt_login_email.getText().toString(), edt_login_password.getText().toString());
        call.enqueue(new Callback<Customer>() {
            @Override
            public void onResponse(Call<Customer> call, Response<Customer> response) {
                if (response.isSuccessful()) {
                    progressDialog.dismiss();
                    customer = response.body();
                    if (!customer.isError()) {
                        if (customer.getCustomer_status().equals("A")) {
                            Toast.makeText(LoginActivity.this, customer.getMessage(), Toast.LENGTH_SHORT).show();
                            tinyDB.putInt("LOGIN_PREF", 1);
                            tinyDB.putInt("CUSTOMERID", customer.getCustomer_id());
                            tinyDB.putString("CUSTOMER_NAME", customer.getCustomer_name());
                            tinyDB.putString("CUSTOMER_EMAIL", customer.getCustomer_email());
                            tinyDB.putString("CUSTOMER_CONTACT", customer.getCustomer_contact());
                            tinyDB.putString("CUSTOMER_PROFILE", customer.getCustomer_profile_img());
                            startActivity(new Intent(getApplicationContext(), UserDrawerActivity.class));
                            finish();
                        } else {
                            ShowAlertDialog();
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, customer.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            }

            @Override
            public void onFailure(Call<Customer> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }


    public void WorkshopLogin() {
        workshop = new Workshop();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Login");
        progressDialog.setMessage("please wait...");
        progressDialog.show();
        WorkshopLoginService service = RetrofitClient.getClient().create(WorkshopLoginService.class);
        Call<Workshop> call = service.workshop_login(edt_login_email.getText().toString(), edt_login_password.getText().toString());
        call.enqueue(new Callback<Workshop>() {
            @Override
            public void onResponse(Call<Workshop> call, Response<Workshop> response) {

                if (response.isSuccessful()) {
                    progressDialog.dismiss();
                    workshop = response.body();
                    if (!workshop.isError()) {
                        if (workshop.getWmechanic_status().equals("A")) {
                            Toast.makeText(LoginActivity.this, workshop.getMessage(), Toast.LENGTH_SHORT).show();
                            tinyDB.putInt("LOGIN_PREF", 3);
                            tinyDB.putInt("WMECHANIC_ID", workshop.getWmechanic_id());
                            tinyDB.putString("WMECHANIC_NAME", workshop.getWmechanic_name());
                            tinyDB.putString("WMECHANIC_EMAIL", workshop.getWmechanic_email());
                            tinyDB.putString("WMECHANIC_CONTACT", workshop.getWmechanic_contact());
                            tinyDB.putString("WMECHANIC_PROFILE", workshop.getWmechanic_profile_img());
                            tinyDB.putString("WMECHANIC_ADDRESS", workshop.getWmechanic_address());
                            tinyDB.putDouble("WMECHANIC_LAT", workshop.getWmechanic_lat());
                            tinyDB.putDouble("WMECHANIC_LNG", workshop.getWmechanic_lng());
                            startActivity(new Intent(getApplicationContext(), WorkshopActivity.class));
                            finish();
                        } else {
                            ShowAlertDialog();
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, workshop.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onFailure(Call<Workshop> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    public void MechanicLogin() {
        mechanic = new Mechanic();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Login");
        progressDialog.setMessage("please wait...");
        progressDialog.show();
        MechanicLoginService service = RetrofitClient.getClient().create(MechanicLoginService.class);
        Call<Mechanic> call = service.mechanic_login(edt_login_email.getText().toString(), edt_login_password.getText().toString());
        call.enqueue(new Callback<Mechanic>() {
            @Override
            public void onResponse(Call<Mechanic> call, Response<Mechanic> response) {
                if (response.isSuccessful()) {
                    progressDialog.dismiss();
                    mechanic = response.body();
                    if (!mechanic.isError()) {
                        if (mechanic.getMechanic_status().equals("A")) {
                            Toast.makeText(LoginActivity.this, mechanic.getMessage(), Toast.LENGTH_SHORT).show();
                            tinyDB.putInt("LOGIN_PREF", 2);
                            tinyDB.putInt("MECHANIC_ID", mechanic.getMechanic_id());
                            tinyDB.putString("MECHANIC_NAME", mechanic.getMechanic_name());
                            tinyDB.putString("MECHANIC_EMAIL", mechanic.getMechanic_email());
                            tinyDB.putString("MECHANIC_CONTACT", mechanic.getMechanic_contact());
                            tinyDB.putString("MECHANIC_PROFILE", mechanic.getMechanic_profile_img());
                            startActivity(new Intent(getApplicationContext(), MechanicDrawerActivity.class));
                            finish();
                        } else {
                            ShowAlertDialog();
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, mechanic.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Mechanic> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    public void AdminLogin() {
        admin = new Admin();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Login");
        progressDialog.setMessage("please wait...");
        progressDialog.show();
        AdminLoginService service = RetrofitClient.getClient().create(AdminLoginService.class);
        Call<Admin> call = service.admin_login(edt_login_email.getText().toString(), edt_login_password.getText().toString());
        call.enqueue(new Callback<Admin>() {
            @Override
            public void onResponse(Call<Admin> call, Response<Admin> response) {
                if (response.isSuccessful()) {
                    progressDialog.dismiss();
                    admin = response.body();
                    if (!admin.isError()) {
                        tinyDB.putInt("LOGIN_PREF", 4);
                        tinyDB.putInt("ADMIN_ID", admin.getAdmin_id());
                        tinyDB.putString("ADMIN_EMAIL", admin.getAdmin_email());
                        //tinyDB.putString("ADMIN_PASSWORD", admin.getAdmin_password());
                        Toast.makeText(LoginActivity.this, admin.getMessage(), Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), AdminDashboardActivity.class));
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, admin.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("ERROR", admin.getMessage());
                    }

                }
            }

            @Override
            public void onFailure(Call<Admin> call, Throwable t) {
                progressDialog.dismiss();
                Log.d("ERROR", t.getMessage());
                Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }


    private void ShowAlertDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Contact to Admin");
        alertDialogBuilder.setMessage("Your Acount Not In Approved Status");
        alertDialogBuilder.setPositiveButton("ok",
                (arg0, arg1) -> arg0.dismiss());
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

}
