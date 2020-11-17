package com.example.brandlancerlead.brandUtility;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.PowerManager;
import android.text.TextUtils;
import android.util.Log;

import com.example.brandlancerlead.model.JsonModelObject;
import com.example.brandlancerlead.model.LocationObject;
import com.google.android.gms.location.LocationResult;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LocationServiceForNougat extends IntentService {

    public static final String SERVICE_ACTION= "com.example.brandlancer.action.serviceUpdate";

    private static final String TAG = LocationServiceForNougat.class.getSimpleName();


    private static final String FileName = "ExecutiveLogin";
    private static final String UserName = "ExecutiveId";

    private static  String Executive = null;

    public LocationServiceForNougat() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if(intent != null){

            String action = intent.getAction();
            if(SERVICE_ACTION.equals(action)){
                LocationResult locationResult = LocationResult.extractResult(intent);
                if(locationResult != null){
                    Location mlocation = locationResult.getLastLocation();
                    if(TextUtils.isEmpty(Executive)){
                        Executive = getSharedPreferences(FileName, Context.MODE_PRIVATE).getString(UserName,null);
                    }
                    if(!TextUtils.isEmpty(Executive)) {
                        PowerManager pm = (PowerManager)getSystemService(
                                Context.POWER_SERVICE);
                        boolean isscreen = pm.isScreenOn();
                        if(!isscreen) {

                            PowerManager.WakeLock cpuWakeup = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK|PowerManager.ACQUIRE_CAUSES_WAKEUP,TAG);
                            cpuWakeup.acquire(2 * 60 * 1000L);

                        }

                        LocationObject latLongiObj = new LocationObject(Executive);
                        latLongiObj .setLatitude( String.valueOf(mlocation.getLatitude()));
                        latLongiObj .setLongitude(String.valueOf(mlocation.getLongitude()));

                        LoadLocationToServer(latLongiObj);
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
                Log.e("Location",response.body().getResultmessage());
            }

            @Override
            public void onFailure(Call<JsonModelObject> call, Throwable t) {

            }
        });
    }
}
