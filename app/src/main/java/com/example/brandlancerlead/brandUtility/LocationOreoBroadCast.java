package com.example.brandlancerlead.brandUtility;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.PowerManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.example.brandlancerlead.model.JsonModelObject;
import com.example.brandlancerlead.model.LocationObject;
import com.google.android.gms.location.LocationResult;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LocationOreoBroadCast extends BroadcastReceiver {
   public static final String ACTION_PROCESS_UPDATES =
            "com.example.brandlancer.location.action" +
                    ".PROCESS_UPDATES";
    private static final String PTAG = LocationOreoBroadCast.class.getSimpleName();


     static  boolean isBroadCastOn = true;


    private static final String FileName = "ExecutiveLogin";
    private static final String UserName = "ExecutiveId";

    private static  String Executive = null;

    @Override
    public void onReceive(Context context, Intent intent) {
        isBroadCastOn = true;

//        PowerManager pm = (PowerManager)context.getSystemService(
//                Context.POWER_SERVICE);
//        boolean isscreen = pm.isScreenOn();
//        if(!isscreen) {
//            PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK |PowerManager.ACQUIRE_CAUSES_WAKEUP |PowerManager.ON_AFTER_RELEASE,PTAG);
//            wl.acquire(2 * 60 * 1000L);
//            PowerManager.WakeLock wl_cpu = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,PTAG);
//
//            wl_cpu.acquire(2 * 60 * 1000L);
//
//
//        }
        if(intent != null){
            String action = intent.getAction();
            if(ACTION_PROCESS_UPDATES.equals(action)){
                LocationResult locationResult = LocationResult.extractResult(intent);
                if(locationResult != null){
                    Location mlocation = locationResult.getLastLocation();
//                    String timeDate =  new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", new Locale("en","in") ).format(mlocation.getTime());
//                    Toast.makeText(context,timeDate,Toast.LENGTH_LONG).show();

//                    WifiManager wifiManager = ((WifiManager) context.getSystemService(Context.WIFI_SERVICE));
//                    if (wifiManager != null && wifiManager.isWifiEnabled()) {
//                    WifiManager.WifiLock wifiLock = wifiManager.createWifiLock(WifiManager.WIFI_MODE_FULL_HIGH_PERF, "myapp.com.wifilock");
//                        wifiLock.acquire();
//                    }
                   if(TextUtils.isEmpty(Executive)){
                       Executive = context.getSharedPreferences(FileName,Context.MODE_PRIVATE).getString(UserName,null);
                   }
                    if(!TextUtils.isEmpty(Executive)) {
                        LocationObject latLongiObj = new LocationObject(Executive);
                        latLongiObj .setLatitude( String.valueOf(mlocation.getLatitude()));
                        latLongiObj .setLongitude(String.valueOf(mlocation.getLongitude()));

                       // LoadLocationToServer(latLongiObj);
                    }
               }
            }
        }
    }

    private void LoadLocationToServer(LocationObject latLongiObj) {
        WebContentsInterface locationWeb = ServerConnection.createRetrofitConnection(WebContentsInterface.class);
        Call<JsonModelObject> locationCall = locationWeb.sendLatiLongi(latLongiObj);
        locationCall.enqueue(new Callback<JsonModelObject>() {
            @Override
            public void onResponse(Call<JsonModelObject> call, Response<JsonModelObject> response) {

            }

            @Override
            public void onFailure(Call<JsonModelObject> call, Throwable t) {

            }
        });
    }

}
