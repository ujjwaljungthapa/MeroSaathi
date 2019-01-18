package com.electronics.invento.merosaathi;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class ProcessExternalDBHelper {
    private static final String DATABASE_NAME = "Saathi_RouteData_db_137.db";
    private static final int DATABASE_VERSION = 1;
    private static String DATABASE_PATH = "";

    private static final String DATABASE_TABLE = "Saathi_RouteData_tbl_1371";
    private static final String KEY_ROWID = "Route_Id";
    private static final String KEY_ROUTEPOINT = "Route_Id_point";
    private static final String KEY_ROUTEKEYNAME = "Route_Point_KeyName";
    private static final String KEY_ROUTEKEYJXN = "Route_Point_KeyJxn";
    private static final String KEY_STOPSNAME = "Stops_Name";
    private static final String KEY_LATITUDE = "Latitude";
    private static final String KEY_LONGITUDE = "Longitude";
    private static final String KEY_AVGDISTANCEBTW = "Average_Distance_btw_METER";
    private static final String KEY_AVERAGETIMEBUS = "Average_Arrival_Time_btw_BUS_MIN";

    private static final String DATABASE_ROUTES_TABLE = "Saathi_RouteName_tbl_1372";
    private static final String ROUTE_ROWID = "RouteName_Id";
    private static final String ROUTE_ROUTEKEYNAME = "Route_Point_KeyName";
    private static final String ROUTE_ROUTENAME = "Route_Name";

    private ExternalDbHelper ourHelper;
    private final Context ourContext;
    private SQLiteDatabase ourDatabase;

    private static class ExternalDbHelper extends SQLiteOpenHelper {

        public ExternalDbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            if (Build.VERSION.SDK_INT >= 17) {
                DATABASE_PATH = context.getApplicationInfo().dataDir + "/databases/";
            } else {
                DATABASE_PATH = "/data/data/" + context.getPackageName() + "/databases/";
            }
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }

    public ProcessExternalDBHelper(Context context) {
        ourContext = context;
    }

    public ProcessExternalDBHelper openRead() throws SQLException {
        ourHelper = new ExternalDbHelper(ourContext);
        ourDatabase = ourHelper.getReadableDatabase();
        return this;
    }

    public void close() {
        if (ourHelper != null) {
            ourHelper.close();
        }
    }


    public void createDatabase() throws IOException {
        createDB();
    }

    //I dont think its needed
   /* public void openDataBase() throws SQLException {
        String myPath = DATABASE_PATH + DATABASE_NAME;
        ourDatabase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
    }*/

    private void createDB() {
        boolean dbExists = checkDatabase();
        if (!dbExists) {
            openRead();             //replacement for this.getReadableDatabase();
            try {
                this.close();
                copyDatabase();
            } catch (IOException ie) {
                throw new Error("Error copying database");
            }
        }
    }

    private boolean checkDatabase() {
        boolean checkDB = false;
        try {
            String myPath = DATABASE_PATH + DATABASE_NAME;
            File dbfile = new File(myPath);
            checkDB = dbfile.exists();
        } catch (SQLiteException e) {

        }
        return checkDB;
    }

    private void copyDatabase() throws IOException {
        InputStream myInput = null;
        OutputStream myOutput = null;
        String outFileName = DATABASE_PATH + DATABASE_NAME;

        try {
            myInput = ourContext.getAssets().open(DATABASE_NAME);
            myOutput = new FileOutputStream(outFileName);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = myInput.read(buffer)) > 0) {
                myOutput.write(buffer, 0, length);
            }
            myOutput.flush();
            myOutput.close();
            myInput.close();
        } catch (IOException ie) {
            throw new Error("Copydatabase run error ");
        }
    }

    //For autocompletetextview
    public ArrayList<String> findallStopsName() {
        boolean distinct = true;

        ArrayList<String> mstops_name = new ArrayList<>();

        String[] columns = new String[]{KEY_STOPSNAME};
        //Cursor cursor = ourDatabase.query(DATABASE_TABLE, columns, null, null, null, null, null);
        //This is used to draw distinct data values from database table
        Cursor cursor = ourDatabase.query(distinct, DATABASE_TABLE, columns, null, null, null, null, null, null);
        int iStopsName = cursor.getColumnIndex(KEY_STOPSNAME);

        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            mstops_name.add(cursor.getString(iStopsName));
        }
        return mstops_name;
    }


    //For finding average time i.e. Average_Arrival_Time_btw_BUS_MIN
    private boolean checkdataindatabase(String stop_name) {
        String[] columns = new String[]{KEY_ROWID, KEY_STOPSNAME};
        String checkRowName = "";
        String givenRowName = stop_name;

        Cursor cursor = ourDatabase.query(DATABASE_TABLE, columns, null, null, null, null, null);

        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            checkRowName = cursor.getString(cursor.getColumnIndex(KEY_STOPSNAME));
            if (checkRowName.equals(givenRowName)) {
                return true;
            }
        }
        cursor.close();
        return false;
    }

    //
    public float findtotalAvgTime(String initial_stop, String final_stop) throws SQLException {
        Cursor cursor;
        //check how many different route can you take
        int no_different_route_buses = 0;

        String[] columns = new String[]{KEY_ROWID, KEY_STOPSNAME, KEY_AVERAGETIMEBUS};
        long row_id_initial, row_id_final;
        float totalavg = 29;                  //if jpt data entered, then system fails to know then make it half an hour
        if (checkdataindatabase(initial_stop) && checkdataindatabase(final_stop)) {
            /*Cursor cursor = ourDatabase.query(DATABASE_TABLE, columns, KEY_STOPSNAME + " = ?", new String[]{initial_stop}, null, null, null);
            if (cursor != null) {*/
            row_id_initial = get_row_id(initial_stop, final_stop, true);
            //cursor.moveToFirst();
            //row_id_initial = cursor.getLong(cursor.getColumnIndex(KEY_ROWID));
            //}
            /*cursor = ourDatabase.query(DATABASE_TABLE, columns, KEY_STOPSNAME + " = ?", new String[]{final_stop}, null, null, null);
            if (cursor != null) {*/
            row_id_final = get_row_id(initial_stop, final_stop, false);
            //cursor.moveToFirst();
            //row_id_final = cursor.getLong(cursor.getColumnIndex(KEY_ROWID));
            //}
            if (row_id_initial != -1 && row_id_final != -1) {
                if (row_id_initial < row_id_final) {
                    cursor = ourDatabase.query(DATABASE_TABLE, columns, KEY_ROWID + " BETWEEN " + row_id_initial + " AND " + row_id_final, null, null, null, KEY_ROWID);
                    int iAvgTime = cursor.getColumnIndex(KEY_AVERAGETIMEBUS);
                    totalavg = 0;   //initialization to zero for total sum
                    //cursor.isLast() is needed as per data logic BUT NOT cursor.isAfterLast() ok!
                    for (cursor.moveToFirst(); !cursor.isLast(); cursor.moveToNext()) {
                        //totalTime = totalTime + cursor.getFloat(iAvgTime);
                        totalavg = totalavg + cursor.getFloat(iAvgTime);
                    }
                    cursor.close();
                } else if (row_id_initial > row_id_final) {
                    cursor = ourDatabase.query(DATABASE_TABLE, columns, KEY_ROWID + " BETWEEN " + row_id_final + " AND " + row_id_initial, null, null, null, KEY_ROWID);
                    int iAvgTime = cursor.getColumnIndex(KEY_AVERAGETIMEBUS);
                    totalavg = 0;   //initialization to zero for total sum
                    //cursor.isLast() is needed as per data logic BUT NOT cursor.isAfterLast() ok!
                    for (cursor.moveToFirst(); !cursor.isLast(); cursor.moveToNext()) {
                        //totalTime = totalTime + cursor.getFloat(iAvgTime);
                        totalavg = totalavg + cursor.getFloat(iAvgTime);
                    }
                    cursor.close();
                } else {
                    totalavg = 0;           //IF BOTH INITIAL AND FINAL DESTINATION SAME
                }
                //return totalavg;
            } else {
                //No row found
                totalavg = 31;          //IF SYSTEM FAILS TO FIND ROUTEKEY WITH GIVEN POINTS
                //return totalavg;
            }
        }
        return totalavg;
    }

    //Multiple routes can have same stop.
    //So to find proper stop name w.r.t. chosen route proper row_id of the stop_name is to be found
    private long get_row_id(String initial_stop, String final_stop, boolean a) {
        boolean initial_or_final = true;        //true is for find initial stop id, false is for find final stop id
        Cursor cursor;
        long mid = -1;
        String stopsKey = "";
        String[] columns = new String[]{KEY_ROWID, KEY_ROUTEKEYNAME, KEY_STOPSNAME};
        ArrayList<String> mstops_key = new ArrayList<>();

        mstops_key = findallStopsKey(initial_stop, final_stop);
        //For showing multiple route buses
        /*for (int i = 0; i < mstops_key.size(); i++) {
            Cursor cursor = ourDatabase.query(DATABASE_TABLE, columns, KEY_STOPSNAME + " = ?", new String[]{initial_stop}, null, null, null);

        }*/
        initial_or_final = a;
        if (mstops_key.size() > 0) {
            stopsKey = mstops_key.get(0);               //index = 0 is taken since taking any different route will yield result to be same for multiple loop calculate NOT NEEDED
            if (!initial_or_final) {
                cursor = ourDatabase.query(DATABASE_TABLE, columns, KEY_ROUTEKEYNAME + " = ? AND " + KEY_STOPSNAME + " = ?", new String[]{stopsKey, final_stop}, null, null, null);
                if (cursor != null) {
                    cursor.moveToFirst();
                    mid = cursor.getLong(cursor.getColumnIndex(KEY_ROWID));
                }
                cursor.close();
            } else {
                cursor = ourDatabase.query(DATABASE_TABLE, columns, KEY_ROUTEKEYNAME + " = ? AND " + KEY_STOPSNAME + " = ?", new String[]{stopsKey, initial_stop}, null, null, null);
                if (cursor != null) {
                    cursor.moveToFirst();
                    mid = cursor.getLong(cursor.getColumnIndex(KEY_ROWID));
                }
                cursor.close();
            }
        }
        return mid;
    }

    //key gives how many different types of routes are available between two stops
    public ArrayList<String> findallStopsKey(String initial_stop, String final_stop) {
        ArrayList<String> mmstops_key = new ArrayList<>();
        String stopsKeya, stopsKeyb;
        //int keyCount = 0;

        String[] columns = new String[]{KEY_ROUTEKEYNAME, KEY_STOPSNAME};

        Cursor cursor1 = ourDatabase.query(DATABASE_TABLE, columns, KEY_STOPSNAME + " = ?", new String[]{initial_stop}, null, null, null);
        Cursor cursor2 = ourDatabase.query(DATABASE_TABLE, columns, KEY_STOPSNAME + " = ?", new String[]{final_stop}, null, null, null);

        int iStopsKey = cursor1.getColumnIndex(KEY_ROUTEKEYNAME);

        for (cursor1.moveToFirst(); !cursor1.isAfterLast(); cursor1.moveToNext()) {
            stopsKeya = cursor1.getString(iStopsKey);
            for (cursor2.moveToFirst(); !cursor2.isAfterLast(); cursor2.moveToNext()) {
                stopsKeyb = cursor2.getString(iStopsKey);
                if (stopsKeya.equals(stopsKeyb)) {
                    //keyCount++;
                    mmstops_key.add(cursor2.getString(iStopsKey));
                }
            }
        }
        cursor1.close();        //resources should be closed :)
        cursor2.close();        //resources should be closed :)
        return mmstops_key;
    }//END OF findallStopsKey() METHOD;

    //ALITER
    /*private ArrayList<String> findallStopsKey(String initial_stop, String final_stop) {
        ArrayList<String> mmstops_key = new ArrayList<>();
        //int keyCount = 0;

        String[] columns = new String[]{KEY_ROUTEKEYNAME};

        //AND statement in the where clause did not work :(
        Cursor cursor = ourDatabase.query(DATABASE_TABLE, columns, KEY_STOPSNAME + " = ? AND " + KEY_STOPSNAME + " = ?", new String[]{initial_stop, final_stop}, null, null, null);

        int iStopsKey = cursor.getColumnIndex(KEY_ROUTEKEYNAME);

        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                    //keyCount++;
                    mmstops_key.add(cursor.getString(iStopsKey));
        }
        return mmstops_key;
    }*///END OF findallStopsKey() METHOD;

    //
    public float findtotalDistance(String initial_stop, String final_stop) {
        Cursor cursor;
        String[] columns = new String[]{KEY_ROWID, KEY_STOPSNAME, KEY_AVGDISTANCEBTW};
        long row_id_initial, row_id_final;
        float totalavgd = 0;                //if system fails to know then make it 0 meter
        if (checkdataindatabase(initial_stop) && checkdataindatabase(final_stop)) {
            /*Cursor cursor = ourDatabase.query(DATABASE_TABLE, columns, KEY_STOPSNAME + " = ?", new String[]{initial_stop}, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                row_id_initial = cursor.getLong(cursor.getColumnIndex(KEY_ROWID));
            }
            cursor = ourDatabase.query(DATABASE_TABLE, columns, KEY_STOPSNAME + " = ?", new String[]{final_stop}, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                row_id_final = cursor.getLong(cursor.getColumnIndex(KEY_ROWID));
            }*/

            row_id_initial = get_row_id(initial_stop, final_stop, true);
            row_id_final = get_row_id(initial_stop, final_stop, false);

            if (row_id_initial != -1 && row_id_final != -1) {
                if (row_id_initial < row_id_final) {
                    cursor = ourDatabase.query(DATABASE_TABLE, columns, KEY_ROWID + " BETWEEN " + row_id_initial + " AND " + row_id_final, null, null, null, KEY_ROWID);
                    int iAvgDistance = cursor.getColumnIndex(KEY_AVGDISTANCEBTW);
                    totalavgd = 0;   //initialization to zero for total sum
                    //cursor.isLast() is needed as per data logic BUT NOT cursor.isAfterLast() ok!
                    for (cursor.moveToFirst(); !cursor.isLast(); cursor.moveToNext()) {
                        //totalTime = totalTime + cursor.getFloat(iAvgTime);
                        totalavgd = totalavgd + cursor.getFloat(iAvgDistance);
                    }
                } else if (row_id_initial > row_id_final) {
                    cursor = ourDatabase.query(DATABASE_TABLE, columns, KEY_ROWID + " BETWEEN " + row_id_final + " AND " + row_id_initial, null, null, null, KEY_ROWID);
                    int iAvgDistance = cursor.getColumnIndex(KEY_AVGDISTANCEBTW);
                    totalavgd = 0;   //initialization to zero for total sum
                    //cursor.isLast() is needed as per data logic BUT NOT cursor.isAfterLast() ok!
                    for (cursor.moveToFirst(); !cursor.isLast(); cursor.moveToNext()) {
                        //totalTime = totalTime + cursor.getFloat(iAvgTime);
                        totalavgd = totalavgd + cursor.getFloat(iAvgDistance);
                    }
                } else {
                    totalavgd = 0;      //IF BOTH INITIAL AND FINAL DESTINATION SAME
                }
            } else {
                //No row found
                totalavgd = 0;          //IF SYSTEM FAILS TO FIND ROUTEKEY WITH GIVEN POINTS
            }
        }
        return totalavgd;
    } //END OF findtotalDistance() METHOD;

    //WHICH ROUTE BUSES ARE AVAILABLE FOR PEOPLE TO USE
    public ArrayList<String> findneededRoutesName(ArrayList<String> stops_key) {
        ArrayList<String> mroutes_name = new ArrayList<>();
        Cursor cursor;

        String[] columns = new String[]{ROUTE_ROUTEKEYNAME, ROUTE_ROUTENAME};

        for (int i = 0; i < stops_key.size(); i++) {
            cursor = ourDatabase.query(DATABASE_ROUTES_TABLE, columns, ROUTE_ROUTEKEYNAME + " = ?", new String[]{stops_key.get(i)}, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                mroutes_name.add(cursor.getString(cursor.getColumnIndex(ROUTE_ROUTENAME)));
            }
        }
        return mroutes_name;
    }//END OF findneededRoutesName() METHOD;


    //WHEN FOR A GIVEN ROUTE ONE HAS TO CHOOSE DIFFERENT BUSES OF DIFFERENT ROUTE i.e. change buses(USE TOTAL 2 BUSES)
    //returns intersecting stops name between two routes but to be given parameter is 2 stops_name of two different routes
    public ArrayList<String> findIntersectionRoute(String initial_stop, String final_stop) {
        ArrayList<String> mintersecting_stops = new ArrayList<>();
        ArrayList<String> initial_rowkey_array = new ArrayList<>();
        ArrayList<String> final_rowkey_array = new ArrayList<>();

        String stopa, stopb;

        String[] columns = new String[]{KEY_ROUTEKEYNAME, KEY_STOPSNAME};

        Cursor cursor1 = ourDatabase.query(DATABASE_TABLE, columns, KEY_STOPSNAME + " = ?", new String[]{initial_stop}, null, null, null);
        Cursor cursor2 = ourDatabase.query(DATABASE_TABLE, columns, KEY_STOPSNAME + " = ?", new String[]{final_stop}, null, null, null);

        if ((cursor1 != null) && (cursor2 != null)) {
            //FIRST FIND ALL ROUTES
            for (cursor1.moveToFirst(); !cursor1.isAfterLast(); cursor1.moveToNext()) {
                initial_rowkey_array.add(cursor1.getString(cursor1.getColumnIndex(KEY_ROUTEKEYNAME)));
            }
            for (cursor2.moveToFirst(); !cursor2.isAfterLast(); cursor2.moveToNext()) {
                final_rowkey_array.add(cursor2.getString(cursor2.getColumnIndex(KEY_ROUTEKEYNAME)));
            }

            //FIND INTERSECTION OF ROUTES
            for (int i = 0; i < initial_rowkey_array.size(); i++) {         //for i = 0 and for j = 0 should be consecutive??????
                cursor1 = ourDatabase.query(DATABASE_TABLE, columns, KEY_ROUTEKEYNAME + " = ?", new String[]{initial_rowkey_array.get(i)}, null, null, null);
                for (cursor1.moveToFirst(); !cursor1.isAfterLast(); cursor1.moveToNext()) {
                    stopa = cursor1.getString(cursor1.getColumnIndex(KEY_STOPSNAME));
                    for (int j = 0; j < final_rowkey_array.size(); j++) {
                        cursor2 = ourDatabase.query(DATABASE_TABLE, columns, KEY_ROUTEKEYNAME + " = ?", new String[]{final_rowkey_array.get(j)}, null, null, null);
                        for (cursor2.moveToFirst(); !cursor2.isAfterLast(); cursor2.moveToNext()) {
                            stopb = cursor2.getString(cursor2.getColumnIndex(KEY_STOPSNAME));
                            if (stopb.equals(stopa)) {
                                mintersecting_stops.add(stopb);
                            }
                        }
                    }
                }
            }

            //HEY WILL HERE OCCUR DUPLICATE INTERSECTINGSTOPS?????

            cursor1.close();        //resources should be closed :)
            cursor2.close();        //resources should be closed :)
        }
        return mintersecting_stops;
    }//END OF findIntersectionRoute() METHOD

    //FIND THAT BEST STOPS_NAME WHICH HAS MINIMUM DISTANCE WITH FINAL DESTINATION
    public String findTransitionStop(String final_stop, ArrayList<String> intersectingStops) {
        String transitionStop = "";
        float a, b;
        int minimum_index = 0;          //since b has value of index = 0 at first
        b = findtotalDistance(intersectingStops.get(0), final_stop);
        for (int i = 1; i < intersectingStops.size(); i++) {
            a = findtotalDistance(intersectingStops.get(i), final_stop);
            if (a < b) {
                b = a;        //NOT NEEDED
                minimum_index = i;
            }
        }   //final value of b is the index to intersectingStops with the minimum distance to final
        transitionStop = intersectingStops.get(minimum_index);
        return transitionStop;
    }   //END OF findTransitionStop()METHOD


    //WHEN FOR A GIVEN ROUTE ONE HAS TO CHOOSE DIFFERENT BUSES OF DIFFERENT ROUTE i.e. change buses(USE TOTAL 3 BUSES)
    public ArrayList<String> findMiddleRouteKey(String initial_stop, String final_stop) {
        ArrayList<String> middleRoutesKey = new ArrayList<>();
        ArrayList<String> tempRoutesKey = new ArrayList<>();
        ArrayList<String> initial_rowkey_array = new ArrayList<>();
        ArrayList<String> final_rowkey_array = new ArrayList<>();

        Set<String> hs = new HashSet<>();

        String stopa, stopb;

        String[] columns = new String[]{KEY_ROUTEKEYNAME, KEY_STOPSNAME};

        Cursor cursor1 = ourDatabase.query(DATABASE_TABLE, columns, KEY_STOPSNAME + " = ?", new String[]{initial_stop}, null, null, null);
        Cursor cursor2 = ourDatabase.query(DATABASE_TABLE, columns, KEY_STOPSNAME + " = ?", new String[]{final_stop}, null, null, null);

        if ((cursor1 != null) && (cursor2 != null)) {
            //FIRST FIND ALL ROUTES
            for (cursor1.moveToFirst(); !cursor1.isAfterLast(); cursor1.moveToNext()) {
                initial_rowkey_array.add(cursor1.getString(cursor1.getColumnIndex(KEY_ROUTEKEYNAME)));
            }
            for (cursor2.moveToFirst(); !cursor2.isAfterLast(); cursor2.moveToNext()) {
                final_rowkey_array.add(cursor2.getString(cursor2.getColumnIndex(KEY_ROUTEKEYNAME)));
            }

            //FIND INTERSECTION OF ROUTES
            for (int i = 0; i < initial_rowkey_array.size(); i++) {
                cursor1 = ourDatabase.query(DATABASE_TABLE, columns, KEY_ROUTEKEYNAME + " = ?", new String[]{initial_rowkey_array.get(i)}, null, null, null);
                for (cursor1.moveToFirst(); !cursor1.isAfterLast(); cursor1.moveToNext()) {
                    stopa = cursor1.getString(cursor1.getColumnIndex(KEY_STOPSNAME));
                    for (int j = 0; j < final_rowkey_array.size(); j++) {
                        cursor2 = ourDatabase.query(DATABASE_TABLE, columns, KEY_ROUTEKEYNAME + " = ?", new String[]{final_rowkey_array.get(j)}, null, null, null);
                        for (cursor2.moveToFirst(); !cursor2.isAfterLast(); cursor2.moveToNext()) {
                            stopb = cursor2.getString(cursor2.getColumnIndex(KEY_STOPSNAME));
                            if ((tempRoutesKey = findallStopsKey(stopa, stopb)).size() > 0) {         //find if there exists any route between the 2 stops of for loop
                                //middleRoutesKey = findallStopsKey(stopa, stopb);   //not needed    //first have an arraylist of arraylist which keeps list of arrays of routes keys that can be used
//                                for (int start = 0; start < tempRoutesKey.size(); start++) {    //this is done so that middleRoutesKey is only arraylist but not arraylist of arraylist so that i can remove duplicate values of arraylist
//                                    middleRoutesKey.add(tempRoutesKey.get(start));          //
//                                }
                                middleRoutesKey.addAll(tempRoutesKey);
                            }
                        }
                    }
                }
            }

            //remove duplicate entries in the arraylist
            hs.addAll(middleRoutesKey);
            middleRoutesKey.clear();
            middleRoutesKey.addAll(hs);

            cursor1.close();
            cursor2.close();
        }
        return middleRoutesKey;
    }// END OF findMiddleRouteKey() METHOD

    //find all intersecting stops between middle routes and final stop
    public ArrayList<String> findMiddleRouteIntersectingStops(String final_stop, ArrayList<String> middleRoutesKey) {
        ArrayList<String> middleIntersectingStops = new ArrayList<>();
        ArrayList<String> tempIntersectingStops = new ArrayList<>();
        String tempMiddleStop;      //this takes INITIAL stops_name of the middle route

        Cursor cursor1;

        Set<String> hs = new HashSet<>();

        String[] columns = new String[]{KEY_ROUTEKEYNAME, KEY_STOPSNAME};

        for (int start = 0; start < middleRoutesKey.size(); start++) {
            cursor1 = ourDatabase.query(DATABASE_TABLE, columns, KEY_ROUTEKEYNAME + " = ?", new String[]{middleRoutesKey.get(start)}, null, null, null);
            if (cursor1 != null) {
                //for (cursor1.moveToFirst(); !cursor1.isAfterLast(); cursor1.moveToNext()) {
                cursor1.moveToFirst();  //using for loop above is redundant
                tempMiddleStop = cursor1.getString(cursor1.getColumnIndex(KEY_STOPSNAME));
                tempIntersectingStops = findIntersectionRoute(tempMiddleStop, final_stop);
                if (tempIntersectingStops.size() > 0) {
                        /*for (int index_point = 0; index_point < tempIntersectingStops.size(); index_point++) {
                            middleIntersectingStops.add(tempIntersectingStops.get(index_point));
                        }*/
                    middleIntersectingStops.addAll(tempIntersectingStops);
                }
                //}

                //remove duplicate entries in the arraylist
                hs.addAll(middleIntersectingStops);
                middleIntersectingStops.clear();
                middleIntersectingStops.addAll(hs);
                cursor1.close();
            }
        }

        return middleIntersectingStops;
    }// END OF findMiddleRouteIntersectingStops() METHOD
}