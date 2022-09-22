package com.example.allinoneapplication.mechanic;

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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.loader.content.CursorLoader;

import com.bumptech.glide.Glide;
import com.example.allinoneapplication.R;
import com.example.allinoneapplication.adapter.RateusAdapter;
import com.example.allinoneapplication.constant.EndPoint;
import com.example.allinoneapplication.constant.TinyDB;
import com.example.allinoneapplication.model.Mechanic;
import com.example.allinoneapplication.model.RateFeedback;
import com.example.allinoneapplication.retrofit.RetrofitClient;
import com.example.allinoneapplication.service.GetRatingService;
import com.example.allinoneapplication.service.MechanicPasswordService;
import com.example.allinoneapplication.service.UpdateMecImageProfileService;
import com.example.allinoneapplication.service.UpdateMechanicProfileService;
import com.example.allinoneapplication.ui.LoginActivity;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MechanicProfileActivity extends AppCompatActivity {

    Button btn_mchngpass, btn_msubmit;
    ListView feedback;
    RatingBar rateus;
    TinyDB tinyDB;
    EditText edt_mname, edt_memail, edt_mcontact;
    String getImage;
    CircleImageView mechanic_Profile_picture;
    ProgressDialog progressDialog;
    Mechanic mechanic;
    File file;
    boolean isCameraCapture = false;
    Uri profilepicPath;
    List<String> feedbacklist = new ArrayList<>();
    RateusAdapter adapter;
    double sum = 0;
    List<RateFeedback> rateFeedbackList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mechanic_profile);
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.colorAccent));
        tinyDB = new TinyDB(this);
        btn_mchngpass = findViewById(R.id.btn_mchngpass);
        btn_msubmit = findViewById(R.id.btn_msubmit);
        edt_mname = findViewById(R.id.edt_mname);
        edt_memail = findViewById(R.id.edt_memail);
        edt_mcontact = findViewById(R.id.edt_mcontact);
        feedback = findViewById(R.id.feedback);
        rateus = findViewById(R.id.rateus);
        mechanic_Profile_picture = findViewById(R.id.mechanic_Profile_picture);
        mechanic_Profile_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseProfilePicture();
            }
        });
        getImage = tinyDB.getString("MECHANIC_PROFILE");
        edt_mname.setText(tinyDB.getString("MECHANIC_NAME"));
        edt_memail.setText(tinyDB.getString("MECHANIC_EMAIL"));
        edt_mcontact.setText(tinyDB.getString("MECHANIC_CONTACT"));
        Glide.with(this)
                .load(EndPoint.IMAGE_URL + getImage)
                .into(mechanic_Profile_picture);

        btn_mchngpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changempassword();
            }
        });
        btn_msubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Updateprofile();
            }
        });
        getMechanicRatingFeedback();

    }

    public void changempassword() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MechanicProfileActivity.this);
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

    private void ChangeMPass(String m_pass, String n_pass) {
        mechanic = new Mechanic();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("please wait...");
        progressDialog.show();

        MechanicPasswordService service = RetrofitClient.getClient().create(MechanicPasswordService.class);
        Call<Mechanic> call = service.updateMPass(tinyDB.getInt("MECHANIC_ID"), m_pass, n_pass);
        call.enqueue(new Callback<Mechanic>() {
            @Override
            public void onResponse(Call<Mechanic> call, Response<Mechanic> response) {
                if (response.isSuccessful()) {
                    progressDialog.dismiss();
                    mechanic = response.body();
                    if (!mechanic.isError()) {
                        Toast.makeText(MechanicProfileActivity.this,
                                mechanic.getMessage(), Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(),
                                LoginActivity.class));
                        finish();

                    } else {
                        Toast.makeText(MechanicProfileActivity.this,
                                mechanic.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onFailure(Call<Mechanic> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(MechanicProfileActivity.this,
                        t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void chooseProfilePicture() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MechanicProfileActivity.this);
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

    public void UpdateMImage() {
        mechanic = new Mechanic();
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

            RequestBody m_id = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(tinyDB.getInt("MECHANIC_ID")));

            MultipartBody.Part body = MultipartBody.Part.createFormData("mechanic_profile_img", file.getName(), requestfile);

            UpdateMecImageProfileService service = RetrofitClient.getClient().create(UpdateMecImageProfileService.class);
            Call<Mechanic> call = service.mechanic_profile(m_id, body);
            call.enqueue(new Callback<Mechanic>() {
                @Override
                public void onResponse(Call<Mechanic> call, Response<Mechanic> response) {
                    if (response.isSuccessful()) {
                        progressDialog.dismiss();
                        mechanic = response.body();
                        if (!mechanic.isError()) {
                            Toast.makeText(MechanicProfileActivity.this, mechanic.getMessage(), Toast.LENGTH_SHORT).show();
                            tinyDB.putString("MECHANIC_PROFILE", mechanic.getMechanic_profile_img());
                        } else {
                            Toast.makeText(MechanicProfileActivity.this, mechanic.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                }

                @Override
                public void onFailure(Call<Mechanic> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(MechanicProfileActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
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
                    mechanic_Profile_picture.setImageURI(selectedImageUri);
                    UpdateMImage();
                }
                break;
            case 2:
                if (resultCode == RESULT_OK) {
                    isCameraCapture = true;
                    Bundle bundle = data.getExtras();
                    Bitmap bitmapimage = (Bitmap) bundle.get("data");
                    profilepicPath = data.getData();
                    mechanic_Profile_picture.setImageBitmap(bitmapimage);
                    UpdateMImage();
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
        mechanic = new Mechanic();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("please wait...");
        progressDialog.show();

        UpdateMechanicProfileService service = RetrofitClient.getClient().create(UpdateMechanicProfileService.class);
        Call<Mechanic> call = service.updateMprofile(tinyDB.getInt("MECHANIC_ID"),
                edt_mname.getText().toString(),
                edt_memail.getText().toString(),
                edt_mcontact.getText().toString());
        call.enqueue(new Callback<Mechanic>() {
            @Override
            public void onResponse(Call<Mechanic> call, Response<Mechanic> response) {
                if (response.isSuccessful()) {
                    progressDialog.dismiss();
                    mechanic = response.body();
                    if (!mechanic.isError()) {
                        Toast.makeText(MechanicProfileActivity.this,
                                mechanic.getMessage(), Toast.LENGTH_SHORT).show();
                        tinyDB.putString("MECHANIC_NAME", edt_mname.getText().toString());
                        tinyDB.putString("MECHANIC_EMAIL", edt_memail.getText().toString());
                        tinyDB.putString("MECHANIC_CONTACT", edt_mcontact.getText().toString());
                    } else {
                        Toast.makeText(MechanicProfileActivity.this,
                                mechanic.getMessage(), Toast.LENGTH_SHORT).show();

                    }

                }
            }

            @Override
            public void onFailure(Call<Mechanic> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(MechanicProfileActivity.this,
                        t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


    public void getMechanicRatingFeedback() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("please wait...");
        progressDialog.show();

        GetRatingService service = RetrofitClient.getClient().create(GetRatingService.class);

        Call<JsonObject> call = service.getRating(tinyDB.getInt("MECHANIC_ID"));
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    progressDialog.dismiss();
                    if (response.code() == 200) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.body().getAsJsonObject().toString());

                            JSONArray jsonArray = jsonObject.getJSONArray("records");

                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject data = jsonArray.getJSONObject(i);

                                rateFeedbackList.add(new RateFeedback(
                                        data.getString("rf_title"),
                                        data.getDouble("rf_star")
                                ));
                                feedbacklist.add(data.getString("rf_title"));
                            }
                            SetRating();

                        } catch (JSONException e) {
                            Toast.makeText(MechanicProfileActivity.this,
                                    response.message(), Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(MechanicProfileActivity.this,
                                response.message(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(MechanicProfileActivity.this,
                            response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(MechanicProfileActivity.this,
                        t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void SetRating() {
        ArrayAdapter<String> feedbackadapter = new ArrayAdapter<>(this,
                R.layout.support_simple_spinner_dropdown_item, feedbacklist);
        feedback.setAdapter(feedbackadapter);
        double result;

        for (int i = 0; i < rateFeedbackList.size(); i++) {
            sum = sum + rateFeedbackList.get(i).getRf_star();
        }
        result = sum / rateFeedbackList.size();
        rateus.setRating((float) result);

    }
}