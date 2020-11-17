package com.example.brandlancerlead;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.brandlancerlead.model.LeadCoordinator;

public class RideStartDialog extends Dialog {
    private Context activityContext;
    private LeadCoordinator displayData;
    private boolean isRunning;
    private  boolean IsLeadPurpose;
    String IsSiteRunning;
    public RideStartDialog(Context context, int themeResId, LeadCoordinator displayData,boolean isRunning,boolean IsLeadPurpose,String IsSiteRunning) {
        super(context, themeResId);
        this.activityContext = context;
        this.displayData = displayData;
        this.isRunning = isRunning;
        this.IsLeadPurpose = IsLeadPurpose;
        this.IsSiteRunning = IsSiteRunning;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lead_overview_dialog);
        LinearLayout mainDailog = findViewById(R.id.mainView);
        String[] leadText = {displayData.getCust_Name(),displayData.getCust_ContactNo(),displayData.getCust_Address(),displayData.getAppointment_Time(),displayData.getTelecallerName()+"("+displayData.getTelecallerId()+")"};
        int[] leadDraw = {R.drawable.ic_person_24dp,R.drawable.ic_phone_24dp,R.drawable.ic_place_24dp,R.drawable.ic_time_24dp,R.drawable.ic_headset_24dp};
        LinearLayout.LayoutParams textParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textParam.setMargins(0,6,0,0);
        for(int i =0;i<leadText.length;i++){
            TextView txtView = new TextView(activityContext);
            txtView.setText(leadText[i]);
            txtView.setTextSize(18f);
            txtView.setPadding(10,5,10,5);
            txtView.setCompoundDrawablesWithIntrinsicBounds(leadDraw[i],0,0,0);
            txtView.setLayoutParams(textParam);
            mainDailog.addView(txtView,textParam);

        }
        LinearLayout horiz = new LinearLayout(activityContext);
        horiz.setOrientation(LinearLayout.VERTICAL);
        horiz.setLayoutParams(textParam);

