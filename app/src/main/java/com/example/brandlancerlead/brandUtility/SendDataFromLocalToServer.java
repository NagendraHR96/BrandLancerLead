package com.example.brandlancerlead.brandUtility;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.media.AudioManager;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.widget.Toast;

import com.example.brandlancerlead.MainActivity;
import com.example.brandlancerlead.R;
import com.example.brandlancerlead.model.FromPlaceUpdate;
import com.example.brandlancerlead.model.JsonModelObject;
import com.example.brandlancerlead.model.LeadSubmitPostObject;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SendDataFromLocalToServer extends Service {


    public static final String ACTION_START_FOREGROUND_SERVICE = "ACTION_START_SYNC_FOREGROUND_SERVICE";
    public static final String ACTION_SUBMIT_FOREGROUND_SERVICE = "ACTION_SUBMIT_SYNC_FOREGROUND_SERVICE";

    public static final String ACTION_STOP_FOREGROUND_SERVICE = "ACTION_STOP_SYNC_FOREGROUND_SERVICE";
    private static final String FileName = "ExecutiveLogin";
    private static final String UserName = "ExecutiveId";

    LeadDataManagement dataManager;
    String userID;


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        dataManager = new LeadDataManagement(this);
        SharedPreferences preferences = getSharedPreferences(FileName, MODE_PRIVATE);
        userID = preferences.getString(UserName, null);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            String action = intent.getAction();
            forgroundRecorder();
            String leadValue = intent.getExtras().getString("leadId");
            Cursor leadData = dataManager.getLeadRecord(leadValue);

            switch (action) {

                case ACTION_START_FOREGROUND_SERVICE:

                    if (leadData != null) {
                        leadData.moveToNext();
                        String valueAddress = sendToGetAddress(leadData.getDouble(2), leadData.getDouble(3));
                        dataManager.updateLocation(valueAddress,leadValue);
                        if (TextUtils.isEmpty(valueAddress))
                            stopForegroundService();
                        else {
                            FromPlaceUpdate placeUpdate = new FromPlaceUpdate(leadValue, valueAddress, userID);
                            sendLocationUpDateToServer(placeUpdate);
                        }
                    } else {
                        stopForegroundService();
                    }

                    break;
                case ACTION_SUBMIT_FOREGROUND_SERVICE:


                    if (leadData != null) {
                        leadData.moveToNext();
                        String valueAddress = sendToGetAddress(leadData.getDouble(6), leadData.getDouble(7));
                        dataManager.updateLocation(valueAddress,leadValue);
                        if (TextUtils.isEmpty(valueAddress))
                            stopForegroundService();
                        else {
                            Location startingLoc = new Location("startPoint");
                            startingLoc.setLatitude(leadData.getDouble(2));
                            startingLoc.setLongitude(leadData.getDouble(3));

                            Location endLocation = new Location("endPoint");
                            endLocation.setLatitude(leadData.getDouble(6));
                            endLocation.setLongitude(leadData.getDouble(7));
                            float distance = startingLoc.distanceTo(endLocation);

                            double distanceInKM = distance/ 998;



                            String lead_Id = leadData.getString(1);
                            String metStat = leadData.getString(10);
                            String feedBack = leadData.getString(11);
                            String status = leadData.getString(12);
                            String reject = leadData.getString(15);
                            String fdate = leadData.getString(13);
                            String ftime = leadData.getString(14);
                            int purpose = leadData.getInt(20);
                            String booking = leadData.getString(21);

                            String receipt = leadData.getString(16);

                            String amount = leadData.getString(17);

                            String metProvePath =  leadData.getString(22);
                            String receiptLoadPath =  leadData.getString(23);

                            String metimagePath =  leadData.getString(18);
                            String receiptImgPath =  leadData.getString(19);




                            if(!TextUtils.isEmpty(metimagePath) && TextUtils.isEmpty(metProvePath)){
                                File mf = new File(metimagePath);
                                if(mf.exists()) {
                                    try {
                                        metProvePath = SendToFttpServer(metimagePath, "Files/Resources/LeadImage/",lead_Id);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    } catch (DocumentException e) {
                                        e.printStackTrace();
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    } catch (ExecutionException e) {
                                        e.printStackTrace();
                                    }

                                    if(TextUtils.isEmpty(metProvePath))
                                        stopForegroundService();
                                    else {
                                        dataManager.updatefttpMetfName(metProvePath,leadValue);
                                        mf.delete();
                                    }
                                }
                            }

                            if(!TextUtils.isEmpty(receiptImgPath)&& TextUtils.isEmpty(receiptLoadPath)) {
                                File mf = new File(receiptImgPath);
                                if (mf.exists()) {
                                    try {
                                        receiptLoadPath = SendToFttpServer(receiptImgPath, "Files/Executive_Income/", receipt);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    } catch (DocumentException e) {
                                        e.printStackTrace();
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    } catch (ExecutionException e) {
                                        e.printStackTrace();
                                    }
                                    if (TextUtils.isEmpty(receiptLoadPath))
                                        stopForegroundService();
                                    else {
                                        dataManager.updatefttpRecfName(receiptLoadPath, leadValue);
                                        mf.delete();

                                    }
                                }
                            }
                            LeadSubmitPostObject postObject = new LeadSubmitPostObject(lead_Id,valueAddress,String.valueOf(distanceInKM),reject,feedBack,fdate,
                                    status,metStat,metProvePath,ftime,receipt,booking,amount,receiptLoadPath,0,purpose);
                            WebContentsInterface webSubmite = ServerConnection.createRetrofitConnection(WebContentsInterface.class);
                            Call<JsonModelObject> submitCall = webSubmite.submitLead(postObject);
                            submitCall.enqueue(new Callback<JsonModelObject>() {
                                @Override
                                public void onResponse(Call<JsonModelObject> call, Response<JsonModelObject> response) {
                                    stopForegroundService();
                                }

                                @Override
                                public void onFailure(Call<JsonModelObject> call, Throwable t) {
                                    stopForegroundService();
                                }
                            });
                        }
                    } else {
                        stopForegroundService();
                    }
                    break;
                default:
                    stopForegroundService();
                    break;
            }

        }

        return super.onStartCommand(intent, flags, startId);
    }

    private String SendToFttpServer(String metimagePath, String destinationPath,String fileStart) throws IOException, DocumentException, ExecutionException, InterruptedException {
        Document    document =new Document(PageSize.A4,25,25,25,25);
        String   directoryPath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+"Submit.pdf";

        File file=new File(directoryPath);
        if (file.exists()){
            file.delete();
        }
        file.createNewFile();

        PdfWriter.getInstance(document, new FileOutputStream(directoryPath)); //  Change pdf's name.

        document.open();
        Image   image = Image.getInstance(metimagePath);

        image.setCompressionLevel(PdfStream.BEST_COMPRESSION);

        Rectangle documentRect = document.getPageSize();
        image.scaleAbsolute(documentRect.getWidth(), documentRect.getHeight());

        image.setAbsolutePosition((documentRect.getWidth() - image.getScaledWidth()) / 2, (documentRect.getHeight() - image.getScaledHeight()) / 2);
        document.add(image);
        document.close();
String resultset = "";

            SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmss", Locale.US);
            String reciptPicName = fileStart+"_"+sdf.format(new Date())+".pdf";
            String destFile = destinationPath+reciptPicName;
            boolean success = new  UploadTask().execute(directoryPath,destFile).get();
            if(success)
                resultset = reciptPicName;

return resultset;
    }
    class UploadTask extends AsyncTask<String, Void, Boolean> {


        @Override
        protected Boolean doInBackground(String... params) {
            FTPClient con = null;
            boolean result=false;
            try {
                con = new FTPClient();
                con.connect(ServerConnection.fttpUrl);

                if (con.login("administrator", "Neory_321")) {
                    con.enterLocalPassiveMode(); // important!
                    con.setFileType(FTP.BINARY_FILE_TYPE);

                    FileInputStream in = new FileInputStream(new File(params[0]));


                    result = con.storeFile(params[1], in);
                    in.close();

                    con.logout();
                    con.disconnect();

                }
            } catch (Exception e) {

            }
            return result;
        }
    }
    private String sendToGetAddress(double lati, double longi) {
        String addressLine = "";
        try {
            Geocoder earthGeo = new Geocoder(this, new Locale("en", "in"));
            List<Address> earthAddress = earthGeo.getFromLocation(lati, longi, 3);
            if (earthAddress != null && !earthAddress.isEmpty()) {
                addressLine = earthAddress.get(0).getAddressLine(0);


            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return addressLine;
    }

    private void sendLocationUpDateToServer(FromPlaceUpdate placeUpdate) {
        WebContentsInterface webLocation = ServerConnection.createRetrofitConnection(WebContentsInterface.class);
        Call<JsonModelObject> plaeUpdateCall = webLocation.leadStartCall(placeUpdate);
        plaeUpdateCall.enqueue(new Callback<JsonModelObject>() {
            @Override
            public void onResponse(Call<JsonModelObject> call, Response<JsonModelObject> response) {
                refreshUiData();
                stopForegroundService();

            }

            @Override
            public void onFailure(Call<JsonModelObject> call, Throwable t) {
                stopForegroundService();
            }
        });
    }

    private void refreshUiData() {
        Intent refresh = new Intent("com.neory.main.uirefresh");
        LocalBroadcastManager.getInstance(this).sendBroadcast(refresh);
    }

    @Override
    public void onDestroy() {
        if (dataManager != null)
            dataManager.close();
        super.onDestroy();

    }

    private void forgroundRecorder() {
        Intent notificationIntent = new Intent();
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);


        String chanel = "";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            chanel = createNotificationChannel();

        }
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, chanel);
        Notification notification = notificationBuilder.setOngoing(true)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("App is Syncing in background")
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();


        startForeground(2045, notification);
    }

    protected void stopForegroundService() {
        stopForeground(true);

        // Stop the foreground service.
        stopSelf();
    }

    @TargetApi(Build.VERSION_CODES.O)
    public String createNotificationChannel() {
        String NOTIFICATION_CHANNEL_ID = "com.example.brandlancerlead.notification";
        String channelName = "SYNC Data";
        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);
        return NOTIFICATION_CHANNEL_ID;
    }
}
