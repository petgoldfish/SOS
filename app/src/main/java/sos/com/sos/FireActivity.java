package sos.com.sos;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;

public class FireActivity extends AppCompatActivity {

    TextView textView;
    private static final String FIREBASE_URL = "https://torrid-fire-5006.firebaseio.com";
    private Firebase mFirebaseRef;


    Button retryButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fire);
        setTitle("Fire");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        GPSTracker gps = new GPSTracker(this);
        textView = (TextView) findViewById(R.id.textView);
        retryButton = (Button) findViewById(R.id.retryButton);
        retryButton.setVisibility(View.INVISIBLE);

        if (gps.isGPSEnabled) {




            mFirebaseRef = new Firebase(FIREBASE_URL).child("fire");
            mFirebaseRef.keepSynced(true);


            if (gps.canGetLocation()) {
                Fire ob = new Fire(gps.getLatitude() + "", gps.getLongitude() + "");
                mFirebaseRef.push().setValue(ob);
                textView.setText("Firetruck Alerted");
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
