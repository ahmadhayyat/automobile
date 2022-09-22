package com.example.allinoneapplication.workshop;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.loader.content.CursorLoader;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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

import com.example.allinoneapplication.R;
import com.example.allinoneapplication.constant.TinyDB;
import com.example.allinoneapplication.model.Mechanic;
import com.example.allinoneapplication.retrofit.RetrofitClient;
import com.example.allinoneapplication.service.AddWorkshopMechanicService;
import com.example.allinoneapplication.service.MechanicSignupService;
import com.example.allinoneapplication.ui.LoginActivity;
import com.example.allinoneapplication.ui.SignUpActivity;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.File;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddMechanicActivity extends AppCompatActivity {

    CircleImageView mechanic_workshop_img;
    EditText edt_add_m_name, edt_m_cnic, edt_m_email, edt_m_password, edt_m_contact;
    boolean isCameraCapture = false;
    Uri profilepicPath;
    TinyDB tinyDB;
    Button btn_added_mechanic;
    TextView add_mech_vehicle_type;
    CharSequence[] items = {"Car", "Bike", "Truck", "Van", "Bus"};
    boolean[] selectitems = {false, false, false, false, false};
    File file;
    ProgressDialog progressDialog;
    Mechanic mechanic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_mechanic);
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.colorAccent));
        tinyDB = new TinyDB(this);
        mechanic_workshop_img = findViewById(R.id.mechanic_workshop_img);
        edt_add_m_name = findViewById(R.id.edt_add_m_name);
        edt_m_cnic = findViewById(R.id.edt_m_cnic);
        edt_m_email = findViewById(R.id.edt_m_email);
        edt_m_password = findViewById(R.id.edt_m_password);
        edt_m_contact = findViewById(R.id.edt_m_contact);
        add_mech_vehicle_type = findViewById(R.id.add_mech_vehicle_type);
        btn_added_mechanic = findViewById(R.id.btn_added_mechanic);
        btn_added_mechanic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Addworkshopmechanic();
            }
        });
        mechanic_workshop_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseProfilePicture();
            }
        });

        add_mech_vehicle_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertdialogbuilder = new AlertDialog.Builder(AddMechanicActivity.this);
                alertdialogbuilder.setTitle("select Vehicle Type");
                alertdialogbuilder.setMultiChoiceItems(items, selectitems, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                        selectitems[i] = b;
                    }
                });
                alertdialogbuilder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        add_mech_vehicle_type.setText(itemsToString());
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog alertDialog = alertdialogbuilder.create();
                alertDialog.setCanceledOnTouchOutside(true);
                alertDialog.show();
            }
        });

    }

    private String itemsToString() {
        String text = "";

        for (int i = 0; i < selectitems.length; i++) {
            if (selectitems[i]) {
                text = text + items[i] + ",";
            }
        }
        if (text.endsWith(",")) {
            text = text.substring(0, text.length() - 1);
        }

        return text.trim();
    }

    private void chooseProfilePicture() {
        AlertDialog.Builder builder = new AlertDialog.Builder(AddMechanicActivity.this);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    Uri selectedImageUri = data.getData();
                    profilepicPath = data.getData();
                    mechanic_workshop_img.setImageURI(selectedImageUri);
                }
                break;
            case 2:
                if (resultCode == RESULT_OK) {
                    isCameraCapture = true;
                    Bundle bundle = data.getExtras();
                    Bitmap bitmapimage = (Bitmap) bundle.get("data");
                    profilepicPath = data.getData();
                    mechanic_workshop_img.setImageBitmap(bitmapimage);
                }
        }
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

    public void Addworkshopmechanic() {
        mechanic = new Mechanic();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Registration");
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

            RequestBody m_name = RequestBody.create(MediaType.parse("text/plain"), edt_add_m_name.getText().toString());

            RequestBody m_email = RequestBody.create(MediaType.parse("text/plain"), edt_m_email.getText().toString());

            RequestBody m_password = RequestBody.create(MediaType.parse("text/plain"), edt_m_password.getText().toString());

            RequestBody m_contant = RequestBody.create(MediaType.parse("text/plain"), edt_m_contact.getText().toString());

            RequestBody m_cnic = RequestBody.create(MediaType.parse("text/plain"), edt_m_cnic.getText().toString());

            RequestBody vehicle_type = RequestBody.create(MediaType.parse("text/plain"), add_mech_vehicle_type.getText().toString());

            RequestBody m_status = RequestBody.create(MediaType.parse("text/plain"), "A");

            RequestBody fk_workshop_id = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(tinyDB.getInt("WMECHANIC_ID")));

            MultipartBody.Part body = MultipartBody.Part.createFormData("mechanic_profile_img", file.getName(), requestfile);

            AddWorkshopMechanicService service = RetrofitClient.getClient().create(AddWorkshopMechanicService.class);
            Call<Mechanic> call = service.add_workshop_mechanic(m_name, m_password, m_email, body, m_contant, m_cnic,
                    vehicle_type, m_status, fk_workshop_id);
            call.enqueue(new Callback<Mechanic>() {

                @Override
                public void onResponse(Call<Mechanic> call, Response<Mechanic> response) {
                    if (response.isSuccessful()) {
                        progressDialog.dismiss();
                        mechanic = response.body();
                        if (!mechanic.isError()) {
                            Toast.makeText(AddMechanicActivity.this, mechanic.getMessage(), Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), WorkshopActivity.class));
                            finish();
                        } else {
                            Toast.makeText(AddMechanicActivity.this, mechanic.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                }

                @Override
                public void onFailure(Call<Mechanic> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(AddMechanicActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });


        }
    }
}