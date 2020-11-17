package com.example.brandlancerlead;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.example.brandlancerlead.brandUtility.LocationOreoBroadCast;
import com.example.brandlancerlead.brandUtility.LocationUtility;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

public class LeadSplashActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    private final int SPLASH_DISPLAY_LENGTH = 1000;
    private final int REQUEST_LOCATION_PERMISSION_CODE = 321;

    private final int REQUEST_ALLPERMISSION_CODE = 323;

    private final int REQUEST_LOCACTIONENABLE_CODE = 324;

    private final static long LOCATION_UPDATE_INTERVAL = 30000;

    private final static long LOCATION_FASTUPDATE_INTERVAL = LOCATION_UPDATE_INTERVAL / 2;

    private static final long MAX_WAIT_TIME = LOCATION_UPDATE_INTERVAL * 4;

    private FusedLocationProviderClient fusedLocationClient = null;

    private LocationRequest mLocationRequest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lead_splash);



//        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
//
//
//        LocationUtility.setrequestLocationUpdates(this, false);
//        createLocationRequest();

        runSplashHandler();
    }


    @Override
    protected void onStart() {
        super.onStart();
        PreferenceManager.getDefaultSharedPreferences(this)
                .registerOnSharedPreferenceChangeListener(this);



    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onStop() {

        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);

        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock lock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,LeadSplashActivity.class.getSimpleName());
        lock.acquire();

        super.onStop();
    }

    private void requestUserPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(LeadSplashActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CALL_PHONE, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_PHONE_STATE}, REQUEST_ALLPERMISSION_CODE);

        } else {


            runSplashHandler();
        }

    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_ALLPERMISSION_CODE) {
            requestUserPermissions();
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(LocationUtility.KEY_REQUEST_LOCATIONS)) {

                try {
                    fusedLocationClient.requestLocationUpdates(mLocationRequest,getPendingIntentForSupportingDevices());
                }catch (SecurityException e){
                    LocationUtility.setrequestLocationUpdates(this, false);
                }
        }
    }

    private PendingIntent getPendingIntentForSupportingDevices() {
        // after Than Oreo
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            Intent broadcastLocation = new Intent(this, LocationOreoBroadCast.class);
            broadcastLocation.setAction(LocationOreoBroadCast.ACTION_PROCESS_UPDATES);
            return PendingIntent.getBroadcast(this,0,broadcastLocation,PendingIntent.FLAG_UPDATE_CURRENT);

//        }else{
//            // lower than oreo
//
//            Intent locationService = new Intent(this, LocationServiceForNougat.class);
//            locationService.setAction(LocationServiceForNougat.SERVICE_ACTION);
//            return PendingIntent.getService(this,0,locationService,PendingIntent.FLAG_UPDATE_CURRENT);
//
//        }
    }


    private void createLocationRequest() {
        mLocationRequest =  LocationRequest.create()
       .setInterval(LOCATION_UPDATE_INTERVAL)
        .setFastestInterval(LOCATION_FASTUPDATE_INTERVAL)
        .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        .setMaxWaitTime(MAX_WAIT_TIME);

        LocationSettingsRequest.Builder requestBuilder = new LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest)
                .setAlwaysShow(true);
        Task<LocationSettingsResponse> responseTask = LocationServices.getSettingsClient(this)
                .checkLocationSettings(requestBuilder.build());
        responseTask.addOnSuccessListener(new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                requestUserPermissions();

            }
        });
        responseTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if(e instanceof ResolvableApiException){
                    try {
                        ResolvableApiException resolvableApi = (ResolvableApiException) e;
                        resolvableApi.startResolutionForResult(LeadSplashActivity.this,REQUEST_LOCACTIONENABLE_CODE);

                    }catch (IntentSender.SendIntentException ex){

                    }
                }

            }
        });

    }

    private void runSplashHandler() {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
//                    if (!LocationUtility.requestLocationUpdates(LeadSplashActivity.this)) {
//                        LocationUtility.setrequestLocationUpdates(LeadSplashActivity.this, true);
//                    }
                    Intent loginIntent = new Intent(LeadSplashActivity.this, LoginActivity.class);
                    LeadSplashActivity.this.startActivity(loginIntent);
                    LeadSplashActivity.this.finish();
                }
            }, SPLASH_DISPLAY_LENGTH);
        }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_LOCACTIONENABLE_CODE){
            createLocationRequest();
        }
    }
}
