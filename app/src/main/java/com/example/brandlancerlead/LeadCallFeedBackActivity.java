package com.example.brandlancerlead;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.provider.CallLog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.brandlancerlead.brandUtility.ServerConnection;
import com.example.brandlancerlead.brandUtility.WebContentsInterface;
import com.example.brandlancerlead.model.FeedbackFilter;
import com.example.brandlancerlead.model.FeedbackSendJsonResult;
import com.example.brandlancerlead.model.GetAllCallStatusJsonObjects;
import com.example.brandlancerlead.model.GetAllCallStatusJsonResult;
import com.example.brandlancerlead.model.LeadStatus;
import com.example.brandlancerlead.model.RejectionJson;
import com.example.brandlancerlead.model.RejectionStatus;
import com.example.brandlancerlead.model.StatusResultJson;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LeadCallFeedBackActivity extends AppCompatActivity {

    private TextView followUpdate;
    private TextView followUpTime;
    private TextView dateHint;
    private TextView timeHint;
    ArrayList<LeadStatus> statusSet;
    Spinner callStatus;
    ArrayList<RejectionStatus> rejectionSet;
    private Dialog progressOnLoad;
    private Spinner statusSpin,RejectionSpinner;
    EditText feedBack;
    int callduration;
    String callDate,statusType ="",callStatusId;
    String lead_Id,audioPath,activeKey;
    Button save;
    LinearLayout followVisible, cancelVisible;
    private String rejection_Id="0",callTime,leadStatusId;
    public Calendar siteCalender,followCalender;
    CheckBox wrong_call;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lead_call_feed_back);
        TextView phoneNumber = findViewById(R.id.number_txt);
        TextView callDuration = findViewById(R.id.duration_txt);

        dateHint = findViewById(R.id.followSiteDate);
        timeHint = findViewById(R.id.followSiteTime);

        statusSpin = findViewById(R.id.feedBackStatus);
        RejectionSpinner = findViewById(R.id.rejectionSpinner);

        save=findViewById(R.id.save);
        cancelVisible =findViewById(R.id.cancelStatus);
        followVisible=findViewById(R.id.isfollowvisible);
        followUpdate=findViewById(R.id.followupdate);
        followUpTime=findViewById(R.id.followuptime);
        callStatus=findViewById(R.id.callStatus);

        wrong_call=findViewById(R.id.wrong_call);

        feedBack=findViewById(R.id.feedback);
         SimpleDateFormat sdfs = new SimpleDateFormat("MM/dd/yyyy",Locale.getDefault());

        String currentTime = sdfs.format(new Date());
        callDate=sdfs.format(new Date());


        followUpTime.setText(currentTime);
        progressOnLoad = new Dialog(this);

        cancelVisible.setVisibility(View.GONE);
        followVisible.setVisibility(View.GONE);

        progressOnLoad.setContentView(new ProgressBar(this));

        if( progressOnLoad.getWindow() != null)
            progressOnLoad.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        progressOnLoad.show();


        Intent phoneIntent = getIntent();
        if(phoneIntent != null){
           Bundle phoneExtra = phoneIntent.getExtras();
            if(phoneExtra != null){
                String number = phoneExtra.getString("number");
                 lead_Id = phoneExtra.getString("lead");
                 audioPath = phoneExtra.getString("audio");
                 activeKey = phoneExtra.getString("activityKey");

                phoneNumber.setText(number);

                String whereClauseCondition = CallLog.Calls.NUMBER +" Like ? and " + CallLog.Calls.TYPE +" =?";
                String orderClause = CallLog.Calls.DATE +" DESC";

                Cursor callCursor = getContentResolver().query(CallLog.Calls.CONTENT_URI,null,whereClauseCondition,new String[]{"%"+number+"%",CallLog.Calls.OUTGOING_TYPE+""},orderClause);

                if(callCursor != null) {
                    int durationClm = callCursor.getColumnIndex(CallLog.Calls.DURATION);
                    int dateClm = callCursor.getColumnIndex(CallLog.Calls.DATE);
                    callCursor.moveToFirst();
                    String duration = callCursor.getString(durationClm);
                    String date = callCursor.getString(dateClm);

                    String finaltime = new SimpleDateFormat("hh:mm a",new Locale("en","in")).format(new Date(Long.valueOf(date)));


                    BigDecimal secondRound = new BigDecimal(duration);

                    BigDecimal hours = new BigDecimal("0");
                    BigDecimal myremainder = new BigDecimal("0");
                    BigDecimal minutes = new BigDecimal("0");
                    BigDecimal seconds = new BigDecimal("0");
                    BigDecimal var3600 = new BigDecimal("3600");
                    BigDecimal var60 = new BigDecimal("60");

                    hours = secondRound.divide(var3600, RoundingMode.FLOOR);

                    myremainder = secondRound.remainder(var3600);

                    minutes = myremainder.divide(var60,RoundingMode.FLOOR);
                    seconds = myremainder.remainder(var60);
                    String durationString ="";
                    if(!hours.equals(new BigDecimal("0"))){
                        durationString += hours.toString()+"h ";
                        if(minutes.equals(new BigDecimal("0")))
                            durationString +="0m";
                    }
                    if(!minutes.equals(new BigDecimal("0")))
                        durationString += minutes.toString()+"m ";

                        durationString += seconds.toString()+"sec";

                    String timeDuration = finaltime + "\r\r"+durationString;

                    callDuration.setText(timeDuration);
                    callTime=finaltime;
                    callduration=Integer.parseInt(duration);

                    callCursor.close();
                    bindStatus(callduration);

                    progressOnLoad.dismiss();
                }

            }

        }else{
            finish();
        }
       siteCalender = Calendar.getInstance ();
        followCalender = Calendar.getInstance();
        followCalender.add(Calendar.DATE,0);

                    followUpdate.setOnClickListener ( new View.OnClickListener () {

                        @Override
                        public void onClick(View v) {
                            Calendar calendar = siteCalender;
                            if(statusType.equals("Postponed")){
                                calendar = followCalender;
                            }
                            DatePickerDialog dateSelect =     new DatePickerDialog( LeadCallFeedBackActivity.this, new DatePickerDialog.OnDateSetListener () {
                                @Override
                                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                    String ndateStr = (month + 1) + "/" + dayOfMonth + "/" + year;
                                    followUpdate.setText ( ndateStr );

                                }
                            }, calendar.get ( Calendar.YEAR ), calendar.get ( Calendar.MONTH ), calendar.get ( Calendar.DAY_OF_MONTH ) );

                            dateSelect.getDatePicker().setMinDate(calendar.getTimeInMillis());
                            dateSelect.show ();

                        }

                    } );
                    followUpTime.setOnClickListener ( new View.OnClickListener () {
                        final Calendar calendar = Calendar.getInstance ();
                        @Override
                        public void onClick(View v) {
                            new TimePickerDialog( LeadCallFeedBackActivity.this, new TimePickerDialog.OnTimeSetListener () {
                                @Override
                                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                    String ntimeStr = hourOfDay + ":" + minute;
                                    followUpTime.setText ( ntimeStr );

                                }
                            }, calendar.get ( Calendar.HOUR_OF_DAY ), calendar.get ( Calendar.MINUTE ), false ).show ();
                        }
                    } );

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (wrong_call.isChecked()) {
                    File file = new File(audioPath);
                    if (file != null) {
                        file.delete();
                    }
                    finish();
                } else{
                    if (TextUtils.isEmpty(feedBack.getText().toString())) {
                        Toast.makeText(LeadCallFeedBackActivity.this, "Enter Feedback", Toast.LENGTH_SHORT).show();
                    }else if (statusType.equals("Not Interested") && rejection_Id.equals("0")) {
                        Toast.makeText(LeadCallFeedBackActivity.this, "Need Rejection Reason", Toast.LENGTH_SHORT).show();
                    } else {
                        progressOnLoad.show();
                        String date = followUpdate.getText().toString().trim();
                        String time = followUpTime.getText().toString().trim();

                        FeedbackFilter feedbackFilter = new FeedbackFilter(lead_Id, callDate, callduration, feedBack.getText().toString().trim());

                        feedbackFilter.setStatus_ID(Integer.valueOf(callStatusId));
                        feedbackFilter.setTime(callTime);
                        feedbackFilter.setLeadStatus(Integer.valueOf(leadStatusId));
                        final File audio = new File(audioPath);
                        if (audio.exists() && audio.length() > 0) {
                            try {
                                FileInputStream audioStream = new FileInputStream(audio);
                                byte[] audioByte = new byte[audioStream.available()];
                                audioStream.read(audioByte);
                                String audioString = Base64.encodeToString(audioByte, Base64.DEFAULT);
                                ArrayList<String> audiolist = new ArrayList<String>();
                                audiolist.add(audioString);
                                feedbackFilter.setAudioString(audiolist);

                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }
                        if (statusType.equals("Postponed")) {
                            feedbackFilter.setFollowupDate(date);
                            feedbackFilter.setFollowupTime(time);
                        } else if (statusType.equals("Site visit confirmed")) {
                            feedbackFilter.setFollowupDate(date);
                            feedbackFilter.setFollowupTime(time);

                        } else if (statusType.equals("Not Interested")) {
                            feedbackFilter.setFollowupDate("");
                            feedbackFilter.setFollowupTime("");
                            feedbackFilter.setRejectionReason(Integer.parseInt(rejection_Id));
                        } else {
                            feedbackFilter.setFollowupDate("");
                            feedbackFilter.setFollowupTime("");
                        }

                        WebContentsInterface webContentsInterface = ServerConnection.createRetrofitConnection(WebContentsInterface.class);
                        Call<FeedbackSendJsonResult> jsonResultCall = webContentsInterface.sendFeedback(feedbackFilter);
                        jsonResultCall.enqueue(new Callback<FeedbackSendJsonResult>() {
                            @Override
                            public void onResponse(Call<FeedbackSendJsonResult> call, Response<FeedbackSendJsonResult> response) {
                                if (response.code() == 200) {
                                    FeedbackSendJsonResult jsonResult = response.body();
                                    if (jsonResult != null && jsonResult.isResult()) {
                                        Toast.makeText(LeadCallFeedBackActivity.this, jsonResult.getResultmessage(), Toast.LENGTH_SHORT).show();
                                        boolean abo = audio.delete();
                                        progressOnLoad.dismiss();
                                        Intent back = new Intent();
                                        setResult(RESULT_OK);
                                        finish();
                                    } else {
                                        progressOnLoad.dismiss();
                                        if (jsonResult != null) {
                                            Toast.makeText(LeadCallFeedBackActivity.this, jsonResult.getResultmessage(), Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                } else {
                                    progressOnLoad.dismiss();
                                    Toast.makeText(LeadCallFeedBackActivity.this, response.message(), Toast.LENGTH_SHORT).show();

                                }

                            }

                            @Override
                            public void onFailure(Call<FeedbackSendJsonResult> call, Throwable t) {
                                progressOnLoad.dismiss();
                                Toast.makeText(LeadCallFeedBackActivity.this, "no connection", Toast.LENGTH_SHORT).show();

                            }
                        });
                    }


                }
            }
        });

    }
    private void loadLeadStatus() {

        WebContentsInterface webCalls = ServerConnection.createRetrofitConnection(WebContentsInterface.class);
        Call<StatusResultJson> statusResultCall = webCalls.leadStatusCalls(activeKey);
        statusResultCall.enqueue(new Callback<StatusResultJson>() {
            @Override
            public void onResponse(Call<StatusResultJson> call, Response<StatusResultJson> response) {
                if (progressOnLoad != null && progressOnLoad.isShowing()) {
                    progressOnLoad.dismiss();
                }
                if (response.code() == 200) {
                    StatusResultJson resultJson = response.body();
                    if (resultJson != null && resultJson.isResult()) {
                        bindStatusToSpinner(resultJson.getResultset());
                    }
                }
            }

            @Override
            public void onFailure(Call<StatusResultJson> call, Throwable t) {
                if (progressOnLoad != null && progressOnLoad.isShowing()) {
                    progressOnLoad.dismiss();
                }
            }
        });
    }

    private void bindStatus(final int duration) {
        WebContentsInterface contentsInterface=ServerConnection.createRetrofitConnection(WebContentsInterface.class);
        Call<GetAllCallStatusJsonResult>jsonResultCall=contentsInterface.bindCallStatus();
        jsonResultCall.enqueue(new Callback<GetAllCallStatusJsonResult>() {
            @Override
            public void onResponse(Call<GetAllCallStatusJsonResult> call, Response<GetAllCallStatusJsonResult> response) {
                if (response.code()==200){
                    GetAllCallStatusJsonResult jsonResult=response.body();
                    if (jsonResult!=null && jsonResult.isResult() && jsonResult.getResultset()!=null){
                        displayCallStatus(jsonResult.getResultset(),duration);
                    }
                }
            }

            @Override
            public void onFailure(Call<GetAllCallStatusJsonResult> call, Throwable t) {

            }
        });

    }

    private void displayCallStatus(final ArrayList<GetAllCallStatusJsonObjects> resultset,int dur) {
        ArrayList<String>list=new ArrayList<>();
        for (GetAllCallStatusJsonObjects objects:resultset){
            list.add(objects.getCallStatusName());
        }
        ArrayAdapter<String>arrayAdapter=new ArrayAdapter<>(LeadCallFeedBackActivity.this,android.R.layout.simple_dropdown_item_1line,list);
        callStatus.setAdapter(arrayAdapter);
        if (callStatus!=null) {
            if (dur > 0) {
                int i = getIndex(callStatus, "Recieved");
                callStatus.setSelection(i);
                callStatus.setEnabled(false);
            }
        }
        if (callStatus!=null){
            callStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    callStatusId=resultset.get(position).getCallStatusID();
                    if (resultset.get(position).getCallStatusName().equals("Recieved")){
                       if(statusSpin.getAdapter() == null)
                           loadLeadStatus();
                       else
                           statusSpin.setSelection(0);

                        statusSpin.setEnabled(true);



                    }else {

                        if(statusSpin.getAdapter() == null)
                            loadLeadStatus();
                        else
                            statusSpin.setSelection(0);
                        statusSpin.setEnabled(false);

                        callStatus.setEnabled(true);

                        statusType="0";
                        rejection_Id="0";
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
    }
    private int getIndex(Spinner spinner, String myString){
        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
                return i;
            }
        }

        return 0;
    }

    private void bindStatusToSpinner(final ArrayList<LeadStatus> resultset) {
        statusSet = new ArrayList<LeadStatus>();

        final ArrayList<String> statusName = new ArrayList<String>();
        for (LeadStatus leadStatus:resultset){
            statusName.add(leadStatus.getStatusName());
        }

        ArrayAdapter<String> statusAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,statusName);
        statusSpin.setAdapter(statusAdapter);

        if (statusSpin!=null){

                int i =getIndex(statusSpin,"Assigned");
                statusSpin.setSelection(i);

        }


        statusSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                statusType =statusName.get(position);
                leadStatusId=resultset.get(position).getStatusId();
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy",Locale.getDefault());

                if(statusType.equals("Postponed")){
                    rejection_Id="";
                    cancelVisible.setVisibility(View.GONE);
                    followVisible.setVisibility(View.VISIBLE);
                    dateHint.setText("Follow up date :");
                    timeHint.setText("Follow up time :");
                    followUpdate.setText(sdf.format(followCalender.getTime()));
                }
                else if(statusType.equals("Site visit confirmed")){
                    cancelVisible.setVisibility(View.GONE);
                    followVisible.setVisibility(View.VISIBLE);
                    dateHint.setText("Site visit date :");
                    timeHint.setText("Site visit time :");
                    followUpdate.setText(sdf.format(siteCalender.getTime()));
                    rejection_Id="0";

                }else if(statusType.equals("Not Interested")){
                    followUpdate.setText("");
                    followUpTime.setText("");
                    cancelVisible.setVisibility(View.VISIBLE);
                    followVisible.setVisibility(View.GONE);
                    if(RejectionSpinner.getAdapter() == null){
                        loadRejectionStatus();
                    }

                }else{
                    followUpdate.setText("");
                    followUpTime.setText("");
                    rejection_Id="";
                    cancelVisible.setVisibility(View.GONE);
                    followVisible.setVisibility(View.GONE);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void loadRejectionStatus() {
       if(progressOnLoad != null && !progressOnLoad.isShowing()){
           progressOnLoad.show();
       }
        WebContentsInterface webCalls = ServerConnection.createRetrofitConnection(WebContentsInterface.class);
        Call<RejectionJson> rejectionsultCall = webCalls.rejectionStates();
        rejectionsultCall.enqueue(new Callback<RejectionJson>() {
            @Override
            public void onResponse(Call<RejectionJson> call, Response<RejectionJson> response) {
                if (progressOnLoad != null && progressOnLoad.isShowing()) {
                    progressOnLoad.dismiss();
                }
                if ( response.code() == 200) {
                    RejectionJson resultJson = response.body();
                    if (resultJson != null && resultJson.isResult()) {
                        bindRejectionsToSpinner(resultJson.getResultset());
                    }
                }
            }

            @Override
            public void onFailure(Call<RejectionJson> call, Throwable t) {
                if (progressOnLoad != null && progressOnLoad.isShowing()) {
                    progressOnLoad.dismiss();
                }
            }
        });
    }
    private void bindRejectionsToSpinner(ArrayList<RejectionStatus> resultset) {
        rejectionSet = resultset;
        final ArrayList<String> rejectionString = new ArrayList<String>();
        rejectionString.add("--Select--");
        for (RejectionStatus status : resultset) {
            rejectionString.add(status.getRejectionName());
        }

        ArrayAdapter<String> rejectionAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, rejectionString);
        rejectionAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        RejectionSpinner.setAdapter(rejectionAdapter);
        RejectionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
             if(position == 0){
                 rejection_Id = "0";
             }else{
                 rejection_Id = rejectionSet.get(position-1).getRejectionId();
             }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onBackPressed() {
//        File audio = new File(audioPath);
//        if(audio.exists()){
//            boolean abo =  audio.delete();
//            Toast.makeText(LeadCallFeedBackActivity.this,"Press again",Toast.LENGTH_SHORT).show();
//        }else{
//            finish();
//        }
       // super.onBackPressed();
    }

}
