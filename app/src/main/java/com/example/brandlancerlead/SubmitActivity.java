package com.example.brandlancerlead;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.icu.text.DateFormatSymbols;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.brandlancerlead.brandUtility.LeadDataManagement;
import com.example.brandlancerlead.brandUtility.SendDataFromLocalToServer;
import com.example.brandlancerlead.brandUtility.ServerConnection;
import com.example.brandlancerlead.brandUtility.WebContentsInterface;
import com.example.brandlancerlead.model.JsonModelObject;
import com.example.brandlancerlead.model.LeadStatus;
import com.example.brandlancerlead.model.LeadSubmitPostObject;
import com.example.brandlancerlead.model.RejectionJson;
import com.example.brandlancerlead.model.RejectionStatus;
import com.example.brandlancerlead.model.StatusResultJson;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfStream;
import com.itextpdf.text.pdf.PdfWriter;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubmitActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, View.OnClickListener {

    private TextView followDate, siteVisit;
    private ImageView leadProof;
    private Spinner statusSpinner;
    private Dialog leadProgress;
    String imageFilePath,imageReciptFilePath,directoryPath;
    Document document;
    private Button submit,uploadPic;
    private LinearLayout followupLayout;
    private Spinner rejection;
    private EditText feedback;
    private static final int REQUEST_CAPTURE_IMAGE = 100;
    private static final int REQUEST_CAMERA_PERMISSION = 200;
    public String lead_Id,activeKey;
    public int statusPos, rejectionPos;
    private LocationManager locManger;
    private static final String FileName = "ExecutiveLogin";
    private static final String LATITUDE = "Latitude";
    private static final String LONGITUDE = "Longitude";
    ArrayList<LeadStatus> statusSet;
    private  Calendar myCal;
    String sendImageName;
    TextView followTime,followorSitevisit;

    RadioGroup radioGroup;
    boolean IsLeadPurpose,isReciptPhoto;
    double distance;
    String earAddress;
    ArrayList<RejectionStatus> rejectionSet;
    boolean isPhoto;
    LinearLayout paymentVisible,metMainLayout;
    ImageView receipt_Photo;

    EditText etReciptNo,etAmount;

    private RadioButton notmet;
    String BookingId="";
    int leadPurposeId;

    String reciptPicName="";
    int indexId;

    private LocationListener endLocationListener = new LocationListener() {
        @TargetApi(Build.VERSION_CODES.KITKAT)
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public void onLocationChanged(Location location) {
            if (location != null) {
                //sendToGetAddress(location);
                addHoleDataToLeadTable(location);
                if (locManger != null) {
                    locManger.removeUpdates(endLocationListener);
                }
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            if (!LocationManager.GPS_PROVIDER.equalsIgnoreCase(provider)) {
                if (locManger != null) {
                    locManger.removeUpdates(endLocationListener);
                }
                Toast.makeText(SubmitActivity.this, "Enable GPS", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {
            if (!LocationManager.GPS_PROVIDER.equalsIgnoreCase(provider)) {
                if (locManger != null) {
                    locManger.removeUpdates(endLocationListener);
                }
                Toast.makeText(SubmitActivity.this, "Enable GPS", Toast.LENGTH_SHORT).show();
            }
        }
    };

    private void addHoleDataToLeadTable(Location location) {
        double lati = location.getLatitude();
        double longi = location.getLongitude();
        long timeStamp = location.getTime();
        Date date = new Date(timeStamp);
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        String timeTxt = dateFormat.format(date);

        String feedbacktext = feedback.getText().toString();
        String statusId = statusSet.get(statusPos - 1).getStatusId();
        String status = statusSet.get(statusPos - 1).getStatusName();

        RadioGroup metGroup = findViewById(R.id.metGruopId);
        RadioButton srbt = findViewById(metGroup.getCheckedRadioButtonId());
        String metStatus = srbt.getText().toString();
        String dateSelection = "";
        String rejectionText = "";
        String followUpTime = "";
        String receiptNum= "";
        String receiptAmt= "";



        if (status.equalsIgnoreCase("Site visit confirmed") || status.contains("Receipt Raised and Site Vist Confirmed")) {
            dateSelection = siteVisit.getText().toString().trim();
            followUpTime = followTime.getText().toString();
        } else if (status.equalsIgnoreCase("Postponed")) {
            followUpTime = followTime.getText().toString();
            dateSelection = followDate.getText().toString().trim();

        } else if (status.equalsIgnoreCase("Not Interested")) {
            rejectionText = rejectionSet.get(rejectionPos - 1).getRejectionId();
        }
        if(status.contains("Receipt Raised")){
           receiptNum = etReciptNo.getText().toString();
           receiptAmt = etAmount.getText().toString();
        }

        LeadDataManagement leadManager = new LeadDataManagement(this);
       boolean done = leadManager.updateEndPointDetails(lati,longi,timeTxt,metStatus,feedbacktext,statusId,dateSelection,followUpTime,rejectionText,receiptNum,receiptAmt,imageFilePath,imageReciptFilePath,lead_Id,leadPurposeId,BookingId);
       if(leadProgress != null && leadProgress.isShowing())
           leadProgress.dismiss();
       if(done){
           Thread submit = new Thread(){
               @Override
               public void run() {
                   super.run();
                   Intent intent = new Intent(SubmitActivity.this, SendDataFromLocalToServer.class);
                   intent.putExtra("leadId", lead_Id);
                   intent.setAction(SendDataFromLocalToServer.ACTION_SUBMIT_FOREGROUND_SERVICE);
                   ContextCompat.startForegroundService(SubmitActivity.this,intent);
               }
           };
           submit.start();

       }else{
           Toast.makeText(SubmitActivity.this,"Try Again",Toast.LENGTH_SHORT).show();
       }
       leadManager.close();

    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void sendToGetAddress(Location location) {

        try {
            Geocoder earthGeo = new Geocoder(SubmitActivity.this, new Locale("en", "in"));
            List<Address> earthAddress = earthGeo.getFromLocation(location.getLatitude(), location.getLongitude(), 3);
            if (earthAddress != null && !earthAddress.isEmpty() && !TextUtils.isEmpty(lead_Id)) {
                SharedPreferences locationPrefer = getSharedPreferences(FileName, MODE_PRIVATE);
                String latiString = locationPrefer.getString(LATITUDE, null);
                String longiString = locationPrefer.getString(LONGITUDE, null);
                if (latiString != null && longiString != null) {
                    double sLati = Double.parseDouble(latiString);
                    double sLongi = Double.parseDouble(longiString);
                    Location startingLoc = new Location("startingPoint");
                    startingLoc.setLatitude(sLati);
                    startingLoc.setLongitude(sLongi);
                    float[] distanceMeter = new float[1];
                    Location.distanceBetween(sLati, sLongi, location.getLatitude(), location.getLongitude(), distanceMeter);
                    double distanceInKM = distanceMeter[0] / 998;
                    String endAddress = earthAddress.get(0).getAddressLine(0);
                    submiteLeadWithImage(distanceInKM, endAddress);

                } else {
                    if (leadProgress != null && leadProgress.isShowing())
                        leadProgress.dismiss();
                    Toast.makeText(SubmitActivity.this, "lead expo Please Try Again", Toast.LENGTH_LONG).show();
                }
            } else if (leadProgress != null && leadProgress.isShowing()) {
                leadProgress.dismiss();
                Toast.makeText(SubmitActivity.this, "Something went wrong.Please Try Again", Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
            if (leadProgress != null && leadProgress.isShowing())
                leadProgress.dismiss();
            Toast.makeText(SubmitActivity.this, "Please Try Again", Toast.LENGTH_LONG).show();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit);
       // followUp = findViewById(R.id.followupSelection);
        followDate = findViewById(R.id.followupId);
        siteVisit = findViewById(R.id.siteVisitDate);
        leadProof = findViewById(R.id.lead_Image);
        statusSpinner = findViewById(R.id.statusSelector);
        submit = findViewById(R.id.leadSubmit);
        rejection = findViewById(R.id.rejectionEditor);
        feedback = findViewById(R.id.feedBackEditor);
        followupLayout = findViewById(R.id.followLayout);
        followTime = findViewById(R.id.followTime);
        notmet = findViewById(R.id.notmetRadio);
        radioGroup = findViewById(R.id.metGruopId);
        followorSitevisit = findViewById(R.id.followorsitevisit);
        paymentVisible = findViewById(R.id.paymentVisible);
        etReciptNo = findViewById(R.id.etReciptNo);
        etAmount = findViewById(R.id.etAmount);
        receipt_Photo = findViewById(R.id.receipt_Photo);
        uploadPic = findViewById(R.id.uploadPic);
        metMainLayout = findViewById(R.id.metLayout);

        submit.setOnClickListener(this);
        followDate.setOnClickListener(this);
        siteVisit.setOnClickListener(this);
        Intent leadIntent = getIntent();
        if (leadIntent != null && leadIntent.getExtras() != null) {
            lead_Id = leadIntent.getExtras().getString("leadId");
            IsLeadPurpose = leadIntent.getExtras().getBoolean("islead");
            indexId = leadIntent.getExtras().getInt("IndexId");
            activeKey = leadIntent.getExtras().getString("activityKey");
            if (IsLeadPurpose){
                leadPurposeId=1;
            }else {
                leadPurposeId=2;
                BookingId = leadIntent.getExtras().getString("BookingId");
            }
            locManger = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            //showpicInfoDialog();
            loadLeadStatus(IsLeadPurpose);

        } else {
            finish();
        }
        myCal=Calendar.getInstance();

        final TimePickerDialog.OnTimeSetListener time=new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                myCal.set(Calendar.HOUR_OF_DAY,hourOfDay);
                myCal.set(Calendar.MINUTE,minute);
                updateSelectTime();
            }
        };
        followTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new TimePickerDialog(SubmitActivity.this,time,myCal.get(Calendar.HOUR_OF_DAY),myCal.get(Calendar.MINUTE),false).show();
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.metRadio){
                    if(TextUtils.isEmpty(imageFilePath))
                        showpicInfoDialog();
                    isPhoto=true;
                }else if (checkedId==R.id.notmetRadio){
                    isPhoto=false;
                    if(statusSpinner!= null && statusSpinner.getAdapter() != null)
                        statusSpinner.setSelection(0);
                }
            }
        });

        receipt_Photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isReciptPhoto=true;
                startCamera();
            }
        });
        leadProof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isReciptPhoto=false;
                startCamera();
            }
        });

        uploadPic.setVisibility(View.GONE);

        uploadPic.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.KITKAT)
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {

                if (!TextUtils.isEmpty(imageReciptFilePath)){
                    try {
                        convertPdf(false);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (DocumentException e) {
                        e.printStackTrace();
                    }
                }else {
                    Toast.makeText(SubmitActivity.this,"Please Take Image",Toast.LENGTH_SHORT).show();
                }
            }
        });

       ComponentName  com = getCallingActivity();
       if(com != null) {
           String activity = com.getShortClassName();
           if(activity.equals(".FollowSiteActivity"))
               metMainLayout.setVisibility(View.GONE);
        RadioButton metclick =   radioGroup.findViewById(R.id.metRadio);
        metclick.setChecked(true);


       }
    }

    private void updateSelectTime() {
        String myFormat = "HH:mm:ss"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        followTime.setText(sdf.format(myCal.getTime()));
    }

    private void showpicInfoDialog() {
        AlertDialog.Builder picinfo = new AlertDialog.Builder(SubmitActivity.this);
        picinfo.setTitle("Image Capture");
        picinfo.setMessage("Take a picture with customer  met place");
        picinfo.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                isReciptPhoto=false;
                startCamera();
            }
        });
        picinfo.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                isPhoto=false;
                notmet.setChecked(true);
            }
        });
        picinfo.setCancelable(false);
        picinfo.show();
    }
