package com.shahrajat.musicrec;

import android.app.Activity;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.drawable.DrawableWrapper;
import android.support.v7.widget.Toolbar;
import android.util.Xml;
import android.view.Gravity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ScrollingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Playing Song in background...", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

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

    private List<Song> getEventsFromAnXML(Activity activity)
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
                xpp.next(); xpp.next();
                newSong.name = xpp.getText();
                xpp.next(); xpp.next();xpp.next();
                newSong.author = xpp.getText();
                xpp.next();xpp.next();xpp.next();
                newSong.year = xpp.getText();
                xpp.next();xpp.next();xpp.next();
                newSong.time = xpp.getText();
                songs.add(newSong);
            }
            eventType = xpp.next();
        }
        return songs;
    }
    /*
    <TextView
                android:id="@+id/textView1"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:text="We don't talk anymore"
                android:layout_column="1"
                android:textAppearance="?android:attr/textAppearanceMedium"></TextView>
                <TableRow
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:paddingTop="5dp"
            android:paddingBottom="5dp"
            android:background="@drawable/cell_shape">

     */
    // Adds all the songs present in xml/songs.xml to the table
    private void populateSongList() {
        TableLayout songsTbl = (TableLayout) findViewById(R.id.song_list);

        try {
            List<Song> songs = getEventsFromAnXML(this);
            for(Song s: songs) {
                TableRow tbrow = new TableRow(this);
                tbrow.setPadding(0, 50, 0, 50);
                tbrow.setBackgroundResource(R.drawable.cell_shape);

                TextView tv1 = new TextView(this);
                tv1.setText(s.name);
                tv1.setTextColor(Color.BLACK);
                tv1.setGravity(Gravity.LEFT);
                tbrow.addView(tv1);

                TextView t2v = new TextView(this);
                t2v.setText(s.author);
                t2v.setTextColor(Color.BLACK);
                tbrow.addView(t2v);

                TextView t3v = new TextView(this);
                t3v.setText(s.year);
                t3v.setTextColor(Color.BLACK);
                tbrow.addView(t3v);

                TextView t4v = new TextView(this);
                t4v.setText(s.time);
                t4v.setTextColor(Color.BLACK);
                t4v.setGravity(Gravity.RIGHT);
                tbrow.addView(t4v);
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
