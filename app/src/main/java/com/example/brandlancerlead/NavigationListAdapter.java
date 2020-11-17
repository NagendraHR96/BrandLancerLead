package com.example.brandlancerlead;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class NavigationListAdapter extends RecyclerView.Adapter<NavigationListAdapter.NlHolder> {
    Context context;
    ArrayList<String>result;

    public NavigationListAdapter(Context context, ArrayList<String> result) {
        this.context = context;
        this.result = result;
    }

    @NonNull
    @Override
    public NlHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list,viewGroup,false);
        return new NavigationListAdapter.NlHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NlHolder nlHolder, final int i) {

        nlHolder.list_name.setText(result.get(i));
        if (i==0){
            nlHolder.line.setVisibility(View.GONE);
            nlHolder.list_name.setTextColor(context.getResources().getColor(R.color.white));
            nlHolder.itemView.setBackgroundColor(context.getResources().getColor(R.color.back));
        }if (i==1){
            nlHolder.line.setVisibility(View.GONE);

        }if (i==8){
            nlHolder.line.setVisibility(View.GONE);

            nlHolder.list_name.setTextColor(context.getResources().getColor(R.color.white));
               nlHolder.itemView.setBackgroundColor(context.getResources().getColor(R.color.back));
        }
        if (i==9){
            nlHolder.line.setVisibility(View.GONE);

        }

        nlHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (context instanceof DashBoardActivity){
                    ((DashBoardActivity)context).closeNavigation(i);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return result.size();
    }

    public class NlHolder extends RecyclerView.ViewHolder {
        TextView list_name,line;

        public NlHolder(@NonNull View itemView) {
            super(itemView);
            line=itemView.findViewById(R.id.line);
            list_name=itemView.findViewById(R.id.list_name);
        }
    }
}
