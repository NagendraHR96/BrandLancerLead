package com.example.brandlancerlead;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.brandlancerlead.model.GetLeadCallFeedbackJsonObjects;

import java.util.ArrayList;

public class CallDisplayAdapter extends RecyclerView.Adapter<CallDisplayAdapter.CaHolder> {
    Context context;
    ArrayList<GetLeadCallFeedbackJsonObjects> resultset;

    public CallDisplayAdapter(Context context, ArrayList<GetLeadCallFeedbackJsonObjects> resultset) {
        this.context = context;
        this.resultset = resultset;
    }

    @NonNull
    @Override
    public CaHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.call_list,viewGroup,false);
        return new CallDisplayAdapter.CaHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull CaHolder caHolder, final int i) {
        caHolder.feedback.setText(resultset.get(i).getCallFeedback());
        caHolder.status.setText(resultset.get(i).getCallStatus());
        caHolder.lead_time.setText(resultset.get(i).getCall_Date()+"  "+resultset.get(i).getTime());
        caHolder.duration.setText(resultset.get(i).getDuration()+" Sec");
        if (resultset.get(i).getBookingID()!=null){
            caHolder.boookingId.setVisibility(View.VISIBLE);
            caHolder.boookingId.setText("BID "+resultset.get(i).getBookingID());
        }else {
            caHolder.boookingId.setVisibility(View.GONE);
        }

        caHolder.play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (context instanceof CallDetailsActivity){
                  //  ( ( CallDetailsActivity)context).playAudio("http://203.192.233.36:8090/UploadAudio/9663667076-09-03-2020-12-54-12-161.wav");
                    ( ( CallDetailsActivity)context).playAudio(resultset.get(i).getAudioFile());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return resultset.size();
    }

    public class CaHolder extends RecyclerView.ViewHolder {
        TextView status,lead_time,feedback,duration,boookingId;
        ImageView play;
        public CaHolder(@NonNull View itemView) {
            super(itemView);
            status=itemView.findViewById(R.id.status);
            lead_time=itemView.findViewById(R.id.lead_time);
            feedback=itemView.findViewById(R.id.feedback);
            duration=itemView.findViewById(R.id.duration);
            boookingId=itemView.findViewById(R.id.boookingId);
            play=itemView.findViewById(R.id.play);
        }
    }
}
