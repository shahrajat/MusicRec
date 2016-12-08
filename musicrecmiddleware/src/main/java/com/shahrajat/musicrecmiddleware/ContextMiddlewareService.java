package com.shahrajat.musicrecmiddleware;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.LocationManager;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;

public class ContextMiddlewareService extends Service implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private GoogleApiClient mGoogleApiClient;
    final DetectedActivity userActivity = null;

    public ContextMiddlewareService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // For activity recognition
        mGoogleApiClient = new GoogleApiClient.Builder(this).build();

        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    // Activity recognition service
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        try {
            Intent intent = new Intent( this, ContextMiddlewareService.class );
            PendingIntent pendingIntent = PendingIntent.getService( this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT );
            ActivityRecognition.ActivityRecognitionApi.requestActivityUpdates( mGoogleApiClient, 3000, pendingIntent );

        } catch (SecurityException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return mBinder;
    }

    private final IContextInterface.Stub mBinder = new IContextInterface.Stub() {

        /*
        Returns the string for current activity of the user
         */


        public String getUserActivity() {
            String activity = "";

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
