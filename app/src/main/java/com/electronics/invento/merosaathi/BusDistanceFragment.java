package com.electronics.invento.merosaathi;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class BusDistanceFragment extends Fragment {
    String initial_stop, final_stop;
    float total_distance = 1000;

    TextView mDistance, mPathInfo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        initial_stop = getArguments().getString("initial");
        final_stop = getArguments().getString("final");
        total_distance = getArguments().getFloat("avgDistance");
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bus_distance, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mDistance = view.findViewById(R.id.tv_distance_taken);
        mPathInfo = view.findViewById(R.id.tv_path_info2);

        mPathInfo.setText("You are going from \n" + initial_stop + "\nto\n" + final_stop);
        mDistance.setText(total_distance + " meter");
    }
}
