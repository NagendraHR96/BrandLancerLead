package com.example.brandlancerlead;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.brandlancerlead.model.LeadCoordinator;

import java.util.ArrayList;

public class LeadmanagementAdapter extends RecyclerView.Adapter<LeadmanagementAdapter.LeadHolder> {

    public interface onItemClickEvent{
         void itemDataPass(LeadCoordinator coordinator,View clickView);
    }
    public interface onRunningItemClickEvent {
        void itemPassInfo(LeadCoordinator leadid);
    }
    private onItemClickEvent itemClick;
    private ArrayList<LeadCoordinator> leadList;
    private String isRunnigAdapter;
    private  onRunningItemClickEvent runningItemClick;
    private int selected = -1;
    public LeadmanagementAdapter(onItemClickEvent itemClick,onRunningItemClickEvent runningItemClick, ArrayList<LeadCoordinator> leadList,String isRunnigAdapter) {
        this.itemClick = itemClick;
        this.runningItemClick = runningItemClick;
        this.leadList = leadList;
        this.isRunnigAdapter = isRunnigAdapter;
    }

    @NonNull
    @Override
    public LeadHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View leadView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.lead_display_row,viewGroup,false);

        return new LeadHolder(leadView);
    }

    @Override
    public void onBindViewHolder(@NonNull LeadHolder leadHolder, int i) {
        LeadCoordinator coordinator = leadList.get(i);

        leadHolder.custName.setText(coordinator.getCust_Name());
        leadHolder.CustNumber.setText(coordinator.getCust_ContactNo());
        leadHolder.CustAddress.setText(coordinator.getCust_Address());
        leadHolder.telecall.setText(coordinator.getTelecallerName());
        leadHolder.appTime.setText(coordinator.getAppointment_Date()+"  "+coordinator.getAppointment_Time());
        leadHolder.feedBack.setText("Feed Back : "+coordinator.getFeedBack());
        String isRefix = "True";
        if(isRefix.equalsIgnoreCase(coordinator.getIsRefix())) {
            leadHolder.leadRefix.setText("ReFix");
        }else{
            leadHolder.leadRefix.setText("Fresh");
        }
        if(coordinator.isHoldFlag() && isRunnigAdapter.equals("1")){
            leadHolder.hold.setText("Onhold");
        }
        if(TextUtils.isEmpty(coordinator.getFeedBack())){
            leadHolder.feedBack.setVisibility(View.GONE);

        }else{
            leadHolder.feedBack.setVisibility(View.VISIBLE);
        }



    }

    @Override
    public int getItemCount() {
        return leadList.size();
    }

    public class LeadHolder  extends RecyclerView.ViewHolder {

        TextView custName,CustAddress,CustNumber,telecall,appTime,leadRefix,hold,feedBack;
        Button callBtn,logBtn;

        public LeadHolder(@NonNull View itemView) {
            super(itemView);
            custName = itemView.findViewById(R.id.cust_name);
            CustAddress = itemView.findViewById(R.id.cust_place);
            CustNumber = itemView.findViewById(R.id.cust_phone);
            telecall = itemView.findViewById(R.id.lead_tele);
            appTime = itemView.findViewById(R.id.lead_time);
            leadRefix = itemView.findViewById(R.id.leadType);
            feedBack = itemView.findViewById(R.id.feedBackHint);
            hold = itemView.findViewById(R.id.leadHold);
            callBtn = itemView.findViewById(R.id.callerButton);
            logBtn = itemView.findViewById(R.id.viewLog);
           if(isRunnigAdapter.equals("2")){
               callBtn.setVisibility(View.GONE);
           }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    LeadCoordinator coordinator = leadList.get(pos);
                    if(isRunnigAdapter.equals("1")){
                       if(runningItemClick != null)
                           runningItemClick.itemPassInfo(coordinator);
                    }else  if(isRunnigAdapter.equals("0")){

                        if(itemClick != null)
                            itemClick.itemDataPass(coordinator, v);
                    }else  if(isRunnigAdapter.equals("2")){

                    }else {

                    }
                }
            });

            callBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    LeadCoordinator coordinator = leadList.get(pos);
                    if(itemClick !=null)
                        itemClick.itemDataPass(coordinator,v);
                }
            });
            logBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    LeadCoordinator coordinator = leadList.get(pos);
                    if(itemClick !=null)
                        itemClick.itemDataPass(coordinator,v);
                }
            });

        }
    }
}
