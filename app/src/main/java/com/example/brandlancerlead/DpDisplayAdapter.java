package com.example.brandlancerlead;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.brandlancerlead.model.DPPendingListJsonObjects;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

public class DpDisplayAdapter extends RecyclerView.Adapter<DpDisplayAdapter.DpHolder> {
    Context context;
    ArrayList<DPPendingListJsonObjects> resultset;
    String isZeroPayment;
    private int newviewSize;

    public DpDisplayAdapter(Context context, ArrayList<DPPendingListJsonObjects> resultset, String isZeroPayment,int flast) {
        this.context = context;
        this.resultset = resultset;
        this.isZeroPayment = isZeroPayment;
        this.newviewSize = flast;
    }

    @NonNull
    @Override
    public DpHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        if(i == 0) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.dplist, viewGroup, false);
        }else{
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.dpfurtherlist, viewGroup, false);
        }

        return new DpDisplayAdapter.DpHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull DpHolder dpHolder, final int i) {
        String offer = resultset.get(i).getOfferName();
        String followupDay = resultset.get(i).getFollowupDate();
        String followuptime = resultset.get(i).getFollowupTime();
        String aging = resultset.get(i).getAgingDays()+" Days";

        dpHolder.followDay.setText(followupDay);
        dpHolder.followTime.setText(followuptime);
        dpHolder.aging_txt.setText(aging);
        if(TextUtils.isEmpty(offer)){
            dpHolder.projectName.setText(resultset.get(i).getProjectName());
        }else {
            offer = " - "+offer;
            SpannableString nameoffer = new SpannableString(offer);
            nameoffer.setSpan(new RelativeSizeSpan(0.85f), 0, nameoffer.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            nameoffer.setSpan(new ForegroundColorSpan(Color.GRAY), 0, nameoffer.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);

            dpHolder.projectName.setText(TextUtils.concat(resultset.get(i).getProjectName(),nameoffer));
        }

        if (resultset.get(i).getDPAmount()!=0){
            String dpAmount=numberWithCommas(resultset.get(i).getDPAmount());
            SpannableString amountSpan = new SpannableString(dpAmount);
            amountSpan.setSpan(new StyleSpan(Typeface.BOLD),0,dpAmount.length(),Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            dpHolder.dpamount.setText(TextUtils.concat("Amount :",amountSpan));
        }else {
            dpHolder.dpamount.setText("Amount :0.00");
        }

        if (resultset.get(i).getDPBalance()!=0){
            String dpBalance=numberWithCommas(resultset.get(i).getDPBalance());
            SpannableString balanceSpan = new SpannableString(dpBalance);
            balanceSpan.setSpan(new StyleSpan(Typeface.BOLD),0,dpBalance.length(),Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            dpHolder.dpbalance.setText(TextUtils.concat("Bal :",balanceSpan));
        }else {
            dpHolder.dpbalance.setText("Bal :0.00");
        }
        dpHolder.phone.setText(resultset.get(i).getCustContactNo());



        dpHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(context,DpAlertActivity.class);
                intent.putExtra("BookingId",resultset.get(i).getBookingID());
                intent.putExtra("PaymentType",isZeroPayment);
                context.startActivity(intent);

            }
        });

        String booking = resultset.get(i).getBookingID();
        if(TextUtils.isEmpty(booking)){
            dpHolder.fcust_name.setText(resultset.get(i).getCustName());
        }else {
            booking = " ( "+booking+" )";
            SpannableString nameBooking = new SpannableString(booking);
            nameBooking.setSpan(new RelativeSizeSpan(0.85f), 0, booking.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);

            dpHolder.fcust_name.setText(TextUtils.concat(resultset.get(i).getCustName(), nameBooking));
        }
    }

    private String numberWithCommas(Double dpAmount) {
        NumberFormat formatter = new DecimalFormat("#,###");
        String formattedNumber = formatter.format(dpAmount);
        return  formattedNumber;
    }

    @Override
    public int getItemViewType(int position) {
        return  newviewSize == position? 1:0;
    }

    @Override
    public int getItemCount() {
        return resultset.size();
    }

    public class DpHolder  extends  RecyclerView.ViewHolder{
        TextView projectName,fcust_name,dpamount,dpbalance,phone,aging_txt,followDay,followTime;
        public DpHolder(@NonNull View itemView) {
            super(itemView);
            projectName=itemView.findViewById(R.id.projectName);
            fcust_name=itemView.findViewById(R.id.fcust_name);
            dpamount=itemView.findViewById(R.id.dpamount);
            dpbalance=itemView.findViewById(R.id.dpbalance);
            phone=itemView.findViewById(R.id.phone);
            aging_txt=itemView.findViewById(R.id.aging_day);
            followDay=itemView.findViewById(R.id.followDate);
            followTime=itemView.findViewById(R.id.follow_dp_time);
        }
    }
}
