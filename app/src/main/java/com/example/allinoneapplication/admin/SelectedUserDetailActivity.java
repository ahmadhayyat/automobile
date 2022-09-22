package com.example.allinoneapplication.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.allinoneapplication.R;
import com.example.allinoneapplication.constant.EndPoint;
import com.example.allinoneapplication.model.Complaint;
import com.example.allinoneapplication.model.Customer;
import com.example.allinoneapplication.model.Workshop;
import com.example.allinoneapplication.retrofit.RetrofitClient;
import com.example.allinoneapplication.service.DeleteComplainsService;
import com.example.allinoneapplication.service.UpdateCustomerstatusService;
import com.example.allinoneapplication.service.UpdateWorkshopMechanicstatusService;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectedUserDetailActivity extends AppCompatActivity {

    CircleImageView selected_user_img;
    TextView selected_customer_name, selected_customer_email, selected_customer_contact, selected_customer_address,
            selected_customer_status, selected_customer_createdDate;
    int cID;
    String cName, cEmail, cContact, cAddress, cProfile, cStatus, cDateTime;
    Button btn_approved_customer, btn_block_customer;
    Customer customer;
    ProgressDialog progressDialog;
    Complaint complaint;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_user_detail);
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.colorAccent));
        selected_user_img = findViewById(R.id.selected_user_img);
        selected_customer_name = findViewById(R.id.selected_customer_name);
        selected_customer_email = findViewById(R.id.selected_customer_email);
        selected_customer_contact = findViewById(R.id.selected_customer_contact);
        selected_customer_address = findViewById(R.id.selected_customer_address);
        selected_customer_status = findViewById(R.id.selected_customer_status);
        selected_customer_createdDate = findViewById(R.id.selected_customer_createdDate);
        btn_approved_customer = findViewById(R.id.btn_approved_customer);
        btn_block_customer = findViewById(R.id.btn_blocked_customer);
        cID = getIntent().getIntExtra("CUSTOMER_ID", 0);
        cName = getIntent().getStringExtra("CUSTOMER_NAME");
        cEmail = getIntent().getStringExtra("CUSTOMER_EMAIL");
        cContact = getIntent().getStringExtra("CUSTOMER_CONTACT");
        cAddress = getIntent().getStringExtra("CUSTOMER_ADDRESS");
        cProfile = getIntent().getStringExtra("CUSTOMER_PROFILE");
        cStatus = getIntent().getStringExtra("CUSTOMER_STATUS");
        cDateTime = getIntent().getStringExtra("CUSTOMER_DATETIME");

        Glide.with(SelectedUserDetailActivity.this)
                .load(EndPoint.IMAGE_URL + cProfile).into(selected_user_img);
        selected_customer_name.setText(cName);
        selected_customer_email.setText(cEmail);
        selected_customer_contact.setText(cContact);
        selected_customer_address.setText(cAddress);
        if (cStatus.equals("A")) {
            selected_customer_status.setText("Approved");
            btn_approved_customer.setVisibility(View.GONE);
            btn_block_customer.setVisibility(View.VISIBLE);
        } else {
            selected_customer_status.setText("Blocked");
            btn_approved_customer.setVisibility(View.VISIBLE);
            btn_block_customer.setVisibility(View.GONE);
        }


        selected_customer_createdDate.setText(cDateTime);

        btn_approved_customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateCStatus(cID,"A");

            }
        });

        btn_block_customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateCStatus(cID,"B");
            }
        });
    }
    private void UpdateCStatus(int CID, String Status){
        customer=new Customer();
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("please wait...");
        progressDialog.show();

        UpdateCustomerstatusService service= RetrofitClient.getClient().create(UpdateCustomerstatusService.class);
        Call<Customer> call=service.updateStatus(CID,Status);
        call.enqueue(new Callback<Customer>() {
            @Override
            public void onResponse(Call<Customer> call, Response<Customer> response) {
                if(response.isSuccessful()){
                    progressDialog.dismiss();
                    customer=response.body();
                    if(!customer.isError()){
                        Toast.makeText(getApplicationContext(),
                                customer.getMessage(), Toast.LENGTH_SHORT).show();
                        DeleteComplain(cID);
                    }else{
                        Toast.makeText(getApplicationContext(),
                                customer.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onFailure(Call<Customer> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(),
                        t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void DeleteComplain(int cID) {
        complaint = new Complaint();
        DeleteComplainsService service = RetrofitClient.getClient().create(DeleteComplainsService.class);
        Call<Complaint> call = service.deleteComp(cID, "c");
        call.enqueue(new Callback<Complaint>() {
            @Override
            public void onResponse(Call<Complaint> call, Response<Complaint> response) {
                if (response.isSuccessful()) {
                    complaint = new Complaint();
                    if (!complaint.isError()) {
                        startActivity(new Intent(getApplicationContext(),
                                ManageUserActivity.class));
                        finish();

                        Log.e("COMPALIN", "onResponse: " + complaint.getMessage());
                    } else {
                        Log.e("COMPALIN", "onResponse: " + complaint.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<Complaint> call, Throwable t) {
                Toast.makeText(SelectedUserDetailActivity.this,
                        t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}