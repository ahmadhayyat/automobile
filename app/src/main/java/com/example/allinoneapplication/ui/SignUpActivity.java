package com.example.allinoneapplication.ui;

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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.loader.content.CursorLoader;

import com.example.allinoneapplication.R;
import com.example.allinoneapplication.constant.Constant;
import com.example.allinoneapplication.model.Customer;
import com.example.allinoneapplication.model.Mechanic;
import com.example.allinoneapplication.model.Workshop;
import com.example.allinoneapplication.retrofit.RetrofitClient;
import com.example.allinoneapplication.service.CustomerSignupService;
import com.example.allinoneapplication.service.MechanicSignupService;
import com.example.allinoneapplication.service.WorkshopSignupService;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {
    EditText edt_signup_email, edt_signup_password, edt_signup_confirm_password,
            edt_signup_name, edt_signup_c_number, edt_signup_cnic_number;
    TextView edt_signup_location, tv_signup_veh_cat;
    Button btn_signup;
    Spinner signup_spinner;
    ImageView signup_img;
    TextView tv_login;
    LinearLayout mechanicSignupLL;
    String signup_array[] = {"Select Role", "Customer", "Mechanic", "WorkShop"};
    String getAddress;
    CharSequence[] items = {"Car", "Bike", "Truck", "Van", "Bus"};
    boolean[] selectitems = {false, false, false, false, false};
    boolean isCameraCapture = false;
    Uri profilepicPath;
    double getlatitude, getlongitude;
    ProgressDialog progressDialog;
    Customer customer;
    Mechanic mechanic;
    Workshop workshop;
    File file;
    EditText edt_signup_mechanic_price;
    int selectedtype = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.colorAccent));
        edt_signup_email = findViewById(R.id.edt_signup_email);
        edt_signup_password = findViewById(R.id.edt_signup_password);
        edt_signup_mechanic_price = findViewById(R.id.edt_signup_mechanic_price);
        edt_signup_confirm_password = findViewById(R.id.edt_signup_confirm_password);
        edt_signup_name = findViewById(R.id.edt_signup_name);
        edt_signup_c_number = findViewById(R.id.edt_signup_c_number);
        signup_spinner = findViewById(R.id.signup_spinner);
        tv_login = findViewById(R.id.tv_login);
        signup_img = findViewById(R.id.signup_img);
        btn_signup = findViewById(R.id.btn_signup);
        mechanicSignupLL = findViewById(R.id.mechanicSignupLL);
        edt_signup_cnic_number = findViewById(R.id.edt_signup_cnic_number);
        edt_signup_location = findViewById(R.id.edt_signup_location);
        tv_signup_veh_cat = findViewById(R.id.tv_signup_veh_cat);
        tv_signup_veh_cat.setText(itemsToString());
        tv_signup_veh_cat.setText("Click to Choose Vehicle Type");
        tv_signup_veh_cat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertdialogbuilder = new AlertDialog.Builder(SignUpActivity.this);
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
                        tv_signup_veh_cat.setText(itemsToString());
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog alertDialog = alertdialogbuilder.create();
                alertDialog.setCanceledOnTouchOutside(true);
                alertDialog.show();
            }
        });

        edt_signup_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), SelectLocationActivity.class));
            }
        });

        signup_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseProfilePicture();
            }
        });
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, signup_array);
        signup_spinner.setAdapter(adapter);

        signup_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    selectedtype = 0;
                    mechanicSignupLL.setVisibility(View.GONE);
                } else if (i == 1) {
                    selectedtype = 1;
                    mechanicSignupLL.setVisibility(View.GONE);
                } else if (i == 2) {
                    selectedtype = 2;
                    mechanicSignupLL.setVisibility(View.VISIBLE);
                } else if (i == 3) {
                    selectedtype = 3;
                    mechanicSignupLL.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        tv_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
            }
        });
        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Validation()) {
                    if (selectedtype == 1) {
                        CustomerSignUp();
                    } else if (selectedtype == 2) {
                        MechanicSignUp();
                    } else if (selectedtype == 3) {
                        WorkshopSignup();
                    }
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getAddress = Constant.ADDRESS;
        getlatitude = Constant.latitude;
        getlongitude = Constant.longitude;


        if (Constant.latitude != 0.0) {
            edt_signup_location.setText(getAddress);
        } else {
            edt_signup_location.setText("Click to Choose Location");
        }

    }

    public boolean Validation() {
        String email_Pattern = "[a-zA-Z0-9._-]+@[a-z.]+\\.+[a-z]+";
        String cnc_pattern = "^[0-9]{5}-[0-9]{7}-[0-9]{1}$";
        if (edt_signup_email.getText().toString().isEmpty()) {
            edt_signup_email.setError("Email field is Empty");
            return false;
        } else if (edt_signup_password.getText().toString().isEmpty()) {
            edt_signup_password.setError("Password field is Empty");
            return false;
        } else if (!edt_signup_email.getText().toString().matches(email_Pattern)) {
            edt_signup_email.setError("Invalid format of email");
            return false;
        } else if (edt_signup_confirm_password.getText().toString().isEmpty()) {
            edt_signup_confirm_password.setError("Confirm Password field is empty");
            return false;
        } else if (edt_signup_password.getText().toString().length() < 8) {
            edt_signup_password.setError("Password must be minimum 8 characters");
            return false;
        } else if (edt_signup_name.getText().toString().isEmpty()) {
            edt_signup_name.setError("Name  field is empty");
            return false;
        } else if (edt_signup_c_number.getText().toString().isEmpty()) {
            edt_signup_c_number.setError("Number field is empty");
            return false;
        } else if (edt_signup_c_number.getText().toString().length() < 11 || edt_signup_c_number.getText().toString().length() > 11) {
            edt_signup_c_number.setError("Invalid mobile number");
            return false;
        } else if (!edt_signup_password.getText().toString().equals(edt_signup_confirm_password.getText().toString())) {
            edt_signup_confirm_password.setError("password not matched");
            return false;
        }
        if (selectedtype != 1) {
            if (edt_signup_cnic_number.getText().toString().isEmpty()) {
                edt_signup_cnic_number.setError("Number field is empty");
                return false;
            } else if (!edt_signup_cnic_number.getText().toString().matches(cnc_pattern)) {
                edt_signup_cnic_number.setError("Invalid CNIC pattern");
                return false;
            } else if (tv_signup_veh_cat.getText().toString().isEmpty()) {
                tv_signup_veh_cat.setError("Field is empty");
                return false;
            }
        }

        return true;

    }

    private void chooseProfilePicture() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
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
                    signup_img.setImageURI(selectedImageUri);
                }
                break;
            case 2:
                if (resultCode == RESULT_OK) {
                    isCameraCapture = true;
                    Bundle bundle = data.getExtras();
                    Bitmap bitmapimage = (Bitmap) bundle.get("data");
                    profilepicPath = data.getData();
                    signup_img.setImageBitmap(bitmapimage);
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

    public void CustomerSignUp() {
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

            RequestBody c_name = RequestBody.create(MediaType.parse("text/plain"), edt_signup_name.getText().toString());

            RequestBody c_email = RequestBody.create(MediaType.parse("text/plain"), edt_signup_email.getText().toString());

            RequestBody c_password = RequestBody.create(MediaType.parse("text/plain"), edt_signup_password.getText().toString());

            RequestBody c_contant = RequestBody.create(MediaType.parse("text/plain"), edt_signup_c_number.getText().toString());

            RequestBody c_lat = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(getlatitude));

            RequestBody c_lng = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(getlongitude));

            RequestBody c_address = RequestBody.create(MediaType.parse("text/plain"), getAddress);

            MultipartBody.Part body = MultipartBody.Part.createFormData("customer_profile_img", file.getName(), requestfile);

            CustomerSignupService service = RetrofitClient.getClient().create(CustomerSignupService.class);
            Call<Customer> call = service.customer_signup(c_name, c_email, c_password, c_contant, c_lat, c_lng, c_address, body);
            call.enqueue(new Callback<Customer>() {
                @Override
                public void onResponse(Call<Customer> call, Response<Customer> response) {
                    if (response.isSuccessful()) {
                        progressDialog.dismiss();
                        customer = response.body();
                        if (!customer.isError()) {
                            Constant.ADDRESS = "";
                            Constant.latitude = 0.0;
                            Constant.longitude = 0.0;
                            Toast.makeText(SignUpActivity.this, customer.getMessage(), Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                            finish();
                        } else {
                            Toast.makeText(SignUpActivity.this, customer.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                }

                @Override
                public void onFailure(Call<Customer> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(SignUpActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });


        }


    }

    public void WorkshopSignup() {
        workshop = new Workshop();
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

            RequestBody wm_name = RequestBody.create(MediaType.parse("text/plain"), edt_signup_name.getText().toString());

            RequestBody wm_email = RequestBody.create(MediaType.parse("text/plain"), edt_signup_email.getText().toString());

            RequestBody wm_password = RequestBody.create(MediaType.parse("text/plain"), edt_signup_password.getText().toString());

            RequestBody wm_address = RequestBody.create(MediaType.parse("text/plain"), getAddress);

            RequestBody wm_contant = RequestBody.create(MediaType.parse("text/plain"), edt_signup_c_number.getText().toString());

            RequestBody wm_lat = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(getlatitude));

            RequestBody wm_lng = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(getlongitude));

            RequestBody wm_cnic = RequestBody.create(MediaType.parse("text/plain"), edt_signup_cnic_number.getText().toString());

            RequestBody vehicle_type = RequestBody.create(MediaType.parse("text/plain"), tv_signup_veh_cat.getText().toString());

            MultipartBody.Part body = MultipartBody.Part.createFormData("wmechanic_profile_img", file.getName(), requestfile);

            WorkshopSignupService service = RetrofitClient.getClient().create(WorkshopSignupService.class);
            Call<Workshop> call = service.workshop_signup(wm_name, wm_email, wm_password, wm_address, wm_contant, wm_lat, wm_lng, wm_cnic, vehicle_type, body);
            call.enqueue(new Callback<Workshop>() {
                @Override
                public void onResponse(Call<Workshop> call, Response<Workshop> response) {
                    if (response.isSuccessful()) {
                        progressDialog.dismiss();
                        workshop = response.body();
                        if (!workshop.isError()) {
                            Constant.ADDRESS = "";
                            Constant.latitude = 0.0;
                            Constant.longitude = 0.0;
                            Toast.makeText(SignUpActivity.this, workshop.getMessage(), Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                            finish();
                        } else {
                            Toast.makeText(SignUpActivity.this, workshop.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.d("signupError", workshop.getMessage());
                        }

                    }
                }

                @Override
                public void onFailure(Call<Workshop> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(SignUpActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.d("signupError", t.getMessage());
                }
            });
        }

    }

    public void MechanicSignUp() {
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

            RequestBody m_name = RequestBody.create(MediaType.parse("text/plain"), edt_signup_name.getText().toString());

            RequestBody m_email = RequestBody.create(MediaType.parse("text/plain"), edt_signup_email.getText().toString());

            RequestBody m_password = RequestBody.create(MediaType.parse("text/plain"), edt_signup_password.getText().toString());

            RequestBody m_address = RequestBody.create(MediaType.parse("text/plain"), getAddress);

            RequestBody m_contant = RequestBody.create(MediaType.parse("text/plain"), edt_signup_c_number.getText().toString());

            RequestBody m_lat = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(getlatitude));

            RequestBody m_lng = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(getlongitude));

            RequestBody m_cnic = RequestBody.create(MediaType.parse("text/plain"), edt_signup_cnic_number.getText().toString());

            RequestBody vehicle_type = RequestBody.create(MediaType.parse("text/plain"), tv_signup_veh_cat.getText().toString());
            RequestBody mechanic_price = RequestBody.create(MediaType.parse("text/plain"), edt_signup_mechanic_price.getText().toString());

            MultipartBody.Part body = MultipartBody.Part.createFormData("mechanic_profile_img", file.getName(), requestfile);

            MechanicSignupService service = RetrofitClient.getClient().create(MechanicSignupService.class);
            Call<Mechanic> call = service.mechanic_signup(m_name, m_password, m_address, m_email,
                    m_lng, m_lat, body, m_contant, m_cnic, vehicle_type, mechanic_price);
            call.enqueue(new Callback<Mechanic>() {

                @Override
                public void onResponse(Call<Mechanic> call, Response<Mechanic> response) {
                    if (response.isSuccessful()) {
                        progressDialog.dismiss();
                        mechanic = response.body();
                        if (!mechanic.isError()) {
                            Constant.ADDRESS = "";
                            Constant.latitude = 0.0;
                            Constant.longitude = 0.0;
                            Toast.makeText(SignUpActivity.this, mechanic.getMessage(), Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                            finish();
                        } else {
                            Toast.makeText(SignUpActivity.this, mechanic.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                }

                @Override
                public void onFailure(Call<Mechanic> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(SignUpActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });


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
}