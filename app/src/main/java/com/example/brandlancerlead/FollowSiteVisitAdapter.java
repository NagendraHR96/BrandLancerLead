package com.example.brandlancerlead;


import android.content.Context;
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

public class FollowSiteVisitAdapter extends RecyclerView.Adapter<FollowSiteVisitAdapter.LeadHolder> {

    public interface onItemClickEvent{
         void itemDataPass(LeadCoordinator coordinator, View clickView);
    }

    private onItemClickEvent itemClick;
    private ArrayList<LeadCoordinator> leadList;
    private String isRunnigAdapter;
    Context context;
    private int selection = -1;


    public FollowSiteVisitAdapter(Context context,onItemClickEvent itemClick, ArrayList<LeadCoordinator> leadList, String isRunnigAdapter) {
        this.itemClick = itemClick;
        this.leadList = leadList;
        this.isRunnigAdapter = isRunnigAdapter;
        this.context=context;
    }

    @NonNull
    @Override
    public LeadHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View leadView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.follow_lead_display_row,viewGroup,false);

        return new LeadHolder(leadView);
    }

    @Override
    public void onBindViewHolder(@NonNull LeadHolder leadHolder, int i) {
        LeadCoordinator coordinator = leadList.get(i);
        String val=coordinator.getIsSiteVisitRunning();

        if (val.equals("Running")){
            //leadHolder.itemView.setBackgroundColor(context.getResources().getColor(R.color.checkedColour));
        }

        leadHolder.custName.setText(coordinator.getCust_Name());
        leadHolder.CustNumber.setText(coordinator.getCust_ContactNo());
        leadHolder.CustAddress.setText(coordinator.getCust_Address());
        leadHolder.telecall.setText(coordinator.getTelecallerName());
        leadHolder.appTime.setText(coordinator.getAppointment_Time());
        leadHolder.followDate.setText(coordinator.getAppointment_Date());
        leadHolder.feedBack.setText("Feed Back : "+coordinator.getFeedBack());

        if(TextUtils.isEmpty(coordinator.getFeedBack())){
            leadHolder.feedBack.setVisibility(View.INVISIBLE);

        }else{
            leadHolder.feedBack.setVisibility(View.VISIBLE);
        }
            leadHolder.siteVist.setText(coordinator.getSiteVisitStatus());


    }

    @Override
    public int getItemCount() {
        return leadList.size();
    }

    public class LeadHolder  extends RecyclerView.ViewHolder {

        TextView custName,CustAddress,CustNumber,telecall,appTime,feedBack,followDate,siteVist;
        Button callBtn,logBtn;

        public LeadHolder(@NonNull View itemView) {
            super(itemView);
            custName = itemView.findViewById(R.id.fcust_name);
            CustAddress = itemView.findViewById(R.id.fcust_place);
            CustNumber = itemView.findViewById(R.id.fcust_phone);
            telecall = itemView.findViewById(R.id.flead_tele);
            appTime = itemView.findViewById(R.id.flead_time);
            followDate = itemView.findViewById(R.id.flead_Date);
            siteVist = itemView.findViewById(R.id.siteVisitDone);
            logBtn = itemView.findViewById(R.id.viewLog);

            feedBack = itemView.findViewById(R.id.ffeedBackHint);

            callBtn = itemView.findViewById(R.id.fcallerButton);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    LeadCoordinator coordinator = leadList.get(pos);
                      if(isRunnigAdapter.equals("1")){
                        if(itemClick != null)
                            itemClick.itemDataPass(coordinator, v);
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
