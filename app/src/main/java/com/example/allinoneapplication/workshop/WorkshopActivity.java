package com.example.allinoneapplication.workshop;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.allinoneapplication.R;
import com.example.allinoneapplication.constant.TinyDB;
import com.example.allinoneapplication.ui.AboutActivity;
import com.example.allinoneapplication.ui.LoginActivity;
import com.google.android.material.navigation.NavigationView;


public class WorkshopActivity extends AppCompatActivity {
    private DrawerLayout drawer;
    private NavigationView navigationview;
    private Toolbar toolbar;
    private ActionBarDrawerToggle toggle;
    CardView logout_CV, manage_bookings_CV, manage_wmechanic_CV, edit_profile_CV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workshop);
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.colorAccent));
        toolbar = findViewById(R.id.toolbar);
        logout_CV = findViewById(R.id.logout_CV);
        manage_bookings_CV = findViewById(R.id.manage_bookings_CV);
        manage_wmechanic_CV = findViewById(R.id.manage_wmechanic_CV);
        edit_profile_CV = findViewById(R.id.edit_profile_CV);
        navigationview = findViewById(R.id.navigation);
        drawer = findViewById(R.id.drawer);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Workshop DashBoard");
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.open, R.string.close);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        manage_bookings_CV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(WorkshopActivity.this, ManageMechanicBookingsActivity.class));
            }
        });
        logout_CV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new TinyDB(getApplicationContext()).clear();
                Toast.makeText(WorkshopActivity.this, "Logout successful", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(WorkshopActivity.this, LoginActivity.class));
            }
        });
        edit_profile_CV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(WorkshopActivity.this, WorkshopMechanicProfileActivity.class));
            }
        });
        manage_wmechanic_CV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(WorkshopActivity.this, ManageMechanicworkshopActivity.class));

            }
        });

        navigationview.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                drawer.closeDrawer(GravityCompat.START);
                switch (item.getItemId()) {
                    case R.id.about:
                        startActivity(new Intent(WorkshopActivity.this, AboutActivity.class));
                        break;
                }
                return true;
            }

        });
    }

    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);

        } else {
            super.onBackPressed();
        }
    }
}