        if(isRunning) {
            Button preLevel = new Button(activityContext);
            preLevel.setText("Hold");
            preLevel.setTextSize(18f);
            preLevel.setAllCaps(false);
            preLevel.setBackgroundColor(Color.WHITE);
            preLevel.setTextColor(Color.BLUE);
            preLevel.setLayoutParams(textParam);
            horiz.addView(preLevel, textParam);

            Button cancelLead = new Button(activityContext);
            cancelLead.setText("Cancel lead");
            cancelLead.setBackgroundColor(Color.GRAY);
            cancelLead.setTextColor(Color.MAGENTA);
            cancelLead.setLayoutParams(textParam);
            horiz.addView(cancelLead, textParam);


            if(displayData.isHoldFlag()){
                preLevel.setText("UnHold");
            }else {
                Button nextlevel = new Button(activityContext);
                nextlevel.setText("End Trip");
                nextlevel.setTextSize(18f);
                nextlevel.setAllCaps(false);
                nextlevel.setBackgroundColor(activityContext.getResources().getColor(R.color.colorAccent));
                nextlevel.setTextColor(Color.GREEN);
                nextlevel.setLayoutParams(textParam);

                nextlevel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(activityContext instanceof MainActivity) {
                            if (IsLeadPurpose){
                                dismiss();
                                ((MainActivity) activityContext).moveToSubmit(displayData);
                            }else {
                                dismiss();
                                ((MainActivity) activityContext).moveToSubmitAppointment(displayData.getLeadId(),displayData.getBooking_ID());
                            }
                        }
                    }
                });

                horiz.addView(nextlevel, textParam);
            }
            preLevel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(activityContext instanceof MainActivity) {
                        dismiss();
                        ((MainActivity) activityContext).holdAndUnload(displayData.getLeadId(),displayData.isHoldFlag());
                    }
                }
            });

        }
        else if(activityContext instanceof MainActivity){
            Button postive = new Button(activityContext);
            postive.setText("Start Trip");
            postive.setBackgroundColor(Color.WHITE);
            postive.setTextColor(Color.GREEN);

            postive.setLayoutParams(textParam);
            horiz.addView(postive, textParam);

            Button cancelLead = new Button(activityContext);
            cancelLead.setText("Cancel lead");
            cancelLead.setBackgroundColor(Color.GRAY);
            cancelLead.setTextColor(Color.MAGENTA);
            cancelLead.setLayoutParams(textParam);
            horiz.addView(cancelLead, textParam);


            Button negative = new Button(activityContext);
            negative.setText("Cancel");
            negative.setBackgroundColor(Color.GRAY);
            negative.setTextColor(Color.RED);
            negative.setLayoutParams(textParam);
            horiz.addView(negative, textParam);


            postive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (activityContext instanceof MainActivity) {
                        dismiss();
                        ((MainActivity) activityContext).startingLocation(displayData.getLeadId());
                    }

                }
            });
            negative.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
        }
        else  if(activityContext instanceof FollowSiteActivity){

            if (IsSiteRunning.equals("Not Started")) {
                Button postive = new Button(activityContext);
                postive.setText("Start Trip");
                postive.setBackgroundColor(Color.WHITE);
                postive.setTextColor(Color.GREEN);
                postive.setLayoutParams(textParam);
                horiz.addView(postive, textParam);

                Button cancelSite = new Button(activityContext);
                cancelSite.setText("Cancel Site Visit");
                cancelSite.setBackgroundColor(Color.WHITE);
                cancelSite.setTextColor(Color.GREEN);
                cancelSite.setLayoutParams(textParam);
                horiz.addView(cancelSite, textParam);

                Button negative = new Button(activityContext);
                negative.setText("Cancel");
                negative.setBackgroundColor(Color.GRAY);
                negative.setTextColor(Color.RED);
                negative.setLayoutParams(textParam);
                horiz.addView(negative, textParam);


                postive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (activityContext instanceof FollowSiteActivity) {
                            dismiss();
                            ((FollowSiteActivity) activityContext).createRefixLead(displayData.getLeadId());
                        }
                    }
                });

                cancelSite.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (activityContext instanceof FollowSiteActivity) {
                            dismiss();
                            ((FollowSiteActivity) activityContext).cancelSiteVisit(displayData.getLeadId());
                        }
                    }
                });
                negative.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dismiss();
                    }
                });
            }else if(IsSiteRunning.equals("Running")) {
                Button postive = new Button(activityContext);
                postive.setText("In Project");
                postive.setBackgroundColor(Color.WHITE);
                postive.setTextColor(Color.GREEN);
                postive.setLayoutParams(textParam);
                horiz.addView(postive, textParam);

                Button endTrip = new Button(activityContext);
                endTrip.setText("End Trip");
                endTrip.setBackgroundColor(Color.WHITE);
                endTrip.setTextColor(Color.GREEN);
                endTrip.setLayoutParams(textParam);
                horiz.addView(endTrip, textParam);

                Button negative = new Button(activityContext);
                negative.setText("Cancel");
                negative.setBackgroundColor(Color.GRAY);
                negative.setTextColor(Color.RED);
                negative.setLayoutParams(textParam);
                horiz.addView(negative, textParam);


                postive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dismiss();
                        ((FollowSiteActivity) activityContext).inProgress(displayData.getLeadId(),displayData.getBooking_ID());
                    }
                });
                endTrip.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dismiss();
                        ((FollowSiteActivity) activityContext).moveToSubmit(displayData.getLeadId(),displayData.getBooking_ID());
                    }
                });

                negative.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dismiss();
                    }
                });
            }else {
                Button postive = new Button(activityContext);
                postive.setText("Start Trip");
                postive.setBackgroundColor(Color.WHITE);
                postive.setTextColor(Color.GREEN);
                postive.setLayoutParams(textParam);
                horiz.addView(postive, textParam);

                Button negative = new Button(activityContext);
                negative.setText("Cancel");
                negative.setBackgroundColor(Color.GRAY);
                negative.setTextColor(Color.RED);
                negative.setLayoutParams(textParam);
                horiz.addView(negative, textParam);


                postive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (activityContext instanceof FollowSiteActivity) {
                            dismiss();
                            ((FollowSiteActivity) activityContext).createRefixLead(displayData.getLeadId());
                        }
                    }
                });
                negative.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dismiss();
                    }
                });
            }

        }
        mainDailog.addView(horiz, textParam);

    }
}
