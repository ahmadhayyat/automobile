package com.example.allinoneapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.allinoneapplication.R;
import com.example.allinoneapplication.customer.SelectedMechanicFurtherDetails;
import com.example.allinoneapplication.customer.ViewPreviousWorkActivity;
import com.example.allinoneapplication.model.Mechanic;
import com.example.allinoneapplication.workshop.ManageWorkshopBookingDetailsActivity;
import com.example.allinoneapplication.workshop.SelectedWorkshopMechanicFurtherDetails;

import java.util.List;

public class WorkShopMechanicAdapter extends BaseAdapter {

    List<Mechanic> mechanicList;
    Context context;
    int check;
    LayoutInflater inflater;

    public WorkShopMechanicAdapter(List<Mechanic> mechanicList, Context context, int check) {
        this.mechanicList = mechanicList;
        this.context = context;
        this.check = check;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mechanicList.size();
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
        view = inflater.inflate(R.layout.item_workshop_mech, viewGroup, false);
        TextView tv_mech_num = view.findViewById(R.id.tv_mech_num);
        TextView tv_mech_email = view.findViewById(R.id.tv_mech_email);
        TextView tv_mech_con = view.findViewById(R.id.tv_mech_con);
        LinearLayout workshop_me_LL = view.findViewById(R.id.workshop_me_LL);
        Button view_workshop_me_history = view.findViewById(R.id.view_workshop_me_history);
        tv_mech_num.setText(mechanicList.get(i).getMechanic_name());
        tv_mech_email.setText(mechanicList.get(i).getMechanic_email());
        tv_mech_con.setText(mechanicList.get(i).getMechanic_contact());


        if (check == 1) {
            view_workshop_me_history.setVisibility(View.VISIBLE);

            view_workshop_me_history.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    context.startActivity(new Intent(context, ViewPreviousWorkActivity.class)
                            .putExtra("MECHANIC_ID", mechanicList.get(i).getMechanic_id())
                    );
                }
            });

            workshop_me_LL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    context.startActivity(new Intent(context, SelectedMechanicFurtherDetails.class)
                            .putExtra("MECHANIC_NAME", mechanicList.get(i).getMechanic_name())
                            .putExtra("MECHANIC_EMAIL", mechanicList.get(i).getMechanic_email())
                            .putExtra("MECHANIC_CONTACT", mechanicList.get(i).getMechanic_contact())
                            .putExtra("MECHANIC_CNIC", mechanicList.get(i).getMechanic_cnic())
                            .putExtra("MECHANIC_IMG", mechanicList.get(i).getMechanic_profile_img())
                            .putExtra("MECHANIC_ID", mechanicList.get(i).getMechanic_id())
                    );
                }


            });

        } else if (check == 3) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    context.startActivity(new Intent(context, ManageWorkshopBookingDetailsActivity.class)
                            .putExtra("MECHANIC_ID", mechanicList.get(i).getMechanic_id())
                    );
                }
            });

        } else {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    context.startActivity(new Intent(context, SelectedWorkshopMechanicFurtherDetails.class)
                            .putExtra("MECHANIC_NAME", mechanicList.get(i).getMechanic_name())
                            .putExtra("MECHANIC_EMAIL", mechanicList.get(i).getMechanic_email())
                            .putExtra("MECHANIC_CONTACT", mechanicList.get(i).getMechanic_contact())
                            .putExtra("MECHANIC_CNIC", mechanicList.get(i).getMechanic_cnic())
                            .putExtra("MECHANIC_IMG", mechanicList.get(i).getMechanic_profile_img())
                            .putExtra("MECHANIC_ID", mechanicList.get(i).getMechanic_id())
                    );
                }
            });

        }

        return view;
    }
}
