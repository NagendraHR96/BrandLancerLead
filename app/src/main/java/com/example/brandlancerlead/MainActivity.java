package com.example.brandlancerlead;

import android.Manifest;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.brandlancerlead.brandUtility.AudioRecordService;
import com.example.brandlancerlead.brandUtility.LeadDataManagement;
import com.example.brandlancerlead.brandUtility.SendDataFromLocalToServer;
import com.example.brandlancerlead.brandUtility.ServerConnection;
import com.example.brandlancerlead.brandUtility.WebContentsInterface;
import com.example.brandlancerlead.model.FromPlaceUpdate;
import com.example.brandlancerlead.model.HoldUnHold;
import com.example.brandlancerlead.model.JsonModelObject;
import com.example.brandlancerlead.model.LeadCoordinator;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomitemsView;
    private ActionBar toolBar;
    private static String userID;
    private static final String FileName = "ExecutiveLogin";
    private static final String UserName = "ExecutiveId";
    private static final String LATITUDE = "Latitude";
    private static final String LONGITUDE = "Longitude";
    private static final String  IsLogin = "Remember";
    private FreshLeadFragment freshLead, runfreshLead,completeLead;
    private LocationManager locManger;
    public String leadID,DailCallNimber;
    private static final int REQUEST_LOCATION_PERMISSION_CODE = 341;
    private static final int REQUEST_CALL_PERMISSION_CODE = 342;
    private static final int SUBMITE_Code = 346;
    private static final int CAll_Code = 347;
    private Dialog progress;
    private boolean isOnRun;
    Fragment active;
    String type,fromParent;

    LeadCoordinator coordinator;
    TelephonyManager callManager;

    private static boolean inCall=false;
    private static int nofCalls =0;

    private static String folderPath;
    public static String fileName = null;
    NavigationView reportNavigator;

    boolean IsLeadPurpose;


    LeadDataManagement dataManagement;

    private BroadcastReceiver uiUpDate = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(active != null)
                active.setUserVisibleHint(true);
        }
    };

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

                    Intent intent = new Intent(MainActivity.this, AudioRecordService.class);
                    intent.putExtra("FileName", fileName);
                    intent.setAction(AudioRecordService.ACTION_START_FOREGROUND_SERVICE);
                    ContextCompat.startForegroundService(MainActivity.this,intent);
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





    private void navigateToFeedBack(String dailCallNimber, String leadid) {
        Intent intent = new Intent(MainActivity.this, AudioRecordService.class);
        intent.setAction(AudioRecordService.ACTION_STOP_FOREGROUND_SERVICE);
       ContextCompat.startForegroundService(MainActivity.this,intent);
        try {
            Thread.sleep(1000);
            Intent callFeedBack = new Intent(MainActivity.this,LeadCallFeedBackActivity.class);
            callFeedBack.putExtra("number",dailCallNimber);
            callFeedBack.putExtra("lead",leadid);
            callFeedBack.putExtra("audio",fileName);
            if(active == freshLead){
                if(!TextUtils.isEmpty(type) && type.equals("Appointment")){
                    callFeedBack.putExtra("activityKey","51");

                }else{
                    callFeedBack.putExtra("activityKey","11");

                }

            }else if(active == runfreshLead){
                if(!TextUtils.isEmpty(type) && type.equals("Appointment")){
                    callFeedBack.putExtra("activityKey","51");

                }else {
                    callFeedBack.putExtra("activityKey", "21");
                }
            }
            startActivityForResult(callFeedBack,CAll_Code);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomitemsView = findViewById(R.id.leadNavigation);
        reportNavigator = findViewById(R.id.reportNavigator);

        toolBar = getSupportActionBar();
        bottomitemsView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        bottomitemsView.setEnabled(false);
        toolBar.setTitle("Fresh Lead");
        SharedPreferences preferencesc = getSharedPreferences(FileName, MODE_PRIVATE);
        userID = preferencesc.getString(UserName, null);

        locManger = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        callManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

        dataManagement =  new LeadDataManagement(this);
      IntentFilter filter =  new IntentFilter();
      filter.addAction("com.neory.main.uirefresh");
        LocalBroadcastManager.getInstance(this).registerReceiver(uiUpDate,filter);

        Intent intent=getIntent();
        if (intent!=null){
            Bundle bundle=intent.getExtras();
            if (bundle!=null){
                leadID = bundle.getString("lead_id");
                fromParent = bundle.getString("parent");


                if(TextUtils.isEmpty(leadID)) {
                    type = bundle.getString("Types");
                    if (type.equals("Appointment")) {
                        if (!TextUtils.isEmpty(userID)) {
                            loadFragments();
                            bottomitemsView.setSelectedItemId(R.id.freshLeadNavi);
                        }
                        IsLeadPurpose = false;
                        Menu menu = bottomitemsView.getMenu();

                        MenuItem item = menu.getItem(2);
                        item.setVisible(false);
                    }  else {
                        if (!TextUtils.isEmpty(userID)) {
                            loadFragments();
                            bottomitemsView.setSelectedItemId(R.id.freshLeadNavi);
                        }
                        IsLeadPurpose = true;

                    }
                }else{
                    startingLocation(leadID);
                }
            }
        }

    }

    private void loadFollowUpFragment() {

    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            int itemId = item.getItemId();
            switch (itemId) {
                case R.id.freshLeadNavi:
                    if (!TextUtils.isEmpty(type) && type.equalsIgnoreCase("Appointment")){
                        toolBar.setTitle(" Fresh Appointments");

                    }else {
                        toolBar.setTitle("Fresh Leads");

                    }
                    isOnRun = false;
                    if (active != null) {


                        getSupportFragmentManager().beginTransaction().hide(active).show(freshLead).commit();
                        active = freshLead;
                        active.setUserVisibleHint(true);
                    }
                    item.setChecked(true);

                    break;
                case R.id.runLeadNavi:
                    if (!TextUtils.isEmpty(type) && type.equalsIgnoreCase("Appointment")){
                        toolBar.setTitle(" Running Appointments");

                    }else {
                        toolBar.setTitle("Running Leads");


                    }
                    if (active != null) {

                        getSupportFragmentManager().beginTransaction().hide(active).show(runfreshLead).commit();
                        active = runfreshLead;
                        active.setUserVisibleHint(true);

                    }
                    isOnRun = true;

                    item.setChecked(true);

                    break;
                case R.id.finishLeadNavi:
                    if (!TextUtils.isEmpty(type) && type.equalsIgnoreCase("Appointment")){
                        toolBar.setTitle(" Finished Appointments");

                    }else {
                        toolBar.setTitle("Finished Leads");

                    }
                    if (active != null) {

                        getSupportFragmentManager().beginTransaction().hide(active).show(completeLead).commit();
                        active = completeLead;
                        active.setUserVisibleHint(true);

                    }
                    item.setChecked(true);

                    break;
            }
            return false;
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if(active != null && active.equals(freshLead))
            freshLead.onRefresh();
    }

    private void loadFragments() {

        completeLead = new FreshLeadFragment();
        Bundle comBundle = new Bundle();
        comBundle.putString("userId", userID);
        comBundle.putString("leadTypeId", "2");
        comBundle.putString("ScreenType",type);
        completeLead.setArguments(comBundle);

        runfreshLead = new FreshLeadFragment();
        Bundle userBundle = new Bundle();
        userBundle.putString("userId", userID);
        userBundle.putString("ScreenType",type);
        userBundle.putString("leadTypeId", "1");
        runfreshLead.setArguments(userBundle);

        freshLead = new FreshLeadFragment();
        Bundle fuserBundle = new Bundle();
        fuserBundle.putString("userId", userID);
        fuserBundle.putString("ScreenType",type);
        fuserBundle.putString("leadTypeId", "0");
        freshLead.setArguments(fuserBundle);

        active = freshLead;
        getSupportFragmentManager().beginTransaction()
                .add(R.id.loadingList, completeLead,"3").hide(completeLead).commit();
        getSupportFragmentManager().beginTransaction()
           .add(R.id.loadingList, runfreshLead,"2").hide(runfreshLead).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.loadingList,freshLead,"1").commit();

        bottomitemsView.setEnabled(true);
    }


    public void itemClickData(LeadCoordinator leadCoordinator, boolean isrunningOne) {

        RideStartDialog startDiaLog = new RideStartDialog(MainActivity.this, R.style.DialogThemeForShow, leadCoordinator, isrunningOne,IsLeadPurpose,"");
        if (startDiaLog.getWindow() != null) {
            startDiaLog.getWindow().setBackgroundDrawableResource(R.color.splashColor);
            int titleDividerId = getResources().getIdentifier("titleDivider", "id", "android");
            View titleDivider = startDiaLog.getWindow().getDecorView().findViewById(titleDividerId);
            titleDivider.setBackgroundColor(Color.WHITE);
        }
        if (isrunningOne) {
            startDiaLog.setTitle("LeadProcess Info");
        } else {
            startDiaLog.setTitle("LeadDetails Info");
        }
        startDiaLog.show();


    }

    public void startingLocation(String LeadId) {
        leadID = LeadId;
        showLoader();
        //servercall me
        WebContentsInterface contentsInterface=ServerConnection.createRetrofitConnection(WebContentsInterface.class);
        Call<JsonModelObject>jsonModelObjectCall=contentsInterface.checkHoldlead(userID);
        jsonModelObjectCall.enqueue(new Callback<JsonModelObject>() {
            @Override
            public void onResponse(Call<JsonModelObject> call, Response<JsonModelObject> response) {
                if (response.code()==200){
                    final JsonModelObject jsonModelObject=response.body();
                    if (jsonModelObject!=null && jsonModelObject.isResult()){
                       runOnUiThread(new Runnable() {
                           @Override
                           public void run() {
//                               if(fromParent.equals("visit")){
//                                moveBack(RESULT_OK,leadID);
//                               }else
                                   requestUserPermissions();
                           }
                       });
                    }else {
                        if (progress!=null && progress.isShowing()){
                            progress.dismiss();
                        }
                        Toast.makeText(MainActivity.this,jsonModelObject.getResultmessage(),Toast.LENGTH_SHORT).show();
                       moveBack(RESULT_CANCELED,"");
                    }
                }else {
                    if (progress!=null && progress.isShowing()){
                        progress.dismiss();

                    }
                    Toast.makeText(MainActivity.this,response.message(),Toast.LENGTH_SHORT).show();

                    moveBack(RESULT_CANCELED,"");
                }
            }

            @Override
            public void onFailure(Call<JsonModelObject> call, Throwable t) {
                if (progress!=null && progress.isShowing()){
                    progress.dismiss();
                }
                Toast.makeText(MainActivity.this,t.getMessage(),Toast.LENGTH_SHORT).show();
                moveBack(RESULT_CANCELED,"");

            }
        });

    }

    private void moveBack(int resultType,String leadID) {
        if(TextUtils.isEmpty(type)){
        Intent back = new Intent();
       back.putExtra("mainleadId",leadID);
        setResult(resultType,back);
        finish();
        }
    }

    private void requestUserPermissions() {
        if (locManger == null)
            locManger = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION_CODE);

        } else if (locManger != null && !locManger.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            AlertDialog.Builder gpsBuilder = new AlertDialog.Builder(MainActivity.this);
            gpsBuilder.setCancelable(false);
            gpsBuilder.setIcon(android.R.drawable.ic_menu_mylocation);
            gpsBuilder.setTitle(getResources().getString(R.string.gps_title));
            gpsBuilder.setMessage(getResources().getString(R.string.gps_message));
            gpsBuilder.setPositiveButton(getResources().getString(R.string.positive_ok), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    Intent gpsEnable = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(gpsEnable);
                    dialog.dismiss();

                }
            });

            gpsBuilder.show();

        } else {
            requestLocations();
        }
    }

    private void requestLocations() {
        //showLoader();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            if (progress != null &&progress.isShowing())
                progress.dismiss();
            Toast.makeText(MainActivity.this,"RequestPermissions",Toast.LENGTH_SHORT).show();
            return;
        }

        locManger.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 1, StartLocation);
    }
    private void showLoader(){
        if (progress == null) {
            progress = new Dialog(MainActivity.this);
            progress.setCancelable(false);
            progress.setContentView(new ProgressBar(MainActivity.this));
            if (progress.getWindow() != null)
                progress.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }
        if (!progress.isShowing())
            progress.show();
    }
    private LocationListener StartLocation = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {


            if(location != null) {
                long time = location.getTime();
                Date dayDate =new Date(time);
                SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
                String timeTxt = dateFormat.format(dayDate);
                if(!TextUtils.isEmpty(fromParent) && fromParent.equals("visit")){

                    addLatiLongiToShared(location);
                    moveBack(RESULT_OK,leadID);
                }else{
                    addLatiLongiToShared(location);
                    if(dataManagement != null){
                        dataManagement.insertStartData(leadID,location.getLatitude(),location.getLongitude(),timeTxt);
                    }
                  //  sendToGetAddress(location);

                    Intent intent = new Intent(MainActivity.this, SendDataFromLocalToServer.class);
                    intent.putExtra("leadId", leadID);
                    intent.setAction(SendDataFromLocalToServer.ACTION_START_FOREGROUND_SERVICE);
                    ContextCompat.startForegroundService(MainActivity.this,intent);
                }

                if (locManger != null) {
                    locManger.removeUpdates(StartLocation);
                }
                if (progress != null &&progress.isShowing())
                    progress.dismiss();
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            if(!LocationManager.GPS_PROVIDER.equalsIgnoreCase(provider)){
                if(locManger !=null){
                    locManger.removeUpdates(StartLocation);
                }
                Toast.makeText(MainActivity.this,"Enable GPS",Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {
            if(!LocationManager.GPS_PROVIDER.equalsIgnoreCase(provider)){
                if(locManger !=null){
                    locManger.removeUpdates(StartLocation);
                }
                Toast.makeText(MainActivity.this,"Enable GPS",Toast.LENGTH_SHORT).show();
            }
        }
    };

    private void sendToGetAddress(Location location) {

        try {
            Geocoder earthGeo = new Geocoder(MainActivity.this,new Locale("en","in"));
            List<Address> earthAddress = earthGeo.getFromLocation(location.getLatitude(),location.getLongitude(),3);
            if(earthAddress != null && !earthAddress.isEmpty() && !TextUtils.isEmpty(leadID)){

                FromPlaceUpdate placeUpdate = new FromPlaceUpdate(leadID,earthAddress.get(0).getAddressLine(0),userID);
                Toast.makeText(MainActivity.this,earthAddress.get(0).getSubLocality(),Toast.LENGTH_SHORT).show();
                sendLocationUpDateToServer(placeUpdate);

            }else if(progress != null && progress.isShowing()) {
                progress.dismiss();
                moveBack(RESULT_CANCELED,"");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendLocationUpDateToServer(FromPlaceUpdate placeUpdate) {
        WebContentsInterface webLocation= ServerConnection.createRetrofitConnection(WebContentsInterface.class);
        Call<JsonModelObject> plaeUpdateCall = webLocation.leadStartCall(placeUpdate);
        plaeUpdateCall.enqueue(new Callback<JsonModelObject>() {
            @Override
            public void onResponse(Call<JsonModelObject> call, Response<JsonModelObject> response) {

                if(progress != null && progress.isShowing())
                    progress.dismiss();
                if(response.code() == 200) {
                    JsonModelObject modelObject = response.body();
                    if(modelObject != null && modelObject.isResult()) {
                        Toast.makeText(MainActivity.this,modelObject.getResultmessage(),Toast.LENGTH_SHORT).show();

                    }
                    if (modelObject != null) {
                        Toast.makeText(MainActivity.this,modelObject.getResultmessage(),Toast.LENGTH_SHORT).show();
                    }
                    moveBack(RESULT_OK,"");
                    if(active != null)
                    active.setUserVisibleHint(true);
                }
            }

            @Override
            public void onFailure(Call<JsonModelObject> call, Throwable t) {
                Toast.makeText(MainActivity.this,t.getMessage(),Toast.LENGTH_SHORT).show();
                if(progress != null && progress.isShowing())
                    progress.dismiss();
                moveBack(RESULT_CANCELED,"");
            }
        });
    }
    private void addLatiLongiToShared(Location flocation) {
        if(flocation != null) {
            SharedPreferences preferences = getSharedPreferences(FileName, MODE_PRIVATE);
            SharedPreferences.Editor write = preferences.edit();
            write.putString(LATITUDE, String.valueOf(flocation.getLatitude()));
            write.putString(LONGITUDE, String.valueOf(flocation.getLongitude()));
            write.apply();


        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(uiUpDate);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(dataManagement != null)
            dataManagement.close();

        if (locManger != null) {
            locManger.removeUpdates(StartLocation);
        }
    }
    public void itemCall(String cust_contactNo, String leadid) {
        DailCallNimber = cust_contactNo;

            leadID = leadid;
            try {
                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE)
                        != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_CALL_LOG)
                        != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_PHONE_STATE)
                        != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.RECORD_AUDIO)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CALL_PHONE, Manifest.permission.READ_CALL_LOG, Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO}, REQUEST_CALL_PERMISSION_CODE);
                } else {

                    if (!TextUtils.isEmpty(cust_contactNo) && cust_contactNo.length() == 10) {
                        if (callManager == null)
                            callManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

                        inCall = false;
                        nofCalls = 1;
                        callManager.listen(callListener, PhoneStateListener.LISTEN_CALL_STATE);


                        Intent my_callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:+91" + cust_contactNo));
                        startActivity(my_callIntent);
                    }
                }
            } catch (Exception ex) {
                Toast.makeText(MainActivity.this, "ex Permission denied", Toast.LENGTH_LONG).show();
            }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_LOCATION_PERMISSION_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                requestLocations();
            }else{
                requestUserPermissions();
            }
        }else if(REQUEST_CALL_PERMISSION_CODE == requestCode){
            itemCall(DailCallNimber,leadID);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == SUBMITE_Code && resultCode == RESULT_OK){
            if(runfreshLead != null){
                runfreshLead.onRefresh();
            }
        }else if(CAll_Code == requestCode && resultCode == RESULT_OK){
            if(active.equals(freshLead) )
                freshLead.onRefresh();
            if(active.equals(runfreshLead) )
                runfreshLead.onRefresh();
        }
    }
    public void moveToSubmit(LeadCoordinator leadData) {
        Intent submit = new Intent(MainActivity.this,SubmitActivity.class);
        submit.putExtra("leadId",leadData.getLeadId());
        submit.putExtra("BookingId",leadData.getBooking_ID());
        submit.putExtra("islead",true);
        String leadStatusKey = "22";

            if (leadData.getSiteVisitCount() > 0 && leadData.getFollowupCount() > 0) {
                leadStatusKey = "24";
            } else if (leadData.getFollowupCount() > 0) {
                leadStatusKey = "23";
            }

        submit.putExtra("activityKey",leadStatusKey);
        startActivityForResult(submit,SUBMITE_Code);
    }

    public void moveToSubmitAppointment(String leadId,String Id) {
        Intent submit = new Intent(MainActivity.this,SubmitActivity.class);
        submit.putExtra("leadId",leadId);
        submit.putExtra("BookingId",Id);
        submit.putExtra("islead",false);
        submit.putExtra("activityKey","52");

        startActivityForResult(submit,SUBMITE_Code);
    }
    public void holdAndUnload(String leadId, boolean holdFlag) {
        if(holdFlag){
           startingLocation(leadId);
        }else{

            HoldUnHold holdUnHold = new HoldUnHold(leadId,!holdFlag);
           updateLeadHold(holdUnHold);
        }

    }

    private void updateLeadHold( HoldUnHold holdUnHold) {
        showLoader();
        WebContentsInterface webHold = ServerConnection.createRetrofitConnection(WebContentsInterface.class);
        Call<JsonModelObject> holdCall = webHold.leadOnHold(holdUnHold);
        holdCall.enqueue(new Callback<JsonModelObject>() {
            @Override
            public void onResponse(Call<JsonModelObject> call, Response<JsonModelObject> response) {
                if(response.code() == 200){
                    JsonModelObject jsonObj = response.body();
                    if(jsonObj != null && jsonObj.isResult()){
                        SharedPreferences preferences = getSharedPreferences(FileName,MODE_PRIVATE);
                        SharedPreferences.Editor clean = preferences.edit();
                        clean.remove(LATITUDE);
                        clean.remove(LONGITUDE);
                        clean.apply();
                        if(active != null)
                            active.setUserVisibleHint(true);
                    }
                }
                if(progress != null && progress.isShowing())
                    progress.dismiss();
            }

            @Override
            public void onFailure(Call<JsonModelObject> call, Throwable t) {
                if(progress != null && progress.isShowing())
                    progress.dismiss();
            }
        });
    }

    public void viewLog(LeadCoordinator coordinator, boolean b) {
        Intent intent=new Intent(MainActivity.this,CallDetailsActivity.class);

        if(!IsLeadPurpose) {
            intent.putExtra("Type", "Booking");
            intent.putExtra("ID",coordinator.getBooking_ID());
        }
        else {
            intent.putExtra("Type", "Lead");
            intent.putExtra("ID", coordinator.getLeadId());
        }

        startActivity(intent);
    }
}
