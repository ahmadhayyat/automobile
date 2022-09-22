package com.example.allinoneapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.allinoneapplication.R;
import com.example.allinoneapplication.admin.SelectedMechanicDetails;
import com.example.allinoneapplication.admin.SelectedUserDetailActivity;
import com.example.allinoneapplication.model.Customer;
import com.example.allinoneapplication.model.Mechanic;

import java.util.List;

public class ManageMechanicDetailsAdapter extends BaseAdapter {
    List<Mechanic> managemechanicList;
    Context context;
    LayoutInflater inflater;
    public ManageMechanicDetailsAdapter(List<Mechanic> mechanicList, Context context) {
        this.managemechanicList = mechanicList;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return managemechanicList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.manage_mechanic_details, viewGroup, false);
        TextView tv_item_mechanicName, tv_mechanic_email, tv_mechanic_contact;
        Button btn_view_mechanic_det;
        tv_item_mechanicName = view.findViewById(R.id.tv_item_mechanicName);
        tv_mechanic_email = view.findViewById(R.id.tv_mechanic_email);
        tv_mechanic_contact = view.findViewById(R.id.tv_mechanic_contact);
        btn_view_mechanic_det = view.findViewById(R.id.btn_view_mechanic_det);

        tv_item_mechanicName.setText(managemechanicList.get(i).getMechanic_name());
        tv_mechanic_email.setText(managemechanicList.get(i).getMechanic_email());
        tv_mechanic_contact.setText(managemechanicList.get(i).getMechanic_contact());

        btn_view_mechanic_det.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, SelectedMechanicDetails.class);
                intent.putExtra("MECHANIC_ID", managemechanicList.get(i).getMechanic_id());
                intent.putExtra("MECHANIC_NAME", managemechanicList.get(i).getMechanic_name());
                intent.putExtra("MECHANIC_EMAIL", managemechanicList.get(i).getMechanic_email());
                intent.putExtra("MECHANIC_CONTACT", managemechanicList.get(i).getMechanic_contact());
                intent.putExtra("MECHANIC_ADDRESS", managemechanicList.get(i).getMechanic_address());
                intent.putExtra("MECHANIC_PROFILE", managemechanicList.get(i).getMechanic_profile_img());
                intent.putExtra("MECHANIC_STATUS", managemechanicList.get(i).getMechanic_status());
                intent.putExtra("MECHANIC_DATETIME", managemechanicList.get(i).getMechanic_datetime());
                intent.putExtra("MECHANIC_CNIC", managemechanicList.get(i).getMechanic_cnic());
                intent.putExtra("MECHANIC_VEHICLE_TYPE", managemechanicList.get(i).getVehicle_type());
                context.startActivity(intent);
            }
        });

        return view;
    }
}
