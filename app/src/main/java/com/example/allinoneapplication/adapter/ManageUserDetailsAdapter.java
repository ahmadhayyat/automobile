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
import com.example.allinoneapplication.admin.SelectedUserDetailActivity;
import com.example.allinoneapplication.model.Customer;
import com.example.allinoneapplication.model.Mechanic;

import java.util.List;

public class ManageUserDetailsAdapter extends BaseAdapter {
    List<Customer> manageuserList;
    Context context;
    LayoutInflater inflater;

    public ManageUserDetailsAdapter(List<Customer> customerList, Context context) {
        this.manageuserList = customerList;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return manageuserList.size();
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
        view = inflater.inflate(R.layout.manage_user_details, viewGroup, false);
        TextView tv_item_customerName, tv_customer_email, tv_customer_contact;
        Button btn_view_cust_det;
        tv_item_customerName = view.findViewById(R.id.tv_item_customerName);
        tv_customer_email = view.findViewById(R.id.tv_customer_email);
        tv_customer_contact = view.findViewById(R.id.tv_customer_contact);
        btn_view_cust_det = view.findViewById(R.id.btn_view_cust_det);

        tv_item_customerName.setText(manageuserList.get(i).getCustomer_name());
        tv_customer_email.setText(manageuserList.get(i).getCustomer_email());
        tv_customer_contact.setText(manageuserList.get(i).getCustomer_contact());

        btn_view_cust_det.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, SelectedUserDetailActivity.class);
                intent.putExtra("CUSTOMER_ID", manageuserList.get(i).getCustomer_id());
                intent.putExtra("CUSTOMER_NAME", manageuserList.get(i).getCustomer_name());
                intent.putExtra("CUSTOMER_EMAIL", manageuserList.get(i).getCustomer_email());
                intent.putExtra("CUSTOMER_CONTACT", manageuserList.get(i).getCustomer_contact());
                intent.putExtra("CUSTOMER_ADDRESS", manageuserList.get(i).getCustomer_address());
                intent.putExtra("CUSTOMER_PROFILE", manageuserList.get(i).getCustomer_profile_img());
                intent.putExtra("CUSTOMER_STATUS", manageuserList.get(i).getCustomer_status());
                intent.putExtra("CUSTOMER_DATETIME", manageuserList.get(i).getCreated_datetime());
                context.startActivity(intent);
            }
        });
        return view;
    }
}
