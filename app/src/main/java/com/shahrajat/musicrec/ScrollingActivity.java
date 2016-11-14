package com.shahrajat.musicrec;

import android.app.Activity;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.drawable.DrawableWrapper;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Xml;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.DetectedActivity;
import com.google.android.gms.location.LocationServices;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.R.attr.resource;
import static com.shahrajat.musicrec.ActivityRecognizedService.mActivityView;

public class ScrollingActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    private GoogleApiClient mGoogleApiClient;
    private HashMap<String, List<String>> activityToGenres;   // Stores user genre preferences
    public static final String MY_PREFS_NAME = "MyPrefsFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Initialize activity text view
        mActivityView = (TextView) findViewById(R.id.activityText);

        // Floating snackbar
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Playing 1 in background...", Snackbar.LENGTH_INDEFINITE)
                        .setAction("Stop", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Snackbar snackbar1 = Snackbar.make(view, "Stopped playing...", Snackbar.LENGTH_SHORT);
                                snackbar1.show();
                            }
                        }).show();
            }
        });

        // For activity recognition
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(ActivityRecognition.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        mGoogleApiClient.connect();

        // Default preferences for user - updated when user chooses song manually
        SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        editor.putString("running", "Rock");
        editor.putString("relaxing", "Country");
        editor.putString("driving", "Country");
        editor.putString("relaxing", "Pop");
        editor.putString("working", "Rap");
        editor.commit();

        populateSongList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // Sensors - location
    @Override
    public void onStart() {
        mGoogleApiClient.connect(); //location
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        mGoogleApiClient.disconnect(); //location
        super.onStop();
    }

    // Activity recognition service
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        try {
            Intent intent = new Intent( this, ActivityRecognizedService.class );
            PendingIntent pendingIntent = PendingIntent.getService( this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT );
            ActivityRecognition.ActivityRecognitionApi.requestActivityUpdates( mGoogleApiClient, 3000, pendingIntent );

        } catch (SecurityException ex) {

        }
    }

    //Location service
    @Override
    public void onConnectionSuspended(int i) {

    }
    //Location service
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    // Take various actions when a song is clicked
    private void songClicked(final View view) {
        Snackbar.make(view, "Song changed to: " + String.valueOf(view.getId()) + "; updating preference", Snackbar.LENGTH_SHORT).show();
        // Show a delayed Playing message
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Snackbar.make(view, "Playing "+ String.valueOf(view.getId()) +" in background...", Snackbar.LENGTH_INDEFINITE)
                        .setAction("Stop", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Snackbar snackbar1 = Snackbar.make(view, "Stopped playing...", Snackbar.LENGTH_SHORT);
                                snackbar1.show();
                            }
                        }).show();
            }
        }, 2000);

        // Get genre of clicked song
        String genre = getGenreFromId(view.getId());

        // Get current activity
        final String text = String.valueOf(mActivityView.getText());
        if(text!=null){
            final String currActivity = text.split(":")[0].toLowerCase();
            // Update the user preference
            SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
            editor.putString(currActivity, genre);
            editor.commit();

            // Update UI for new preference
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    SharedPreferences prefs = getSharedPreferences(ScrollingActivity.MY_PREFS_NAME, MODE_PRIVATE);
                    mActivityView.setText(text.split(" ")[0] + " " + prefs.getString(currActivity, ""));
                }
            });
        }
    }


    public String getGenreFromId(int id) {
        String genre="";
        try {
            List<Song> allSongs = getSongsFromAnXML(this);
            for(Song song : allSongs) {
                if(song.id == id) {
                    genre = song.genre;
                }
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return genre;
    }

    // Returns all the available songs with provided genre
    public List<Song> getGenreSongs(Activity activity, String genre) {
        List<Song> filteredSongs = new ArrayList<Song>();

        try {
            List<Song> allSongs = getSongsFromAnXML(this);
            for(Song song : allSongs) {
                if(song.genre.equals(genre)) {
                    filteredSongs.add(song);
                }
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return filteredSongs;
    }

    // Helper function
    private List<Song> getSongsFromAnXML(Activity activity)
            throws XmlPullParserException, IOException
    {
        List<Song> songs = new ArrayList<Song>();

        StringBuffer stringBuffer = new StringBuffer();
        Resources res = activity.getResources();
        XmlResourceParser xpp = res.getXml(R.xml.songs);
        xpp.next();

        int eventType = xpp.getEventType();
        while (eventType != XmlPullParser.END_DOCUMENT)
        {
            if(eventType == XmlPullParser.START_TAG && xpp.getName().equals("item"))
            {
                Song newSong = new Song();
                newSong.id = Integer.parseInt(xpp.getAttributeValue(null, "id"));
                xpp.next(); xpp.next();
                newSong.name = xpp.getText();
                xpp.next(); xpp.next();xpp.next();
                newSong.author = xpp.getText();
                xpp.next();xpp.next();xpp.next();
                newSong.year = xpp.getText();
                xpp.next();xpp.next();xpp.next();
                newSong.time = xpp.getText();
                xpp.next();xpp.next();xpp.next();
                newSong.genre = xpp.getText();
                songs.add(newSong);
            }
            eventType = xpp.next();
        }
        return songs;
    }

    // Adds all the songs present in xml/songs.xml to the table
    private void populateSongList() {
        TableLayout songsTbl = (TableLayout) findViewById(R.id.song_list);
        int textColor = Color.BLACK;
        float textSize = 16;
        try {

            // Add header row
            TableRow tbrow = new TableRow(this);
            tbrow.setPadding(0, 70, 0, 70);
            tbrow.setBackgroundResource(R.drawable.cell_shape);

            TextView tv0 = new TextView(this);
            tv0.setText("ID");
            tv0.setTextColor(textColor);
            tv0.setMinWidth(100);
            tv0.setPadding(20, 0, 0, 0);
            tbrow.addView(tv0);

            TextView tv1 = new TextView(this);
            tv1.setTextSize(textSize);
            tv1.setText("Name");
            tv1.setTextColor(textColor);
            tv1.setGravity(Gravity.LEFT);
            tbrow.addView(tv1);

            TextView tv2 = new TextView(this);
            tv2.setText("Author");
            tv2.setTextColor(textColor);
            tbrow.addView(tv2);

                /*
                TextView t3v = new TextView(this);
                t3v.setText(song.year);
                t3v.setTextColor(textColor);
                tbrow.addView(t3v);
                */

            TextView tv4 = new TextView(this);
            tv4.setText("Time");
            tv4.setTextColor(textColor);
            tbrow.addView(tv4);

            TextView tv5 = new TextView(this);
            tv5.setText("Genre");
            tv5.setTextColor(textColor);
            tv5.setGravity(Gravity.RIGHT);
            tbrow.addView(tv5);

            tbrow.setBackgroundResource(R.color.common_plus_signin_btn_text_dark_disabled);

            songsTbl.addView(tbrow);

            List<Song> songs = getSongsFromAnXML(this);
            for(Song song: songs) {
                tbrow = new TableRow(this);
                tbrow.setPadding(0, 70, 0, 70);
                tbrow.setBackgroundResource(R.drawable.cell_shape);

                tv0 = new TextView(this);
                tv0.setText(String.valueOf(song.id));
                tv0.setTextColor(textColor);
                tv0.setGravity(Gravity.LEFT);
                tv0.setPadding(20, 0, 0, 0);
                tbrow.addView(tv0);

                tv1 = new TextView(this);
                tv1.setTextSize(textSize);
                tv1.setText(song.name);
                tv1.setTextColor(textColor);
                tbrow.addView(tv1);

                tv2 = new TextView(this);
                tv2.setText(song.author);
                tv2.setTextColor(textColor);
                tbrow.addView(tv2);

                /*
                TextView t3v = new TextView(this);
                t3v.setText(song.year);
                t3v.setTextColor(textColor);
                tbrow.addView(t3v);
                */

                tv4 = new TextView(this);
                tv4.setText(song.time);
                tv4.setTextColor(textColor);
                tbrow.addView(tv4);

                tv5 = new TextView(this);
                tv5.setText(song.genre);
                tv5.setTextColor(textColor);
                tv5.setGravity(Gravity.RIGHT);
                tbrow.addView(tv5);

                tbrow.setId(song.id);
                tbrow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick( View v ) {
                        songClicked(v);
                    }
                });
                songsTbl.addView(tbrow);

            }
        } catch (XmlPullParserException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        songsTbl.requestLayout();
    }
}
