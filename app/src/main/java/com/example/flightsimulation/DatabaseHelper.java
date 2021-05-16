package com.example.flightsimulation;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Log tag
    private static final String LOG = DatabaseHelper.class.getName();

    // Database Version
    private static final int DATABASE_VERSION = 2;

    // Database Name
    private static final String DATABASE_NAME = "Missions";

    // Table Name
    private static final String TABLE_CurrentMission = "CurrentMission";

    // Column Names
    private static final String KEY_WaypointCount = "WaypointCount";
    private static final String KEY_Lat = "Lat";
    private static final String KEY_Lng = "Lng";
    private static final String KEY_Alt = "Alt";

    //Table Create Statements
    private static final String CREATE_TABLE_CurrentMission = "CREATE TABLE " + TABLE_CurrentMission
            + "(" + KEY_WaypointCount + " INTEGER PRIMARY KEY," + KEY_Lat + " REAL," + KEY_Lng
            + " REAL," + KEY_Alt + " REAL" + ")";


    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //creating required tables
        db.execSQL(CREATE_TABLE_CurrentMission);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CurrentMission);

        // create new tables
        onCreate(db);
    }

    // Creating a Current Mission Entry
    public void createCurrentMissionData (ArrayList<Waypoints> WpList) {
        SQLiteDatabase db = this.getWritableDatabase();

        for(int count = 0;count<WpList.size();count++) {
            ContentValues values = new ContentValues();
            Waypoints WP = WpList.get(count);
            values.put(KEY_WaypointCount, count+1);
            values.put(KEY_Lat, WP.getLat());
            values.put(KEY_Lng, WP.getLng());
            values.put(KEY_Alt, WP.getalt());
            db.insert(TABLE_CurrentMission, null, values);
        }
        db.close();

    }


    // Getting Current Mission Data
    public ArrayList<Waypoints> getCurrentMissionData() {
        ArrayList<Waypoints> WpList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_CurrentMission;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if(c.moveToFirst()) {
            do {
                Waypoints wp = new Waypoints();
                wp.setLat(c.getDouble(c.getColumnIndex(KEY_Lat)));
                wp.setLng(c.getDouble(c.getColumnIndex(KEY_Lng)));
                wp.setalt(c.getDouble(c.getColumnIndex(KEY_Alt)));

                // adding to list
                WpList.add(wp);
            } while (c.moveToNext());
        }
        c.close();

        return WpList;
    }

    // Delete Current Mission
    public void deleteCurrentMission() {
        SQLiteDatabase db = this.getWritableDatabase();

        // delete
        db.delete(TABLE_CurrentMission, null, null);
    }
}
