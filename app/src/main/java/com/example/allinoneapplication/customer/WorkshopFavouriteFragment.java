package com.example.allinoneapplication.customer;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.example.allinoneapplication.R;
import com.example.allinoneapplication.adapter.FavAdapter;
import com.example.allinoneapplication.constant.TinyDB;
import com.example.allinoneapplication.model.Favourite;
import com.example.allinoneapplication.retrofit.RetrofitClient;
import com.example.allinoneapplication.service.GetFavWorkshopService;
import com.example.allinoneapplication.service.GetMechFavoriteService;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WorkshopFavouriteFragment extends Fragment {

    ListView workshop_fav_LV;
    View view;
    TinyDB tinyDB;
    ProgressDialog progressDialog;
    List<Favourite> favouriteList = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_workshop_favourite, container, false);
        workshop_fav_LV = view.findViewById(R.id.workshop_fav_LV);
        tinyDB = new TinyDB(requireContext());
        getFavWorkshop();
        return view;

    }

    public void getFavWorkshop() {
        progressDialog = new ProgressDialog(requireContext());
        progressDialog.setMessage("please wait...");
        progressDialog.show();

        GetFavWorkshopService service = RetrofitClient.getClient().create(GetFavWorkshopService.class);

        Call<JsonObject> call = service.getFavourite(tinyDB.getInt("CUSTOMERID"));
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

                                favouriteList.add(new Favourite(
                                        data.getInt("mechanic_id"),
                                        data.getString("mechanic_name"),
                                        data.getString("mechanic_email"),
                                        data.getString("mehanic_contact"),
                                        data.getString("mechanic_cnic"),
                                        data.getString("mechanic_profile_img")
                                ));
                            }

                            FavAdapter adapter = new FavAdapter(favouriteList, requireContext(),2);
                            workshop_fav_LV.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    } else {
                        Toast.makeText(requireContext(),
                                response.message(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(requireContext(),
                            response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(requireContext(),
                        t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}