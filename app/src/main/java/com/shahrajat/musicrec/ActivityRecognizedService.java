package com.shahrajat.musicrec;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;

import java.util.List;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions and extra parameters.
 */
public class ActivityRecognizedService extends IntentService {

    public static TextView mActivityView;
    Handler handler;

    public ActivityRecognizedService() {
        super("ActivityRecognizedService");
        handler = new Handler();
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        if (intent != null) {
            if(ActivityRecognitionResult.hasResult(intent)) {
                ActivityRecognitionResult result = ActivityRecognitionResult.extractResult(intent);
                handleDetectedActivities( result.getProbableActivities() );
            }
        }
    }

    private void runOnUiThread(Runnable runnable) {
        handler.post(runnable);
    }

    // Create a new thread to update the UI
    private void updateUI(final DetectedActivity userActivity) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                // All options: IN_VEHICLE, ON_BICYCLE, ON_FOOT, RUNNING, STILL, TILTING, WALKING
                switch (userActivity.getType()) {
                    case DetectedActivity.IN_VEHICLE:
                        mActivityView.setText("Activity: Driving");
                        break;
                    case DetectedActivity.RUNNING:
                        mActivityView.setText("Activity: Jogging");
                        break;
                    case DetectedActivity.STILL:
                        mActivityView.setText("Activity: Relaxing"); //Set the distance from Current location
                        break;
                    case DetectedActivity.TILTING:
                        mActivityView.setText("Activity: WorkingOut");
                        break;
                    case DetectedActivity.UNKNOWN:
                        mActivityView.setText("Activity: Unknown");
                        break;
                }
            }
        });
    }

    private void handleDetectedActivities(List<DetectedActivity> probableActivities) {
        for( DetectedActivity activity : probableActivities ) {
            updateUI(activity);
        }
    }
}

