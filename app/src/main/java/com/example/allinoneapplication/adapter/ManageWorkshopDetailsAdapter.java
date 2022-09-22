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
import com.example.allinoneapplication.workshop.WorkshopDetailActivity;
import com.example.allinoneapplication.model.Workshop;

import java.util.List;

public class ManageWorkshopDetailsAdapter extends BaseAdapter {

    List<Workshop> manageworkshopList;
    Context context;
    LayoutInflater inflater;

    public ManageWorkshopDetailsAdapter(List<Workshop> workshopList, Context context) {
        this.manageworkshopList = workshopList;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return manageworkshopList.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.manage_workshop_details, viewGroup, false);
        TextView tv_item_workshopName, tv_workshop_address, tv_workshop_contact;
        Button btn_view_workshop_det;
        tv_item_workshopName = view.findViewById(R.id.tv_item_workshopName);
        tv_workshop_address = view.findViewById(R.id.tv_workshop_address);
        tv_workshop_contact = view.findViewById(R.id.tv_workshop_contact);
        btn_view_workshop_det = view.findViewById(R.id.btn_view_workshop_det);
        tv_item_workshopName.setText(manageworkshopList.get(i).getWmechanic_name());
        tv_workshop_address.setText(manageworkshopList.get(i).getWmechanic_address());
        tv_workshop_contact.setText(manageworkshopList.get(i).getWmechanic_contact());


        btn_view_workshop_det.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, WorkshopDetailActivity.class);
                intent.putExtra("WORKSHOP_ID", manageworkshopList.get(i).getWmechanic_id());
                intent.putExtra("WORKSHOP_NAME", manageworkshopList.get(i).getWmechanic_name());
                intent.putExtra("WORKSHOP_EMAIL", manageworkshopList.get(i).getWmechanic_email());
                intent.putExtra("WORKSHOP_ADDRESS", manageworkshopList.get(i).getWmechanic_address());
                intent.putExtra("WORKSHOP_PROFILE", manageworkshopList.get(i).getWmechanic_profile_img());
                intent.putExtra("WORKSHOP_CONTACT", manageworkshopList.get(i).getWmechanic_contact());
                intent.putExtra("WORKSHOP_STATUS", manageworkshopList.get(i).getWmechanic_status());
                intent.putExtra("WORKSHOP_DATETIME", manageworkshopList.get(i).getWmechanic_datetime());
                intent.putExtra("WORKSHOP_CNIC", manageworkshopList.get(i).getWmechanic_cnic());
                intent.putExtra("WORKSHOP_VTYPE", manageworkshopList.get(i).getVehicle_type());
                context.startActivity(intent);
            }
        });

        return view;
    }
}
