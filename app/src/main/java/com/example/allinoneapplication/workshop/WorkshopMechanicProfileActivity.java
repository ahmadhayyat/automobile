package com.example.allinoneapplication.workshop;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.loader.content.CursorLoader;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.allinoneapplication.R;
import com.example.allinoneapplication.constant.EndPoint;
import com.example.allinoneapplication.constant.TinyDB;
import com.example.allinoneapplication.mechanic.MechanicProfileActivity;
import com.example.allinoneapplication.model.Mechanic;
import com.example.allinoneapplication.model.Workshop;
import com.example.allinoneapplication.retrofit.RetrofitClient;
import com.example.allinoneapplication.service.MechanicPasswordService;
import com.example.allinoneapplication.service.UpdateMecImageProfileService;
import com.example.allinoneapplication.service.UpdateMechanicProfileService;
import com.example.allinoneapplication.service.UpdateWorkshopMechanicProfileService;
import com.example.allinoneapplication.service.UpdateWorkshopMechanicimgservice;
import com.example.allinoneapplication.service.Workshopmechanicpasswordservice;
import com.example.allinoneapplication.ui.LoginActivity;
import com.example.allinoneapplication.ui.SelectLocationActivity;
import com.example.allinoneapplication.ui.SignUpActivity;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WorkshopMechanicProfileActivity extends AppCompatActivity {
    Button btn_wmchngpass, btn_wmsubmit;
    TinyDB tinyDB;
    EditText edt_wmname, edt_wmemail, edt_wmcontact;
    TextView edt_wmaddress;
    String getImage;
    CircleImageView wmechanic_Profile_picture;
    ProgressDialog progressDialog;
    Workshop workshop;
    File file;
    boolean isCameraCapture = false;
    Uri profilepicPath;
    boolean mapCheck = false;
    double getlatitude, getlongitude;
    String getAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workshop_mechanic_profile);
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.colorAccent));
        tinyDB = new TinyDB(this);
        btn_wmchngpass = findViewById(R.id.btn_wmchngpass);
        btn_wmsubmit = findViewById(R.id.btn_wmsubmit);
        edt_wmname = findViewById(R.id.edt_wmname);
        edt_wmemail = findViewById(R.id.edt_wmemail);
        edt_wmcontact = findViewById(R.id.edt_wmcontact);
        edt_wmaddress = findViewById(R.id.edt_wmaddress);
        wmechanic_Profile_picture = findViewById(R.id.wmechanic_Profile_picture);
        wmechanic_Profile_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseProfilePicture();
            }
        });
        getImage = tinyDB.getString("WMECHANIC_PROFILE");
        edt_wmname.setText(tinyDB.getString("WMECHANIC_NAME"));
        edt_wmemail.setText(tinyDB.getString("WMECHANIC_EMAIL"));
        edt_wmcontact.setText(tinyDB.getString("WMECHANIC_CONTACT"));
        edt_wmaddress.setText(tinyDB.getString("WMECHANIC_ADDRESS"));
        Glide.with(this)
                .load(EndPoint.IMAGE_URL + getImage)
                .into(wmechanic_Profile_picture);
        btn_wmchngpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changewmpassword();
            }
        });
        btn_wmsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Updateprofile();
            }
        });
        edt_wmaddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(WorkshopMechanicProfileActivity.this, UpdateLocationWorkshopMActivity.class));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getAddress = getIntent().getStringExtra("ADDRESS");
        getlatitude = getIntent().getDoubleExtra("LATITUDE", 0.0);
        getlongitude = getIntent().getDoubleExtra("LONGITUDE", 0.0);
        if (getAddress != null) {
            mapCheck = true;
            edt_wmaddress.setText(getAddress);
        }

    }

    public void changewmpassword() {
        AlertDialog.Builder builder = new AlertDialog.Builder(WorkshopMechanicProfileActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.change_mechanic_password, null);
        builder.setCancelable(true);
        builder.setView(dialogView);

        EditText old_password = dialogView.findViewById(R.id.edt_old_mpassword);
        EditText new_password = dialogView.findViewById(R.id.edt_new_mpassword);
        EditText confirm_password = dialogView.findViewById(R.id.edt_confirm_mpassword);
        final AlertDialog change_mechanic_password = builder.create();
        change_mechanic_password.show();
        Button submit = dialogView.findViewById(R.id.btn_msub);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangeMPass(old_password.getText().toString(), confirm_password.getText().toString());

            }
        });
    }

    private void ChangeMPass(String wm_pass, String n_pass) {
        workshop = new Workshop();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("please wait...");
        progressDialog.show();

        Workshopmechanicpasswordservice service = RetrofitClient.getClient().create(Workshopmechanicpasswordservice.class);
        Call<Workshop> call = service.updateWMPass(tinyDB.getInt("WMECHANIC_ID"), wm_pass, n_pass);
        call.enqueue(new Callback<Workshop>() {
            @Override
            public void onResponse(Call<Workshop> call, Response<Workshop> response) {
                if (response.isSuccessful()) {
                    progressDialog.dismiss();
                    workshop = response.body();
                    if (!workshop.isError()) {
                        Toast.makeText(WorkshopMechanicProfileActivity.this,
                                workshop.getMessage(), Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(),
                                LoginActivity.class));
                        finish();

                    } else {
                        Toast.makeText(WorkshopMechanicProfileActivity.this,
                                workshop.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onFailure(Call<Workshop> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(WorkshopMechanicProfileActivity.this,
                        t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void chooseProfilePicture() {
        AlertDialog.Builder builder = new AlertDialog.Builder(WorkshopMechanicProfileActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.alertdialogprofilepicture, null);
        builder.setCancelable(true);
        builder.setView(dialogView);

        ImageView camera = dialogView.findViewById(R.id.camera);
        ImageView gallery = dialogView.findViewById(R.id.gallery);

        final AlertDialog alertDialogprofilepicture = builder.create();
        alertDialogprofilepicture.show();
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isStoragePermissionGranted()) {
                    takePictureFromCamera();
                    alertDialogprofilepicture.cancel();
                }
            }
        });
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isStoragePermissionGranted()) {
                    takePictureFromGallery();
                    alertDialogprofilepicture.cancel();
                }

            }
        });
    }

    private void takePictureFromGallery() {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto, 1);
    }

    private void takePictureFromCamera() {
        Intent takepicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takepicture.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takepicture, 2);
        }
    }

    private boolean isStoragePermissionGranted() {
        try {
            if (Build.VERSION.SDK_INT >= 23) {

                if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_GRANTED) {
                    return true;
                } else {

                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 1);
                    return false;
                }
            } else {
                return true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        try {
            switch (requestCode) {
                case 0:
                    boolean isPerpermissionForAllGranted = false;
                    if (grantResults.length > 0 && permissions.length == grantResults.length) {
                        for (int i = 0; i < permissions.length; i++) {
                            if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                                isPerpermissionForAllGranted = true;
                            } else {
                                isPerpermissionForAllGranted = false;
                            }
                        }

                        Toast.makeText(this, "Permission accepted!", Toast.LENGTH_SHORT).show();
                    } else {
                        isPerpermissionForAllGranted = true;
                        Toast.makeText(this, "Permission denied!", Toast.LENGTH_SHORT).show();
                    }
                    if (isPerpermissionForAllGranted) {
                        Toast.makeText(this, "Permission accepted!", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void UpdateWMImage() {
        workshop = new Workshop();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Profile Updation");
        progressDialog.setMessage("please wait...");
        progressDialog.show();

        if (profilepicPath == null) {
            Toast.makeText(this, "Choose Image", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        } else {
            if (isCameraCapture) {
                file = new File(String.valueOf(profilepicPath));
            } else {
                file = new File(getRealPathFromURI(profilepicPath));
            }

            RequestBody requestfile = RequestBody.create(MediaType.parse("image/*"), file);

            RequestBody wm_id = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(tinyDB.getInt("WMECHANIC_ID")));

            MultipartBody.Part body = MultipartBody.Part.createFormData("wmechanic_profile_img", file.getName(), requestfile);

            UpdateWorkshopMechanicimgservice service = RetrofitClient.getClient().create(UpdateWorkshopMechanicimgservice.class);
            Call<Workshop> call = service.wmechanic_profile(wm_id, body);
            call.enqueue(new Callback<Workshop>() {
                @Override
                public void onResponse(Call<Workshop> call, Response<Workshop> response) {
                    if (response.isSuccessful()) {
                        progressDialog.dismiss();
                        workshop = response.body();
                        if (!workshop.isError()) {
                            Toast.makeText(WorkshopMechanicProfileActivity.this, workshop.getMessage(), Toast.LENGTH_SHORT).show();
                            tinyDB.putString("WMECHANIC_PROFILE", workshop.getWmechanic_profile_img());
                        } else {
                            Toast.makeText(WorkshopMechanicProfileActivity.this, workshop.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                }

                @Override
                public void onFailure(Call<Workshop> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(WorkshopMechanicProfileActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });


        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    Uri selectedImageUri = data.getData();
                    profilepicPath = data.getData();
                    wmechanic_Profile_picture.setImageURI(selectedImageUri);
                    UpdateWMImage();
                }
                break;
            case 2:
                if (resultCode == RESULT_OK) {
                    isCameraCapture = true;
                    Bundle bundle = data.getExtras();
                    Bitmap bitmapimage = (Bitmap) bundle.get("data");
                    profilepicPath = data.getData();
                    wmechanic_Profile_picture.setImageBitmap(bitmapimage);
                    UpdateWMImage();
                }
        }
    }

    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(this, contentUri, proj,
                null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }

    private void Updateprofile() {
        workshop = new Workshop();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("please wait...");
        progressDialog.show();
        if (mapCheck) {
            UpdateWorkshopMechanicProfileService service = RetrofitClient.getClient().create(UpdateWorkshopMechanicProfileService.class);
            Call<Workshop> call = service.updateWMprofile(tinyDB.getInt("WMECHANIC_ID"),
                    edt_wmname.getText().toString(),
                    edt_wmemail.getText().toString(),
                    edt_wmcontact.getText().toString(),
                    edt_wmaddress.getText().toString(),
                    getlongitude = getIntent().getDoubleExtra("LONGITUDE", 0.0),
                    getlatitude = getIntent().getDoubleExtra("LATITUDE", 0.0)

            );
            call.enqueue(new Callback<Workshop>() {
                @Override
                public void onResponse(Call<Workshop> call, Response<Workshop> response) {
                    if (response.isSuccessful()) {
                        progressDialog.dismiss();
                        workshop = response.body();
                        if (!workshop.isError()) {
                            Toast.makeText(WorkshopMechanicProfileActivity.this,
                                    workshop.getMessage(), Toast.LENGTH_SHORT).show();
                            tinyDB.putString("WMECHANIC_NAME", edt_wmname.getText().toString());
                            tinyDB.putString("WMECHANIC_EMAIL", edt_wmemail.getText().toString());
                            tinyDB.putString("WMECHANIC_CONTACT", edt_wmcontact.getText().toString());
                            tinyDB.putString("WMECHANIC_ADDRESS", edt_wmaddress.getText().toString());
                            tinyDB.putDouble("WMECHANIC_LAT", getlatitude);
                            tinyDB.putDouble("WMECHANIC_LNG", getlongitude);
                        } else {
                            Toast.makeText(WorkshopMechanicProfileActivity.this,
                                    workshop.getMessage(), Toast.LENGTH_SHORT).show();

                        }

                    }
                }

                @Override
                public void onFailure(Call<Workshop> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(WorkshopMechanicProfileActivity.this,
                            t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            UpdateWorkshopMechanicProfileService service = RetrofitClient.getClient().create(UpdateWorkshopMechanicProfileService.class);
            Call<Workshop> call = service.updateWMprofile(tinyDB.getInt("WMECHANIC_ID"),
                    edt_wmname.getText().toString(),
                    edt_wmemail.getText().toString(),
                    edt_wmcontact.getText().toString(),
                    edt_wmaddress.getText().toString(),
                    tinyDB.getDouble("WMECHANIC_LAT"),
                    tinyDB.getDouble("WMECHANIC_LNG")
            );
            call.enqueue(new Callback<Workshop>() {
                @Override
                public void onResponse(Call<Workshop> call, Response<Workshop> response) {
                    if (response.isSuccessful()) {
                        progressDialog.dismiss();
                        workshop = response.body();
                        if (!workshop.isError()) {
                            Toast.makeText(WorkshopMechanicProfileActivity.this,
                                    workshop.getMessage(), Toast.LENGTH_SHORT).show();
                            tinyDB.putString("WMECHANIC_NAME", edt_wmname.getText().toString());
                            tinyDB.putString("WMECHANIC_EMAIL", edt_wmemail.getText().toString());
                            tinyDB.putString("WMECHANIC_CONTACT", edt_wmcontact.getText().toString());
                            tinyDB.putString("WMECHANIC_ADDRESS", edt_wmaddress.getText().toString());
                        } else {
                            Toast.makeText(WorkshopMechanicProfileActivity.this,
                                    workshop.getMessage(), Toast.LENGTH_SHORT).show();

                        }

                    } else {

                    }
                }

                @Override
                public void onFailure(Call<Workshop> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(WorkshopMechanicProfileActivity.this,
                            t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }


    }


}