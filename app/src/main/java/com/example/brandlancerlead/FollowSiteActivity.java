package com.example.brandlancerlead;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.brandlancerlead.brandUtility.AudioRecordService;
import com.example.brandlancerlead.brandUtility.ServerConnection;
import com.example.brandlancerlead.brandUtility.WebContentsInterface;
import com.example.brandlancerlead.model.FollowSiteJsonObject;
import com.example.brandlancerlead.model.JsonModelObject;
import com.example.brandlancerlead.model.LeadCoordinator;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FollowSiteActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    public String userId="";
    public String flagId="";
    
    RecyclerView todayView,featureView,previousView;

    SwipeRefreshLayout followRefresh ;

    LinearLayout todayLayout,featureLayout,previousLayout;

    private static final int REQUEST_CALL_PERMISSION_CODE = 352;

    private static final int CAll_Code = 357;
    private static final int FOLLOWSTARTCODE = 501;
    private static final int SITESTARTCODE = 601;
    private static final int BACKTOMAIN = 801;

    TelephonyManager callManager;

    private static boolean inCall=false;

    private static int nofCalls =0;

    private static String folderPath;
    public static String fileName = null;

    public String leadID,DailCallNimber;
    private LocationManager locManger;
    private static final int SUBMITE_Code = 346;

    LeadCoordinator leadcoordinator;



    private PhoneStateListener callListener = new PhoneStateListener(){
        @Override
        public void onCallStateChanged(int state, String phoneNumber) {
            super.onCallStateChanged(state, phoneNumber);
            if(!inCall) {
                if (state == TelephonyManager.CALL_STATE_OFFHOOK ) {
                    inCall = true;
                    if (TextUtils.isEmpty(folderPath)) {
                        File folder = new File(Environment.getExternalStorageDirectory() +
                                File.separator + ".LeadCallAudio");
                        boolean success = true;
                        if (!folder.exists()) {
                            success = folder.mkdirs();
                        }
                        if (success) {
                            folderPath = folder.getAbsolutePath();

                        }
                    }
                    String fileSuffix = new SimpleDateFormat("yyyyMMddHHmmss",new Locale("en","in")).format(new Date());
                    fileName = folderPath + "/" + DailCallNimber + "_" + fileSuffix + ".mp3";

                    Intent intent = new Intent(FollowSiteActivity.this, AudioRecordService.class);
                    intent.putExtra("FileName", fileName);
                    intent.setAction(AudioRecordService.ACTION_START_FOREGROUND_SERVICE);
                    startService(intent);
                }

            }else if( state == TelephonyManager.CALL_STATE_RINGING &&!TextUtils.isEmpty(phoneNumber) ){
                nofCalls++;

            }else if(state == TelephonyManager.CALL_STATE_OFFHOOK &&!TextUtils.isEmpty(phoneNumber)){
                nofCalls--;
            }
            if (state == TelephonyManager.CALL_STATE_IDLE ) {
                if(inCall) {
                    if(nofCalls <= 1) {

                        callManager.listen(this, PhoneStateListener.LISTEN_NONE);
                        inCall = false;
                        navigateToFeedBack(DailCallNimber,leadID);
                    }else{
                        nofCalls--;
                    }
                }
            }

        }
    };

    private void navigateToFeedBack(String dailCallNimber, String leadID) {
        Intent intent = new Intent(FollowSiteActivity.this, AudioRecordService.class);
        intent.setAction(AudioRecordService.ACTION_STOP_FOREGROUND_SERVICE);
        startService(intent);

        try {
            Thread.sleep(1000);

            Intent callFeedBack = new Intent(FollowSiteActivity.this,LeadCallFeedBackActivity.class);
            callFeedBack.putExtra("number",dailCallNimber);
            callFeedBack.putExtra("lead",leadID);
            if(!TextUtils.isEmpty(flagId)){
                if(flagId.equals("4")){
                    if(leadcoordinator.getSiteVisitCount()>0){
                        callFeedBack.putExtra("activityKey","32");

                    }else{
                        callFeedBack.putExtra("activityKey","31");

                    }
                }else{
                    if(!TextUtils.isEmpty(leadcoordinator.getIsSiteVisitRunning())&&leadcoordinator.getIsSiteVisitRunning().equals("Running")){
                        callFeedBack.putExtra("activityKey","42");

                    }else{
                        callFeedBack.putExtra("activityKey","41");

                    }
                }
            }

            callFeedBack.putExtra("audio",fileName);

            startActivityForResult(callFeedBack,CAll_Code);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    TextView todayText,featureText,previousText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_foolow_site);
        todayView = findViewById(R.id.todayFollowUps);
        featureView = findViewById(R.id.featureFollowUps);
        previousView = findViewById(R.id.previousFollowUps);
        todayText =findViewById(R.id.todayTxt);
        previousText =findViewById(R.id.previousTxt);
        featureText  =findViewById(R.id.featureTxt);
        followRefresh = findViewById(R.id.refreshFollowSite);
        todayLayout = findViewById(R.id.todayLayout);
        previousLayout = findViewById(R.id.previousLayout);
        featureLayout = findViewById(R.id.featureLayout);

        followRefresh.setOnRefreshListener(this);

        todayView.setLayoutManager(new LinearLayoutManager(this));
        featureView.setLayoutManager(new LinearLayoutManager(this));
        previousView.setLayoutManager(new LinearLayoutManager(this));
        Intent activityData = getIntent();
        if(activityData != null){

            if(activityData.getExtras() != null) {
                userId = activityData.getExtras().getString("userName");
                flagId = activityData.getExtras().getString("flag");
                if(!TextUtils.isEmpty(flagId)) {
                    if (flagId.equals("4")) {
                        todayText.setText(getResources().getString(R.string.today_follow));
                        featureText.setText(getResources().getString(R.string.feature_follow));
                        previousText.setText(getResources().getString(R.string.previous_followups));
                    } else {
                        todayText.setText(getResources().getString(R.string.today_site));
                        previousText.setText(getResources().getString(R.string.previous_site));
                        featureText.setText(getResources().getString(R.string.feature_site));
                    }
                    todayLayout.setVisibility(View.GONE);
                    featureLayout.setVisibility(View.GONE);
                    previousLayout.setVisibility(View.GONE);
                    onRefresh();
                    followRefresh.setRefreshing(true);
                }
            }
        }else{
            finish();
        }
        locManger = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

    }

    private FollowSiteVisitAdapter.onItemClickEvent  clickListen = new FollowSiteVisitAdapter.onItemClickEvent() {
        @Override
        public void itemDataPass(LeadCoordinator coordinator, View clickView) {
            int clickId = clickView.getId();
            switch (clickId){
                case R.id.fcallerButton:
                    leadcoordinator = coordinator;
                       itemCall(coordinator);
                    break;
                case R.id.fleadView:
                    RideStartDialog startDiaLog = new RideStartDialog(FollowSiteActivity.this, R.style.DialogThemeForShow, coordinator, false,true,coordinator.getIsSiteVisitRunning());
                    if (startDiaLog.getWindow() != null) {
                        startDiaLog.getWindow().setBackgroundDrawableResource(R.color.splashColor);
                        int titleDividerId = getResources().getIdentifier("titleDivider", "id", "android");
                        View titleDivider = startDiaLog.getWindow().getDecorView().findViewById(titleDividerId);
                        titleDivider.setBackgroundColor(Color.WHITE);
                    }
                    if (flagId.equals("8")) {
                        startDiaLog.setTitle("Site visit Info");
                    } else {
                        startDiaLog.setTitle("Followup Info");
                    }
                    startDiaLog.show();
                    break;

                case R.id.viewLog:
                        viewLog(coordinator,false);
                    break;

            }

        }
    };

    private void loadFollowBasedOnFlag(String userId,  String flagId) {

        WebContentsInterface webLead = ServerConnection.createRetrofitConnection(WebContentsInterface.class);
        Call<FollowSiteJsonObject> Leadcall = webLead.userFollowSiteCall(userId,flagId);
        Leadcall.enqueue(new Callback<FollowSiteJsonObject>() {
            @Override
            public void onResponse(Call<FollowSiteJsonObject> call, Response<FollowSiteJsonObject> response) {
              followRefresh.setRefreshing(false);
                if(response.code() == 200){
                    FollowSiteJsonObject leadsObject= response.body();
                    if(leadsObject  != null){
                        if(leadsObject.isResult()){
                            todayLayout.setVisibility(View.VISIBLE);
                            featureLayout.setVisibility(View.VISIBLE);
                            previousLayout.setVisibility(View.VISIBLE);
                            if (leadsObject.getCurrentFollowups()!=null) {
                                displayLeadList(leadsObject.getCurrentFollowups(), "1");
                            }else {
                                todayLayout.setVisibility(View.GONE);
                            }

                            if (leadsObject.getFutureFollowups()!=null) {
                                displayLeadList(leadsObject.getFutureFollowups(),"2");
                            }else {
                                featureLayout.setVisibility(View.GONE);
                            }
                            if (leadsObject.getPreviousFollowups()!=null){
                                displayLeadList(leadsObject.getPreviousFollowups(),"3");

                            }else {
                                previousLayout.setVisibility(View.GONE);
                            }

                        }else{
                            todayLayout.setVisibility(View.GONE);
                            featureLayout.setVisibility(View.GONE);
                            previousLayout.setVisibility(View.GONE);
                            Toast.makeText(FollowSiteActivity.this,leadsObject.getResultmessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
                }else{
                    Toast.makeText(FollowSiteActivity.this,response.message(),Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<FollowSiteJsonObject> call, Throwable t) {
                followRefresh.setRefreshing(false);
                todayLayout.setVisibility(View.GONE);
                featureLayout.setVisibility(View.GONE);
                previousLayout.setVisibility(View.GONE);
                Toast.makeText(FollowSiteActivity.this,t.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });
    }
    public void viewLog(LeadCoordinator coordinator, boolean b) {

        Intent intent=new Intent(FollowSiteActivity.this,CallDetailsActivity.class);
        intent.putExtra("ID",coordinator.getLeadId());
        intent.putExtra("Type","Lead");
        startActivity(intent);

    }

    private void displayLeadList(ArrayList<LeadCoordinator> resultset, String flagId) {

        if(resultset != null && !resultset.isEmpty()){
            FollowSiteVisitAdapter leadAdapter = new FollowSiteVisitAdapter(FollowSiteActivity.this,clickListen,resultset,flagId);
            if(flagId.equals("1")){
            todayView.setAdapter(leadAdapter);
            }
            else if(flagId.equals("3")){
                previousView.setAdapter(leadAdapter);
            }
            else{
                featureView.setAdapter(leadAdapter);
            }
        }
        else{
            if(flagId.equals("1"))
                todayLayout.setVisibility(View.GONE);
            else  if (flagId.equals("3")){
                previousLayout.setVisibility(View.GONE);
            }
            else
                featureLayout.setVisibility(View.GONE);
        }
    }

    public void itemCall(LeadCoordinator leadData) {
        if(leadData != null) {
            DailCallNimber = leadData.getCust_ContactNo();
            leadID = leadData.getLeadId();
            try {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                        != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG)
                        != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                        != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE, Manifest.permission.READ_CALL_LOG, Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO}, REQUEST_CALL_PERMISSION_CODE);
                } else {

                    if (!TextUtils.isEmpty(DailCallNimber) && DailCallNimber.length() == 10) {
                        if (callManager == null)
                            callManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

                        inCall = false;
                        nofCalls = 1;
                        callManager.listen(callListener, PhoneStateListener.LISTEN_CALL_STATE);


                        Intent my_callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:+91" + DailCallNimber));
                        startActivity(my_callIntent);
                    }


                }
            } catch (Exception ex) {
                Toast.makeText(FollowSiteActivity.this, "ex Permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
         if(REQUEST_CALL_PERMISSION_CODE == requestCode){
            itemCall(leadcoordinator);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(CAll_Code == requestCode && resultCode == RESULT_OK){
            onRefresh();
        }else if(FOLLOWSTARTCODE==requestCode && resultCode==RESULT_OK){
            Intent gotiMain=new Intent(FollowSiteActivity.this,MainActivity.class);
            gotiMain.putExtra("Types","Lead");
            startActivity(gotiMain);
        }else if(SITESTARTCODE==requestCode && resultCode==RESULT_OK && data!= null){
            Intent intent=new Intent(FollowSiteActivity.this,SiteProgressActivity.class);
          String lead =  data.getExtras().getString("mainleadId");
            intent.putExtra("leadId",lead);
            intent.putExtra("IndexId",1);
            startActivityForResult(intent,BACKTOMAIN);
        }else if(BACKTOMAIN==requestCode && resultCode==RESULT_OK){
            onRefresh();
        }else if(SUBMITE_Code==requestCode && resultCode==RESULT_OK){
            onRefresh();
        }
    }

    public void createRefixLead(String leadId) {

        leadID=leadId;

       // followRefresh.setRefreshing(true);
//        WebContentsInterface contentsInterface = ServerConnection.createRetrofitConnection(WebContentsInterface.class);
//        Call<JsonModelObject> createLeadCall = contentsInterface.createReFixLead(leadId);
//        createLeadCall.enqueue(new Callback<JsonModelObject>() {
//            @Override
//            public void onResponse(Call<JsonModelObject> call, Response<JsonModelObject> response) {
//                JsonModelObject leadJson = response.body();
//                if(leadJson != null){
//                    if(leadJson.isResult()){
//                        onRefresh();
//                    }else {
//                        Toast.makeText(FollowSiteActivity.this,leadJson.getResultmessage(),Toast.LENGTH_SHORT).show();
//                        followRefresh.setRefreshing(false);
//                    }
//                }else{
//                    followRefresh.setRefreshing(false);
//                }
//
//            }
//
//            @Override
//            public void onFailure(Call<JsonModelObject> call, Throwable t) {
//                followRefresh.setRefreshing(false);
//
//            }
//        });

        Intent main = new Intent(FollowSiteActivity.this,MainActivity.class);
        main.putExtra("lead_id",leadId);

        if (flagId.equals("4")){
            main.putExtra("parent","follow");
            startActivityForResult(main,FOLLOWSTARTCODE);

        }else if(flagId.equals("8")){
            main.putExtra("parent","visit");
            startActivityForResult(main,SITESTARTCODE);

        }

    }

    @Override
    public void onRefresh() {
        loadFollowBasedOnFlag(userId,flagId);
    }

    public void cancelSiteVisit(String leadId) {


    }

    public void moveToSubmit(String leadId, String booking_id) {
        Intent submit = new Intent(FollowSiteActivity.this,SubmitActivity.class);
        submit.putExtra("leadId",leadId);
        submit.putExtra("BookingId",booking_id);
        submit.putExtra("islead",true);
        submit.putExtra("IndexId",3);
        submit.putExtra("activityKey","43");
        startActivityForResult(submit,SUBMITE_Code);
    }
    public void inProgress(String leadId, String booking_id) {
        Intent submit = new Intent(FollowSiteActivity.this,SiteProgressActivity.class);
        submit.putExtra("leadId",leadId);
        submit.putExtra("IndexId",2);
        startActivityForResult(submit,BACKTOMAIN);
    }
}
