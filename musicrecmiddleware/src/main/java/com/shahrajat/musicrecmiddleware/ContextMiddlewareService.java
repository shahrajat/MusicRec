package com.shahrajat.musicrecmiddleware;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.media.AudioManager;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;

public class ContextMiddlewareService extends Service {

    public ContextMiddlewareService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return mBinder;
    }

    private final IContextInterface.Stub mBinder = new IContextInterface.Stub() {

        /*
         * This function immediately returns the result without using a callback interface.
         * Avoid using functions that work synchronously if there is a possibility of delay in getting the required response.
         */

        public String getUserActivity() {
            String activity = "";
            final DetectedActivity userActivity = null;
            switch (userActivity.getType()) {
                case DetectedActivity.IN_VEHICLE:
                    activity = "driving";
                    break;
                case DetectedActivity.RUNNING:
                    activity = "jogging";
                    break;
                case DetectedActivity.STILL:
                    activity = "relaxing";
                    break;
                case DetectedActivity.TILTING:
                    activity = "working";
                    break;
                case DetectedActivity.UNKNOWN:
                    activity = "unknown";
                    break;
            }
            return activity;
        }

    };

}
