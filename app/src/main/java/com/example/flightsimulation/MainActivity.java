package com.example.flightsimulation;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    DatabaseHelper db;
    ArrayList<Waypoints> WpList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Mission Planning");
        }
        mToolbar.setTitleTextColor(Color.WHITE);

        db = new DatabaseHelper(getApplicationContext());
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            if (Build.VERSION.SDK_INT > 22) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }

        }

    }

    public void openFlightPath(View view){
        Intent intent = new Intent(this, FlightPath.class);
        startActivity(intent);
    }

    public void editFlightPath(View view){
        WpList = db.getCurrentMissionData();
        if (WpList.size() > 0) {
            Intent intent = new Intent(this, VerifyFlightPath.class);
            startActivity(intent);
        } else {
            AlertDialog.Builder accept = new AlertDialog.Builder(MainActivity.this);
            accept.setTitle("No Mission Data")
                    .setMessage("Please Create a Mission First")
                    .setPositiveButton("OK", (dialog, id) -> dialog.cancel());
            AlertDialog alert = accept.create();
            alert.show();
        }
    }
}
