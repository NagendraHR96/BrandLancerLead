package com.example.brandlancerlead;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class SummaryHeadAdapter extends RecyclerView.Adapter<SummaryHeadAdapter.SummaryHeadHolder> {
    ArrayList<String > summaryHeadList ;

    public SummaryHeadAdapter(ArrayList<String> summaryHeadList) {
        this.summaryHeadList = summaryHeadList;
    }

    @NonNull
    @Override
    public SummaryHeadHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View headView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.summary_head_row,viewGroup,false);
        return new SummaryHeadHolder(headView);
    }

    @Override
    public void onBindViewHolder(@NonNull SummaryHeadHolder headHolder, int i) {
        headHolder.summaryHead.setText(summaryHeadList.get(i));
    }

    @Override
    public int getItemCount() {
        return summaryHeadList.size();
    }


    public class SummaryHeadHolder extends RecyclerView.ViewHolder {
        TextView summaryHead;
        public SummaryHeadHolder(@NonNull View itemView) {
            super(itemView);
            summaryHead = itemView.findViewById(R.id.txt_head_summary);
        }
    }
}
