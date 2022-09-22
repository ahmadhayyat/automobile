package com.example.allinoneapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.allinoneapplication.R;
import com.example.allinoneapplication.customer.SelectedMechanicFurtherDetails;
import com.example.allinoneapplication.customer.SelectedWorkshopDetails;
import com.example.allinoneapplication.customer.UserDrawerActivity;
import com.example.allinoneapplication.model.Favourite;

import java.util.List;

public class FavAdapter extends BaseAdapter {

    List<Favourite> favouriteList;
    Context context;
    int check;
    LayoutInflater inflater;

    public FavAdapter(List<Favourite> favouriteList, Context context, int check) {
        this.favouriteList = favouriteList;
        this.context = context;
        this.check = check;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return favouriteList.size();
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
        view = inflater.inflate(R.layout.item_favourite, viewGroup, false);
        TextView tv_mechanic_name, tv_mechanic_email, tv_mechanic_contact,
                tv_mec_name,tv_mec_email,tv_mec_contact;
        tv_mechanic_name = view.findViewById(R.id.tv_mechanic_name);
        tv_mechanic_email = view.findViewById(R.id.tv_fmechanic_email);
        tv_mechanic_contact = view.findViewById(R.id.tv_fmechanic_contact);

        tv_mechanic_contact.setText(favouriteList.get(i).getMehanic_contact());
        tv_mechanic_name.setText(favouriteList.get(i).getMechanic_name());
        tv_mechanic_email.setText(favouriteList.get(i).getMechanic_email());
        tv_mec_name=view.findViewById(R.id.Tv_mec_name);
        tv_mec_email=view.findViewById(R.id.Tv_mec_email);
        tv_mec_contact=view.findViewById(R.id.Tv_mec_contact);
        if(check==1){
            tv_mec_name.setText("Mechanic Name");
            tv_mec_email.setText("Mechanic Email");
            tv_mec_contact.setText("Mechanic Contact");
        }
        else if (check==2){
            tv_mec_name.setText("Workshop Name");
            tv_mec_email.setText("Workshop Email");
            tv_mec_contact.setText("Workshop Contact");
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (check == 1) {
                    context.startActivity(new Intent(context, SelectedMechanicFurtherDetails.class)
                            .putExtra("MECHANIC_NAME", favouriteList.get(i).getMechanic_name())
                            .putExtra("MECHANIC_EMAIL", favouriteList.get(i).getMechanic_email())
                            .putExtra("MECHANIC_CONTACT", favouriteList.get(i).getMehanic_contact())
                            .putExtra("MECHANIC_CNIC", favouriteList.get(i).getMechanic_cnic())
                            .putExtra("MECHANIC_IMG", favouriteList.get(i).getMechanic_profile_img())
                            .putExtra("MECHANIC_ID", favouriteList.get(i).getMechanic_id())
                    );
                } else if (check == 2) {
                    context.startActivity(new Intent(context, SelectedWorkshopDetails.class)
                            .putExtra("Workshop_MECHANIC_NAME", favouriteList.get(i).getMechanic_name())
                            .putExtra("Workshop_MECHANIC_EMAIL", favouriteList.get(i).getMechanic_email())
                            .putExtra("Workshop_MECHANIC_CONTACT", favouriteList.get(i).getMehanic_contact())
                            .putExtra("Workshop_MECHANIC_CNIC", favouriteList.get(i).getMechanic_cnic())
                            .putExtra("Workshop_MECHANIC_PIC", favouriteList.get(i).getMechanic_profile_img())
                            .putExtra("WORKSHOP_ID", favouriteList.get(i).getMechanic_id())
                    );

                }

            }
        });

        return view;
    }
}
