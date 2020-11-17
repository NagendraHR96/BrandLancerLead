package com.example.brandlancerlead;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.brandlancerlead.brandUtility.ServerConnection;
import com.example.brandlancerlead.brandUtility.WebContentsInterface;
import com.example.brandlancerlead.model.EmailSendJsonResult;
import com.example.brandlancerlead.model.FeedbackFilter;
import com.example.brandlancerlead.model.GetAllCallStatusJsonObjects;
import com.example.brandlancerlead.model.GetAllCallStatusJsonResult;
import com.example.brandlancerlead.model.InsertBookingCallFeedbackFilter;
import com.example.brandlancerlead.model.JsonModelObject;

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

public class StatusUpdateActivity extends AppCompatActivity {

    TextView number_txt,duration_txt,followTimeHint,followDateHint,followupdate,followuptime;
    EditText feedback;
    Button save;
    int callduration;
    String statusType;
    String callDate,userID;
    String bookingId,audioPath;
    LinearLayout isRecived,isfollowvisible;
    Spinner status_Spinner;
    String callStatusId;
    boolean isFollowUpGiven,IsAppointmentGiven,isRequestForBookingCancelGiven;
    RadioGroup radioGroup;
    String callTime;
    int SourceId;
    CheckBox wrong_call;
    private Dialog progressOnLoad;
    RadioButton isRequestForBookingCancel,isfollow;
     AlertDialog.Builder builder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_status_update);
        number_txt=findViewById(R.id.number_txt);
        feedback=findViewById(R.id.feedback);
        duration_txt=findViewById(R.id.duration_txt);
        save=findViewById(R.id.save);
        followTimeHint=findViewById(R.id.followTimeHint);
        followDateHint=findViewById(R.id.followDateHint);
        isRecived=findViewById(R.id.isRecived);
        followupdate=findViewById(R.id.followupdate);
        followuptime=findViewById(R.id.followuptime);
        isfollowvisible=findViewById(R.id.isfollowvisible);
        radioGroup=findViewById(R.id.radioGroup);
        status_Spinner=findViewById(R.id.callStatus);
        wrong_call=findViewById(R.id.wrong_call);
        isRequestForBookingCancel=findViewById(R.id.isRequestForBookingCancel);
        isfollow=findViewById(R.id.isfollow);


        SharedPreferences preferencesc = getSharedPreferences("ExecutiveLogin", MODE_PRIVATE);
        userID = preferencesc.getString("ExecutiveId", null);

        SimpleDateFormat sdfs = new SimpleDateFormat("MM/dd/yyyy",Locale.getDefault());


        // String currentTime = sdfs.format(new Date());
        callDate=sdfs.format(new Date());
        //followupdate.setText(callDate);
        progressOnLoad = new Dialog(this);


        progressOnLoad.setContentView(new ProgressBar(this));

        if( progressOnLoad.getWindow() != null)
            progressOnLoad.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        progressOnLoad.show();
        builder = new AlertDialog.Builder(StatusUpdateActivity.this);


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(wrong_call.isChecked()){
                   File audioFile=new File(audioPath);
                   if (audioFile.exists()){
                       audioFile.delete();
                   }
                   finish();
                }else {

                    if (TextUtils.isEmpty(feedback.getText().toString())) {
                        Toast.makeText(StatusUpdateActivity.this, "Please Enter Feedback", Toast.LENGTH_SHORT).show();
                    } else if (statusType.equalsIgnoreCase("Recieved") && radioGroup.getCheckedRadioButtonId() == -1) {
                        Toast.makeText(StatusUpdateActivity.this, "Please Select FollowUp or Appointment", Toast.LENGTH_SHORT).show();

                    }else  if(statusType.equalsIgnoreCase("Recieved")&& radioGroup.getCheckedRadioButtonId() != R.id.isRequestForBookingCancel &&(TextUtils.isEmpty(followupdate.getText().toString())| TextUtils.isEmpty(followuptime.getText().toString()))){
                        Toast.makeText(StatusUpdateActivity.this, "Please Select Date or Time", Toast.LENGTH_SHORT).show();

                    }
                    else {
                        progressOnLoad.show();
                        InsertBookingCallFeedbackFilter feedbackFilter = new InsertBookingCallFeedbackFilter();
                        final File audio = new File(audioPath);
                        if (audio.exists() && audio.length() > 0) {
                            try {
                                FileInputStream audioStream = new FileInputStream(audio);
                                byte[] audioByte = new byte[audioStream.available()];
                                audioStream.read(audioByte);
                                String audioString = Base64.encodeToString(audioByte, Base64.DEFAULT);
                                ArrayList<String> audiolist = new ArrayList<String>();
                                audiolist.add(audioString);
                                feedbackFilter.setAudioFile(audiolist);

                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }

                        if (isFollowUpGiven) {
                            feedbackFilter.setAppointmentDate("");
                            feedbackFilter.setAppointmentTime("");
                            feedbackFilter.setFollowupDate(followupdate.getText().toString());
                            feedbackFilter.setFollowupTime(followuptime.getText().toString());

                        } else if (IsAppointmentGiven) {
                            feedbackFilter.setFollowupDate("");
                            feedbackFilter.setFollowupTime("");
                            feedbackFilter.setAppointmentDate(followupdate.getText().toString());
                            feedbackFilter.setAppointmentTime(followuptime.getText().toString());
                        } else {
                            feedbackFilter.setFollowupDate("");
                            feedbackFilter.setFollowupTime("");
                            feedbackFilter.setAppointmentDate("");
                            feedbackFilter.setAppointmentTime("");
                        }
                        feedbackFilter.setRequestForBookingCancel(isRequestForBookingCancelGiven);
                        feedbackFilter.setSourceID(SourceId);
                        feedbackFilter.setBookingID(Integer.valueOf(bookingId));
                        feedbackFilter.setCall_Date(callDate);
                        feedbackFilter.setCallFeedback(feedback.getText().toString());
                        feedbackFilter.setAppointmentGiven(IsAppointmentGiven);
                        feedbackFilter.setFollowup(isFollowUpGiven);
                        feedbackFilter.setExecutiveID(userID);
                        feedbackFilter.setTime(callTime);
                        feedbackFilter.setStatus_ID(Integer.valueOf(callStatusId));

                        WebContentsInterface contentsInterface = ServerConnection.createRetrofitConnection(WebContentsInterface.class);
                        Call<JsonModelObject> jsonModelObjectCall = contentsInterface.sendBookingCallFeedback(feedbackFilter);
                        jsonModelObjectCall.enqueue(new Callback<JsonModelObject>() {
                            @Override
                            public void onResponse(Call<JsonModelObject> call, Response<JsonModelObject> response) {
                                if (response.code() == 200) {
                                    JsonModelObject jsonModelObject = response.body();
                                    if (jsonModelObject != null && jsonModelObject.isResult()) {
                                        progressOnLoad.dismiss();
                                        Toast.makeText(StatusUpdateActivity.this, jsonModelObject.getResultmessage(), Toast.LENGTH_SHORT).show();
                                        Intent back = new Intent();
                                        setResult(RESULT_OK);
                                        finish();
                                    } else {
                                        Toast.makeText(StatusUpdateActivity.this, jsonModelObject.getResultmessage(), Toast.LENGTH_SHORT).show();
                                        progressOnLoad.dismiss();

                                    }
                                } else {
                                    Toast.makeText(StatusUpdateActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                                    progressOnLoad.dismiss();

                                }
                            }

                            @Override
                            public void onFailure(Call<JsonModelObject> call, Throwable t) {
                                Toast.makeText(StatusUpdateActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                progressOnLoad.dismiss();
                            }
                        });


                    }
                }

            }
        });

        final Calendar calendar = Calendar.getInstance ();
        followupdate.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                new DatePickerDialog ( StatusUpdateActivity.this, new DatePickerDialog.OnDateSetListener () {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String ndateStr = (month + 1) + "/" + dayOfMonth + "/" + year;
                        followupdate.setText ( ndateStr );
                    }
                }, calendar.get ( Calendar.YEAR ), calendar.get ( Calendar.MONTH ), calendar.get ( Calendar.DAY_OF_MONTH ) ).show ();

            }

        } );
        followuptime.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                new TimePickerDialog( StatusUpdateActivity.this, new TimePickerDialog.OnTimeSetListener () {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String ntimeStr = hourOfDay + ":" + minute;
                        followuptime.setText ( ntimeStr );
                    }
                }, calendar.get ( Calendar.HOUR_OF_DAY ), calendar.get ( Calendar.MINUTE ), false ).show ();
            }
        } );

        Intent phoneIntent = getIntent();
        if(phoneIntent != null){
            Bundle phoneExtra = phoneIntent.getExtras();
            if(phoneExtra != null){
                String number = phoneExtra.getString("number");
                bookingId = phoneExtra.getString("lead");
                SourceId = phoneExtra.getInt("Source");
                audioPath = phoneExtra.getString("audio");


                number_txt.setText(number);

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

                    duration_txt.setText(timeDuration);
                    callduration=Integer.parseInt(duration);
                    callTime=finaltime;

                    bindStatus(callduration);


                    callCursor.close();
                }

            }

        }else{
            finish();
        }
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId==R.id.isfollow){
                    isfollowvisible.setVisibility(View.VISIBLE);
                    followDateHint.setText("FollowUp Date");
                    followTimeHint.setText("FollowUp Time");
                    isFollowUpGiven=true;
                    IsAppointmentGiven=false;
                    isRequestForBookingCancelGiven=false;
                } else if(checkedId==R.id.isRequestForBookingCancel){
                    isfollowvisible.setVisibility(View.GONE);

                    builder.setTitle("Confirm Dialog !");
                    builder.setMessage("Are you sure you want to cancel the Booking");
                    builder.setCancelable(false);
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            isRequestForBookingCancelGiven=true;

                        }
                    });

                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            isRequestForBookingCancelGiven=false;
                            isfollow.setChecked(true);
                        }
                    });

                    builder.show();
                }
                else  {
                    isRequestForBookingCancelGiven=false;
                    isFollowUpGiven=false;
                    IsAppointmentGiven=true;
                    isfollowvisible.setVisibility(View.VISIBLE);
                    followDateHint.setText("Appointment Date");
                    followTimeHint.setText("Appointment Time");
                }
            }
        });

    }
    private void bindStatus(final int dur) {
        WebContentsInterface contentsInterface= ServerConnection.createRetrofitConnection(WebContentsInterface.class);
        Call<GetAllCallStatusJsonResult> jsonResultCall=contentsInterface.bindCallStatus();
        jsonResultCall.enqueue(new Callback<GetAllCallStatusJsonResult>() {
            @Override
            public void onResponse(Call<GetAllCallStatusJsonResult> call, Response<GetAllCallStatusJsonResult> response) {
                if (response.code()==200){
                    GetAllCallStatusJsonResult jsonResult=response.body();
                    if (jsonResult!=null && jsonResult.isResult() && jsonResult.getResultset()!=null){
                        displayCallStatus(jsonResult.getResultset(),dur);
                        progressOnLoad.dismiss();
                    }else {
                        progressOnLoad.dismiss();

                    }
                }else {
                    progressOnLoad.dismiss();
                }
            }

            @Override
            public void onFailure(Call<GetAllCallStatusJsonResult> call, Throwable t) {
                progressOnLoad.dismiss();
            }
        });

    }

    private void displayCallStatus(final ArrayList<GetAllCallStatusJsonObjects> resultset,int dura) {
        ArrayList<String>list=new ArrayList<>();
        for (GetAllCallStatusJsonObjects objects:resultset){
            list.add(objects.getCallStatusName());
        }
        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<>(StatusUpdateActivity.this,android.R.layout.simple_dropdown_item_1line,list);
        status_Spinner.setAdapter(arrayAdapter);

        if (status_Spinner!=null){
            if (dura>0) {
                int pos = getIndex(status_Spinner, "Recieved");
                status_Spinner.setSelection(pos);
                status_Spinner.setEnabled(false);
            }
        }
        if (status_Spinner!=null){
            status_Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    callStatusId=resultset.get(position).getCallStatusID();
                    statusType=resultset.get(position).getCallStatusName();
                    if (resultset.get(position).getCallStatusName().equals("Recieved")){
                        isRecived.setVisibility(View.VISIBLE);

                    }else {
                        isRecived.setVisibility(View.GONE);
                        followupdate.setText("");
                        followuptime.setText("");
                        isFollowUpGiven=false;
                        IsAppointmentGiven=false;
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

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
}
