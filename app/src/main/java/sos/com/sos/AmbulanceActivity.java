package sos.com.sos;

import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.ValueEventListener;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParseQuery;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

public class AmbulanceActivity extends AppCompatActivity {

    TextView textView;
    private static final String FIREBASE_URL = "https://torrid-fire-5006.firebaseio.com";
    private Firebase mFirebaseRef;


    Button retryButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ambulance);
        setTitle("Ambulance");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        GPSTracker gps = new GPSTracker(this);
        textView = (TextView) findViewById(R.id.textView);
        retryButton = (Button) findViewById(R.id.retryButton);
        retryButton.setVisibility(View.INVISIBLE);

        if (gps.isGPSEnabled) {




            mFirebaseRef = new Firebase(FIREBASE_URL).child("ambulance");
            mFirebaseRef.keepSynced(true);


            if (gps.canGetLocation()) {
                Calendar c =Calendar.getInstance();
                int s= c.get(Calendar.SECOND);
                int m= c.get(Calendar.MINUTE);
                int h=c.get(Calendar.HOUR);
                String time=h+":"+m+":"+s;
                Ambulance ob = new Ambulance(gps.getLatitude() + "", gps.getLongitude() + "", time);
                mFirebaseRef.push().setValue(ob);
                JSONObject jsonObject = new JSONObject();
                ParsePush push = new ParsePush();
                ParseQuery query = ParseInstallation.getQuery();
                try {
                    jsonObject.put("is_background", false);
                    jsonObject.put("type", true);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                query.whereEqualTo("deviceType", "android");
                push.setQuery(query);
                push.setData(jsonObject);
                push.sendInBackground();
                textView.setText("Ambulance Alerted");
            } else
                Toast.makeText(getApplicationContext(), "Unable to get location", Toast.LENGTH_LONG).show();
        }
        else
        {
            gps.showSettingsAlert();
            retryButton.setVisibility(View.VISIBLE);

        }

    }

    public void okay(View view) {
        finish();
    }

    public void retry(View view) {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }
}
