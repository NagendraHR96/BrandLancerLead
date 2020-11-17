package com.example.brandlancerlead;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.brandlancerlead.brandUtility.ServerConnection;
import com.example.brandlancerlead.brandUtility.WebContentsInterface;
import com.example.brandlancerlead.model.DPPendingListJsonObjects;
import com.example.brandlancerlead.model.DPPendingListJsonResult;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DpPendingActivity extends AppCompatActivity {

    RecyclerView dplist_recyclerView;
    private Dialog progressOnLoad;
    Button SourceClicked,itemClicked;
    String type;
    String val;
    public static boolean isRefresh;
    boolean isFollow;
    LinearLayout isFollowVisible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dp_pending);
        dplist_recyclerView=findViewById(R.id.dplist_recyclerView);
        SourceClicked=findViewById(R.id.SourceClicked);
        itemClicked=findViewById(R.id.itemClicked);
        isFollowVisible=findViewById(R.id.isFollowVisible);

        LinearLayoutManager manager=new LinearLayoutManager(DpPendingActivity.this);
        dplist_recyclerView.setLayoutManager(manager);
        progressOnLoad = new Dialog(this);

        progressOnLoad.setContentView(new ProgressBar(this));

        if( progressOnLoad.getWindow() != null)
            progressOnLoad.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        progressOnLoad.show();

        Intent intent=getIntent();
        if (intent!=null){
            Bundle bundle=intent.getExtras();
            if (bundle!=null){
                 val=bundle.getString("userName");
                 type=bundle.getString("Type");

                if (type.equals("DpPending")){
                    isFollowVisible.setVisibility(View.VISIBLE);

                    bindDpDetails(val,false);
                    SourceClicked.setText("DP Pending");
                    itemClicked.setText("FollowUp");
                }else if(type.equals("ZeroPayment")){
                    isFollowVisible.setVisibility(View.VISIBLE);
                    bindZeroDetails(val,false);
                    SourceClicked.setText("Zero Payment");
                    itemClicked.setText("FollowUp");
                }else if(type.equals("DpClosed")){
                    SourceClicked.setText("Dp Closed");
                    itemClicked.setText(" FollowUp");
                    isFollowVisible.setVisibility(View.GONE);
                    bindDpClosedDetails(val,false);
                }
            }
        }

        SourceClicked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isFollow = false;
                if (type.equals("DpPending")){
                    progressOnLoad.show();

                    bindDpDetails(val,false);
                    SourceClicked.setBackgroundColor(getResources().getColor(R.color.checkedColour));
                    itemClicked.setBackgroundColor(getResources().getColor(R.color.gold));

                }else if(type.equals("ZeroPayment")){
                    progressOnLoad.show();
                    SourceClicked.setBackgroundColor(getResources().getColor(R.color.checkedColour));
                    itemClicked.setBackgroundColor(getResources().getColor(R.color.gold));
                    bindZeroDetails(val,false);

                }

            }
        });
        itemClicked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isFollow = true;
                if (type.equals("DpPending")){
                    progressOnLoad.show();
                    SourceClicked.setBackgroundColor(getResources().getColor(R.color.gold));
                    itemClicked.setBackgroundColor(getResources().getColor(R.color.checkedColour));
                    bindDpDetails(val,true);

                }else if(type.equals("ZeroPayment")){
                    progressOnLoad.show();
                    SourceClicked.setBackgroundColor(getResources().getColor(R.color.gold));
                    itemClicked.setBackgroundColor(getResources().getColor(R.color.checkedColour));
                    bindZeroDetails(val,true);

                }
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if(isRefresh)
            if(isFollow && itemClicked != null )
                itemClicked.performClick();
            else if(SourceClicked != null)
                SourceClicked.performClick();
            isRefresh = false;
    }

    private void bindDpClosedDetails(String val, boolean IsFollow) {
        dplist_recyclerView.setAdapter(null);

        WebContentsInterface webContentsInterface= ServerConnection.createRetrofitConnection(WebContentsInterface.class);
        Call<DPPendingListJsonResult>listJsonResultCall=webContentsInterface.bindDpClosedPayments(val,IsFollow);
        listJsonResultCall.enqueue(new Callback<DPPendingListJsonResult>() {
            @Override
            public void onResponse(Call<DPPendingListJsonResult> call, Response<DPPendingListJsonResult> response) {
                if (response.code()==200){
                    DPPendingListJsonResult jsonResult=response.body();
                    if (jsonResult!=null && jsonResult.isResult() && jsonResult.getResultset()!=null){
                        diplayDpClosedDetaisl(jsonResult.getResultset());
                    }else {
                        progressOnLoad.dismiss();
                        //progressbar.setVisibility(View.GONE);
                        Toast.makeText(DpPendingActivity.this,jsonResult.getResultmessage(),Toast.LENGTH_SHORT).show();
                    }
                }else {
                    progressOnLoad.dismiss();
                    Toast.makeText(DpPendingActivity.this,response.message(),Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<DPPendingListJsonResult> call, Throwable t) {
                progressOnLoad.dismiss();
                Toast.makeText(DpPendingActivity.this,t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void bindDpDetails(String userName,boolean isfollow) {
        //VSAN-BNG-002896
        dplist_recyclerView.setAdapter(null);

        WebContentsInterface webContentsInterface= ServerConnection.createRetrofitConnection(WebContentsInterface.class);
        Call<DPPendingListJsonResult>listJsonResultCall=webContentsInterface.bindDpPending(userName,isfollow);
        listJsonResultCall.enqueue(new Callback<DPPendingListJsonResult>() {
            @Override
            public void onResponse(Call<DPPendingListJsonResult> call, Response<DPPendingListJsonResult> response) {
                if (response.code()==200){
                    DPPendingListJsonResult jsonResult=response.body();
                    if (jsonResult!=null && jsonResult.isResult() && jsonResult.getResultset()!=null){
                        diplayDpPendingDetaisl(jsonResult.getResultset(),jsonResult.getFurtherresultset());
                    }else {
                        progressOnLoad.dismiss();

                        Toast.makeText(DpPendingActivity.this,jsonResult.getResultmessage(),Toast.LENGTH_SHORT).show();
                    }
                }else {
                    progressOnLoad.dismiss();

                    Toast.makeText(DpPendingActivity.this,response.message(),Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<DPPendingListJsonResult> call, Throwable t) {
                progressOnLoad.dismiss();

                Toast.makeText(DpPendingActivity.this,t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });



    }

    private void bindZeroDetails(String userName,boolean isFollow) {
        //VSAN-BNG-002896
        dplist_recyclerView.setAdapter(null);
        WebContentsInterface webContentsInterface= ServerConnection.createRetrofitConnection(WebContentsInterface.class);
        Call<DPPendingListJsonResult>listJsonResultCall=webContentsInterface.bindZeroPayments(userName,isFollow);
        listJsonResultCall.enqueue(new Callback<DPPendingListJsonResult>() {
            @Override
            public void onResponse(Call<DPPendingListJsonResult> call, Response<DPPendingListJsonResult> response) {
                if (response.code()==200){
                    DPPendingListJsonResult jsonResult=response.body();
                    if (jsonResult!=null && jsonResult.isResult() && jsonResult.getResultset()!=null){
                        diplayZeroPaymentDetaisl(jsonResult.getResultset());
                    }else {
                        progressOnLoad.dismiss();

                        Toast.makeText(DpPendingActivity.this,jsonResult.getResultmessage(),Toast.LENGTH_SHORT).show();
                    }
                }else {
                    progressOnLoad.dismiss();

                    Toast.makeText(DpPendingActivity.this,response.message(),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DPPendingListJsonResult> call, Throwable t) {
                progressOnLoad.dismiss();

                Toast.makeText(DpPendingActivity.this,t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void diplayZeroPaymentDetaisl(ArrayList<DPPendingListJsonObjects> resultset) {
        progressOnLoad.dismiss();
        DpDisplayAdapter adapter=new DpDisplayAdapter(DpPendingActivity.this,resultset,"ZeroPayment",-1);
        dplist_recyclerView.setAdapter(adapter);
    }

    private void diplayDpPendingDetaisl(ArrayList<DPPendingListJsonObjects> resultset,ArrayList<DPPendingListJsonObjects> fesultset) {
        progressOnLoad.dismiss();
        int dividerSize = resultset.size();
        if(fesultset != null )
            resultset.addAll(fesultset);
        DpDisplayAdapter adapter=new DpDisplayAdapter(DpPendingActivity.this,resultset,"DpPayment",dividerSize);
        dplist_recyclerView.setAdapter(adapter);
    }
    private void diplayDpClosedDetaisl(ArrayList<DPPendingListJsonObjects> resultset) {
        progressOnLoad.dismiss();
        DpDisplayAdapter adapter=new DpDisplayAdapter(DpPendingActivity.this,resultset,"DpClosed",-1);
        dplist_recyclerView.setAdapter(adapter);
    }


}
