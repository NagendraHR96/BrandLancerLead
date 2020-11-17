package com.example.brandlancerlead;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.example.brandlancerlead.model.SiteObjects;

import java.util.ArrayList;

public class SiteDisplayAdapter extends RecyclerView.Adapter<SiteDisplayAdapter.SdHolder> {
    Context scontext;
    ArrayList<SiteObjects>siteObjects;

    public SiteDisplayAdapter(Context scontext, ArrayList<SiteObjects> siteObjects) {
        this.scontext = scontext;
        this.siteObjects = siteObjects;
    }

    @NonNull
    @Override
    public SdHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View nameView=LayoutInflater.from ( viewGroup.getContext () ).inflate ( R.layout.sitejson,viewGroup,false );
        return new SiteDisplayAdapter.SdHolder(nameView);
    }

    @Override
    public void onBindViewHolder(@NonNull SdHolder sdHolder, int i) {
        if ( siteObjects.get ( i ).getStatusCode ()==0){
           sdHolder.backLaypot.setBackgroundColor ( scontext.getResources ().getColor ( R.color.white ) );

        }else if (siteObjects.get ( i ).getStatusCode ()==1){
            sdHolder.backLaypot.setBackgroundColor ( scontext.getResources ().getColor ( R.color.boooked ) );

        }else if (siteObjects.get ( i ).getStatusCode ()==2){
            sdHolder.backLaypot.setBackgroundColor ( scontext.getResources ().getColor ( R.color.tempblocked ) );

        }else
        {
            sdHolder.backLaypot.setBackgroundColor ( scontext.getResources ().getColor ( R.color.companyblocked ) );
        }
        sdHolder.numberText.setText(siteObjects.get(i).getSiteNo());

    }

    @Override
    public int getItemCount() {
        return siteObjects.size ();
    }




    public class SdHolder extends RecyclerView.ViewHolder {
        CardView backLaypot;
        TextView numberText;
        public SdHolder(@NonNull View itemView) {
            super ( itemView );
            backLaypot=itemView.findViewById ( R.id.card_main);
            numberText=itemView.findViewById ( R.id.site_number);
        }
    }
}
