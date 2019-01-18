package com.electronics.invento.merosaathi;


import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class InformationViewFragment extends Fragment {

    TextView mCountDown;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_information_view, container, false);
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mCountDown = view.findViewById(R.id.time_taken_countdown);
        new CountDownTimer(30000, 1000){

            @Override
            public void onTick(long millisUntilFinished) {
                mCountDown.setText("time: "+millisUntilFinished/1000);
            }

            @Override
            public void onFinish() {
                mCountDown.setText("Reached");
            }
        }.start();
    }

}