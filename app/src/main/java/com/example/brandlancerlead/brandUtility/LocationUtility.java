package com.example.brandlancerlead.brandUtility;

import android.content.Context;
import android.preference.PreferenceManager;

public class LocationUtility {
    public static final String KEY_REQUEST_LOCATIONS = "request_location_update_state";
   public static boolean requestLocationUpdates(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(KEY_REQUEST_LOCATIONS,false);

    }
    public static void setrequestLocationUpdates(Context context,boolean requests){
         PreferenceManager.getDefaultSharedPreferences(context)
                 .edit()
                 .putBoolean(KEY_REQUEST_LOCATIONS,requests)
                 .apply();

    }
}
