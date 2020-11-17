package com.example.brandlancerlead;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.brandlancerlead.brandUtility.ServerConnection;
import com.example.brandlancerlead.brandUtility.WebContentsInterface;
import com.example.brandlancerlead.model.LeadCoordinator;
import com.example.brandlancerlead.model.LeadJsonObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class FreshLeadFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView freshRecycle,futureRecycle,previousRecycle;
    private SwipeRefreshLayout refreshLayout;
    private String userLoginID,typeId;
    private boolean isRun;
    TextView featureTxt,todayTxt,previousTxt;
    LinearLayout todayLayout,featureLayout,previousLayout;
    private Context mContext;
    String screenType,leadPurposeID;



    private LeadmanagementAdapter.onItemClickEvent  clickListen = new LeadmanagementAdapter.onItemClickEvent() {
        @Override
        public void itemDataPass(LeadCoordinator coordinator,View clickView) {
            int clickId = clickView.getId();
            switch (clickId){
                case R.id.callerButton:
                    if(getActivity() instanceof MainActivity)
                        ((MainActivity) getActivity()).itemCall(coordinator.getCust_ContactNo(),coordinator.getLeadId());
                    break;
                case R.id.leadView:
                    if(getActivity() instanceof MainActivity)
                        ((MainActivity) getActivity()).itemClickData(coordinator,false);
                    break;
                case R.id.viewLog:
                    if(getActivity() instanceof MainActivity)
                        ((MainActivity) getActivity()).viewLog(coordinator,false);
                    break;
            }

        }
    };
    private LeadmanagementAdapter.onRunningItemClickEvent onRunningItemListen = new LeadmanagementAdapter.onRunningItemClickEvent() {
        @Override
        public void itemPassInfo(LeadCoordinator coordinator) {
            if(getActivity() instanceof MainActivity)
                ((MainActivity) getActivity()).itemClickData(coordinator,true);
        }
    };
    public FreshLeadFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View freshView= inflater.inflate(R.layout.fragment_fresh_lead, container, false);
        freshRecycle = freshView.findViewById(R.id.freshLeadHolder);
        futureRecycle = freshView.findViewById(R.id.futureRecycle);
        previousRecycle = freshView.findViewById(R.id.previousRecycle);
        featureTxt = freshView.findViewById(R.id.featureTxt);
        todayTxt = freshView.findViewById(R.id.todayTxt);
        previousTxt = freshView.findViewById(R.id.previousTxt);
        featureLayout = freshView.findViewById(R.id.featureLayout);
        todayLayout = freshView.findViewById(R.id.todayLayout);
        refreshLayout = freshView.findViewById(R.id.refreshLead);
        previousLayout = freshView.findViewById(R.id.previousLayout);
        freshRecycle.setLayoutManager(new LinearLayoutManager(getActivity()));
        futureRecycle.setLayoutManager(new LinearLayoutManager(getActivity()));
        previousRecycle.setLayoutManager(new LinearLayoutManager(getActivity()));
        freshRecycle.setNestedScrollingEnabled(false);
        previousRecycle.setNestedScrollingEnabled(false);
        futureRecycle.setNestedScrollingEnabled(false);

        refreshLayout.setOnRefreshListener(this);

        Bundle userArguments = getArguments();
        if(userArguments != null) {
            userLoginID = userArguments.getString("userId");
            typeId = userArguments.getString("leadTypeId");
            screenType = userArguments.getString("ScreenType");
            assert typeId != null;
            assert screenType != null;

            if (screenType.equals("Appointment")){
                leadPurposeID="2";
                featureTxt.setText("Future Appointment");
                todayTxt.setText("Today's Appointment");
                previousTxt.setText("Previous Appointment");
            }else {
                leadPurposeID="1";
            }

            if (typeId.equals("0")){
               todayTxt.setVisibility(View.VISIBLE);
               featureTxt.setVisibility(View.VISIBLE);
               futureRecycle.setVisibility(View.VISIBLE);
                previousRecycle.setVisibility(View.VISIBLE);
                previousTxt.setVisibility(View.VISIBLE);
           }else if(typeId.equals("1")){
               featureTxt.setVisibility(View.GONE);
               todayTxt.setVisibility(View.GONE);
               futureRecycle.setVisibility(View.GONE);
                previousRecycle.setVisibility(View.GONE);
                previousTxt.setVisibility(View .GONE);
           }else {
               futureRecycle.setVisibility(View.GONE);
               featureTxt.setVisibility(View.GONE);
               todayTxt.setVisibility(View.GONE);
                previousRecycle.setVisibility(View.GONE);
                previousTxt.setVisibility(View.GONE);
           }
            onRefresh();
        }
        return freshView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
       if(isVisibleToUser) {
           if(refreshLayout != null)
               refreshLayout.setRefreshing(true);
           onRefresh();
       }
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public void onRefresh() {
            if (!TextUtils.isEmpty(userLoginID)) {
                callExecutiveLead(userLoginID);
            }
    }

    private void callExecutiveLead(String userID) {
        futureRecycle.setAdapter(null);
        freshRecycle.setAdapter(null);
        previousRecycle.setAdapter(null);

        WebContentsInterface webLead = ServerConnection.createRetrofitConnection(WebContentsInterface.class);
        Call<LeadJsonObject> Leadcall = webLead.userLeadCall(userID,typeId,leadPurposeID);
        Leadcall.enqueue(new Callback<LeadJsonObject>() {
            @Override
            public void onResponse(Call<LeadJsonObject> call, Response<LeadJsonObject> response) {
                if(response.code() == 200){
                    LeadJsonObject leadsObject= response.body();
                    if(leadsObject  != null){
                        if(leadsObject.isResult()){
                            if (leadsObject.getResultset()!=null) {
                                displayLeadList(leadsObject.getResultset(), false,false);
                            }else {
                                todayLayout.setVisibility(View.GONE);
                            }
                            if (leadsObject.getFutureleads()!=null) {
                                displayLeadList(leadsObject.getFutureleads(),true,false);
                            }else {
                                featureLayout.setVisibility(View.GONE);
                            }
                            if (leadsObject.getPreviousLeads()!=null) {
                                displayLeadList(leadsObject.getPreviousLeads(),false,true);
                            }else {
                                previousLayout.setVisibility(View.GONE);
                            }
                        }else{
                            todayLayout.setVisibility(View.GONE);
                            featureLayout.setVisibility(View.GONE);
                            previousLayout.setVisibility(View.GONE);
                            refreshLayout.setRefreshing(false);
                        }
                    }else {
                        previousLayout.setVisibility(View.GONE);
                        todayLayout.setVisibility(View.GONE);
                        featureLayout.setVisibility(View.GONE);
                        refreshLayout.setRefreshing(false);
                    }
                }else{
                    previousLayout.setVisibility(View.GONE);
                    todayLayout.setVisibility(View.GONE);
                    featureLayout.setVisibility(View.GONE);
                    refreshLayout.setRefreshing(false);
                }
            }
            @Override
            public void onFailure(Call<LeadJsonObject> call, Throwable t) {
                freshRecycle.setAdapter(null);
                refreshLayout.setRefreshing(false);
                todayLayout.setVisibility(View.GONE);
                featureLayout.setVisibility(View.GONE);
                previousLayout.setVisibility(View.GONE);
                Toast.makeText(mContext,t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void displayLeadList(ArrayList<LeadCoordinator> resultset,boolean isFuture,boolean isPrevious) {
        if(resultset != null){
            if (isFuture){

                LeadmanagementAdapter leadAdapter = new LeadmanagementAdapter(clickListen,onRunningItemListen,resultset,"4");
                futureRecycle.setAdapter(leadAdapter);
            } else if(isPrevious){

                LeadmanagementAdapter leadAdapter1 = new LeadmanagementAdapter(clickListen,onRunningItemListen,resultset,"5");
                previousRecycle.setAdapter(leadAdapter1);
            }
            else {

                LeadmanagementAdapter leadAdapter2 = new LeadmanagementAdapter(clickListen,onRunningItemListen,resultset,typeId);
                freshRecycle.setAdapter(leadAdapter2);
            }

        }
        refreshLayout.setRefreshing(false);
    }

}
