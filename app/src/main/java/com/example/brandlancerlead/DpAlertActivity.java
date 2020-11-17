package com.example.brandlancerlead;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.brandlancerlead.brandUtility.AudioRecordService;
import com.example.brandlancerlead.brandUtility.ServerConnection;
import com.example.brandlancerlead.brandUtility.WebContentsInterface;
import com.example.brandlancerlead.model.DPPendingDetailsJsonObjects;
import com.example.brandlancerlead.model.DPPendingDetailsJsonResult;
import com.example.brandlancerlead.model.EmailSendJsonResult;
import com.example.brandlancerlead.model.PaymentsListJsonObjects;
import com.example.brandlancerlead.model.PaymentsListJsonResult;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DpAlertActivity extends AppCompatActivity {
    LinearLayout call,sms,email;

    Button viewPayments,ViewLogs;
    String BookingID;

    private static final int REQUEST_CALL_PERMISSION_CODE = 352;

    private static final int CAll_Code = 358;

    TelephonyManager callManager;

    private static boolean inCall=false;

    private static int nofCalls =0;

    private static String folderPath;
    public static String fileName = null;

    public String leadID,DailCallNimber;
    int SourceId;
    private Dialog progressOnLoad;
    LinearLayout isVissible;

    TextView customername,project,Offer,bookingIdText,offerPrice,bookingDate,
            dpAmount,dpbalance,actualdpDate,dateDifference,amount,ContactNo,address;


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

                    Intent intent = new Intent(DpAlertActivity.this, AudioRecordService.class);
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dp_alert);
        call=findViewById(R.id.call);
        sms=findViewById(R.id.sms);
        email=findViewById(R.id.email);
        customername=findViewById(R.id.customername);
        project=findViewById(R.id.project);
        Offer=findViewById(R.id.Offer);
        bookingIdText=findViewById(R.id.bookingId);
        offerPrice=findViewById(R.id.offerPrice);
        bookingDate=findViewById(R.id.bookingDate);
        dpAmount=findViewById(R.id.dpAmount);
        dpbalance=findViewById(R.id.dpbalance);
        actualdpDate=findViewById(R.id.actualdpDate);
        dateDifference=findViewById(R.id.dateDifference);
        amount=findViewById(R.id.amount);
        ContactNo=findViewById(R.id.ContactNo);
        address=findViewById(R.id.address);
        viewPayments=findViewById(R.id.viewPayments);
        ViewLogs=findViewById(R.id.ViewLogs);
        isVissible=findViewById(R.id.isVissible);


        progressOnLoad = new Dialog(this);


        progressOnLoad.setContentView(new ProgressBar(this));
        progressOnLoad.setCancelable(false);

        if( progressOnLoad.getWindow() != null)
            progressOnLoad.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        progressOnLoad.show();

        final Intent intent=getIntent();
        if (intent!=null){
            Bundle filterBundle=intent.getExtras();
            if (filterBundle!=null){
                 BookingID=filterBundle.getString("BookingId");
                 String type=filterBundle.getString("PaymentType");
                 if (type.equals("ZeroPayment")){
                     SourceId=1;
                     bindZeroPayments(BookingID);

                 }else if(type.equals("DpPayment")) {
                     SourceId=2;
                     bindPdDetails(BookingID);
                 }else if(type.equals("DpClosed")){
                     SourceId=3;
                     bindPdClosedDetails(BookingID);
                     isVissible.setVisibility(View.GONE);
                 }
            }
        }

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemCall(ContactNo.getText().toString(),BookingID);
            }
        });
        sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(DpAlertActivity.this,"Sms",Toast.LENGTH_SHORT).show();
            }
        });

        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Dialog emailDiaolog=new Dialog(DpAlertActivity.this);
                emailDiaolog.setContentView(R.layout.email_diaolog);


                emailDiaolog.setTitle("Confirm Dialog !");

                final AlertDialog.Builder builder = new AlertDialog.Builder(DpAlertActivity.this);
                builder.setTitle("Confirm Dialog !");
                builder.setMessage("Are you sure you want to send this Email ");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        WebContentsInterface contentsInterface=ServerConnection.createRetrofitConnection(WebContentsInterface.class);
                        Call<EmailSendJsonResult>jsonResultCall=contentsInterface.sendMail(BookingID);
                        jsonResultCall.enqueue(new Callback<EmailSendJsonResult>() {
                            @Override
                            public void onResponse(Call<EmailSendJsonResult> call, Response<EmailSendJsonResult> response) {
                                if (response.code()==200){
                                    EmailSendJsonResult jsonResult=response.body();
                                    if (jsonResult!=null && jsonResult.isResult()){
                                        Toast.makeText(DpAlertActivity.this, jsonResult.getResultmessage(), Toast.LENGTH_LONG).show();
                                    }else {
                                        Toast.makeText(DpAlertActivity.this, jsonResult.getResultmessage(), Toast.LENGTH_LONG).show();
                                    }
                                }else {
                                    Toast.makeText(DpAlertActivity.this, response.message(), Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<EmailSendJsonResult> call, Throwable t) {
                                Toast.makeText(DpAlertActivity.this,t.getMessage(), Toast.LENGTH_LONG).show();

                            }
                        });
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                builder.show();


            }
        });
        viewPayments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle booking = new Bundle();
               PaymentViewFragment paymentView = new PaymentViewFragment();
               booking.putString("booking_id",BookingID);
               paymentView.setArguments(booking);
               paymentView.show(getSupportFragmentManager(),"payment");
            }
        });
        ViewLogs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent viewIntent=new Intent(DpAlertActivity.this,CallDetailsActivity.class);
                viewIntent.putExtra("ID",BookingID);
                viewIntent.putExtra("Type","Booking");
                startActivity(viewIntent);
            }
        });
    }

    private void bindPdClosedDetails(String bookingID) {
        WebContentsInterface contentsInterface= ServerConnection.createRetrofitConnection(WebContentsInterface.class);
        Call<DPPendingDetailsJsonResult>jsonResultCall=contentsInterface.dpPaymentDeatils(bookingID);
        jsonResultCall.enqueue(new Callback<DPPendingDetailsJsonResult>() {
            @Override
            public void onResponse(Call<DPPendingDetailsJsonResult> call, Response<DPPendingDetailsJsonResult> response) {
                if (response.code()==200){
                    DPPendingDetailsJsonResult jsonResult=response.body();
                    if (jsonResult!=null && jsonResult.isResult() && jsonResult.getResultset()!=null){
                        progressOnLoad.dismiss();
                        DPPendingDetailsJsonObjects objects=jsonResult.getResultset();
                        customername.setText(objects.getCustomerName());
                        project.setText(objects.getProjectName());
                        offerPrice.setText(""+objects.getOfferPrice());
                        Offer.setText(objects.getOffer());
                        bookingDate.setText(objects.getBookingDate());
                        dpAmount.setText(""+objects.getDPAmount());
                        dpbalance.setText(""+objects.getDPBalance());
                        dateDifference.setText(""+objects.getAgingInDays());
                        amount.setText(""+objects.getAmountPaid());
                        ContactNo.setText(""+objects.getContactNumber());
                        address.setText(""+objects.getAddress());
                        actualdpDate.setText(objects.getDPDate());
                        bookingIdText.setText(objects.getBookingID());
                    }else {
                        progressOnLoad.dismiss();
                        Toast.makeText(DpAlertActivity.this,jsonResult.getResultmessage(),Toast.LENGTH_SHORT).show();
                    }
                }else {
                    progressOnLoad.dismiss();
                    Toast.makeText(DpAlertActivity.this,response.message(),Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<DPPendingDetailsJsonResult> call, Throwable t) {
                progressOnLoad.dismiss();


                Toast.makeText(DpAlertActivity.this,t.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void bindZeroPayments(String bookingID) {
        WebContentsInterface contentsInterface= ServerConnection.createRetrofitConnection(WebContentsInterface.class);
        Call<DPPendingDetailsJsonResult>jsonResultCall=contentsInterface.zeroPaymentDeatils(bookingID);
        jsonResultCall.enqueue(new Callback<DPPendingDetailsJsonResult>() {
            @Override
            public void onResponse(Call<DPPendingDetailsJsonResult> call, Response<DPPendingDetailsJsonResult> response) {
                if (response.code()==200){
                    progressOnLoad.dismiss();
                    DPPendingDetailsJsonResult jsonResult=response.body();
                    if (jsonResult!=null && jsonResult.isResult() && jsonResult.getResultset()!=null){
                        DPPendingDetailsJsonObjects objects=jsonResult.getResultset();
                        customername.setText(objects.getCustomerName());
                        project.setText(objects.getProjectName());
                        offerPrice.setText(""+objects.getOfferPrice());
                        Offer.setText(objects.getOffer());
                        bookingDate.setText(objects.getBookingDate());
                        dpAmount.setText(""+objects.getDPAmount());
                        dpbalance.setText(""+objects.getDPBalance());
                        dateDifference.setText(""+objects.getAgingInDays());
                        amount.setText(""+objects.getAmountPaid());
                        ContactNo.setText(""+objects.getContactNumber());
                        address.setText(""+objects.getAddress());
                        actualdpDate.setText(objects.getDPDate());
                        bookingIdText.setText(objects.getBookingID());
                    }else {
                        progressOnLoad.dismiss();
                        Toast.makeText(DpAlertActivity.this,jsonResult.getResultmessage(),Toast.LENGTH_SHORT).show();
                    }
                }else {
                    progressOnLoad.dismiss();
                    Toast.makeText(DpAlertActivity.this,response.message(),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DPPendingDetailsJsonResult> call, Throwable t) {
                progressOnLoad.dismiss();
                Toast.makeText(DpAlertActivity.this,t.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });

    }


    private void navigateToFeedBack(String dailCallNimber, String leadID) {
        Intent intent = new Intent(DpAlertActivity.this, AudioRecordService.class);
        intent.setAction(AudioRecordService.ACTION_STOP_FOREGROUND_SERVICE);
        startService(intent);

        try {
            Thread.sleep(1000);

            Intent callFeedBack = new Intent(DpAlertActivity.this,StatusUpdateActivity.class);
            callFeedBack.putExtra("number",dailCallNimber);
            callFeedBack.putExtra("lead",leadID);
            callFeedBack.putExtra("Source",SourceId);
            callFeedBack.putExtra("audio",fileName);

            startActivityForResult(callFeedBack,CAll_Code);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void bindPdDetails(final String bookingId) {

        WebContentsInterface contentsInterface= ServerConnection.createRetrofitConnection(WebContentsInterface.class);
        Call<DPPendingDetailsJsonResult>jsonResultCall=contentsInterface.bindPendingDetails(bookingId);
        jsonResultCall.enqueue(new Callback<DPPendingDetailsJsonResult>() {
            @Override
            public void onResponse(Call<DPPendingDetailsJsonResult> call, Response<DPPendingDetailsJsonResult> response) {
                if (response.code()==200){
                    progressOnLoad.dismiss();
                    DPPendingDetailsJsonResult jsonResult=response.body();
                    if (jsonResult!=null && jsonResult.isResult() && jsonResult.getResultset()!=null){
                        DPPendingDetailsJsonObjects objects=jsonResult.getResultset();
                        customername.setText(objects.getCustomerName());
                        project.setText(objects.getProjectName());
                        offerPrice.setText(""+objects.getOfferPrice());
                        Offer.setText(objects.getOffer());
                        bookingDate.setText(objects.getBookingDate());
                        dpAmount.setText(""+objects.getDPAmount());
                        dpbalance.setText(""+objects.getDPBalance());
                        dateDifference.setText(""+objects.getAgingInDays());
                        amount.setText(""+objects.getAmountPaid());
                        ContactNo.setText(""+objects.getContactNumber());
                        address.setText(""+objects.getAddress());
                        actualdpDate.setText(objects.getDPDate());
                        bookingIdText.setText(objects.getBookingID());
                    }else {
                        progressOnLoad.dismiss();
                        Toast.makeText(DpAlertActivity.this,jsonResult.getResultmessage(),Toast.LENGTH_SHORT).show();
                    }
                }else {
                    progressOnLoad.dismiss();
                    Toast.makeText(DpAlertActivity.this,response.message(),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DPPendingDetailsJsonResult> call, Throwable t) {
                progressOnLoad.dismiss();
                Toast.makeText(DpAlertActivity.this,t.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void itemCall(String cust_contactNo, String leadId) {
        DailCallNimber = cust_contactNo;
        leadID = leadId;
        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                    != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG )
                    != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                    != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                    != PackageManager.PERMISSION_GRANTED   ){
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE, Manifest.permission.READ_CALL_LOG,Manifest.permission.READ_PHONE_STATE,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.RECORD_AUDIO}, REQUEST_CALL_PERMISSION_CODE);
            } else {

                if(!TextUtils.isEmpty(cust_contactNo) && cust_contactNo.length() == 10) {
                    if (callManager == null)
                        callManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

                    inCall = false;
                    nofCalls = 1;
                    callManager.listen(callListener, PhoneStateListener.LISTEN_CALL_STATE);

                    Intent my_callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:+91" + cust_contactNo));
                    startActivity(my_callIntent);
                }

            }
        }catch (Exception ex){
            Toast.makeText ( DpAlertActivity.this, "ex Permission denied", Toast.LENGTH_LONG ).show ();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(REQUEST_CALL_PERMISSION_CODE == requestCode){
            itemCall(DailCallNimber,leadID);
        }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(CAll_Code == requestCode ){

        if(resultCode == RESULT_OK)
            DpPendingActivity.isRefresh = true;
        else
            DpPendingActivity.isRefresh = false;

        }
    }
}
