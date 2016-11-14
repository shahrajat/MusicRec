package com.shahrajat.musicrec;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
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
    private DetectedActivity prevActivity;      // Keep track of prev activity to detect changes
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

    private void reorderPlayList(String act) {
        SharedPreferences prefs = getSharedPreferences(ScrollingActivity.MY_PREFS_NAME, MODE_PRIVATE);
        String genre  = prefs.getString(act, "");
        ScrollingActivity sa = new ScrollingActivity();
        //sa.populateSongList(genre);
    }

    // Create a new thread to update the UI
    private void updateUI(final DetectedActivity userActivity) {

        if(mActivityView == null)
            return;

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // All options: IN_VEHICLE, ON_BICYCLE, ON_FOOT, RUNNING, STILL, TILTING, WALKING
                String toDisplay = "";
                SharedPreferences prefs = getSharedPreferences(ScrollingActivity.MY_PREFS_NAME, MODE_PRIVATE);
                switch (userActivity.getType()) {
                    case DetectedActivity.IN_VEHICLE:
                        toDisplay = "Driving: " + prefs.getString("driving", "No pref");
                        mActivityView.setText(toDisplay);
                        break;
                    case DetectedActivity.RUNNING:
                        toDisplay = "Jogging: " + prefs.getString("running", "No pref");
                        mActivityView.setText(toDisplay);
                        break;
                    case DetectedActivity.STILL:
                        toDisplay = "Relaxing: " + prefs.getString("relaxing", "No pref");
                        mActivityView.setText(toDisplay);
                        break;
                    case DetectedActivity.TILTING:
                        toDisplay = "WorkingOut: " + prefs.getString("working", "No pref");
                        mActivityView.setText(toDisplay);
                        break;
                    case DetectedActivity.UNKNOWN:
                        toDisplay = "Unknown: " + prefs.getString("random", "Random");
                        mActivityView.setText(toDisplay);
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

