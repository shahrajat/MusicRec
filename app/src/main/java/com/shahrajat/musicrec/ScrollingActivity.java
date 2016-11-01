package com.shahrajat.musicrec;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
import org.xmlpull.v1.XmlPullParserFactory;

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
                Snackbar.make(view, "Playing Song now...", Snackbar.LENGTH_LONG)
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

    // Adds all the songs present in xml/songs.xml to the table

    /*

    <TextView
                android:id="@+id/textView1"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:text="We don't talk anymore"
                android:layout_column="1"
                android:textAppearance="?android:attr/textAppearanceMedium"></TextView>
     */

    private void populateSongList() {
        TableLayout stk = (TableLayout) findViewById(R.id.song_list);
        TableLayout songsTbl = (TableLayout) findViewById(R.id.song_list);
        TextView errorBox = (TextView) findViewById(R.id.activityName);
        // All static variables
        //final String URL = "http://api.androidhive.info/pizza/?format=xml";
        // XML node keys
        final String KEY_ITEM = "item"; // parent node
        final String KEY_NAME = "name";
        final String KEY_COST = "cost";
        final String KEY_DESC = "description";

        XMLParser parser = new XMLParser();
        String xml = parser.getXmlFromUrl(errorBox); // getting XML
        Document doc = parser.getDomElement(xml); // getting DOM element
        if(doc == null) {

            return;
        }
        NodeList nl = doc.getElementsByTagName(KEY_ITEM);

        // looping through all item nodes <item>
        for (int i = 0; i < nl.getLength(); i++) {
            Element e = (Element) nl.item(i);
            String name = parser.getValue(e, KEY_NAME); // name child value
            String cost = parser.getValue(e, KEY_COST); // cost child value
            String description = parser.getValue(e, KEY_DESC); // description child value

            TableRow tbrow = new TableRow(this);
            TextView t2v = new TextView(this);
            t2v.setText("We don't talk anymore");
            t2v.setTextColor(Color.BLACK);
            t2v.setGravity(Gravity.CENTER);
            tbrow.addView(t2v);
            songsTbl.addView(tbrow);
        }

        for (int i = 0; i < 25; i++) {
            TableRow tbrow = new TableRow(this);

            TextView t2v = new TextView(this);
            t2v.setText("We don't talk anymore");
            t2v.setTextColor(Color.BLACK);
            t2v.setGravity(Gravity.CENTER);
            tbrow.addView(t2v);
            TextView t3v = new TextView(this);
            t3v.setText("Charlie Puth");
            t3v.setTextColor(Color.BLACK);
            t3v.setGravity(Gravity.CENTER);
            tbrow.addView(t3v);
            TextView t4v = new TextView(this);
            t4v.setText("4.05");
            t4v.setTextColor(Color.BLACK);
            t4v.setGravity(Gravity.CENTER);
            tbrow.addView(t4v);
            songsTbl.addView(tbrow);
        }
        songsTbl.requestLayout();
    }
}
