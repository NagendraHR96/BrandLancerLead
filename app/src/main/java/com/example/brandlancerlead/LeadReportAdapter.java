package com.example.brandlancerlead;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.brandlancerlead.model.LeadReportListJsonObjects;

import java.util.ArrayList;

public class LeadReportAdapter extends RecyclerView.Adapter<LeadReportAdapter.LrHolder> {
    Context context;
    ArrayList<LeadReportListJsonObjects> resultset;

    public LeadReportAdapter(Context context, ArrayList<LeadReportListJsonObjects> resultset) {
        this.context = context;
        this.resultset = resultset;
    }

    @NonNull
    @Override
    public LrHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.leads_list,viewGroup,false);
        return new LeadReportAdapter.LrHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LrHolder lrHolder, int i) {
        lrHolder.cust_name.setText(resultset.get(i).getCustName());
        lrHolder.cust_phone.setText(resultset.get(i).getContactNumber());
        lrHolder.lead_tele.setText(resultset.get(i).getTelecaller());
        lrHolder.lead_time.setText(resultset.get(i).getAppointmentDate()+" "+resultset.get(i).getAppointmentTime());
        lrHolder.feedBackHint.setText(resultset.get(i).getFeedback());
        lrHolder.metStatus.setText(resultset.get(i).getMetStatus());
        lrHolder.lead_Status.setText(resultset.get(i).getLeadStatus());

    }

    @Override
    public int getItemCount() {
        return resultset.size();

    }

    public class LrHolder extends RecyclerView.ViewHolder{
        TextView cust_name,cust_phone,lead_tele,lead_time,feedBackHint,metStatus,lead_Status;
        Button viewAll;
        public LrHolder(@NonNull View itemView) {
            super(itemView);
            cust_name=itemView.findViewById(R.id.cust_name);
            cust_phone=itemView.findViewById(R.id.cust_phone);
            lead_tele=itemView.findViewById(R.id.lead_tele);
            lead_time=itemView.findViewById(R.id.lead_time);
            feedBackHint=itemView.findViewById(R.id.feedBackHint);
            metStatus=itemView.findViewById(R.id.metStatus);
            lead_Status=itemView.findViewById(R.id.lead_Status);
            viewAll=itemView.findViewById(R.id.viewBut);
        }
    }
}