//    @Override
//    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//        if (isChecked) {
//            showDateDialog();
//        } else {
//            followDate.setText("");
//        }
//    }


    private void showDateDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog pickerDialog = new DatePickerDialog(SubmitActivity.this, AlertDialog.THEME_HOLO_LIGHT, this, year, month, day);
        calendar.add(Calendar.DATE, 0);
        DateFormat df = DateFormat.getDateInstance(DateFormat.LONG, new Locale("en", "in"));
        String formattedDate = df.format(calendar.getTime());
        followDate.setText(formattedDate);
        siteVisit.setText(formattedDate);
        pickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
        calendar.add(Calendar.DATE, 30);
        pickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());
        pickerDialog.show();
        pickerDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                followTime.performClick();
            }
        });

    }




    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(0);
        cal.set(year, month, dayOfMonth, 0, 0, 0);
        Date chosenDate = cal.getTime();

        // Format the date using style and locale
        DateFormat df = DateFormat.getDateInstance(DateFormat.LONG, new Locale("en", "in"));
        String formattedDate = df.format(chosenDate);
        followDate.setText(formattedDate);
        siteVisit.setText(formattedDate);
    }

    public void startCamera() {
        if (ContextCompat.checkSelfPermission(SubmitActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(SubmitActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(SubmitActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CAMERA_PERMISSION);
        } else {
            Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (pictureIntent.resolveActivity(getPackageManager()) != null) {
                //Create a file to store the image
                File photoFile = null;
                try {
                    if(isReciptPhoto){
                        if (TextUtils.isEmpty(imageReciptFilePath))
                            photoFile = createImageFile();
                        else
                            photoFile = new File(imageReciptFilePath);

                    }else {
                        if (TextUtils.isEmpty(imageFilePath))
                            photoFile = createImageFile();
                        else
                            photoFile = new File(imageFilePath);
                    }
                } catch (IOException ex) {
                    // Error occurred while creating the File
                }
                if (photoFile != null) {
                    Uri photoURI = FileProvider.getUriForFile(this, "com.example.brandlancerlead.provider", photoFile);
                    pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                            photoURI);
                    startActivityForResult(pictureIntent,
                            REQUEST_CAPTURE_IMAGE);
                }
            }
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp =
                new SimpleDateFormat("yyyyMMdd_HHmmss",
                        Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir =
                getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        if(isReciptPhoto)
            imageReciptFilePath =  image.getAbsolutePath();
        else
            imageFilePath = image.getAbsolutePath();

        return image;
    }


    private void loadLeadStatus(boolean isLead) {
        showLoder();

        WebContentsInterface webCalls = ServerConnection.createRetrofitConnection(WebContentsInterface.class);
        Call<StatusResultJson> statusResultCall = webCalls.leadStatusCalls(activeKey);
        statusResultCall.enqueue(new Callback<StatusResultJson>() {
            @Override
            public void onResponse(Call<StatusResultJson> call, Response<StatusResultJson> response) {
                if (leadProgress != null && leadProgress.isShowing()) {
                    leadProgress.dismiss();
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
                if (leadProgress != null && leadProgress.isShowing()) {
                    leadProgress.dismiss();
                }
            }
        });
    }

    private void bindStatusToSpinner(ArrayList<LeadStatus> resultset) {
        statusSet = resultset;
        final ArrayList<String> statusString = new ArrayList<String>();
        statusString.add("--Select--");
        for (LeadStatus status : resultset) {
            statusString.add(status.getStatusName());
        }

        ArrayAdapter<String> statusAdapter = new ArrayAdapter<String>(SubmitActivity.this, android.R.layout.simple_list_item_1, statusString);
        statusAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        statusSpinner.setAdapter(statusAdapter);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            statusSpinner.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        }
        statusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                statusPos = position;
                if (position != 0) {

                    String status = statusString.get(position);
                    if (status.equalsIgnoreCase("Site visit confirmed")) {
                        followupLayout.setVisibility(View.VISIBLE);
                        etAmount.setText("");
                        etReciptNo.setText("");
                        rejection.setVisibility(View.GONE);
                        siteVisit.setVisibility(View.VISIBLE);
                        followTime.setVisibility(View.VISIBLE);
                        followDate.setVisibility(View.GONE);
                        rejectionPos=0;
                        followorSitevisit.setVisibility(View.VISIBLE);
                        paymentVisible.setVisibility(View.GONE);
                        followorSitevisit.setText("Site Visit Date and Time");
                        showDateDialog();
                    } else if (status.equalsIgnoreCase("Postponed")) {
                        siteVisit.setText("");
                        etAmount.setText("");
                        etReciptNo.setText("");
                        rejectionPos=0;
                        showDateDialog();
                        followupLayout.setVisibility(View.VISIBLE);
                        followDate.setVisibility(View.VISIBLE);

                        siteVisit.setVisibility(View.GONE);
                        followTime.setVisibility(View.VISIBLE);
                        rejection.setVisibility(View.GONE);
                        followorSitevisit.setVisibility(View.VISIBLE);
                        paymentVisible.setVisibility(View.GONE);
                        followorSitevisit.setText("FollowUp Date and Time");


                    } else if (status.equalsIgnoreCase("Not Interested")) {
                        etAmount.setText("");
                        etReciptNo.setText("");
                        followupLayout.setVisibility(View.GONE);
                        siteVisit.setVisibility(View.GONE);
                        followTime.setVisibility(View.GONE);
                        followorSitevisit.setVisibility(View.GONE);
                        rejection.setVisibility(View.VISIBLE);
                        paymentVisible.setVisibility(View.GONE);
                        followTime.setText("");

                        if (rejectionSet == null) {
                            loadRejectionStatus();
                        }
                    }else if(status.contains("Receipt Raised") ){
                        if( !notmet.isChecked()){
                            paymentVisible.setVisibility(View.VISIBLE);
                            followupLayout.setVisibility(View.GONE);
                            followTime.setVisibility(View.GONE);
                            followorSitevisit.setVisibility(View.GONE);
                            siteVisit.setVisibility(View.GONE);
                            followDate.setVisibility(View.GONE);
                        if(status.contains("Receipt Raised and Site Vist Confirmed")) {
                            followupLayout.setVisibility(View.VISIBLE);
                            followTime.setVisibility(View.VISIBLE);
                            siteVisit.setVisibility(View.VISIBLE);
                            followorSitevisit.setVisibility(View.VISIBLE);
                            followorSitevisit.setText("Site Visit Date and Time");
                            showDateDialog();
                        }
                        }
                        else {
                            statusSpinner.setSelection(0);
                            Toast.makeText(SubmitActivity.this,"Need To Met",Toast.LENGTH_SHORT).show();
                        }

                    }
                    else {
                        etAmount.setText("");
                        etReciptNo.setText("");
                        paymentVisible.setVisibility(View.GONE);
                        followorSitevisit.setVisibility(View.GONE);
                        followTime.setVisibility(View.GONE);
                        followupLayout.setVisibility(View.GONE);
                        siteVisit.setVisibility(View.GONE);
                        rejection.setVisibility(View.GONE);
                    }
                }else{
                    if(paymentVisible.getVisibility() == View.VISIBLE)
                        paymentVisible.setVisibility(View.GONE);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void loadRejectionStatus() {
        showLoder();
        WebContentsInterface webCalls = ServerConnection.createRetrofitConnection(WebContentsInterface.class);
        Call<RejectionJson> rejectionsultCall = webCalls.rejectionStates();
        rejectionsultCall.enqueue(new Callback<RejectionJson>() {
            @Override
            public void onResponse(Call<RejectionJson> call, Response<RejectionJson> response) {
                if (leadProgress != null && leadProgress.isShowing()) {
                    leadProgress.dismiss();
                }
                if (response.code() == 200) {
                    RejectionJson resultJson = response.body();
                    if (resultJson != null && resultJson.isResult()) {
                        bindRejectionsToSpinner(resultJson.getResultset());
                    }
                }
            }

            @Override
            public void onFailure(Call<RejectionJson> call, Throwable t) {
                if (leadProgress != null && leadProgress.isShowing()) {
                    leadProgress.dismiss();
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

        ArrayAdapter<String> rejectionAdapter = new ArrayAdapter<String>(SubmitActivity.this, android.R.layout.simple_list_item_1, rejectionString);
        rejectionAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        rejection.setAdapter(rejectionAdapter);
        rejection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                rejectionPos = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void showLoder() {
        if (leadProgress == null) {
            leadProgress = new Dialog(SubmitActivity.this);
            leadProgress.setContentView(new ProgressBar(SubmitActivity.this));
            if (leadProgress.getWindow() != null)
                leadProgress.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }
        if (leadProgress != null && !leadProgress.isShowing()) {
            leadProgress.setCancelable(false);
            leadProgress.show();
        }
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        switch (viewId) {
            case R.id.leadSubmit:
                String feedBackText = feedback.getText().toString().trim();
                if (radioGroup.getCheckedRadioButtonId()== -1){
                    Toast.makeText(SubmitActivity.this, "Please Select Met Status", Toast.LENGTH_SHORT).show();
                }
               else if (TextUtils.isEmpty(feedBackText)) {
                    Toast.makeText(SubmitActivity.this, "Enter FeedBack", Toast.LENGTH_SHORT).show();

                } else if (statusPos == 0) {
                    Toast.makeText(SubmitActivity.this, "Select Status", Toast.LENGTH_SHORT).show();

                } else if (statusSet == null) {
                    Toast.makeText(SubmitActivity.this, "Wait Something this is Loading", Toast.LENGTH_SHORT).show();
                    loadLeadStatus(IsLeadPurpose);
                } else if ((statusSet.get(statusPos - 1).getStatusName().equalsIgnoreCase("Not Interested") ) && rejectionPos == 0) {
                    Toast.makeText(SubmitActivity.this, "Select  Reason", Toast.LENGTH_SHORT).show();
                } else if((statusSet.get(statusPos - 1).getStatusName().contains("Receipt Raised") ) && (TextUtils.isEmpty(etReciptNo.getText().toString()) || TextUtils.isEmpty(etAmount.getText().toString()) || TextUtils.isEmpty(imageReciptFilePath))){

                    Toast.makeText(SubmitActivity.this, "Please Fill All Receipt Info", Toast.LENGTH_SHORT).show();
                }
               else {
                    if (locManger == null)
                        locManger = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        Toast.makeText(SubmitActivity.this, "Enable Gps", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    showLoder();
                    locManger.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 1, endLocationListener);
                }
                break;
            case R.id.followupId:
            case R.id.siteVisitDate:
                showDateDialog();
                break;
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void submiteLeadWithImage(double distanceInKM, String earthAddress) {
        distance=distanceInKM;
        earAddress=earthAddress;
        if(TextUtils.isEmpty(imageFilePath)){
            UploadToServerWIthoutImage();
        }else {
            try {
                if(statusSet.get(statusPos - 1).getStatusName().contains("Receipt Raised") ) {
                    if (!TextUtils.isEmpty(imageReciptFilePath)) {
                        try {
                            convertPdf(false);
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (DocumentException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(SubmitActivity.this, "Please Take Receipt Photo", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                    convertPdf(true);


            } catch (IOException e) {
                e.printStackTrace();
            } catch (DocumentException e) {
                e.printStackTrace();
            }
        }


    }

    private void UploadToServerWIthoutImage() {

            String feedbacktext = feedback.getText().toString();
            String statusId = statusSet.get(statusPos - 1).getStatusId();
            String status = statusSet.get(statusPos - 1).getStatusName();

            RadioGroup metGroup = findViewById(R.id.metGruopId);
            RadioButton srbt = findViewById(metGroup.getCheckedRadioButtonId());
            String metStatus = srbt.getText().toString();
            String dateSelection = "";
            String rejectionText = "";
            String followUpTime = "";
            String dist = String.valueOf(distance);

            if (status.equalsIgnoreCase("Site visit confirmed") || status.contains("Receipt Raised and Site Vist Confirmed")) {
                dateSelection = siteVisit.getText().toString().trim();
                followUpTime=followTime.getText().toString();
            } else if (status.equalsIgnoreCase("Postponed")) {
                    followUpTime=followTime.getText().toString();
                dateSelection = followDate.getText().toString().trim();
            } else if (status.equalsIgnoreCase("Not Interested")) {
                rejectionText = rejectionSet.get(rejectionPos - 1).getRejectionId();
            }

            LeadSubmitPostObject postLead = new LeadSubmitPostObject(lead_Id, earAddress, dist, rejectionText, feedbacktext, dateSelection, statusId, metStatus,"",followUpTime);

        if(status.contains("Receipt Raised")){
            postLead.setAmount(etAmount.getText().toString());
            postLead.setReceiptNo(etReciptNo.getText().toString());
        }else {
            postLead.setAmount("0");
            postLead.setReceiptNo(" ");
        }
        postLead.setLeadPurposeID(leadPurposeId);
        postLead.setBookingID(BookingId);
        postLead.setReceiptPhoto(reciptPicName);
        postLead.setIndexID(indexId);

        WebContentsInterface webSubmite = ServerConnection.createRetrofitConnection(WebContentsInterface.class);
            Call<JsonModelObject> submitCall = webSubmite.submitLead(postLead);
            submitCall.enqueue(new Callback<JsonModelObject>() {
                @Override
                public void onResponse(Call<JsonModelObject> call, Response<JsonModelObject> response) {

                    if (response.code() == 200) {
                        JsonModelObject submitJson = response.body();
                        if (submitJson != null) {
                            Toast.makeText(SubmitActivity.this, submitJson.getResultmessage(), Toast.LENGTH_SHORT).show();
                            if (submitJson.isResult()) {
                                Intent back = new Intent();
                                setResult(RESULT_OK);
                                finish();
                            }
                        } else {
                            Toast.makeText(SubmitActivity.this, "Failed To Submit", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(SubmitActivity.this, "Failed To Submit", Toast.LENGTH_SHORT).show();
                    }

                    if (leadProgress != null && leadProgress.isShowing())
                        leadProgress.dismiss();
                }

                @Override
                public void onFailure(Call<JsonModelObject> call, Throwable t) {
                    if (leadProgress != null && leadProgress.isShowing())
                        leadProgress.dismiss();
                    Toast.makeText(SubmitActivity.this, "Failed To Submit", Toast.LENGTH_SHORT).show();
                }
            });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            startCamera();
            isReciptPhoto=false;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (REQUEST_CAPTURE_IMAGE == requestCode) {
            if (resultCode == RESULT_OK) {
                if (isReciptPhoto){

                    Glide.with(this).load(imageReciptFilePath).into(receipt_Photo);
                }else {

                    Glide.with(this).load(imageFilePath).into(leadProof);
                }
                // imageFilePath we obtained before opening the cameraIntent
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(SubmitActivity.this, "You need to acttach photo for Submit", Toast.LENGTH_LONG).show();
                //startCamera();
            }
        }
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();

    }



    private void convertPdf( boolean isLeadPic) throws IOException, DocumentException {


        document =new Document(PageSize.A4,25,25,25,25);
        directoryPath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+"Submit.pdf";

        File file=new File(directoryPath);
        if (file.exists()){
            file.delete();
        }
        file.createNewFile();

        PdfWriter.getInstance(document, new FileOutputStream(directoryPath)); //  Change pdf's name.

        document.open();
        addImageTodoc(document,isLeadPic);

        document.close();
        if (isLeadPic) {
            new uploadTask().execute(directoryPath);
        }else {
            new UploadReciptTask().execute(directoryPath);
        }



    }

    public void addImageTodoc(Document document,boolean isLeadPic) throws IOException, DocumentException {


        Image image ;
        if(isLeadPic)
             image = Image.getInstance(imageFilePath);
        else
            image = Image.getInstance(imageReciptFilePath);

        image.setCompressionLevel(PdfStream.BEST_COMPRESSION);

       Rectangle documentRect = document.getPageSize();
        image.scaleAbsolute(documentRect.getWidth(), documentRect.getHeight());

        image.setAbsolutePosition((documentRect.getWidth() - image.getScaledWidth()) / 2, (documentRect.getHeight() - image.getScaledHeight()) / 2);
        document.add(image);


    }


    class  UploadReciptTask extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... strings) {
            FTPClient con = null;
            String resultset = "";
            try
            {
                con = new FTPClient();
                con.connect(ServerConnection.fttpUrl);

                if (con.login("administrator", "Neory_321")) {
                    con.enterLocalPassiveMode(); // important!
                    con.setFileType( FTP.BINARY_FILE_TYPE);

                    FileInputStream in = new FileInputStream (new File(strings[0]));
                    SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmss", Locale.US);
                    reciptPicName=lead_Id+"_"+sdf.format(new Date())+".pdf";
                    boolean result = con.storeFile("Files/Executive_Income/"+reciptPicName, in);
                    in.close();
                    if (result) {
                        resultset = "succeeded";
                    }else {
                        resultset = "failed";
                    }
                    con.logout();
                    con.disconnect();

                }else{
                    resultset = con.getStatus();
                }
            }
            catch (Exception e) {
                resultset = e.getLocalizedMessage ();
            }
            return resultset;
        }

        @Override
        protected void onPostExecute(String s) {

            if (s.equals("succeeded")){
                try {
                    convertPdf(true);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (DocumentException e) {
                    e.printStackTrace();
                }
            }else {
                reciptPicName="";

            }
            Toast.makeText(SubmitActivity.this,s,Toast.LENGTH_SHORT).show();
            super.onPostExecute(s);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
    }


    class uploadTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute ();
        }

        @Override
        protected String doInBackground(String... params) {
            FTPClient con = null;
            String resultset = "";
            try
            {
                con = new FTPClient();
                con.connect(ServerConnection.fttpUrl);

                if (con.login("administrator", "Neory_321")) {
                    con.enterLocalPassiveMode(); // important!
                    con.setFileType( FTP.BINARY_FILE_TYPE);

                    FileInputStream in = new FileInputStream (new File(params[0]));
                    SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmss", Locale.US);
                    sendImageName=lead_Id+"_"+sdf.format(new Date())+".pdf";

                    boolean result = con.storeFile("Files/Resources/LeadImage/"+sendImageName, in);
                    in.close();
                    if (result) {
                        resultset = "succeeded";
                    }else {
                        resultset = "failed";
                    }
                    con.logout();
                    con.disconnect();

                }else{
                    resultset = con.getStatus();
                }
            }
            catch (Exception e) {
                resultset = e.getLocalizedMessage ();
            }
            return resultset;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute ( s );
            if (s.equals ("succeeded")) {
                String feedbacktext = feedback.getText().toString();
                String statusId = statusSet.get(statusPos - 1).getStatusId();
                String status = statusSet.get(statusPos - 1).getStatusName();

                RadioGroup metGroup = findViewById(R.id.metGruopId);
                RadioButton srbt = findViewById(metGroup.getCheckedRadioButtonId());
                String metStatus = srbt.getText().toString();
                String dateSelection = "";
                String rejectionText = "";
                String followUpTime = "";
                String dist = String.valueOf(distance);

                if (status.equalsIgnoreCase("Site visit confirmed") || status.contains("Receipt Raised and Site Vist Confirmed")) {
                    dateSelection = siteVisit.getText().toString().trim();
                    followUpTime=followTime.getText().toString();
                } else if (status.equalsIgnoreCase("Postponed")) {
                        followUpTime=followTime.getText().toString();
                        dateSelection = followDate.getText().toString().trim();
                } else if (status.equalsIgnoreCase("Not Interested")) {
                    rejectionText = rejectionSet.get(rejectionPos - 1).getRejectionId();
                }

                LeadSubmitPostObject postLead = new LeadSubmitPostObject(lead_Id, earAddress, dist, rejectionText, feedbacktext, dateSelection, statusId, metStatus,sendImageName,followUpTime);

                if(status.contains("Receipt Raised")){
                    postLead.setAmount(etAmount.getText().toString());
                    postLead.setReceiptNo(etReciptNo.getText().toString());
                }else {
                    postLead.setAmount("0");
                    postLead.setReceiptNo(" ");
                }
                postLead.setLeadPurposeID(leadPurposeId);
                postLead.setBookingID(BookingId);
                postLead.setReceiptPhoto(reciptPicName);
                postLead.setIndexID(indexId);
                WebContentsInterface webSubmite = ServerConnection.createRetrofitConnection(WebContentsInterface.class);
                Call<JsonModelObject> submitCall = webSubmite.submitLead(postLead);
                submitCall.enqueue(new Callback<JsonModelObject>() {
                    @Override
                    public void onResponse(Call<JsonModelObject> call, Response<JsonModelObject> response) {

                        if (response.code() == 200) {
                            JsonModelObject submitJson = response.body();
                            if (submitJson != null) {
                                Toast.makeText(SubmitActivity.this, submitJson.getResultmessage(), Toast.LENGTH_SHORT).show();
                                if (submitJson.isResult()) {
                                    Intent back = new Intent();
                                    setResult(RESULT_OK);
                                    finish();
                                }
                            } else {
                                Toast.makeText(SubmitActivity.this, "Failed To Submit", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(SubmitActivity.this, "Failed To Submit", Toast.LENGTH_LONG).show();
                        }

                        if (leadProgress != null && leadProgress.isShowing())
                            leadProgress.dismiss();
                    }

                    @Override
                    public void onFailure(Call<JsonModelObject> call, Throwable t) {
                        if (leadProgress != null && leadProgress.isShowing())
                            leadProgress.dismiss();
                            Toast.makeText(SubmitActivity.this, "Failed To Submit", Toast.LENGTH_LONG).show();
                    }
                });
            }else {
                if (leadProgress != null && leadProgress.isShowing())
                    leadProgress.dismiss();
                Toast.makeText(SubmitActivity.this,"Failed to Upload Image ",Toast.LENGTH_LONG).show();
            }
        }
    }


}
