package com.electronics.invento.merosaathi;

import android.app.Dialog;
import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

public class TransportMainActivity extends AppCompatActivity implements View.OnClickListener {

    Button button_sqlsearch_bus, button_sqlsearch_taxi;
    AutoCompleteTextView edittext_finaldestination, editext_initialstop;
    ImageView image, image1;

    private ArrayList<String> stops_name = new ArrayList<>();
    private ArrayList<String> stops_key = new ArrayList<>();
    private ArrayList<String> routes_name = new ArrayList<>();

    Bundle basket = new Bundle();

    Button button_taxiContinue, button_busContinue, button_okReset;
    Dialog MyDialog;
    ImageView imageView_CloseTaxi, imageView_CloseBus, imageView_closePop;

    float totalAverageTime, totalDistance;
    float totalAverageTime_initial, totalDistance_initial, totalAverageTime_final, totalDistance_final;
    float totalAverageTime_midinitial, totalAverageTime_middle, totalAverageTime_midfinal, totalDistance_midinitial, totalDistance_middle, totalDistance_midfinal;

    ArrayList<String> intersectingStops;
    ArrayList<String> routes_name_initial;  // = new ArrayList<>();     //redundant
    ArrayList<String> routes_name_final; // = new ArrayList<>();        //redundant
    String transitionStop;
    ArrayList<String> intersectingStops_final;
    ArrayList<String> middleRoutesKey;
    String transitionStop_initial, transitionStop_final;
    ArrayList<String> routes_name_middle;
    ArrayList<String> routes_name_midinitial;
    ArrayList<String> routes_name_midfinal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transport_main);

        ProcessExternalDBHelper myDbHelper = new ProcessExternalDBHelper(this);
        try {
            myDbHelper.createDatabase();
        } catch (IOException ioe) {
            throw new Error("Unable to CREATE DATABASE");
        } finally {      //resources should be closed right??
            myDbHelper.close();
        }
        //I dont think its needed
        /*try{
            myDbHelper.openDataBase();
        }catch (SQLException sqle){
            throw sqle;
        }*/

        setUP();

        autocompleteInitialize();


        ArrayAdapter<String> adapter_forauto = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, stops_name);
        editext_initialstop.setAdapter(adapter_forauto);
        ArrayAdapter<String> adapter_forauto1 = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, stops_name);
        edittext_finaldestination.setAdapter(adapter_forauto1);

        button_sqlsearch_bus.setOnClickListener(this);
        button_sqlsearch_taxi.setOnClickListener(this);
        image.setOnClickListener(this);
        image1.setOnClickListener(this);


        // button_busPopUp.setOnClickListener(this);
    }

    private void autocompleteInitialize() {
        try {
            ProcessExternalDBHelper autoProcess = new ProcessExternalDBHelper(this);
            autoProcess.openRead();
            stops_name = autoProcess.findallStopsName();
            autoProcess.close();
        } catch (Exception e) {
            String error = e.toString();
            Dialog d = new Dialog(this);
            d.setTitle("Failed");
            TextView tv = new TextView(this);
            tv.setText(error);
            d.setContentView(tv);
            d.show();
        }
    }

    private void setUP() {
        edittext_finaldestination = findViewById(R.id.etSQL_finaldestination);
        editext_initialstop = findViewById(R.id.etSQL_initalstop);
        edittext_finaldestination.setThreshold(1);
        editext_initialstop.setThreshold(1);

        image = findViewById(R.id.image);
        image1 = findViewById(R.id.image1);

        button_sqlsearch_bus = findViewById(R.id.bSQLsearch);
        button_sqlsearch_taxi = findViewById(R.id.bSQLsearch_taxi);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.image:
                editext_initialstop.showDropDown();
                break;
            case R.id.image1:
                edittext_finaldestination.showDropDown();
                break;
            case R.id.bSQLsearch:
                String initial_stop = editext_initialstop.getText().toString();
                String final_stop = edittext_finaldestination.getText().toString();

                if (!(initial_stop.isEmpty()) && !(final_stop.isEmpty())) {
                    if ((stops_name.contains(initial_stop)) && (stops_name.contains(final_stop))) {
                        processForBus(initial_stop, final_stop);
                    } else {   //END OF --- if ((stops_name.contains(initial_stop)) && (stops_name.contains(final_stop)))
                        showWarningPopUp();
                    }
                }   //END OF --- if (!(initial_stop.isEmpty()) && !(final_stop.isEmpty()))

                break;
            case R.id.bSQLsearch_taxi:
                initial_stop = editext_initialstop.getText().toString();
                final_stop = edittext_finaldestination.getText().toString();
                if (!(initial_stop.isEmpty()) && !(final_stop.isEmpty())) {
                    if ((stops_name.contains(initial_stop)) && (stops_name.contains(final_stop))) {
                        processForTaxi(initial_stop, final_stop);
                    } else {   //END OF --- if ((stops_name.contains(initial_stop)) && (stops_name.contains(final_stop)))
                        showWarningPopUp();
                    }
                }   //END OF --- if (!(initial_stop.isEmpty()) && !(final_stop.isEmpty()))

                break;
        }

    }

    private void callNextInformationActivity(String initial_stop, String final_stop, float totalAverageTime, float totalDistance, ArrayList<String> routes_name, String mode) {
        //Bundle basket = new Bundle();
        basket.putString("mode", mode);
        basket.putString("initial", initial_stop);
        basket.putString("final", final_stop);
        basket.putFloat("avgTime", totalAverageTime);
        basket.putFloat("avgDistance", totalDistance);
        basket.putStringArrayList("routeArray", routes_name);
    }

    private void callNextInformationActivity(String initial_stop, String transition_stop, String final_stop,
                                             float totalAverageTime_initial, float totalDistance_initial, ArrayList<String> routes_name_initial,
                                             float totalAverageTime_final, float totalDistance_final, ArrayList<String> routes_name_final, String mode) {
        basket.putString("mode", mode);
        basket.putString("initial", initial_stop);
        basket.putString("transition", transition_stop);
        basket.putString("final", final_stop);
        basket.putFloat("avgTime_initial", totalAverageTime_initial);
        basket.putFloat("avgDistance_initial", totalDistance_initial);
        basket.putStringArrayList("routeArray_initial", routes_name_initial);
        basket.putFloat("avgTime_final", totalAverageTime_final);
        basket.putFloat("avgDistance_final", totalDistance_final);
        basket.putStringArrayList("routeArray_final", routes_name_final);
    }

    private void callNextInformationActivity(String initial_stop, String transition_stop_initial, String transition_stop_final, String final_stop,
                                             float totalAverageTime_initial, float totalDistance_initial, ArrayList<String> routes_name_initial,
                                             float totalAverageTime_middle, float totalDistance_middle, ArrayList<String> routes_name_middle,
                                             float totalAverageTime_final, float totalDistance_final, ArrayList<String> routes_name_final, String mode) {
        basket.putString("mode", mode);

        basket.putString("initial", initial_stop);
        basket.putString("transition_initial", transition_stop_initial);
        basket.putString("transition_final", transition_stop_final);
        basket.putString("final", final_stop);

        basket.putFloat("avgTime_initial", totalAverageTime_initial);
        basket.putFloat("avgDistance_initial", totalDistance_initial);
        basket.putStringArrayList("routeArray_initial", routes_name_initial);

        basket.putFloat("avgTime_middle", totalAverageTime_middle);
        basket.putFloat("avgDistance_middle", totalDistance_middle);
        basket.putStringArrayList("routeArray_middle", routes_name_middle);

        basket.putFloat("avgTime_final", totalAverageTime_final);
        basket.putFloat("avgDistance_final", totalDistance_final);
        basket.putStringArrayList("routeArray_final", routes_name_final);
    }

    private void callNextInformationTaxiActivity(String initial_stop, String final_stop, float totalAverageTime, float totalDistance) {
        //Bundle basket = new Bundle();
        basket.putString("initial", initial_stop);
        basket.putString("final", final_stop);
        basket.putFloat("avgTime", totalAverageTime);
        basket.putFloat("avgDistance", totalDistance);
    }

    private void showTaxiPopUp() {
        MyDialog = new Dialog(TransportMainActivity.this);
        MyDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        MyDialog.setContentView(R.layout.epic_popup_negative);

        button_taxiContinue = MyDialog.findViewById(R.id.btn_ContinueTaxi);
        imageView_CloseTaxi = MyDialog.findViewById(R.id.closePopupNegative);

        button_taxiContinue.setEnabled(true);
        imageView_CloseTaxi.setEnabled(true);

        button_taxiContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(),"Taxi Clicked",Toast.LENGTH_SHORT).show();
                MyDialog.cancel();
                /*Intent intent = new Intent(MainActivity.this, SearchTaxiActivity.class);
                intent.putExtras(basket);
                startActivity(intent);*/
                Intent intent = new Intent(TransportMainActivity.this, SearchViewTaxiActivity.class);
                intent.putExtras(basket);
                startActivity(intent);
            }
        });
        imageView_CloseTaxi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDialog.cancel();
            }
        });

        if (MyDialog.getWindow() != null) {         //used this since it was telling me that setBackgroundDrawable my produce nullpointer exception
            MyDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            MyDialog.show();
        } else {
            MyDialog.cancel();
        }
    }

    private void showBusPopUp() {
        MyDialog = new Dialog(TransportMainActivity.this);
        MyDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        MyDialog.setContentView(R.layout.epic_popup_positive);

        button_busContinue = MyDialog.findViewById(R.id.btn_ContinueBus);
        imageView_CloseBus = MyDialog.findViewById(R.id.closePopupPositive);

        button_busContinue.setEnabled(true);
        imageView_CloseBus.setEnabled(true);

        button_busContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(),"Taxi Clicked",Toast.LENGTH_SHORT).show();
                MyDialog.cancel();
                Intent searchActivityIntent = new Intent(TransportMainActivity.this, SearchActivity.class);
                searchActivityIntent.putExtras(basket);
                startActivity(searchActivityIntent);
            }
        });
        imageView_CloseBus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDialog.cancel();
            }
        });
        if (MyDialog.getWindow() != null) {       //used this since it was telling me that setBackgroundDrawable my produce nullpointer exception
            MyDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            MyDialog.show();
        } else {
            MyDialog.cancel();
        }
    }


    private void showWarningPopUp() {
        MyDialog = new Dialog(TransportMainActivity.this);
        MyDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        MyDialog.setContentView(R.layout.alert_pop_check);

        button_okReset = MyDialog.findViewById(R.id.btn_Ok_reset);
        imageView_closePop = MyDialog.findViewById(R.id.closePopup);

        button_okReset.setEnabled(true);
        imageView_closePop.setEnabled(true);

        button_okReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(),"Taxi Clicked",Toast.LENGTH_SHORT).show();
                editext_initialstop.setText("");
                edittext_finaldestination.setText("");
                editext_initialstop.requestFocus();         //This is to set focus on editext_initialstop after reset.
                MyDialog.cancel();

            }
        });
        imageView_closePop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDialog.cancel();
            }
        });
        if (MyDialog.getWindow() != null) {       //used this since it was telling me that setBackgroundDrawable my produce nullpointer exception
            MyDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            MyDialog.show();
        } else {
            MyDialog.cancel();
        }
    }


    private void processForBus(String initial_stop, String final_stop) {
        boolean operationSuccess = true;

        String mode;
        try {
            ProcessExternalDBHelper searchprocess = new ProcessExternalDBHelper(this);
            searchprocess.openRead();

            stops_key = searchprocess.findallStopsKey(initial_stop, final_stop);
            intersectingStops = searchprocess.findIntersectionRoute(initial_stop, final_stop);
            middleRoutesKey = searchprocess.findMiddleRouteKey(initial_stop, final_stop);

            searchprocess.close();
            if (stops_key.size() > 0) {
                processSingleBusmode(stops_key, initial_stop, final_stop);
            }
            if(intersectingStops.size() > 0){
                processDoubleBusmode(intersectingStops, initial_stop, final_stop);
            }
            if(middleRoutesKey.size() > 0){
                processTripleBusmode(middleRoutesKey, initial_stop, final_stop);
            }

            if ((stops_key.size() > 0) && (intersectingStops.size() > 0) && (middleRoutesKey.size() > 0)) {         //there are 3 ways to travel: single, double and triple
                //processSingleBusmode(stops_key, initial_stop, final_stop);
                //processDoubleBusmode(intersectingStops, initial_stop, final_stop);
                //processTripleBusmode(middleRoutesKey, initial_stop, final_stop);

                if ((totalAverageTime < (totalAverageTime_initial + totalAverageTime_final))
                        && (totalAverageTime < (totalAverageTime_midinitial + totalAverageTime_middle + totalAverageTime_midfinal))) {
                    //Single Bus mode is fastest
                    mode = "Single Bus";            //Choose 1 bus for the travel
                    callNextInformationActivity(initial_stop, final_stop, totalAverageTime, totalDistance, routes_name, mode);
                } else if (((totalAverageTime_initial + totalAverageTime_final) < totalAverageTime)
                        && ((totalAverageTime_initial + totalAverageTime_final) < (totalAverageTime_midinitial + totalAverageTime_middle + totalAverageTime_midfinal))) {
                    //Double Bus mode is fastest
                    mode = "Two Buses";             //Choose 2 buses for the travel
                    callNextInformationActivity(initial_stop, transitionStop, final_stop,
                            totalAverageTime_initial, totalDistance_initial, routes_name_initial,
                            totalAverageTime_final, totalDistance_final, routes_name_final, mode);
                } else if ((totalAverageTime > (totalAverageTime_midinitial + totalAverageTime_middle + totalAverageTime_midfinal))
                        && ((totalAverageTime_initial + totalAverageTime_final) > (totalAverageTime_midinitial + totalAverageTime_middle + totalAverageTime_midfinal))) {
                    //Triple Bus mode is fastest
                    mode = "Three Buses";           //Choose 3 buses for the travel
                    callNextInformationActivity(initial_stop, transitionStop_initial, transitionStop_final, final_stop,
                            totalAverageTime_midinitial, totalDistance_midinitial, routes_name_midinitial,
                            totalAverageTime_middle, totalDistance_middle, routes_name_middle,
                            totalAverageTime_midfinal, totalDistance_midfinal, routes_name_midfinal, mode);
                } else {  //no worries and just take single bus
                    //default
                    mode = "Single Bus";            //Choose 1 bus for the travel
                    callNextInformationActivity(initial_stop, final_stop, totalAverageTime, totalDistance, routes_name, mode);
                }

            } else if ((stops_key.size() > 0) && (intersectingStops.size() > 0)) {                                  //there are 2 ways to travel: single and double
                //processSingleBusmode(stops_key, initial_stop, final_stop);
                //processDoubleBusmode(intersectingStops, initial_stop, final_stop);

                if (totalAverageTime < (totalAverageTime_initial + totalAverageTime_final)) {
                    //Single Bus mode is fastest
                    mode = "Single Bus";            //Choose 1 bus for the travel
                    callNextInformationActivity(initial_stop, final_stop, totalAverageTime, totalDistance, routes_name, mode);
                } else if (totalAverageTime > (totalAverageTime_initial + totalAverageTime_final)) {
                    //Double Bus mode is fastest
                    mode = "Two Buses";             //Choose 2 buses for the travel
                    callNextInformationActivity(initial_stop, transitionStop, final_stop,
                            totalAverageTime_initial, totalDistance_initial, routes_name_initial,
                            totalAverageTime_final, totalDistance_final, routes_name_final, mode);
                } else {
                    //Single Bus mode is better                 /#######################################
                    mode = "Single Bus";            //Choose 1 bus for the travel
                    callNextInformationActivity(initial_stop, final_stop, totalAverageTime, totalDistance, routes_name, mode);
                }

            } else if ((stops_key.size() > 0) && (middleRoutesKey.size() > 0)) {                                    //there are 2 ways to travel: single and triple
                //processSingleBusmode(stops_key, initial_stop, final_stop);
                //processTripleBusmode(middleRoutesKey, initial_stop, final_stop);

                if (totalAverageTime < (totalAverageTime_midinitial + totalAverageTime_middle + totalAverageTime_midfinal)) {
                    //Single Bus mode is fastest
                    mode = "Single Bus";            //Choose 1 bus for the travel
                    callNextInformationActivity(initial_stop, final_stop, totalAverageTime, totalDistance, routes_name, mode);
                } else if (totalAverageTime > (totalAverageTime_midinitial + totalAverageTime_middle + totalAverageTime_midfinal)) {
                    //Triple Bus mode is fastest
                    mode = "Three Buses";           //Choose 3 buses for the travel
                    callNextInformationActivity(initial_stop, transitionStop_initial, transitionStop_final, final_stop,
                            totalAverageTime_midinitial, totalDistance_midinitial, routes_name_midinitial,
                            totalAverageTime_middle, totalDistance_middle, routes_name_middle,
                            totalAverageTime_midfinal, totalDistance_midfinal, routes_name_midfinal, mode);
                } else {
                    //Single Bus mode is better             ################################################
                    mode = "Single Bus";            //Choose 1 bus for the travel
                    callNextInformationActivity(initial_stop, final_stop, totalAverageTime, totalDistance, routes_name, mode);
                }

            } else if ((intersectingStops.size() > 0) && (middleRoutesKey.size() > 0)) {                            //there are 2 ways to travel: double and triple
                //processDoubleBusmode(intersectingStops, initial_stop, final_stop);
                //processTripleBusmode(middleRoutesKey, initial_stop, final_stop);

                if ((totalAverageTime_initial + totalAverageTime_final) < (totalAverageTime_midinitial + totalAverageTime_middle + totalAverageTime_midfinal)) {
                    //Double Bus mode is fastest
                    mode = "Two Buses";             //Choose 2 buses for the travel
                    callNextInformationActivity(initial_stop, transitionStop, final_stop,
                            totalAverageTime_initial, totalDistance_initial, routes_name_initial,
                            totalAverageTime_final, totalDistance_final, routes_name_final, mode);
                } else if ((totalAverageTime_initial + totalAverageTime_final) > (totalAverageTime_midinitial + totalAverageTime_middle + totalAverageTime_midfinal)) {
                    //Triple Bus mode is fastest
                    mode = "Three Buses";           //Choose 3 buses for the travel
                    callNextInformationActivity(initial_stop, transitionStop_initial, transitionStop_final, final_stop,
                            totalAverageTime_midinitial, totalDistance_midinitial, routes_name_midinitial,
                            totalAverageTime_middle, totalDistance_middle, routes_name_middle,
                            totalAverageTime_midfinal, totalDistance_midfinal, routes_name_midfinal, mode);
                } else {
                    //Double Bus mode is better                 #####################################
                    mode = "Two Buses";             //Choose 2 buses for the travel
                    callNextInformationActivity(initial_stop, transitionStop, final_stop,
                            totalAverageTime_initial, totalDistance_initial, routes_name_initial,
                            totalAverageTime_final, totalDistance_final, routes_name_final, mode);
                }

            } else if (stops_key.size() > 0) {                                                                      //there is only 1 way to travel: single
                //processSingleBusmode(stops_key, initial_stop, final_stop);

                mode = "Single Bus";            //Choose 1 bus for the travel
                callNextInformationActivity(initial_stop, final_stop, totalAverageTime, totalDistance, routes_name, mode);

            } else if (intersectingStops.size() > 0) {                                                              //there is only 1 way to travel: double
                //processDoubleBusmode(intersectingStops, initial_stop, final_stop);

                mode = "Two Buses";             //Choose 2 buses for the travel
                callNextInformationActivity(initial_stop, transitionStop, final_stop,
                        totalAverageTime_initial, totalDistance_initial, routes_name_initial,
                        totalAverageTime_final, totalDistance_final, routes_name_final, mode);

            } else if (middleRoutesKey.size() > 0) {                                                                //there is only 1 way to travel: triple
                //processTripleBusmode(middleRoutesKey, initial_stop, final_stop);

                mode = "Three Buses";           //Choose 3 buses for the travel
                callNextInformationActivity(initial_stop, transitionStop_initial, transitionStop_final, final_stop,
                        totalAverageTime_midinitial, totalDistance_midinitial, routes_name_midinitial,
                        totalAverageTime_middle, totalDistance_middle, routes_name_middle,
                        totalAverageTime_midfinal, totalDistance_midfinal, routes_name_midfinal, mode);

            } else {                            //WHEN FOR A GIVEN ROUTE ONE HAS TO CHOOSE DIFFERENT BUSES OF DIFFERENT ROUTE i.e. change buses

                //NO DATA IN DATABASE

                //I THINK PEOPLE DONT NEED TO USE MORE THAN 3 BUSES TO GO TO PLACES
                //HENCE SHOW NO PATH FOUND. TAKE TAXI??

            }//WHAT TO DO IF ONE HAS TO TAKE MORE THAN 3 BUSES

            //searchprocess.close();
        } catch (Exception e) {

            operationSuccess = false;
            String error = e.toString();
            Dialog d = new Dialog(this);
            d.setTitle("Failed okkk");
            TextView tv = new TextView(this);
            tv.setText(error);
            d.setContentView(tv);
            d.show();
        } finally {
            if (operationSuccess) {
                showBusPopUp();
            }
        }
    }// END OF processForBus(String initial_stop, String final_stop)


    private void processSingleBusmode(ArrayList<String> stops_key, String initial_stop, String final_stop) {
        try {
            ProcessExternalDBHelper searchprocess = new ProcessExternalDBHelper(this);
            searchprocess.openRead();
            routes_name = searchprocess.findneededRoutesName(stops_key);
            totalAverageTime = searchprocess.findtotalAvgTime(initial_stop, final_stop);
            totalDistance = searchprocess.findtotalDistance(initial_stop, final_stop);
            searchprocess.close();
        } catch (SQLiteException se) {

        }
    }

    private void processDoubleBusmode(ArrayList<String> intersectingStops, String initial_stop, String final_stop) {
        ArrayList<String> stops_key2;
        try {
            ProcessExternalDBHelper searchprocess = new ProcessExternalDBHelper(this);
            searchprocess.openRead();
            if (intersectingStops.size() == 1) {
                transitionStop = intersectingStops.get(0);       //index = 0 means first and only value of intersectingStops array here
            } else {
                transitionStop = searchprocess.findTransitionStop(final_stop, intersectingStops);   //find best transition stop
            }
            stops_key2 = searchprocess.findallStopsKey(initial_stop, transitionStop);
            if (stops_key2.size() > 0) {
                routes_name_initial = searchprocess.findneededRoutesName(stops_key2);
                totalAverageTime_initial = searchprocess.findtotalAvgTime(initial_stop, transitionStop);
                totalDistance_initial = searchprocess.findtotalDistance(initial_stop, transitionStop);
                stops_key2 = searchprocess.findallStopsKey(transitionStop, final_stop);
                if (stops_key2.size() > 0) {
                    routes_name_final = searchprocess.findneededRoutesName(stops_key2);
                    totalAverageTime_final = searchprocess.findtotalAvgTime(transitionStop, final_stop);
                    totalDistance_final = searchprocess.findtotalDistance(transitionStop, final_stop);
                }
            }
            searchprocess.close();
        } catch (SQLiteException se) {

        }
    }

    private void processTripleBusmode(ArrayList<String> middleRoutesKey, String initial_stop, String final_stop) {
        try {
            ProcessExternalDBHelper searchprocess = new ProcessExternalDBHelper(this);
            searchprocess.openRead();

            ArrayList<String> intersectingStops_initial;
            ArrayList<String> stops_key_initial_middle_final;

            intersectingStops_final = searchprocess.findMiddleRouteIntersectingStops(final_stop, middleRoutesKey);
            if (intersectingStops_final.size() > 0) {
                if (intersectingStops_final.size() == 1) {
                    transitionStop_final = intersectingStops_final.get(0);  //index = 0 means only 1 intersectingStops_final
                } else {
                    transitionStop_final = searchprocess.findTransitionStop(final_stop, intersectingStops_final);    //find best transition stop between middle route and final destination
                }
                intersectingStops_initial = searchprocess.findIntersectionRoute(initial_stop, transitionStop_final);
                if (intersectingStops_initial.size() > 0) {
                    if (intersectingStops_initial.size() == 1) {
                        transitionStop_initial = intersectingStops_initial.get(0);  //index = 0 means only 1 intersectingStops_initial
                    } else {
                        transitionStop_initial = searchprocess.findTransitionStop(transitionStop_final, intersectingStops_initial); //find best initial transition stop
                    }

                    stops_key_initial_middle_final = searchprocess.findallStopsKey(initial_stop, transitionStop_initial);
                    if (stops_key_initial_middle_final.size() > 0) {
                        routes_name_midinitial = searchprocess.findneededRoutesName(stops_key_initial_middle_final);
                        totalAverageTime_midinitial = searchprocess.findtotalAvgTime(initial_stop, transitionStop_initial);
                        totalDistance_midinitial = searchprocess.findtotalDistance(initial_stop, transitionStop_initial);
                        stops_key_initial_middle_final = searchprocess.findallStopsKey(transitionStop_initial, transitionStop_final);
                        if (stops_key_initial_middle_final.size() > 0) {
                            routes_name_middle = searchprocess.findneededRoutesName(stops_key_initial_middle_final);
                            totalAverageTime_middle = searchprocess.findtotalAvgTime(transitionStop_initial, transitionStop_final);
                            totalDistance_middle = searchprocess.findtotalDistance(transitionStop_initial, transitionStop_final);
                            stops_key_initial_middle_final = searchprocess.findallStopsKey(transitionStop_final, final_stop);
                            if (stops_key_initial_middle_final.size() > 0) {
                                routes_name_midfinal = searchprocess.findneededRoutesName(stops_key_initial_middle_final);
                                totalAverageTime_midfinal = searchprocess.findtotalAvgTime(transitionStop_final, final_stop);
                                totalDistance_midfinal = searchprocess.findtotalDistance(transitionStop_final, final_stop);
                            }
                        }
                    }
                }
            }
            searchprocess.close();
        } catch (SQLiteException se) {

        }
    }

    private void processForTaxi(String initial_stop, String final_stop) {
        boolean operationSuccess = true;
        float totalAverageTime;
        float totalDistance;
        try {
            ProcessExternalDBHelper searchprocess = new ProcessExternalDBHelper(this);
            searchprocess.openRead();
            stops_key = searchprocess.findallStopsKey(initial_stop, final_stop);
            if (stops_key.size() > 0) {
                totalAverageTime = searchprocess.findtotalAvgTime(initial_stop, final_stop);
                totalDistance = searchprocess.findtotalDistance(initial_stop, final_stop);

                callNextInformationTaxiActivity(initial_stop, final_stop, totalAverageTime, totalDistance);
            } else {
                ArrayList<String> intersectingStops;
                float totalAverageTime_initial, totalDistance_initial, totalAverageTime_final, totalDistance_final;

                intersectingStops = searchprocess.findIntersectionRoute(initial_stop, final_stop);
                if (intersectingStops.size() > 0) {
                    String transitionStop;
                    if (intersectingStops.size() == 1) {
                        transitionStop = intersectingStops.get(0);       //index = 0 means first and only value of intersectingStops array here
                    } else {
                        transitionStop = searchprocess.findTransitionStop(final_stop, intersectingStops);   //find best transition stop
                    }
                    stops_key = searchprocess.findallStopsKey(initial_stop, transitionStop);
                    if (stops_key.size() > 0) {
                        totalAverageTime_initial = searchprocess.findtotalAvgTime(initial_stop, transitionStop);
                        totalDistance_initial = searchprocess.findtotalDistance(initial_stop, transitionStop);
                        stops_key = searchprocess.findallStopsKey(transitionStop, final_stop);
                        if (stops_key.size() > 0) {
                            totalAverageTime_final = searchprocess.findtotalAvgTime(transitionStop, final_stop);
                            totalDistance_final = searchprocess.findtotalDistance(transitionStop, final_stop);

                            //SEND TO BUNDLE
                            totalAverageTime = totalAverageTime_initial + totalAverageTime_final;
                            totalDistance = totalDistance_initial + totalDistance_final;
                            callNextInformationTaxiActivity(initial_stop, final_stop, totalAverageTime, totalDistance);
                        }
                    }
                } else {        //Choose 3 buses for the travel
                    ArrayList<String> intersectingStops_final;
                    ArrayList<String> middleRoutesKey;
                    String transitionStop_final;
                    middleRoutesKey = searchprocess.findMiddleRouteKey(initial_stop, final_stop);
                    if (middleRoutesKey.size() > 0) {
                        intersectingStops_final = searchprocess.findMiddleRouteIntersectingStops(final_stop, middleRoutesKey);
                        if (intersectingStops_final.size() > 0) {
                            if (intersectingStops_final.size() == 1) {
                                transitionStop_final = intersectingStops_final.get(0);  //index = 0 means only 1 intersectingStops_final
                            } else {
                                transitionStop_final = searchprocess.findTransitionStop(final_stop, intersectingStops_final);    //find best transition stop between middle route and final destination
                            }

                            ArrayList<String> intersectingStops_initial;
                            ArrayList<String> stops_key_initial_middle_final;

                            float totalAverageTime_middle, totalDistance_middle;

                            intersectingStops_initial = searchprocess.findIntersectionRoute(initial_stop, transitionStop_final);
                            if (intersectingStops_initial.size() > 0) {
                                String transitionStop_initial;
                                if (intersectingStops_initial.size() == 1) {
                                    transitionStop_initial = intersectingStops_initial.get(0);  //index = 0 means only 1 intersectingStops_initial
                                } else {
                                    transitionStop_initial = searchprocess.findTransitionStop(transitionStop_final, intersectingStops_initial);
                                }

                                stops_key_initial_middle_final = searchprocess.findallStopsKey(initial_stop, transitionStop_initial);
                                if (stops_key_initial_middle_final.size() > 0) {
                                    totalAverageTime_initial = searchprocess.findtotalAvgTime(initial_stop, transitionStop_initial);
                                    totalDistance_initial = searchprocess.findtotalDistance(initial_stop, transitionStop_initial);
                                    stops_key_initial_middle_final = searchprocess.findallStopsKey(transitionStop_initial, transitionStop_final);
                                    if (stops_key_initial_middle_final.size() > 0) {
                                        totalAverageTime_middle = searchprocess.findtotalAvgTime(transitionStop_initial, transitionStop_final);
                                        totalDistance_middle = searchprocess.findtotalDistance(transitionStop_initial, transitionStop_final);
                                        stops_key_initial_middle_final = searchprocess.findallStopsKey(transitionStop_final, final_stop);
                                        if (stops_key_initial_middle_final.size() > 0) {
                                            totalAverageTime_final = searchprocess.findtotalAvgTime(transitionStop_final, final_stop);
                                            totalDistance_final = searchprocess.findtotalDistance(transitionStop_final, final_stop);

                                            //SEND TO BUNDLE
                                            totalAverageTime = totalAverageTime_initial + totalAverageTime_middle + totalAverageTime_final;
                                            totalDistance = totalDistance_initial + totalDistance_middle + totalDistance_final;
                                            callNextInformationTaxiActivity(initial_stop, final_stop, totalAverageTime, totalDistance);
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        //I THINK PEOPLE DONT NEED TO USE MORE THAN 3 BUSES TO GO TO PLACES
                        //HENCE SHOW NO PATH FOUND. TAKE TAXI??
                    }
                }
            }//WHAT TO DO IF ONE HAS TO TAKE MORE THAN 3 BUSES


            searchprocess.close();
        } catch (Exception e) {

            operationSuccess = false;
            String error = e.toString();
            Dialog d = new Dialog(this);
            d.setTitle("Failed okkk");
            TextView tv = new TextView(this);
            tv.setText(error);
            d.setContentView(tv);
            d.show();
        } finally {
            if (operationSuccess) {
                        /*Intent searchActivityIntent = new Intent(MainActivity.this, SearchActivity.class);
                        searchActivityIntent.putExtras(basket);
                        startActivity(searchActivityIntent);*/
                showTaxiPopUp();
            }
        }
    }// END OF processForTaxi(String initial_stop, String final_stop)

}
