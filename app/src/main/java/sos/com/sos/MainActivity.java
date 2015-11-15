package sos.com.sos;


import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.parse.Parse;
import com.parse.ParseInstallation;


public class MainActivity extends AppCompatActivity {

    private ValueEventListener mConnectedListener;
    private static final String FIREBASE_URL = "https://torrid-fire-5006.firebaseio.com";
    private Firebase mFirebaseRef;
    public static final String PARSE_APP_ID = "6lNlmGr70HHy9cLZcmGTk4QXxuLzobNtcm3fTwwQ";
    public static final String PARSE_CLIENT_KEY = "hrIINY23wceLgNlyyUciskBdcZQBlq8zqiqDBYah";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFirebaseRef = new Firebase(FIREBASE_URL).child("ambulance");
        mFirebaseRef.keepSynced(true);
        Parse.initialize(this, PARSE_APP_ID, PARSE_CLIENT_KEY);
        ParseInstallation.getCurrentInstallation().saveInBackground();
        mConnectedListener = mFirebaseRef.getRoot().child(".info/connected").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean connected = (Boolean) dataSnapshot.getValue();
                if (connected) {
                    Toast.makeText(getApplicationContext(), "Connected to Firebase",Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Disconnected from Firebase",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                // No-op
            }
        });

    }


    public void report(View view) {

        startActivity(new Intent(MainActivity.this, ReportActivity.class));


    }

    public void ambulance(View view){
        new MaterialDialog.Builder(this)
                .title("Are you sure?")
                .content("An ambulance will be called")
                .positiveText("Yes")
                .negativeText("Cancel").onPositive(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {
                GPSTracker gpsTracker= new GPSTracker(getApplicationContext());
                if (gpsTracker.isGPSEnabled){
                    startActivity(new Intent(MainActivity.this,AmbulanceActivity.class));
                }
                else
                    gpsTracker.showSettingsAlert();

            }
        }).onNegative(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {

            }
        }).show();



    }

    public void fire(View view) {
        new MaterialDialog.Builder(this)
                .title("Are you sure?")
                .content("A firetruck will be called")
                .positiveText("Yes")
                .negativeText("Cancel").onPositive(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {
                GPSTracker gpsTracker= new GPSTracker(getApplicationContext());
                if (gpsTracker.isGPSEnabled){
                    startActivity(new Intent(MainActivity.this,FireActivity.class));
                }
                else
                    gpsTracker.showSettingsAlert();

            }
        }).onNegative(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {

            }
        }).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mFirebaseRef.getRoot().child(".info/connected").removeEventListener(mConnectedListener);
    }




}
