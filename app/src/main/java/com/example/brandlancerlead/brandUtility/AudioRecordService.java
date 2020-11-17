package com.example.brandlancerlead.brandUtility;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import com.example.brandlancerlead.R;


public class AudioRecordService extends Service {

    private MediaRecorder recorder = null;
    public static final String ACTION_START_FOREGROUND_SERVICE = "ACTION_START_FOREGROUND_SERVICE";

    public static final String ACTION_STOP_FOREGROUND_SERVICE = "ACTION_STOP_FOREGROUND_SERVICE";
    AudioManager manager;


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            String action = intent.getAction();

            switch (action) {
                case ACTION_START_FOREGROUND_SERVICE:
                    forgroundRecorder();
                    if(intent.getExtras() != null){
                      String  fileName  = intent.getExtras().getString("FileName");
                                recorder = new MediaRecorder();
                                manager = (AudioManager) getSystemService(AUDIO_SERVICE);
                        try {
                            recorder.reset();
                            recorder.setAudioSource(MediaRecorder.AudioSource.VOICE_COMMUNICATION);
                            recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                            //String fileName = audiofile.getAbsolutePath();
                            recorder.setOutputFile(fileName);


                            recorder.prepare();
                          //  manager.setWiredHeadsetOn(true);
                            manager.setMode(AudioManager.ROUTE_HEADSET);
                          int max=   manager.getStreamMaxVolume(AudioManager.ROUTE_HEADSET);
                          manager.setStreamVolume(AudioManager.ROUTE_HEADSET,max,0);
                            // Sometimes prepare takes some time to complete
                            Thread.sleep(2000);
                            recorder.start();
                        }catch (Exception e){
                            e.getMessage();

                        }
                    }
                    break;
                case ACTION_STOP_FOREGROUND_SERVICE:
                    stopForegroundService();
                    break;

            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(recorder != null){
            manager.setMode(AudioManager.MODE_NORMAL);
            recorder.stop();
            recorder.release();
            recorder = null;
        }
    }

    private void forgroundRecorder(){
        Intent notificationIntent = new Intent();
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);


        String chanel = "";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            chanel = createNotificationChannel();

        }
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, chanel);
        Notification notification = notificationBuilder.setOngoing(true)
                .setSmallIcon(R.drawable.ic_record_over_black_24dp)
                .setContentTitle("App is running in background")
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();


        startForeground(2045, notification);
    }

    protected void stopForegroundService(){
        stopForeground(true);

        // Stop the foreground service.
        stopSelf();
    }
    @TargetApi(Build.VERSION_CODES.O)
    public String createNotificationChannel(){
        String NOTIFICATION_CHANNEL_ID = "com.example.brandlancerlead.notification";
        String channelName = "My Record Service";
        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);
        return  NOTIFICATION_CHANNEL_ID;
    }
}
