package com.example.allinoneapplication.customer;

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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.allinoneapplication.R;
import com.example.allinoneapplication.constant.EndPoint;
import com.example.allinoneapplication.constant.TinyDB;
import com.example.allinoneapplication.model.Customer;
import com.example.allinoneapplication.retrofit.RetrofitClient;
import com.example.allinoneapplication.service.CustomerPasswordService;
import com.example.allinoneapplication.service.CustomerSignupService;
import com.example.allinoneapplication.service.UpdateCustomerProfileService;
import com.example.allinoneapplication.service.UpdateCustprofileService;
import com.example.allinoneapplication.ui.LoginActivity;
import com.example.allinoneapplication.ui.SignUpActivity;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserProfileActivity extends AppCompatActivity {

    Button btn_chngpass, btn_submit;
    TinyDB tinyDB;
    EditText edt_cname, edt_cemail, edt_ccontact;
    String getImage;
    CircleImageView cust_Profile_picture;
    ProgressDialog progressDialog;
    Customer customer;
    File file;
    boolean isCameraCapture = false;
    Uri profilepicPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.colorAccent));
        tinyDB = new TinyDB(this);
        btn_chngpass = findViewById(R.id.btn_chngpass);
        btn_submit = findViewById(R.id.btn_submit);
        edt_cname = findViewById(R.id.edt_cname);
        edt_cemail = findViewById(R.id.edt_cemail);
        edt_ccontact = findViewById(R.id.edt_ccontact);
        cust_Profile_picture = findViewById(R.id.cust_Profile_picture);
        cust_Profile_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseProfilePicture();
            }
        });
        getImage = tinyDB.getString("CUSTOMER_PROFILE");
        edt_cname.setText(tinyDB.getString("CUSTOMER_NAME"));
        edt_cemail.setText(tinyDB.getString("CUSTOMER_EMAIL"));
        edt_ccontact.setText(tinyDB.getString("CUSTOMER_CONTACT"));
        Glide.with(this)
                .load(EndPoint.IMAGE_URL + getImage)
                .into(cust_Profile_picture);

        btn_chngpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Changepassword();
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Updateprofile();
            }
        });

    }

    private void Changepassword() {
        AlertDialog.Builder builder = new AlertDialog.Builder(UserProfileActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.change_password_dialog, null);
        builder.setCancelable(false);
        builder.setView(dialogView);

        EditText old_password = dialogView.findViewById(R.id.edt_old_password);
        EditText new_password = dialogView.findViewById(R.id.edt_new_password);
        EditText confirm_password = dialogView.findViewById(R.id.edt_confirm_password);
        final AlertDialog change_password_dialog = builder.create();
        change_password_dialog.show();
        change_password_dialog.setCanceledOnTouchOutside(true);
        Button submit = dialogView.findViewById(R.id.btn_sub);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangePass(old_password.getText().toString(), confirm_password.getText().toString());
                change_password_dialog.cancel();
            }
        });
    }

    private void Updateprofile() {
        customer = new Customer();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("please wait...");
        progressDialog.show();

        UpdateCustomerProfileService service = RetrofitClient.getClient().create(UpdateCustomerProfileService.class);
        Call<Customer> call = service.updateprofile(tinyDB.getInt("CUSTOMERID"),
                edt_cname.getText().toString(),
                edt_cemail.getText().toString(),
                edt_ccontact.getText().toString());
        call.enqueue(new Callback<Customer>() {
            @Override
            public void onResponse(Call<Customer> call, Response<Customer> response) {
                if (response.isSuccessful()) {
                    progressDialog.dismiss();
                    customer = response.body();
                    if (!customer.isError()) {
                        Toast.makeText(UserProfileActivity.this,
                                customer.getMessage(), Toast.LENGTH_SHORT).show();
                        tinyDB.putString("CUSTOMER_NAME", edt_cname.getText().toString());
                        tinyDB.putString("CUSTOMER_EMAIL", edt_cemail.getText().toString());
                        tinyDB.putString("CUSTOMER_CONTACT", edt_ccontact.getText().toString());
                    } else {
                        Toast.makeText(UserProfileActivity.this,
                                customer.getMessage(), Toast.LENGTH_SHORT).show();

                    }

                }
            }

            @Override
            public void onFailure(Call<Customer> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(UserProfileActivity.this,
                        t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void chooseProfilePicture() {
        AlertDialog.Builder builder = new AlertDialog.Builder(UserProfileActivity.this);
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

    public void UpdateImage() {
        customer = new Customer();
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

            RequestBody c_id = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(tinyDB.getInt("CUSTOMERID")));

            MultipartBody.Part body = MultipartBody.Part.createFormData("customer_profile_img", file.getName(), requestfile);

            UpdateCustprofileService service = RetrofitClient.getClient().create(UpdateCustprofileService.class);
            Call<Customer> call = service.customer_profile(c_id, body);
            call.enqueue(new Callback<Customer>() {
                @Override
                public void onResponse(Call<Customer> call, Response<Customer> response) {
                    if (response.isSuccessful()) {
                        progressDialog.dismiss();
                        customer = response.body();
                        if (!customer.isError()) {
                            Toast.makeText(UserProfileActivity.this, customer.getMessage(), Toast.LENGTH_SHORT).show();
                            tinyDB.putString("CUSTOMER_PROFILE", customer.getCustomer_profile_img());
                        } else {
                            Toast.makeText(UserProfileActivity.this, customer.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                }

                @Override
                public void onFailure(Call<Customer> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(UserProfileActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
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
                    cust_Profile_picture.setImageURI(selectedImageUri);
                    UpdateImage();
                }
                break;
            case 2:
                if (resultCode == RESULT_OK) {
                    isCameraCapture = true;
                    Bundle bundle = data.getExtras();
                    Bitmap bitmapimage = (Bitmap) bundle.get("data");
                    profilepicPath = data.getData();
                    cust_Profile_picture.setImageBitmap(bitmapimage);
                    UpdateImage();
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

    private void ChangePass(String c_pass, String n_pass) {
        customer = new Customer();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("please wait...");
        progressDialog.show();

        CustomerPasswordService service = RetrofitClient.getClient().create(CustomerPasswordService.class);
        Call<Customer> call = service.updatePass(tinyDB.getInt("CUSTOMERID"), c_pass, n_pass);
        call.enqueue(new Callback<Customer>() {
            @Override
            public void onResponse(Call<Customer> call, Response<Customer> response) {
                if (response.isSuccessful()) {
                    progressDialog.dismiss();
                    customer = response.body();
                    if (!customer.isError()) {
                        Toast.makeText(UserProfileActivity.this,
                                customer.getMessage(), Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(),
                                LoginActivity.class));
                        finish();

                    } else {
                        Toast.makeText(UserProfileActivity.this,
                                customer.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Customer> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(UserProfileActivity.this,
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