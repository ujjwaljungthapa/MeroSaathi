package com.electronics.invento.merosaathi;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class BusCostFragment extends Fragment {
    String initial_stop, final_stop;

    TextView mPathInfo;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        initial_stop = getArguments().getString("initial");
        final_stop = getArguments().getString("final");

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bus_cost, container, false);
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mPathInfo = view.findViewById(R.id.tv_path_info3);

        mPathInfo.setText("You are going from \n" + initial_stop + "\nto\n" + final_stop);
    }
}
