package com.example.allinoneapplication.customer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.allinoneapplication.R;
import com.example.allinoneapplication.constant.EndPoint;
import com.example.allinoneapplication.constant.TinyDB;
import com.example.allinoneapplication.model.Booking;

import de.hdodenhof.circleimageview.CircleImageView;

public class SelectedWorkshopDetails extends AppCompatActivity {
    CircleImageView wmechanic_selected_img;
    TextView wmechanic_selected_name, wmechanic_selected_email, wmechanic_selected_contact,
            wmechanic_selected_cnic, wmechanic_selected_price;
    EditText wmoptional;
    Button Button_submit;
    int getWID;
    String wmName, wmEmail, wmContact, wmCnic, wmProfile;
    Booking booking;
    TinyDB tinyDB;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_workshop_details);
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.colorAccent));
        wmechanic_selected_img = findViewById(R.id.wmechanic_selected_img);
        wmechanic_selected_name = findViewById(R.id.wmechanic_selected_name);
        wmechanic_selected_email = findViewById(R.id.wmechanic_selected_email);
        wmechanic_selected_contact = findViewById(R.id.wmechanic_selected_contact);
        wmechanic_selected_cnic = findViewById(R.id.wmechanic_selected_cnic);
        wmechanic_selected_price = findViewById(R.id.wmechanic_selected_price);
        Button_submit = findViewById(R.id.Button_submit);
        wmoptional = findViewById(R.id.wmoptional);
        tinyDB = new TinyDB(this);
        wmName = getIntent().getStringExtra("Workshop_MECHANIC_NAME");
        wmEmail = getIntent().getStringExtra("Workshop_MECHANIC_EMAIL");
        wmContact = getIntent().getStringExtra("Workshop_MECHANIC_CONTACT");
        wmCnic = getIntent().getStringExtra("Workshop_MECHANIC_CNIC");
        wmProfile = getIntent().getStringExtra("Workshop_MECHANIC_PIC");
        getWID = getIntent().getIntExtra("WORKSHOP_ID", 0);
        Glide.with(SelectedWorkshopDetails.this)
                .load(EndPoint.IMAGE_URL + wmProfile).into(wmechanic_selected_img);
        wmechanic_selected_name.setText(wmName);
        wmechanic_selected_email.setText(wmEmail);
        wmechanic_selected_contact.setText(wmContact);
        wmechanic_selected_cnic.setText(wmCnic);
        Button_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), SelectedWorkshopMechanicActivity.class)
                        .putExtra("WORKSHOP_ID", getWID));

            }
        });
    }


}