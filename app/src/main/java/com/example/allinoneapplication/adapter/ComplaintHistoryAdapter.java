package com.example.allinoneapplication.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.allinoneapplication.R;
import com.example.allinoneapplication.model.Complaint;

import java.util.List;

public class ComplaintHistoryAdapter extends RecyclerView.Adapter<ComplaintHistoryAdapter.ComplaintHolder> {

    List<Complaint> complaintList;
    Context context;

    public ComplaintHistoryAdapter(List<Complaint> complaintList, Context context) {
        this.complaintList = complaintList;
        this.context = context;
    }

    @NonNull
    @Override
    public ComplaintHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_complaint, parent, false);
        return new ComplaintHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull ComplaintHolder holder, int position) {
        holder.tv_comp_num.setText(complaintList.get(position).getComp_num());
        holder.tv_comp_sub.setText(complaintList.get(position).getComp_sub());
        holder.tv_comp_against.setText(complaintList.get(position).getD_name());
        holder.tv_comp_date.setText(complaintList.get(position).getComp_datetime());
        String getStatus = complaintList.get(position).getComp_status();
        if (getStatus.equals("P")) {
            holder.tv_cmp_status.setText("Pending");
            holder.tv_cmp_status.setTextColor(R.color.quantum_googyellowA400);
        } else if (getStatus.equals("A")) {
            holder.tv_cmp_status.setText("Accepted");
            holder.tv_cmp_status.setTextColor(Color.GREEN);
        } else if (getStatus.equals("R")) {
            holder.tv_cmp_status.setText("Rejected");
            holder.tv_cmp_status.setTextColor(Color.RED);
        } else if (getStatus.equals("IP")) {
            holder.tv_cmp_status.setText("In-Process");
            holder.tv_cmp_status.setTextColor(R.color.quantum_googyellowA400);
        } else if (getStatus.equals("C")) {
            holder.tv_cmp_status.setText("Completed");
            holder.tv_cmp_status.setTextColor(Color.GREEN);
        }

    }

    @Override
    public int getItemCount() {
        return complaintList.size();
    }

    public class ComplaintHolder extends RecyclerView.ViewHolder {

        TextView tv_comp_num, tv_comp_sub, tv_comp_against, tv_comp_date, tv_cmp_status;

        public ComplaintHolder(@NonNull View itemView) {
            super(itemView);
            tv_comp_num = itemView.findViewById(R.id.tv_comp_num);
            tv_comp_sub = itemView.findViewById(R.id.tv_comp_sub);
            tv_comp_against = itemView.findViewById(R.id.tv_comp_against);
            tv_comp_date = itemView.findViewById(R.id.tv_comp_date);
            tv_cmp_status = itemView.findViewById(R.id.tv_cmp_status);
        }
    }

}
