package com.electronics.invento.merosaathi;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    TextView textView_showInfo;
    String inital_name, final_name, mode_status, transition_stop;
    String transition_stop_initial, transition_stop_final;

    private ArrayList<String> searched_routes_name = new ArrayList<>();
    private ArrayList<String> searched_routes_name_initial = new ArrayList<>();
    private ArrayList<String> searched_routes_name_final = new ArrayList<>();
    private ArrayList<String> searched_routes_name_middle = new ArrayList<>();

    float total_time, total_distance;
    float total_time_initial, total_distance_initial, total_time_final, total_distance_final;
    float total_time_middle, total_distance_middle;

    String testkeep = "";
    String initialtestkeep = "";
    String finaltestkeep = "";
    String middletestkeep = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        textView_showInfo = findViewById(R.id.tv_avg_arrival_timing);

        Bundle gotBasket = getIntent().getExtras();
        mode_status = gotBasket.getString("mode");
        if (mode_status.equals("Single Bus")) {
            inital_name = gotBasket.getString("initial");
            final_name = gotBasket.getString("final");
            total_time = gotBasket.getFloat("avgTime");
            total_distance = gotBasket.getFloat("avgDistance");
            searched_routes_name = gotBasket.getStringArrayList("routeArray");

            for (int i = 0; i < searched_routes_name.size(); i++) {
                testkeep = testkeep + searched_routes_name.get(i) + " Bus\n";
            }

            textView_showInfo.setText("Only 1 bus is needed for your travel!\n\nYou are going from \n" + inital_name + " to \n" + final_name
                    + ". \n\nIt will take approximately " + total_time + " minutes, \nAnd " + total_distance + " meter distance.\n" + "\nThe buses you can take are: \n" + testkeep + " \nThank You!\n");

        } else if (mode_status.equals("Two Buses")) {
            inital_name = gotBasket.getString("initial");
            transition_stop = gotBasket.getString("transition");
            final_name = gotBasket.getString("final");
            total_time_initial = gotBasket.getFloat("avgTime_initial");
            total_distance_initial = gotBasket.getFloat("avgDistance_initial");
            searched_routes_name_initial = gotBasket.getStringArrayList("routeArray_initial");
            total_time_final = gotBasket.getFloat("avgTime_final");
            total_distance_final = gotBasket.getFloat("avgDistance_final");
            searched_routes_name_final = gotBasket.getStringArrayList("routeArray_final");

            total_time = total_time_initial + total_time_final;
            total_distance = total_distance_initial + total_distance_final;

            for (int i = 0; i < searched_routes_name_initial.size(); i++) {
                initialtestkeep = initialtestkeep + searched_routes_name_initial.get(i) + " Bus\n";
            }
            for (int i = 0; i < searched_routes_name_final.size(); i++) {
                finaltestkeep = finaltestkeep + searched_routes_name_final.get(i) + " Bus\n";
            }

            textView_showInfo.setText("2 buses are needed for your travel!\n\nYou are going from \n" + inital_name + " to \n" + final_name + ".\n\n " +
                    "But you need to take a transit stop at \n" + transition_stop + ".\n\n" + " To go from " + inital_name + " to " + transition_stop + ". It will take " +
                    total_time_initial + " minutes and " + total_distance_initial + " meter. \n\n" + "And to go from " + transition_stop + " to " + final_name + ". It will take " +
                    total_time_final + " minutes and " + total_distance_final + " meter. \n\n" + "Finally total time taken = " + total_time + "\nAnd total distance = " + total_distance + "\n\n Routes for initial bus are: \n" +
                    initialtestkeep + "\nRoutes for final bus are: \n" + finaltestkeep + " \nThank you\n");
        } else if (mode_status.equals("Three Buses")) {

            inital_name = gotBasket.getString("initial");
            transition_stop_initial = gotBasket.getString("transition_initial");
            transition_stop_final = gotBasket.getString("transition_final");
            final_name = gotBasket.getString("final");

            total_time_initial = gotBasket.getFloat("avgTime_initial");
            total_distance_initial = gotBasket.getFloat("avgDistance_initial");
            searched_routes_name_initial = gotBasket.getStringArrayList("routeArray_initial");

            total_time_middle = gotBasket.getFloat("avgTime_middle");
            total_distance_middle = gotBasket.getFloat("avgDistance_middle");
            searched_routes_name_middle = gotBasket.getStringArrayList("routeArray_middle");

            total_time_final = gotBasket.getFloat("avgTime_final");
            total_distance_final = gotBasket.getFloat("avgDistance_final");
            searched_routes_name_final = gotBasket.getStringArrayList("routeArray_final");

            total_time = total_time_initial + total_time_middle + total_time_final;
            total_distance = total_distance_initial + total_distance_middle + total_distance_final;

            for (int i = 0; i < searched_routes_name_initial.size(); i++) {
                initialtestkeep = initialtestkeep + searched_routes_name_initial.get(i) + " Bus\n";
            }
            for (int i = 0; i< searched_routes_name_middle.size(); i++) {
                middletestkeep = middletestkeep + searched_routes_name_middle.get(i) + " Bus\n";
            }
            for (int i = 0; i < searched_routes_name_final.size(); i++) {
                finaltestkeep = finaltestkeep + searched_routes_name_final.get(i) + " Bus\n";
            }

            textView_showInfo.setText("HURRAY 3 buses are needed for your travel!\n\nYou are going from \n" + inital_name + " to \n" + final_name + ".\n\n " +
                    "But you need to take a transit route to take another bus at \n" + transition_stop_initial + " and go to "+ transition_stop_final +".\n"+
                    " Then from there choose another bus to go to"+final_name+".\n\n" +
                    " To go from " + inital_name + " to " + transition_stop_initial + ". It will take " +
                    total_time_initial + " minutes and " + total_distance_initial + " meter. \n\n" +
                    " To go from " + transition_stop_initial + " to " + transition_stop_final + ". It will take " +
                    total_time_middle + " minutes and " + total_distance_middle + " meter. \n\n" +
                    "And finally to go from " + transition_stop_final + " to " + final_name + ". It will take " +
                    total_time_final + " minutes and " + total_distance_final + " meter. \n\n" +
                    "Overall total time taken = " + total_time + "\nAnd total distance = " + total_distance +
                    "\n\n Routes for initial bus are: \n" + initialtestkeep +
                    "\nRoutes for middle bus are: \n" + middletestkeep +
                    "\nRoutes for final bus are: \n" + finaltestkeep +
                    " \nThank you\n");


        } else {          //WHEN mode_status values does not match
            textView_showInfo.setText("NO DATA. Sorry!");
        }


    }
}
